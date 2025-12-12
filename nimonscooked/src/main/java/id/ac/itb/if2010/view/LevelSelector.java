package id.ac.itb.if2010.view;

import javax.swing.*;
import java.awt.*;
import id.ac.itb.if2010.App; 

public class LevelSelector extends JFrame {
    private final int GAME_DURATION = 180; 

    public LevelSelector() {
        setTitle("Select Level");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new GridBagLayout()); 

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(240, 240, 240));

        JLabel headerLabel = new JLabel("SELECT LEVEL");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel lvlPanel = new JPanel();
        lvlPanel.setLayout(new BoxLayout(lvlPanel, BoxLayout.Y_AXIS));
        lvlPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lvlPanel.setBackground(Color.WHITE);
        lvlPanel.setPreferredSize(new Dimension(450, 400)); 
        lvlPanel.setMaximumSize(new Dimension(450, 400));

        JLabel lvlTitle = new JLabel("SUSHI BAR");
        lvlTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lvlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lvlInfo = new JLabel("<html><center>Menu: Kappa Maki, Sakana Maki, Ebi Maki</center></html>");
        lvlInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        lvlInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnEasy = createDifficultyButton("EASY (Target: 150)", new Color(46, 204, 113), 150);
        JButton btnMedium = createDifficultyButton("MEDIUM (Target: 250)", new Color(243, 156, 18), 250);
        JButton btnHard = createDifficultyButton("HARD (Target: 400)", new Color(231, 76, 60), 400);

        lvlPanel.add(Box.createVerticalStrut(30));
        lvlPanel.add(lvlTitle);
        lvlPanel.add(Box.createVerticalStrut(10));
        lvlPanel.add(lvlInfo);
        
        lvlPanel.add(Box.createVerticalStrut(30)); 
        lvlPanel.add(btnEasy);
        lvlPanel.add(Box.createVerticalStrut(15)); 
        lvlPanel.add(btnMedium);
        lvlPanel.add(Box.createVerticalStrut(15)); 
        lvlPanel.add(btnHard);

        JButton btnBack = new JButton("Back to Main Menu");
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.addActionListener(e -> {
            dispose(); 
            new MainMenu().setVisible(true); 
        });

        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(headerLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(lvlPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(btnBack);

        add(mainPanel);
    }
    
    private JButton createDifficultyButton(String text, Color color, int targetScore) {
        JButton btn = new JButton(text);
        
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setPreferredSize(new Dimension(350, 50)); 
        btn.setMaximumSize(new Dimension(350, 50));
        
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        
        btn.addActionListener(e -> {
            dispose();
            System.out.println("Starting Game. Difficulty Target: " + targetScore);
            App.startGame("SUSHI", targetScore, GAME_DURATION);
        });
        
        return btn;
    }
}