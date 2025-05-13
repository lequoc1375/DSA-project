package scenes;

import controller.KeyHandler;
import enemies.*;
import entity.Barrier;
import entity.Bullet;
import entity.MovedObject.*;
import entity.Turret;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import javax.swing.*;
import main.GamePanel;
import manager.AlliesManager;
import manager.EnemiesManager;
import manager.MoveManager;
import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;

public class Playing {
    private boolean isActive = false;

    private List<Bullet> bullets = new ArrayList<>();
    private List<MoveManager> alliesMoveManager = new ArrayList<>();
    private EnemiesManager enemiesManager;
    private AlliesManager alliesManager;
    private MoveManager playerManager;
    private Barrier barrier;
    // private Turret turret;
    private Player player;
    private int ROWS;
    private int COLS;
    private int TILE_SIZE;
    private Point targetPosition = null;
    private int noOfAllies = 50;
    private Color randomColor;
    private int countBoomer = 0;
    private int countSlower = 0;
    private int countBreaker = 0;
    private int countSniper = 0;
    private BrownAllies brownAllies;
    private int countBrown = 0;
    private int countOrange = 0;
    private OrangeAllies orangeAllies;
    private Point playerPixel;
    private int countPurple = 0;
    private PurpleAllies purpleAllies;
    private int NoOfAlliesCurrent = 0;

    private int MaxLevelOfBlue = 1;
    private final Random random = new Random();

    private Timer enemySpawnTimer; 
    private Timer allySpawnTimer;

    public Playing() {
        initGenerate();
    }

    public void startGame() {
        isActive = true;
        enemySpawnTimer = new Timer(5000, e -> {
            if (isActive) {
                SpawnEnemies();
            }
        });
        enemySpawnTimer.start();

        allySpawnTimer = new Timer(15000, e -> {
            if (isActive) {
                SpawnAllies();
            }
        });
        allySpawnTimer.start();
    }

    public void pauseGame() {
        isActive = false;
        if (enemySpawnTimer != null) {
            enemySpawnTimer.stop();
        }
        if (allySpawnTimer != null) {
            allySpawnTimer.stop();
        }
    }

    public void replayGame() {
        System.out.println("replay 1.1");
        pauseGame();
        System.out.println("replay 1.2");
        isActive = false;
        bullets.clear();
        System.out.println("replay 1.3");
        targetPosition = null;
        countBoomer = 0;
        countSlower = 0;
        countBreaker = 0;
        countSniper = 0;
        countBrown = 0;
        countOrange = 0;
        countPurple = 0;
        NoOfAlliesCurrent = 0;
        MaxLevelOfBlue = 1;
        brownAllies = null;
        orangeAllies = null;
        purpleAllies = null;
        System.out.println("replay 1.4");
        initGenerate();
        System.out.println("replay 1.5");
        startGame();
        System.out.println("replay 1.6");
    }

    public void initGenerate() {
        ROWS = GamePanel.ROWS;
        COLS = GamePanel.COLS;
        TILE_SIZE = GamePanel.TILE_SIZE;
        player = new Player(10, 10, this);
        barrier = new Barrier();
        playerManager = new MoveManager(player);
        enemiesManager = new EnemiesManager();
        alliesManager = new AlliesManager();
        Random random = new Random();
        randomColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
        alliesManager.clear();
        alliesMoveManager.clear();
        enemiesManager.clear();
        barrier.generateObstacles(player.getPosition());
    }

    private void SpawnEnemies() {
        Random r = new Random();
        int x = r.nextInt(COLS);
        int y = r.nextInt(ROWS);
        switch (r.nextInt(4) + 1) {
            case 1:
                if (countBreaker < 3) {
                    enemiesManager.add(new EMPDisabler(x, y, 100, 2500, playerManager, () -> alliesManager.getAlliesList(), player,() -> alliesMoveManager));
                    countBreaker++;
                }
                break;
            case 2:
                if (countBoomer < 3) {
                    enemiesManager.add(new Boomer(x, y, 75, 20000, player, alliesManager));
                    countBoomer++;
                }
                break;
            case 3:
                if (countSlower < 1) {
                    enemiesManager.add(new Slower(x, y, 100, 5000, player)); 
                    countSlower++;
                }
                break;
            case 4:
                if (countSniper < 3) {
                    enemiesManager.add(new Sniper(x, y, 100, 7500, playerManager, player)); // Sniper
                    countSniper++;
                }
                break;
        }
    }

