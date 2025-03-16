package entity;

import java.awt.*;
import static main.GamePanel.TILE_SIZE;

public class Boom {

    private Point point;
    private boolean exploded = false;
    private long explosionStartTime = 0;
    private long explosionDuration = 1000; // hiệu ứng nổ kéo dài 1 giây

    public Boom(Point point) {
        this.point = point;
    }

    // Gọi khi người chơi dẫm mìn
    public void triggerExplosion() {
        if (!exploded) {
            exploded = true;
            explosionStartTime = System.currentTimeMillis();
        }
    }

    // Vẽ mìn bình thường hoặc hiệu ứng nổ nếu đã kích hoạt
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
        int size = TILE_SIZE; // 16 pixel
        int centerX = x + size / 2;
        int centerY = y + size / 2;

        int radius = 6;         // bán kính thân mìn
        int spikeExtension = 2; // spike kéo dài thêm 2px

        // Vẽ thân mìn
        g2d.setColor(Color.GRAY);
        g2d.fillOval(x, y, size, size);

        // Vẽ viền
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawOval(x, y, size, size);

        // Vẽ spike
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
        Graphics2D g2d = (Graphics2D) g;
        // Bật antialiasing để hiệu ứng mịn màng
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = point.x;
        int y = point.y;
        int size = TILE_SIZE; // 16 pixel
        int centerX = x + size / 2;
        int centerY = y + size / 2;

        // Cho hiệu ứng nổ, ta có thể cho bán kính nổ lớn hơn (ví dụ 10px)
        int explosionRadius = 10;
        int diameter = explosionRadius * 2;

        // Sử dụng RadialGradientPaint để tạo hiệu ứng nổ
        float[] fractions = {0.0f, 0.5f, 1.0f};
        Color[] colors = {
                new Color(255, 255, 0, 255),  // Vàng rực
                new Color(255, 165, 0, 200),    // Cam mờ
                new Color(255, 0, 0, 0)         // Đỏ trong suốt
        };
        RadialGradientPaint rgp = new RadialGradientPaint(centerX, centerY, explosionRadius, fractions, colors);
        g2d.setPaint(rgp);
        g2d.fillOval(centerX - explosionRadius, centerY - explosionRadius, diameter, diameter);
    }

    // Có thể thêm phương thức update() nếu cần xử lý thời gian nổ (ví dụ, tự xóa mìn sau khi hết hiệu ứng)
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
