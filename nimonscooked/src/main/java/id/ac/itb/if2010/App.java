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
    
    private static Timer gameTimer;
    private static String lastMapType;
    private static int lastTargetScore;
    private static int timeRemaining;

    public static void startGame(String mapType, int targetScore) {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        if (window != null) {
            window.dispose(); 
        }

        lastMapType = mapType;
        lastTargetScore = targetScore;
        timeRemaining = 60000;

        isGameOver = false;
        isPaused = false; 
        activeChefIndex = 0;  

        GameMap map = new GameMap(); 
        
        ChefPlayer chef1 = new ChefPlayer("Chef 1", new Position(4, 3));
        chef1.setInventory(new Plate(chef1.getPosition()));
        map.addChef(chef1);
        
        ChefPlayer chef2 = new ChefPlayer("Chef 2", new Position(10, 3));
        map.addChef(chef2);
        
        window = new JFrame("SUSHIMATE");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        
        GamePanel gamePanel = new GamePanel(map);
        gamePanel.setGameInfo(targetScore);
        
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String action = gamePanel.checkClick(e.getX(), e.getY());
                
                if (action.equals("PAUSE")) togglePause(gamePanel);
                else if (action.equals("RESUME")) { if (isPaused) togglePause(gamePanel); }
                else if (action.equals("RESTART")) {
                    if (isPaused) {
                        int confirm = JOptionPane.showConfirmDialog(window, "Restart Level?", "Restart", JOptionPane.YES_NO_OPTION);
                        if(confirm == JOptionPane.YES_OPTION) startGame(lastMapType, lastTargetScore);
                    }
                }
                else if (action.equals("EXIT")) { if (isPaused) exitToMenu(); }
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

        gameTimer = new Timer(50, e -> {
            if (isGameOver || isPaused || window == null) return;

            timeRemaining -= 50;
            gamePanel.setTimeRemaining(timeRemaining / 1000);

            if (map.getOrderManager() != null) {
                map.getOrderManager().tick();
                
                int score = map.getOrderManager().getScore();
                int failed = map.getOrderManager().getFailedOrders();
                int success = map.getOrderManager().getSuccessfulOrders();
                int wrong = map.getOrderManager().getWrongOrders();
                
                if (failed >= 5) {
                    isGameOver = true;
                    gameTimer.stop();
                    
                    JOptionPane.showMessageDialog(window, 
                        "GAME OVER! Too many failed orders.\nFinal Score: " + score + "\nSuccessful Orders: " + success + "\nFailed (Late) Orders: " + failed + "\nWrong Orders: " + wrong);
                    
                    window.dispose();
                    new MainMenu().setVisible(true);
                }
                
                else if (timeRemaining <= 0) {
                    isGameOver = true;
                    gameTimer.stop();
                    if (score >= targetScore) {                    
                        JOptionPane.showMessageDialog(window, 
                        "VICTORY! Target Score Reached!\nFinal Score: " + score + "\nSuccessful Orders: " + success + "\nFailed (Late) Orders: " + failed + "\nWrong Orders: " + wrong);
                        window.dispose();
                        new MainMenu().setVisible(true);
                    }
                    else{
                        isGameOver = true;
                        gameTimer.stop();  
                        JOptionPane.showMessageDialog(window, 
                        "GAME OVER! Time's Up!\nFinal Score: " + score + "\nSuccessful Orders: " + success + "\nFailed (Late) Orders: " + failed + "\nWrong Orders: " + wrong);
                        window.dispose();
                        new MainMenu().setVisible(true);
                    }
                }
            }
            
            if (!isGameOver) gamePanel.repaint();
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