    private void removeDeadEnemies() {
        List<Enemy> toRemove = new ArrayList<>();
        for (Enemy e : enemiesManager.getEnemies()) {
            if (e.getEnemyHealth() <= 0) {
                if (e instanceof EMPDisabler) countBreaker--;
                else if (e instanceof Boomer) countBoomer--;
                else if (e instanceof Slower) countSlower--;
                else if (e instanceof Sniper) countSniper--;
                toRemove.add(e);
            }
        }
    
        for (Enemy dead : toRemove) {
            enemiesManager.remove(dead);
        }
    }
    
    

    private void SpawnAllies() {
        if (alliesManager.getAlliesList().size() >= noOfAllies) {
            return;
        }
        Random r = new Random();
        int spawnX, spawnY;
        Point spawnPoint;
        int attempt = 0;
        do {
            spawnX = r.nextInt(COLS);
            spawnY = r.nextInt(ROWS);
            spawnPoint = new Point(spawnX, spawnY);
            attempt++;
            if (attempt > 100) {
                return;
            }
        } while (!isValidPosition(spawnPoint));

        Allies newAlly = spawnWeightedAlly(spawnX, spawnY);
        alliesManager.add(newAlly);
        alliesMoveManager.add(new MoveManager(newAlly));
        NoOfAlliesCurrent ++;
        
        player.setPlayerHealth(player.getPlayerHealth() + 1);

        mergeIfPossible(newAlly);
    }

    private Allies spawnWeightedAlly(int spawnX, int spawnY) {
        List<WeightedAlly> weightedAllies = new ArrayList<>();
    
        // Always allow Blue
        weightedAllies.add(new WeightedAlly("BlueAllies", 10));
    
        // Add only if not spawned yet
        if (countBrown < 1) {
            weightedAllies.add(new WeightedAlly("BrownAllies", 10));
        }
    
        if (countOrange < 1) {
            weightedAllies.add(new WeightedAlly("OrangeAllies", 40));
        }
    
        if (countPurple < 1) {
            weightedAllies.add(new WeightedAlly("PurpleAllies", 40));
        }
    
        // Compute total weight
        int totalWeight = 0;
        for (WeightedAlly wa : weightedAllies) {
            totalWeight += wa.weight;
        }
    
        // Select random weight
        Random random = new Random();
        int randomWeight = random.nextInt(totalWeight);
    
        // Choose ally based on weight
        int sum = 0;
        String selectedType = "";
        for (WeightedAlly wa : weightedAllies) {
            sum += wa.weight;
            if (randomWeight < sum) {
                selectedType = wa.type;
                break;
            }
        }
    
        // Create instance and increase count
        switch (selectedType) {
            case "BlueAllies":
                return new BlueAllies(spawnX, spawnY);
            case "BrownAllies":
                countBrown++;
                brownAllies = new BrownAllies(spawnX, spawnY, playerManager, player);
                return brownAllies;
            case "OrangeAllies":
                countOrange++;
                orangeAllies = new OrangeAllies(spawnX, spawnY, playerManager);
                return orangeAllies;
            case "PurpleAllies":
                countPurple++;
                purpleAllies = new PurpleAllies(spawnX, spawnY, playerManager);
                return purpleAllies;
            default:
                return new BlueAllies(spawnX, spawnY); // fallback
        }
    }
    


    private static class WeightedAlly {
        String type;
        int weight;
        WeightedAlly(String type, int weight) {
            this.type = type;
            this.weight = weight;
        }
    }

