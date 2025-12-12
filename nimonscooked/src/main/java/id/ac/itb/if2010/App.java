package id.ac.itb.if2010;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import id.ac.itb.if2010.model.*;
import id.ac.itb.if2010.view.GamePanel;
import id.ac.itb.if2010.view.MainMenu;

public class App {
    private static JFrame window; 
    private static boolean isGameOver = false;
    private static boolean isPaused = false;
    private static int activeChefIndex = 0; 
    
    private static int gameTimeLeft = 0;
    private static long lastSecondTime = 0;
    private static Timer gameTimer;
    
    private static String lastMapType;
    private static int lastTargetScore;
    private static int lastDuration;

    public static void startGame(String mapType, int targetScore, int durationInSeconds) {
        if (window != null) {
            window.dispose();
            if (gameTimer != null) gameTimer.stop();
        }

        lastMapType = mapType;
        lastTargetScore = targetScore;
        lastDuration = durationInSeconds;

        isGameOver = false;
        isPaused = false; 
        activeChefIndex = 0;  
        gameTimeLeft = durationInSeconds; 

        GameMap map = new GameMap(); 
        
        ChefPlayer chef1 = new ChefPlayer("Chef 1", new Position(4, 3));
        chef1.setInventory(new Plate(chef1.getPosition()));
        map.addChef(chef1);
        
        ChefPlayer chef2 = new ChefPlayer("Chef 2", new Position(10, 3));
        map.addChef(chef2);
        
        window = new JFrame("SUSHIMATE - " + mapType);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        
        GamePanel gamePanel = new GamePanel(map);
        gamePanel.setGameInfo(targetScore, gameTimeLeft);
        
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String action = gamePanel.checkClick(e.getX(), e.getY());
                
                if (action.equals("PAUSE")) {
                    togglePause(gamePanel);
                } 
                else if (action.equals("RESUME")) {
                    if (isPaused) togglePause(gamePanel);
                }
                else if (action.equals("RESTART")) {
                    if (isPaused) {
                        int confirm = JOptionPane.showConfirmDialog(window, "Restart Level?", "Restart", JOptionPane.YES_NO_OPTION);
                        if(confirm == JOptionPane.YES_OPTION) {
                            startGame(lastMapType, lastTargetScore, lastDuration);
                        }
                    }
                }
                else if (action.equals("EXIT")) {
                    if (isPaused) exitToMenu();
                }
            }
        });

        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isGameOver) return;
                
                String key = KeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
                
                if (key.equals("escape")) {
                    togglePause(gamePanel);
                    return;
                }

                if (isPaused) return; 

                List<ChefPlayer> chefs = map.getChefs();
                ChefPlayer activeChef = chefs.get(activeChefIndex);

                if (key.equals("b")) {
                    activeChefIndex = (activeChefIndex + 1) % chefs.size();
                    gamePanel.setActiveChefIndex(activeChefIndex);
                    gamePanel.repaint();
                    return;
                }

                Direction dir = null;
                if (key.equals("w")) dir = Direction.UP;
                if (key.equals("s")) dir = Direction.DOWN;
                if (key.equals("a")) dir = Direction.LEFT;
                if (key.equals("d")) dir = Direction.RIGHT;
                
                if (dir != null) {
                    int nextX = activeChef.getPosition().getX();
                    int nextY = activeChef.getPosition().getY();
                    if (dir == Direction.UP) nextY--;
                    if (dir == Direction.DOWN) nextY++;
                    if (dir == Direction.LEFT) nextX--;
                    if (dir == Direction.RIGHT) nextX++;

                    if (map.isValidPosition(nextX, nextY)) {
                        Station s = map.getStationAt(nextX, nextY);
                        boolean hitOtherChef = false;
                        for(ChefPlayer other : chefs) {
                            if (other != activeChef && other.getPosition().getX() == nextX && other.getPosition().getY() == nextY) {
                                hitOtherChef = true;
                                break;
                            }
                        }

                        if (s == null && !hitOtherChef) {
                            activeChef.move(dir);
                        } else {
                            activeChef.setDirection(dir);
                        }
                    } else {
                        activeChef.setDirection(dir);
                    }
                }

                if (key.equals("e")) {
                    int tx = activeChef.getPosition().getX();
                    int ty = activeChef.getPosition().getY();
                    switch (activeChef.getDirection()) {
                        case UP:    ty--; break;
                        case DOWN:  ty++; break;
                        case LEFT:  tx--; break;
                        case RIGHT: tx++; break;
                    }
                    Station s = map.getStationAt(tx, ty);
                    if (s != null) s.interact(activeChef);
                }
                
                gamePanel.repaint();
            }
        });

        lastSecondTime = System.currentTimeMillis();

        gameTimer = new Timer(50, e -> {
            if (isGameOver || isPaused || !window.isVisible()) {
                lastSecondTime = System.currentTimeMillis(); 
                return;
            }

            long now = System.currentTimeMillis();
            if (now - lastSecondTime >= 1000) {
                gameTimeLeft--;
                gamePanel.updateTimeLeft(gameTimeLeft);
                lastSecondTime = now;
                
                if (gameTimeLeft <= 0) {
                    isGameOver = true;
                    int finalScore = map.getOrderManager().getScore();
                    
                    if (finalScore >= targetScore) {
                        JOptionPane.showMessageDialog(window, 
                            "VICTORY!\nScore: " + finalScore + "\nTarget Reached!");
                    } else {
                        JOptionPane.showMessageDialog(window, 
                            "GAME OVER (TIME UP)!\nScore: " + finalScore + "\nTarget Failed!");
                    }
                    
                    window.dispose();
                    window = null; 
                    new MainMenu().setVisible(true);
                }
            }

            if (map.getOrderManager() != null) {
                map.getOrderManager().tick();
            }
            gamePanel.repaint();
        });
        gameTimer.start();
        
        System.out.println("Game Started! Target: " + targetScore);
    }
    
    private static void togglePause(GamePanel panel) {
        isPaused = !isPaused;
        panel.setPaused(isPaused);
    }
    
    private static void exitToMenu() {
        int confirm = JOptionPane.showConfirmDialog(window, 
            "Exit to Main Menu?", 
            "Exit Game", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            window.setVisible(false);
            new MainMenu().setVisible(true);
        }
    }
    
    public static void resumeGame() {
        if (window != null) {
            window.setVisible(true);
            window.requestFocus();
        }
    }
    
    public static boolean hasActiveGame() {
        return window != null && !isGameOver;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu().setVisible(true);
        });
    }
}