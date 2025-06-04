package manager;

import entity.Barrier;
import entity.MovedObject.ObjectCanMove;
import entity.MovedObject.Player;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;
import main.GamePanel;

public class MoveManager {
    private static final int ROWS = GamePanel.ROWS;
    private static final int COLS = GamePanel.COLS;
    private static final int TILE_SIZE = 16;
    private static final int[][] costMap = new int[ROWS][COLS];
    private static final int MAX_OBJECTS_PER_CELL = 2;
    private static final List<Point> stationaryObjects = new ArrayList<>();
    private static final Map<Point, Integer> objectCount = new HashMap<>();
    private static final Set<Point> reservedPositions = new HashSet<>();

    private final Point[][] flowField = new Point[ROWS][COLS];
    private final ObjectCanMove movingObject;
    private Point targetPos;
    private boolean isMoving;
    private int stuckCounter = 0;
    private Point currentGrid;
    private Point2D.Float pixelPos;

    public MoveManager(ObjectCanMove movingObject) {

        this.movingObject = movingObject;
        this.isMoving = false;
        Point initialPos = movingObject.getPosition();
        currentGrid = initialPos;
        pixelPos = new Point2D.Float(initialPos.x * TILE_SIZE + TILE_SIZE / 2f, initialPos.y * TILE_SIZE + TILE_SIZE / 2f);
        updateObjectCount(initialPos, 1);
        reservedPositions.add(initialPos);
        initializeCostMap();
    }

