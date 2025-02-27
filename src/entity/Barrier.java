package entity;

import java.awt.*;
import java.util.Random;

public class Barrier {
    private static final int ROWS = 40;
    private static final int COLS = 40;
    private static final int[][] costField = new int[ROWS][COLS];

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
        int numObstacles = (ROWS * COLS) / 5;
        
        for (int i = 0; i < numObstacles; i++) {
            int x = rand.nextInt(COLS);
            int y = rand.nextInt(ROWS);
            if (!start.equals(new Point(x, y)) && !turretPos.equals(new Point(x, y))) {
                costField[y][x] = 1000; 
            }
        }
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
