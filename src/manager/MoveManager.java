package manager;

import entity.Barrier;
import entity.MovedObject.ObjectCanMove;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MoveManager {
    private static final int ROWS = 40;
    private static final int COLS = 40;
    private Point[][] flowField = new Point[ROWS][COLS]; 
    private static final int[][] costMap = new int[ROWS][COLS]; 

    private Point targetPos;
    private boolean moving;
    private ObjectCanMove objectCanMove;
    private static final int MAX_OBJECTS_PER_CELL = 2;

    private static List<Point> stationaryObjects = new ArrayList<>();
    private static Map<Point, Integer> objectCount = new HashMap<>();
    
    public MoveManager(ObjectCanMove objectCanMove) {
        this.objectCanMove = objectCanMove;
        this.moving = false;
        Point initialPos = objectCanMove.getPosition();
        objectCount.put(initialPos, objectCount.getOrDefault(initialPos, 0) + 1);
        costMap[initialPos.y][initialPos.x] = 1 + 100 * objectCount.get(initialPos);
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
            computeFlowField(target);
            moving = true;
        }
    }

    private void computeFlowField(Point target) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                flowField[r][c] = null;
            }
        }

        Queue<Point> queue = new LinkedList<>();
        int[][] distances = new int[ROWS][COLS];
        for (int[] row : distances)
            Arrays.fill(row, Integer.MAX_VALUE);

        distances[target.y][target.x] = 0;
        queue.add(target);

        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 }, { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];
                if (nx >= 0 && nx < COLS && ny >= 0 && ny < ROWS && !Barrier.isObstacle(nx, ny)) {
                    int newCost = distances[current.y][current.x] + costMap[ny][nx];
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
        Point objectPos = objectCanMove.getPosition();
        if (objectPos.equals(targetPos)) {
            moving = false;
            return;
        }

        Point next = flowField[objectPos.y][objectPos.x];
        if (next == null || Barrier.isObstacle(next.x, next.y) || objectCount.getOrDefault(next, 0) > 0) {
            computeFlowField(targetPos); 
            next = flowField[objectPos.y][objectPos.x];
        }

        if (objectCount.getOrDefault(next, 0) < MAX_OBJECTS_PER_CELL) {
          
            int count = objectCount.get(objectPos) - 1;
            objectCount.put(objectPos, count);
            int baseCost = stationaryObjects.contains(objectPos) ? 1000 : 1;
            costMap[objectPos.y][objectPos.x] = baseCost + 100 * count;

           
            objectCanMove.setPosition(next);
            count = objectCount.getOrDefault(next, 0) + 1;
            objectCount.put(next, count);
            baseCost = stationaryObjects.contains(next) ? 1000 : 1;
            costMap[next.y][next.x] = baseCost + 100 * count;
        } else {
            moving = false;
        }
    }

    public Point getObjectPos() {
        return objectCanMove.getPosition();
    }

    public boolean isMoving() {
        return moving;
    }

    public Point getTarget() {
        return targetPos;
    }
}