    public static void initializeCostMap() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                costMap[r][c] = 1;
            }
        }
        for (Point stationary : stationaryObjects) {
            costMap[stationary.y][stationary.x] = 1000;
        }
    }

    public void setTarget(Point target) {
        if (!Barrier.isObstacle(target.x, target.y)) {
            this.targetPos = target;
            computeFlowField();
            isMoving = true;
            stuckCounter = 0;
        }
    }

    private void computeFlowField() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                flowField[r][c] = null;
            }
        }
        int[][] distances = new int[ROWS][COLS];
        for (int[] row : distances)
            Arrays.fill(row, Integer.MAX_VALUE);
        Queue<Point> queue = new LinkedList<>();
        distances[targetPos.y][targetPos.x] = 0;
        queue.add(targetPos);
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        while (!queue.isEmpty()) {
            Point current = queue.poll();
            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];
                if (isValidPosition(nx, ny) && !Barrier.isObstacle(nx, ny)) {
                    int moveCost = (dir[0] != 0 && dir[1] != 0) ? 14 : 10;
                    int occupancyCost = objectCount.getOrDefault(new Point(nx, ny), 0) * 200;
                    int newCost = distances[current.y][current.x] + moveCost + costMap[ny][nx] + occupancyCost;

                    if (newCost < distances[ny][nx]) {
                        distances[ny][nx] = newCost;
                        flowField[ny][nx] = current;
                        queue.add(new Point(nx, ny));
                    }
                }
            }
        }
    }

    public void moveObject(float deltaTime, List<MoveManager> allManagers) {
        if (targetPos == null) {
            isMoving = false;
            return;
        }
        Point nextGrid = flowField[currentGrid.y][currentGrid.x];
        if (nextGrid == null || !canMoveTo(nextGrid)) {
            stuckCounter++;
            if (stuckCounter > 3) {
                computeFlowField();
                stuckCounter = 0;
            }
            return;
        } else {
            stuckCounter = 0;
        }
        float targetPixelX = nextGrid.x * TILE_SIZE + TILE_SIZE / 2f;
        float targetPixelY = nextGrid.y * TILE_SIZE + TILE_SIZE / 2f;
        float dx = targetPixelX - pixelPos.x;
        float dy = targetPixelY - pixelPos.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        if (distance < Player.speed * deltaTime) {
            float t = (Player.speed * deltaTime) / distance;
            pixelPos.x = pixelPos.x + dx * t;
            pixelPos.y = pixelPos.y + dy * t;
            updateObjectCount(currentGrid, -1);
            reservedPositions.remove(currentGrid);
            currentGrid = nextGrid;
            updateObjectCount(currentGrid, 1);
            movingObject.setPosition(currentGrid);
            if (currentGrid.equals(targetPos)) {
                isMoving = false;
            }
        } else {
            float seekX = (dx / distance) * Player.speed;
            float seekY = (dy / distance) * Player.speed;
            Point2D.Float separationVector = new Point2D.Float(0, 0);
            float separationDistance = 2 * TILE_SIZE;
            for (MoveManager other : allManagers) {
                if (other != this) {
                    Point2D.Float otherPos = other.getPixelPos2D();
                    float sepDx = pixelPos.x - otherPos.x;
                    float sepDy = pixelPos.y - otherPos.y;
                    float sepDistance = (float) Math.sqrt(sepDx * sepDx + sepDy * sepDy);
                    if (sepDistance < separationDistance && sepDistance > 0) {
                        float force = Math.min((separationDistance - sepDistance) / sepDistance, 1.0f) * 0.5f;
                        separationVector.x += (sepDx / sepDistance) * force;
                        separationVector.y += (sepDy / sepDistance) * force;
                    }
                }
            }
            float sepMagnitude = (float) Math.sqrt(separationVector.x * separationVector.x + separationVector.y * separationVector.y);
            float sepX = 0, sepY = 0;
            if (sepMagnitude > 0) {
                float separationSpeed = Player.speed * 0.3f;
                if (sepMagnitude > 0) {
                    sepX = (separationVector.x / sepMagnitude) * Math.min(separationSpeed, sepMagnitude);
                    sepY = (separationVector.y / sepMagnitude) * Math.min(separationSpeed, sepMagnitude);
                }
            }
            float velX = seekX + sepX;
            float velY = seekY + sepY;
            float velMagnitude = (float) Math.sqrt(velX * velX + velY * velY);
            if (velMagnitude > Player.speed) {
                velX = (velX / velMagnitude) * Player.speed;
                velY = (velY / velMagnitude) * Player.speed;
            }
            pixelPos.x += velX * deltaTime;
            pixelPos.y += velY * deltaTime;
        }
    }

    private boolean canMoveTo(Point pos) {
        int currentCount = objectCount.getOrDefault(pos, 0);
        return isValidPosition(pos.x, pos.y) && !Barrier.isObstacle(pos.x, pos.y) && currentCount < MAX_OBJECTS_PER_CELL && !reservedPositions.contains(pos);
    }

    private void updateObjectCount(Point pos, int delta) {
        int count = objectCount.getOrDefault(pos, 0) + delta;
        if (count < 0) {
            count = 0;
        }
        objectCount.put(pos, count);
        int baseCost = stationaryObjects.contains(pos) ? 1000 : 1;
        costMap[pos.y][pos.x] = baseCost + 100 * count;
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < COLS && y >= 0 && y < ROWS;
    }

    public Point getPixelPosition() {
        return new Point((int) pixelPos.x, (int) pixelPos.y);
    }

    public void setPixelPosition(Point pos) {
        pixelPos.x = pos.x;
        pixelPos.y = pos.y;
    }

    public Point2D.Float getPixelPos2D() {
        return pixelPos;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void addStationNaryObject(Point pos) {
        stationaryObjects.add(pos);
    }

    public void setPixelPos(Point2D.Float pos) {
        pixelPos = pos;
    }

    public void stopMoving() {
        isMoving = false;
        targetPos = null;
        stuckCounter = 0;
    }

    public void teleportTo(Point newGrid) {

        updateObjectCount(currentGrid, -1);
        reservedPositions.remove(currentGrid);


        currentGrid = newGrid;
        movingObject.setPosition(newGrid);

        pixelPos.x = newGrid.x * TILE_SIZE + TILE_SIZE / 2f;
        pixelPos.y = newGrid.y * TILE_SIZE + TILE_SIZE / 2f;

        updateObjectCount(currentGrid, 1);
        reservedPositions.add(currentGrid);

        isMoving = false;
        targetPos = null;
        stuckCounter = 0;
    }

    public ObjectCanMove getMovedObject() {
        return movingObject;
    }

    public void cleanup() {
        updateObjectCount(currentGrid, -1);
        reservedPositions.remove(currentGrid);
    }
    
    public Rectangle getBound() {
        int x = (int) (pixelPos.x - TILE_SIZE / 2f);
        int y = (int) (pixelPos.y - TILE_SIZE / 2f);
        return new Rectangle(x, y, TILE_SIZE, TILE_SIZE);
    }

    public Point[][] getFlowField() {
        return flowField;
    }

    
    
}