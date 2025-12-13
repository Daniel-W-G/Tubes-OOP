package id.ac.itb.if2010.view;

import javax.swing.*;
import java.awt.*;
import id.ac.itb.if2010.App;

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

        JLabel titleLabel = new JLabel("SUSHIMATE") {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF); // Pixel perfect
        
        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
        
        // 1. Gambar outline oren dulu (lebih tebal)
        g2d.setColor(new Color(255, 140, 0)); // Oren
        g2d.setStroke(new BasicStroke(3)); // Ketebalan outline
        
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i != 0 || j != 0) {
                    g2d.drawString(getText(), x + i, y + j);
                }
            }
        }
        
        // 2. Gambar gradient text di tengah
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(255, 215, 0),      // Kuning gold
            0, getHeight(), new Color(255, 140, 0)  // Oren
        );
        
        g2d.setPaint(gradient);
        g2d.drawString(getText(), x, y);
        
        g2d.dispose();
    }
};


try {
    java.io.InputStream is = getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
    Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(48f);
    
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    ge.registerFont(pixelFont);
    
    titleLabel.setFont(pixelFont);

} catch (Exception e) {
    titleLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
}

titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
titleLabel.setPreferredSize(new Dimension(650, 100)); 
        
        JButton btnResume = createStyledButton("Resume Game");
        JButton btnStart = createStyledButton("New Game");
        JButton btnHowToPlay = createStyledButton("How to Play");
        JButton btnExit = createStyledButton("Exit");
        
        if (App.hasActiveGame()) {
            btnResume.setEnabled(true);
            btnResume.setBackground(new Color(255, 255, 200)); 
        } else {
            btnResume.setEnabled(false);
            btnResume.setForeground(Color.LIGHT_GRAY);
        }

        btnResume.addActionListener(e -> {
            dispose();
            App.resumeGame(); 
        });

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
        panel.add(Box.createVerticalStrut(60)); 
        
        panel.add(btnResume);
        panel.add(Box.createVerticalStrut(10));
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
        
        JPanel grid = new JPanel(new GridLayout(0, 2, 10, 5));
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
        lblKey.setBackground(new Color(220, 220, 220));
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