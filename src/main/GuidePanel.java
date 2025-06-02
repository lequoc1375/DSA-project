package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class GuidePanel extends JPanel {
    private GamePanel gamePanel;
    private List<Image> pages;
    private int currentPageIndex;
    private JButton prevButton, nextButton;

    public GuidePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setOpaque(true);
        setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(GamePanel.COLS * GamePanel.TILE_SIZE, GamePanel.ROWS * GamePanel.TILE_SIZE));
        setLayout(null);
        loadPages();
        initComponents();
    }

    private void loadPages() {
        pages = new ArrayList<>();
        try {
            pages.add(new ImageIcon(getClass().getResource("/resources/page1.png")).getImage());
            pages.add(new ImageIcon(getClass().getResource("/resources/page2.png")).getImage());
            pages.add(new ImageIcon(getClass().getResource("/resources/page3.png")).getImage());
            pages.add(new ImageIcon(getClass().getResource("/resources/page4.png")).getImage());
            pages.add(new ImageIcon(getClass().getResource("/resources/page5.png")).getImage());
        } catch (Exception e) {
            System.out.println("Cannot load guide images: " + e.getMessage());
        }
    }

    private void initComponents() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        prevButton = createStyledButton("BACK");
        prevButton.addActionListener(e -> showPreviousPage());
        buttonPanel.add(prevButton);

        nextButton = createStyledButton("NEXT");
        nextButton.addActionListener(e -> showNextPage());
        buttonPanel.add(nextButton);

        buttonPanel.setBounds(0, 700, GamePanel.COLS * GamePanel.TILE_SIZE, 60);
        add(buttonPanel);
        updateNavigationButtons();

        setFocusable(true);
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }

    private void showPreviousPage() {
        if (currentPageIndex > 0) {
            currentPageIndex--;
            updateContent();
        }
    }

    private void showNextPage() {
        if (currentPageIndex < pages.size() - 1) {
            currentPageIndex++;
            updateContent();
        }
    }

    private void updateContent() {
        repaint();
        updateNavigationButtons();
    }

    private void updateNavigationButtons() {
        if (pages == null || pages.isEmpty()) {
            prevButton.setEnabled(false);
            nextButton.setEnabled(false);
        } else {
            prevButton.setEnabled(currentPageIndex > 0);
            nextButton.setEnabled(currentPageIndex < pages.size() - 1);
            if(currentPageIndex > 0) {
                prevButton.setVisible(true);
            } else {
                prevButton.setVisible(false);
            }
            if(currentPageIndex < pages.size() - 1) {
                nextButton.setVisible(true);
            } else {
                nextButton.setVisible(false);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (pages != null && !pages.isEmpty() && currentPageIndex < pages.size()) {
            Image img = pages.get(currentPageIndex);
            g2.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
            g2.drawString("Guide Not Available", getWidth() / 2 - 100, getHeight() / 2);
        }

        g2.dispose();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;
            private boolean isPressed = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 20, 20);

                GradientPaint gp;
                if (isPressed) {
                    gp = new GradientPaint(0, 0, new Color(50, 100, 150), 
                                          0, getHeight(), new Color(20, 60, 100));
                } else if (isHovered) {
                    gp = new GradientPaint(0, 0, new Color(100, 160, 210), 
                                          0, getHeight(), new Color(50, 100, 150));
                } else {
                    gp = new GradientPaint(0, 0, new Color(70, 130, 180), 
                                          0, getHeight(), new Color(30, 80, 120));
                }
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 20, 20);

                super.paintComponent(g);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }

            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        isHovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        isHovered = false;
                        repaint();
                    }

                    @Override
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        isPressed = true;
                        repaint();
                    }

                    @Override
                    public void mouseReleased(java.awt.event.MouseEvent evt) {
                        isPressed = false;
                        repaint();
                    }
                });
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    public void setPads(List<Image> pages) {
        this.pages = pages;
        currentPageIndex = 0;
        repaint();
        updateNavigationButtons();
    }
}