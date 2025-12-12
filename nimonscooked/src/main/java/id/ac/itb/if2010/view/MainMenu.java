package id.ac.itb.if2010.view;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Main Menu");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); 
        panel.setBackground(new Color(240, 240, 240)); 

        JLabel titleLabel = new JLabel("NIMONSCOOKED");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnStart = createStyledButton("Start Game");
        JButton btnHowToPlay = createStyledButton("How to Play");
        JButton btnExit = createStyledButton("Exit");

        
        btnStart.addActionListener(e -> {
            dispose(); 
            new LevelSelector().setVisible(true); 
        });

        btnHowToPlay.addActionListener(e -> {
            showHowToPlay();
        });

        btnExit.addActionListener(e -> {
            System.exit(0);
        });

        panel.add(Box.createVerticalStrut(50));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(50));
        panel.add(btnStart);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnHowToPlay);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnExit);

        add(panel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 50));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBackground(Color.WHITE); 
        
        return button;
    }

    private void showHowToPlay() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("CONTROLS");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel grid = new JPanel(new GridLayout(0, 2, 10, 10));
        grid.setBackground(Color.WHITE);
        grid.setAlignmentX(Component.CENTER_ALIGNMENT);

        addControlItem(grid, "[ W ]", "Move UP");
        addControlItem(grid, "[ A ]", "Move LEFT");
        addControlItem(grid, "[ S ]", "Move DOWN");
        addControlItem(grid, "[ D ]", "Move RIGHT");
        
        grid.add(new JLabel("")); grid.add(new JLabel(""));
        
        addControlItem(grid, "[ E ]", "Interact (Pick Up / Cook / Place)");
        addControlItem(grid, "[ B ]", "Switch Chef");
        
        JLabel missionTitle = new JLabel("MISSION");
        missionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        missionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextArea missionText = new JTextArea(
            "1. Grab ingredients from the Crate\n" +
            "2. Chop (at Cutting Board) or Cook (at Stove)\n" +
            "3. Place food on a Plate\n" +
            "4. Deliver to Serving Window before time runs out!"
        );

        missionText.setEditable(false);
        missionText.setBackground(new Color(240, 240, 240)); 
        missionText.setFont(new Font("Arial", Font.PLAIN, 14));
        missionText.setMargin(new Insets(10, 10, 10, 10));

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(grid);
        panel.add(Box.createVerticalStrut(20));
        panel.add(new JSeparator());
        panel.add(Box.createVerticalStrut(10));
        panel.add(missionTitle);
        panel.add(Box.createVerticalStrut(10));
        panel.add(missionText);

        JOptionPane.showMessageDialog(this, panel, "How to Play", JOptionPane.PLAIN_MESSAGE);
    }

    private void addControlItem(JPanel parent, String key, String desc) {
        JLabel lblKey = new JLabel(key, SwingConstants.CENTER);
        lblKey.setFont(new Font("Monospaced", Font.BOLD, 16));
        lblKey.setOpaque(true);
        lblKey.setBackground(new Color(220, 220, 220)); // Abu-abu mirip tombol keyboard
        lblKey.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        
        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        
        parent.add(lblKey);
        parent.add(lblDesc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu().setVisible(true);
        });
    }
}