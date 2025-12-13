package id.ac.itb.if2010.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import id.ac.itb.if2010.App; 
import java.awt.image.BufferedImage;

public class LevelSelector extends JFrame {
    
    private int selectedTargetScore = 0; 
    
    private JButton btnEasy, btnMedium, btnHard;
    private JButton btnStart;

    public LevelSelector() {
        setTitle("Select Level");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        BufferedImage tempImage = null;
        try {
            tempImage = ImageIO.read(getClass().getResourceAsStream("/assets/level select bg.png"));
        } catch (Exception e) {
            System.err.println("Gagal memuat background: " + e.getMessage());
        }
        final BufferedImage bgImage = tempImage;

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                if (bgImage != null) {
                    double scale = Math.max((double)getWidth() / bgImage.getWidth(), (double)getHeight() / bgImage.getHeight());
                    int scaledW = (int) (bgImage.getWidth() * scale);
                    int scaledH = (int) (bgImage.getHeight() * scale);
                    int x = (getWidth() - scaledW) / 2;
                    int y = (getHeight() - scaledH) / 2;
                    g2d.drawImage(bgImage, x, y, scaledW, scaledH, null);
                } else {
                    g2d.setColor(new Color(240, 240, 240));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }

                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel headerLabel = new JLabel("SELECT LEVEL");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        headerLabel.setForeground(Color.BLACK);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel lvlPanel = new JPanel();
        lvlPanel.setLayout(new BoxLayout(lvlPanel, BoxLayout.Y_AXIS));
        lvlPanel.setOpaque(false); 
        
        btnEasy = createOptionButton("<html><center><b>EASY</b><br>Target: 150</center></html>");
        btnMedium = createOptionButton("<html><center><b>MEDIUM</b><br>Target: 250</center></html>");
        btnHard = createOptionButton("<html><center><b>HARD</b><br>Target: 400</center></html>");
        
        btnEasy.addActionListener(e -> selectDifficulty(btnEasy, 150, new Color(46, 204, 113))); 
        btnMedium.addActionListener(e -> selectDifficulty(btnMedium, 250, new Color(243, 156, 18))); 
        btnHard.addActionListener(e -> selectDifficulty(btnHard, 400, new Color(231, 76, 60))); 

        lvlPanel.add(btnEasy);
        lvlPanel.add(Box.createVerticalStrut(20)); 
        lvlPanel.add(btnMedium);
        lvlPanel.add(Box.createVerticalStrut(20)); 
        lvlPanel.add(btnHard);

        btnStart = new JButton("START GAME");
        btnStart.setFont(new Font("Arial", Font.BOLD, 20));
        btnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnStart.setMaximumSize(new Dimension(200, 60));
        
        btnStart.setBackground(Color.LIGHT_GRAY);
        btnStart.setForeground(Color.DARK_GRAY);
        
        btnStart.setOpaque(true);
        btnStart.setBorderPainted(false);
        btnStart.setFocusPainted(false);
        
        btnStart.setEnabled(false); 
        
        btnStart.addActionListener(e -> {
            if (selectedTargetScore > 0) {
                dispose();
                App.startGame("SUSHI", selectedTargetScore);
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(Color.BLACK);
        
        btnBack.setOpaque(true);
        btnBack.setBorderPainted(false);
        
        btnBack.addActionListener(e -> {
            dispose(); 
            new MainMenu().setVisible(true); 
        });

        mainPanel.add(Box.createVerticalStrut(40));
        mainPanel.add(headerLabel);
        mainPanel.add(Box.createVerticalStrut(40));
        mainPanel.add(lvlPanel);
        mainPanel.add(Box.createVerticalStrut(40));
        mainPanel.add(btnStart);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(btnBack);

        add(mainPanel);
    }
    
    private JButton createOptionButton(String text) {
        JButton btn = new JButton(text) {
            private final int RADIUS = 25; 
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS, RADIUS);
                
                if (getBackground() != Color.WHITE) {
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS, RADIUS);
                }

                super.paintComponent(g2); 
                g2.dispose();
            }

            @Override
            public void setContentAreaFilled(boolean b) {
                
            }
        };

        btn.setFont(new Font("Arial", Font.PLAIN, 18));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(400, 80)); 
        btn.setPreferredSize(new Dimension(400, 80));
        
        btn.setBackground(Color.WHITE); 
        btn.setForeground(Color.BLACK);
        
        btn.setFocusPainted(false);
        btn.setOpaque(false); 
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return btn;
    }
    
    private void selectDifficulty(JButton selectedBtn, int score, Color activeColor) {
        this.selectedTargetScore = score;
        
        resetButtonStyle(btnEasy);
        resetButtonStyle(btnMedium);
        resetButtonStyle(btnHard);
        
        selectedBtn.setBackground(activeColor);
        selectedBtn.setForeground(Color.WHITE);
        
        btnStart.setEnabled(true);
        btnStart.setBackground(new Color(30, 30, 30)); 
        btnStart.setForeground(Color.WHITE);
        btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void resetButtonStyle(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
    }
}