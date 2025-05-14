package entity;

import java.awt.Point;
import java.util.*;
import main.GamePanel;

public class Barrier {
    private static final int ROWS = GamePanel.ROWS;
    private static final int COLS = GamePanel.COLS;
    private static final int[][] costField = new int[ROWS][COLS];
    private List<Integer> count = new ArrayList<>();

    public Barrier() {

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                costField[r][c] = 1;
            }
        }
    }

    public void generateObstacles(Point start) {
        Random rand = new Random();
        int totalObstacles = (ROWS * COLS) / 5;
        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
        Set<Point> usedPositions = new HashSet<>();

        int placedObstacles = 0;
        int maxTotalAttempts = totalObstacles * 10;
        int totalAttempts = 0;

        System.out.println("1.4.4.1");
        while (placedObstacles < totalObstacles && totalAttempts < maxTotalAttempts) {
            totalAttempts++;
            int x = rand.nextInt(COLS);
            int y = rand.nextInt(ROWS);
            Point startPoint = new Point(x, y);
            System.out.println("1.4.4.2");

            if (!start.equals(startPoint) && costField[y][x] != OBSTACLE_COST) {
                System.out.println("1.4.4.3");
                int clusterSize = 2 + rand.nextInt(4);
                if (placedObstacles + clusterSize > totalObstacles) {
                    clusterSize = totalObstacles - placedObstacles;
                }
                System.out.println("1.4.4.4");

                List<Point> cluster = new ArrayList<>();
                cluster.add(startPoint);
                usedPositions.add(startPoint);
                System.out.println("1.4.4.5");

                int attempts = 0;
                while (cluster.size() < clusterSize && attempts < 20) {
                    Point current = cluster.get(rand.nextInt(cluster.size()));
                    int[] dir = directions[rand.nextInt(directions.length)];
                    int newX = current.x + dir[0];
                    int newY = current.y + dir[1];
                    Point newPoint = new Point(newX, newY);

                    if (isValidPosition(newX, newY)
                            && !start.equals(newPoint)
                            && !usedPositions.contains(newPoint)
                            && costField[newY][newX] != OBSTACLE_COST) {
                        cluster.add(newPoint);
                        usedPositions.add(newPoint);
                    }
                    attempts++;
                }

                System.out.println("1.4.4.6");
                if (cluster.size() >= 2) {
                    for (Point p : cluster) {
                        costField[p.y][p.x] = OBSTACLE_COST;
                    }
                    placedObstacles += cluster.size();
                } else {
                    costField[startPoint.y][startPoint.x] = OBSTACLE_COST;
                    placedObstacles++;
                }
            }
        }
        System.out.println("1.4.4.7");
    }

    private static final int OBSTACLE_COST = 1000;


    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < COLS && y >= 0 && y < ROWS;
    }

    public static int[][] getCostField() {
        return costField;
    }

    public static boolean isObstacle(int x, int y) {
        return costField[y][x] == 1000;
    }

    public static void setObstacle(int x, int y) {
        costField[y][x] = 1000;
    }


}