    private void mergeIfPossible(Allies newAlly) {
        String type = newAlly.getType();
        int level = newAlly.getLevel();
    
        List<Allies> same = new ArrayList<>();
        for (Allies a : alliesManager.getAlliesList()) {
            if (a != newAlly && a.getType().equals(type) && a.getLevel() == level) {
                same.add(a);
            }
        }
    
        if (same.size() >= 2) {
            Allies a1 = same.get(0);
            Allies a2 = same.get(1);
    
            // Cleanup all related MoveManagers
            for (MoveManager mm : new ArrayList<>(alliesMoveManager)) {
                if (mm.getMovedObject() == a1 || mm.getMovedObject() == a2 || mm.getMovedObject() == newAlly) {
                    mm.cleanup();
                }
            }
    
            // Remove merged allies from the queue
            alliesManager.getAlliesQueue().remove(a1);
            alliesManager.getAlliesQueue().remove(a2);
            alliesManager.getAlliesQueue().remove(newAlly);
    
            // Remove related MoveManagers
            alliesMoveManager.removeIf(m ->
                m.getMovedObject() == a1 ||
                m.getMovedObject() == a2 ||
                m.getMovedObject() == newAlly
            );
    
            // Create upgraded ally
            Allies upgraded = upgradeAlly(newAlly.getType(), newAlly.getX(), newAlly.getY(), level + 1);
            alliesManager.add(upgraded);
            alliesMoveManager.add(new MoveManager(upgraded));

            if(upgraded.getLevel() < 6) {
                mergeIfPossible(upgraded);
            }
            
            if (MaxLevelOfBlue < upgraded.getLevel() && upgraded.getType().equals("BlueAllies")) {
                MaxLevelOfBlue++;
                upgradeRandomSpecialAlly();
            }
        }
    }
    

    private Allies upgradeAlly(String type, int x, int y, int newLevel) {
        Allies ally;
        switch (type) {
            case "BlueAllies":
                ally = new BlueAllies(x, y);
                break;
            case "BrownAllies":
                ally = new BrownAllies(x, y, playerManager, player);
                break;
            case "OrangeAllies":
                ally = new OrangeAllies(x, y, playerManager);
                break;
            case "PurpleAllies":
                ally = new PurpleAllies(x, y, playerManager);
                break;
            default:
                ally = new BlueAllies(x, y);
        }
        ally.setLevel(newLevel);
        return ally;
    }

    private void upgradeRandomSpecialAlly() {
        List<Allies> specialList = new ArrayList<>();
    
        if (brownAllies != null) specialList.add(brownAllies);
        if (orangeAllies != null) specialList.add(orangeAllies);
        if (purpleAllies != null) specialList.add(purpleAllies);
    
        if (!specialList.isEmpty()) {
            Allies toUpgrade = specialList.get(random.nextInt(specialList.size()));
    
            String type = toUpgrade.getType();
            int level = toUpgrade.getLevel();
            int x = toUpgrade.getX();
            int y = toUpgrade.getY();
    
            alliesManager.remove(toUpgrade);
            alliesMoveManager.removeIf(m -> m.getMovedObject() == toUpgrade);
            for (MoveManager mm : new ArrayList<>(alliesMoveManager)) {
                if (mm.getMovedObject() == toUpgrade) {
                    mm.cleanup();
                }
            }
    
            Allies upgraded = upgradeAlly(type, x, y, level + 1);
            alliesManager.add(upgraded);
            alliesMoveManager.add(new MoveManager(upgraded));
    
            switch (type) {
                case "BrownAllies":
                    brownAllies = (BrownAllies) upgraded;
                    break;
                case "OrangeAllies":
                    orangeAllies = (OrangeAllies) upgraded;
                    break;
                case "PurpleAllies":
                    purpleAllies = (PurpleAllies) upgraded;
                    break;
            }
        }
    }

