package manager;

import entity.Barrier;
import entity.MovedObject.ObjectCanMove;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class MoveManager {
    private static final int ROWS = 60;
    private static final int COLS = 60;
    private static final int TILE_SIZE = 16;
    private static final int[][] costMap = new int[ROWS][COLS];
    private static final int MAX_OBJECTS_PER_CELL = 2;
    private static final List<Point> stationaryObjects = new ArrayList<>();
    private static final Map<Point, Integer> objectCount = new HashMap<>();
    private static final Set<Point> reservedPositions = new HashSet<>();
    private final float speed = 120;
    private final Point[][] flowField = new Point[ROWS][COLS];
    private Point targetPos;
    private boolean isMoving;
    private final ObjectCanMove movingObject;
    private int stuckCounter = 0;
    private Point currentGrid;
    private Point2D.Float pixelPos;

    public MoveManager(ObjectCanMove movingObject) {
        this.movingObject = movingObject;
        this.isMoving = false;
        Point initialPos = movingObject.getPosition();
        currentGrid = initialPos;
        pixelPos = new Point2D.Float(initialPos.x * TILE_SIZE + TILE_SIZE / 2f,
                initialPos.y * TILE_SIZE + TILE_SIZE / 2f);
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
        // Reset flow field
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

        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 }, { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };

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

        //Center of next grid
        float targetPixelX = nextGrid.x * TILE_SIZE + TILE_SIZE / 2f;
        float targetPixelY = nextGrid.y * TILE_SIZE + TILE_SIZE / 2f;
        //distance from center of next grid to center current grid
        float dx = targetPixelX - pixelPos.x;
        float dy = targetPixelY - pixelPos.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < speed * deltaTime) { // s = v*t
            
            float t = (speed * deltaTime) / distance; // Tỷ lệ  s1/s2
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
            // Tính velocity vector
            float seekX = (dx / distance) * speed; // unit vecto vận tốc
            float seekY = (dy / distance) * speed;

            Point2D.Float separationVector = new Point2D.Float(0, 0); //Declare vecto tổng hợp
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
                        separationVector.y += (sepDy / sepDistance) * force ;
                    }
                }
            }

            float sepMagnitude = (float) Math
                    .sqrt(separationVector.x * separationVector.x + separationVector.y * separationVector.y);
            float sepX = 0, sepY = 0;
            if (sepMagnitude > 0) { // Tránh trường hợp = 0
                float separationSpeed = speed * 0.3f; // giới hạn tốc độ tránh
                if (sepMagnitude > 0) { //Tính vecto đợn vị của vecto tổng hợp
                    sepX = (separationVector.x / sepMagnitude) * Math.min(separationSpeed, sepMagnitude);

                    sepY = (separationVector.y / sepMagnitude) * Math.min(separationSpeed, sepMagnitude);
                }
            }

            float velX = seekX + sepX; //Vecto tổng hợp từ vecto đơn vị giữa object đến nextGrid và vecto đơn vị tổng hợp
            float velY = seekY + sepY;

            float velMagnitude = (float) Math.sqrt(velX * velX + velY * velY);
            if (velMagnitude > speed) {
                velX = (velX / velMagnitude) * speed; // tính độ dời 
                velY = (velY / velMagnitude) * speed;
            }

            pixelPos.x += velX * deltaTime;
            pixelPos.y += velY * deltaTime;
        }
    }

    private boolean canMoveTo(Point pos) {
        int currentCount = objectCount.getOrDefault(pos, 0);
        return isValidPosition(pos.x, pos.y) && !Barrier.isObstacle(pos.x, pos.y) &&
                currentCount < MAX_OBJECTS_PER_CELL && !reservedPositions.contains(pos);
    }

    private void updateObjectCount(Point pos, int delta) {
        int count = objectCount.getOrDefault(pos, 0) + delta;
        if (count < 0) {
            count = 0;
        }
        objectCount.put(pos, count);
        int baseCost = stationaryObjects.contains(pos) ? 1000 : 1;
        costMap[pos.y][pos.x] = baseCost + 100 * count; // Adjust cost based on occupancy
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < COLS && y >= 0 && y < ROWS;
    }

    public Point getPixelPosition() {
        return new Point((int) pixelPos.x, (int) pixelPos.y);
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
}