package main;

import controller.MouseHandler;
import entity.*;
import entity.MovedObject.Allies;
import entity.MovedObject.Player;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import javax.swing.*;
import manager.*;

public class GamePanel extends JPanel implements Runnable {
    public static final int TILE_SIZE = 16;
    private static final int ROWS = 40;
    private static final int COLS = 40;
    private static final float TIME_STEP = 0.04f;

    private Player player;
    private MoveManager playerManager;
    private Barrier barrier;
    private Turret turret;
    private List<Bullet> bullets = new ArrayList<>();
    private List<Allies> alliesList = new ArrayList<>();
    private List<MoveManager> alliesManagers = new ArrayList<>();
    private Point targetPosition = null;

    public GamePanel() {
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
        initGenerate();
        new Timer(1000, e -> turret.fireBullet(player.getPosition())).start();
        new Thread(this).start();
        addMouseListener(new MouseHandler(this));
    }

    public void initGenerate() {
        player = new Player(10, 10);
        turret = new Turret(20, 20, bullets);
        barrier = new Barrier();
        playerManager = new MoveManager(player);

        alliesList.clear();
        alliesManagers.clear();

        for (int i = 0; i < 3; i++) {
            Allies ally = new Allies(40, 40);
            alliesList.add(ally);
            alliesManagers.add(new MoveManager(ally));
        }

        barrier.generateObstacles(player.getPosition(), new Point(turret.getX(), turret.getY()));
    }

    @Override
    public void run() {
        while (true) {
            updateGame(TIME_STEP);
            repaint();
            try {
                Thread.sleep((int) (TIME_STEP * 1700)); // 40ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGame(float dT) {
        if (targetPosition != null) {
            moveEntities();
        }

        bullets.removeIf(bullet -> {
            bullet.move();
            return bullet.isOutOfBounds();
        });

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawEntity(g);
    }

    public void drawEntity(Graphics g) {
        int[][] costField = Barrier.getCostField();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                g.setColor(costField[r][c] == 1000 ? Color.BLACK : Color.WHITE);
                g.fillRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                g.setColor(Color.GRAY);
                g.drawRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        player.draw(g, 8);

        for (Allies ally : alliesList) {
            ally.draw(g, 8);
        }

        turret.draw(g);

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    public void onMouseClick(MouseEvent e) {
        targetPosition = new Point(e.getX() / TILE_SIZE, e.getY() / TILE_SIZE);
        assignTargetPositions(targetPosition);
    }

    private void assignTargetPositions(Point center) {
        List<Point> positions = calculateFormation(center);

        playerManager.setTarget(positions.get(0));
        for (int i = 0; i < alliesManagers.size(); i++) {
            alliesManagers.get(i).setTarget(positions.get(i + 1));
        }
    }

    private List<Point> calculateFormation(Point center) {
        List<Point> positions = new ArrayList<>();
        Set<Point> visited = new HashSet<>();
        Queue<Point> queue = new LinkedList<>();

        // Thêm điểm trung tâm vào danh sách duyệt
        queue.add(center);
        visited.add(center);

        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 }, { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };

        while (!queue.isEmpty() && positions.size() < 4) {
            Point current = queue.poll();

            if (isValidPosition(current)) {
                positions.add(current);
                if (positions.size() == 4)
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

    private boolean isValidPosition(Point p) {
        int[][] costField = Barrier.getCostField();
        return p.x >= 0 && p.x < COLS && p.y >= 0 && p.y < ROWS && costField[p.y][p.x] != 1000;
    }

    private void moveEntities() {
        boolean allStopped = true;

        if (playerManager.isMoving()) {
            playerManager.moveObject();
            allStopped = false;
        }

        for (MoveManager manager : alliesManagers) {
            if (manager.isMoving()) {
                manager.moveObject();
                allStopped = false;
            }
        }

        if (allStopped) {
            targetPosition = null;
        }
    }
}