    private void avoidOverlapping() {
        List<MoveManager> managers = new ArrayList<>();
        managers.add(playerManager);
        managers.addAll(alliesMoveManager);
        int n = managers.size();
        // System.out.println("Size is = "+n);
        float[] offsetX = new float[n];
        float[] offsetY = new float[n];
        for (int i = 0; i < n; i++) {
            offsetX[i] = 0;
            offsetY[i] = 0;
        }
        float minDistance = 10f;
        float damping = 0.3f;
        for (int i = 0; i < n; i++) {
            Point2D.Float pos1 = managers.get(i).getPixelPos2D();
            for (int j = i + 1; j < n; j++) {
                Point2D.Float pos2 = managers.get(j).getPixelPos2D();
                float dx = pos1.x - pos2.x;
                float dy = pos1.y - pos2.y;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                if (dist < minDistance && dist > 0) {
                    float overlap = (minDistance - dist) / 2f;
                    float adjustX = (dx / dist) * overlap * damping;
                    float adjustY = (dy / dist) * overlap * damping;
                    offsetX[i] += adjustX;
                    offsetY[i] += adjustY;
                    offsetX[j] -= adjustX;
                    offsetY[j] -= adjustY;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            Point2D.Float pos = managers.get(i).getPixelPos2D();
            pos.x += offsetX[i];
            pos.y += offsetY[i];
            managers.get(i).setPixelPos(pos);
        }
    }

    public void draw(Graphics g) {
        int[][] costField = Barrier.getCostField();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                g.setColor(costField[r][c] == 1000 ? Color.BLACK : Color.WHITE);
                g.fillRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
        enemiesManager.render(g);

        playerPixel = playerManager.getPixelPosition();
        g.setColor(Color.RED);
        g.fillOval(playerPixel.x - 4, playerPixel.y - 4, 8, 8);


        List<Allies> newList = alliesManager.getAlliesList();
        for (int i = 0; i < Math.min(alliesMoveManager.size(), newList.size()); i++) {
            MoveManager manager = alliesMoveManager.get(i);
            Point allyPixel = manager.getPixelPosition();
            Allies ally = newList.get(i);
            ally.draw(g, allyPixel.x, allyPixel.y);
        }
    }

    public void updateGame(float dT) {
        if (targetPosition != null) {
            moveEntities(dT);
            avoidOverlapping();
        }
        enemiesManager.update();
//        bullets.removeIf(bullet -> {
//            bullet.move();
//            return bullet.isOutOfBounds();
//        });

        for (Allies ally : alliesManager.getAlliesList()) {
            ally.update();
        }

        player.setAlliesManager(alliesManager);
        // System.out.println(countBrown);
        // System.out.println(countOrange);
        // System.out.println(countPurple);

        removeDeadEnemies();
    }

    private void moveEntities(float dT) {
        List<MoveManager> allManagers = new ArrayList<>();
        allManagers.add(playerManager);
        allManagers.addAll(alliesMoveManager);
        boolean allStopped = true;
        if (playerManager.isMoving()) {
            playerManager.moveObject(dT, allManagers);
            allStopped = false;
        }
        for (MoveManager manager : new ArrayList<>(alliesMoveManager)) {
            if (manager.isMoving()) {
                manager.moveObject(dT, allManagers);
                allStopped = false;
            }
        }
        if (allStopped) {
            targetPosition = null;
        }
    }

    private boolean isValidPosition(Point p) {
        int[][] costField = Barrier.getCostField();
        return p.x >= 0 && p.x < COLS && p.y >= 0 && p.y < ROWS && costField[p.y][p.x] != 1000;
    }

    private List<Point> calculateFormation(Point center) {
        List<Point> positions = new ArrayList<>();
        Set<Point> visited = new HashSet<>();
        Queue<Point> queue = new LinkedList<>();
        queue.add(center);
        visited.add(center);
        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 },
                { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };
        while (!queue.isEmpty() && positions.size() < noOfAllies + 1) {
            Point current = queue.poll();
            if (isValidPosition(current)) {
                positions.add(current);
                if (positions.size() == noOfAllies + 1)
                    break;
            }
            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];
                Point neighbor = new Point(nx, ny);
                if (nx >= 0 && nx < COLS && ny >= 0 && ny < ROWS && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return positions;
    }

    public void onMouseClick(java.awt.event.MouseEvent e) {
        targetPosition = new Point(e.getX() / TILE_SIZE, e.getY() / TILE_SIZE);
        if (orangeAllies == null || !orangeAllies.isUseSkill) {
            assignTargetPositions(targetPosition);
        } else {
            orangeAllies.setUseSkill(false);
            assignTargetPositions(targetPosition);
        }
    }

    private void assignTargetPositions(Point center) {
            List<Point> positions = calculateFormation(center);
            playerManager.setTarget(positions.get(0));
            for (int i = 0; i < alliesMoveManager.size(); i++) {
                alliesMoveManager.get(i).setTarget(positions.get(i + 1));
            }
    }

    public BrownAllies getBrownAllies() {
        return brownAllies;
    }

    public OrangeAllies getOrangeAllies() {
        return orangeAllies;
    }

    public PurpleAllies getPurpleAllies() {
        return purpleAllies;
    }

    public Player getPlayer() {
        return player;
    }
}
