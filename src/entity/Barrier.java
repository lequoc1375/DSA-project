package entity;

import java.awt.Point;
import java.util.*;

public class Barrier {
    private static final int ROWS = 60;
    private static final int COLS = 60;
    private static final int[][] costField = new int[ROWS][COLS];
    private List<Integer> count = new ArrayList<>();

    public Barrier() {
        // Mặc định đặt tất cả các ô có giá trị 1 (có thể đi qua)
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                costField[r][c] = 1;
            }
        }
    }

    public void generateObstacles(Point start, Point turretPos) {
        Random rand = new Random();
        int totalObstacles = (ROWS * COLS) / 5; // Tổng số ô chướng ngại vật
        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
        Set<Point> usedPositions = new HashSet<>();

        int placedObstacles = 0;
        while (placedObstacles < totalObstacles) {

            int x = rand.nextInt(COLS);
            int y = rand.nextInt(ROWS);
            Point startPoint = new Point(x, y);

            if (!start.equals(startPoint) &&
                    !turretPos.equals(startPoint) &&
                    costField[y][x] != 1000) {

                int clusterSize = 2 + rand.nextInt(4);
                if (placedObstacles + clusterSize > totalObstacles) {
                    clusterSize = totalObstacles - placedObstacles;
                }

                if (clusterSize >= 2) {
                    List<Point> cluster = new ArrayList<>();
                    cluster.add(startPoint);
                    usedPositions.add(startPoint);

                    int attempts = 0;
                    while (cluster.size() < clusterSize && attempts < 10) {
                        Point current = cluster.get(rand.nextInt(cluster.size()));
                        int[] dir = directions[rand.nextInt(directions.length)];
                        int newX = current.x + dir[0];
                        int newY = current.y + dir[1];
                        Point newPoint = new Point(newX, newY);

                        if (isValidPosition(newX, newY) &&
                                !start.equals(newPoint) &&
                                !turretPos.equals(newPoint) &&
                                !usedPositions.contains(newPoint)) {
                            cluster.add(newPoint);
                            usedPositions.add(newPoint);
                        }
                        attempts++;
                    }

                    if (cluster.size() >= 2) {
                        for (Point p : cluster) {
                            costField[p.y][p.x] = 1000;
                        }
                        placedObstacles += cluster.size();
                    }
                }
            }
        }
    }

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