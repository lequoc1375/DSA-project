package manager;

import entity.Barrier;
import entity.MovedObject.ObjectCanMove;
import java.awt.Point;
import java.util.*;

public class MoveManager {
    private static final int ROWS = 40;
    private static final int COLS = 40;
    private Point[][] flowField = new Point[ROWS][COLS];
    private static final int[][] costMap = new int[ROWS][COLS];
    private static final int MAX_OBJECTS_PER_CELL = 2;

    private Point targetPos;
    private boolean isMoving;
    private ObjectCanMove movingObject;
    private static final List<Point> stationaryObjects = new ArrayList<>();
    private static final Map<Point, Integer> objectCount = new HashMap<>();
    private int stuckCounter = 0;
    private static final Set<Point> reservedPositions = new HashSet<>(); 

    public MoveManager(ObjectCanMove movingObject) {
        this.movingObject = movingObject;
        this.isMoving = false;
        Point initialPos = movingObject.getPosition();
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

    public static void addStationaryObject(Point position) {
        stationaryObjects.add(position);
        costMap[position.y][position.x] = 1000;
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

        int[][] directions = {
                { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 }, { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } 
        };

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

    public void moveObject() {
        Point currentPos = movingObject.getPosition();
        if (currentPos == null) {
            isMoving = false;
            return;
        }

        if (currentPos.equals(targetPos)) {
            isMoving = false;
            stuckCounter = 0;
            reservedPositions.remove(currentPos);
            return;
        }

        Point next = flowField[currentPos.y][currentPos.x];

        if (next == null || !canMoveTo(next)) {
            stuckCounter++;
            if (stuckCounter > 3) {
                computeFlowField();
                stuckCounter = 0;
                next = flowField[currentPos.y][currentPos.x];
            }
            if (next == null || !canMoveTo(next)) {
                next = findAlternativeMove(currentPos);
            }
        } else {
            stuckCounter = 0;
        }

        if (next != null && canMoveTo(next) && reservePosition(next)) {
            updateObjectCount(currentPos, -1);
            reservedPositions.remove(currentPos);
            movingObject.setPosition(next);
            updateObjectCount(next, 1);
            if (objectCount.get(next) >= MAX_OBJECTS_PER_CELL - 1) {
                computeFlowField();
            }
        } else {
            if (next == null) {
                isMoving = false;
                reservedPositions.remove(currentPos);
            }
           
        }
    }

    private boolean canMoveTo(Point pos) {
        int currentCount = objectCount.getOrDefault(pos, 0);
        return isValidPosition(pos.x, pos.y) &&
                !Barrier.isObstacle(pos.x, pos.y) &&
                currentCount < MAX_OBJECTS_PER_CELL &&
                !reservedPositions.contains(pos);
    }

    private boolean reservePosition(Point pos) {
        if (reservedPositions.contains(pos)) {
            return false;
        }
        reservedPositions.add(pos);
        return true;
    }

    private Point findAlternativeMove(Point currentPos) {
        int[][] directions = {
                { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 }, { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } 
        };

        Point bestAlternative = null;
        int minDistance = Integer.MAX_VALUE;

        for (int[] dir : directions) {
            int nx = currentPos.x + dir[0];
            int ny = currentPos.y + dir[1];
            Point candidate = new Point(nx, ny);

            if (canMoveTo(candidate)) {
                int distToTarget = manhattanDistance(candidate, targetPos);
                if (distToTarget < minDistance) {
                    minDistance = distToTarget;
                    bestAlternative = candidate;
                }
            }
        }
        return bestAlternative;
    }

    private int manhattanDistance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
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

    public Point getObjectPos() {
        return movingObject.getPosition();
    }

    public boolean isMoving() {
        return isMoving;
    }

    public Point getTarget() {
        return targetPos;
    }
    public static int getObjectCountAt(Point pos) {
        return objectCount.getOrDefault(pos, 0);
    }
}