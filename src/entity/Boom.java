package entity;

import java.awt.*;
import javax.swing.Timer;
import static main.GamePanel.TILE_SIZE;

public class Boom {

    private Point point;
    private boolean exploded = false;
    private long explosionStartTime = 0;
    private long explosionDuration = 500; 
    private Timer timeExist;

    public Boom(Point point) {
        this.point = point;
    }

    
    public void triggerExplosion() {
        if (!exploded) {
            exploded = true;
            explosionStartTime = System.currentTimeMillis();
        }
    }


    public void draw(Graphics g) {
        if (!exploded) {
            drawNormal(g);
        } else {
            drawExplosion(g);
        }
    }

    private void drawNormal(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = point.x;
        int y = point.y;
        int size = TILE_SIZE; 
        int centerX = x + size / 2;
        int centerY = y + size / 2;

        int radius = 6;         
        int spikeExtension = 2; 

    
        g2d.setColor(Color.GRAY);
        g2d.fillOval(x, y, size, size);

      
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawOval(x, y, size, size);


        int numSpikes = 8;
        for (int i = 0; i < numSpikes; i++) {
            double angle = Math.toRadians(i * (360.0 / numSpikes));
            int spikeStartX = (int) (centerX + Math.cos(angle) * radius);
            int spikeStartY = (int) (centerY + Math.sin(angle) * radius);
            int spikeEndX = (int) (centerX + Math.cos(angle) * (radius + spikeExtension));
            int spikeEndY = (int) (centerY + Math.sin(angle) * (radius + spikeExtension));
            g2d.drawLine(spikeStartX, spikeStartY, spikeEndX, spikeEndY);
        }
    }

    private void drawExplosion(Graphics g) {
        long elapsed = System.currentTimeMillis() - explosionStartTime;
    
        if ((elapsed / 200) % 2 == 0) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
            int x = point.x;
            int y = point.y;
            int size = TILE_SIZE;
            int centerX = x + size / 2;
            int centerY = y + size / 2;
    
            int explosionRadius = 10;
            int diameter = explosionRadius * 2;
    
            float[] fractions = {0.0f, 0.5f, 1.0f};
            Color[] colors = {
                new Color(255, 255, 0, 255), 
                new Color(255, 165, 0, 200), 
                new Color(255, 0, 0, 0)      
            };
            RadialGradientPaint rgp = new RadialGradientPaint(centerX, centerY, explosionRadius, fractions, colors);
            g2d.setPaint(rgp);
            g2d.fillOval(centerX - explosionRadius, centerY - explosionRadius, diameter, diameter);
        }
    }
    

  
    public boolean isExplosionOver() {
        if (!exploded) return false;
        return System.currentTimeMillis() - explosionStartTime >= explosionDuration;
    }

    public int getX() {
        return point.x;
    }

    public int getY() {
        return point.y;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
