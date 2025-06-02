package ui;

import java.awt.*;

public class Button {
    private int x, y, width, height;
    private String text;
    private boolean hovered; // Track hover state

    public Button(String text, int x, int y, int width, int height) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hovered = false;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

    
        g2d.setColor(hovered ? new Color(150, 150, 255) : Color.GRAY); 
        g2d.fillRect(x, y, width, height);


        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);


        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));

        FontMetrics metrics = g2d.getFontMetrics();
        int textX = x + (width - metrics.stringWidth(text)) / 2;
        int textY = y + (height - metrics.getHeight()) / 2 + metrics.getAscent();
        g2d.drawString(text, textX, textY);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }
}