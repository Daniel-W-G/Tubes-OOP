package id.ac.itb.if2010;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import id.ac.itb.if2010.model.ChefPlayer;
import id.ac.itb.if2010.model.Direction;
import id.ac.itb.if2010.model.GameMap;
import id.ac.itb.if2010.model.Plate;
import id.ac.itb.if2010.model.Position;
import id.ac.itb.if2010.model.Station;
import id.ac.itb.if2010.view.GamePanel;
import id.ac.itb.if2010.view.MainMenu;

public class App {
    private static boolean isGameOver = false;
    private static int activeChefIndex = 0; 
    private static int gameTimeLeft = 0;
    private static long lastSecondTime = 0;

    public static void startGame(String mapType, int targetScore, int durationInSeconds) {
        isGameOver = false;
        activeChefIndex = 0;  
        gameTimeLeft = durationInSeconds; 

        GameMap map = new GameMap(); 
        
        ChefPlayer chef1 = new ChefPlayer("Chef 1", new Position(4, 3));
        chef1.setInventory(new Plate(chef1.getPosition()));
        map.addChef(chef1);
        
        ChefPlayer chef2 = new ChefPlayer("Chef 2", new Position(10, 3));
        map.addChef(chef2);
        
        JFrame window = new JFrame("Nimonscooked - " + mapType);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        
        GamePanel gamePanel = new GamePanel(map);
        
        gamePanel.setGameInfo(targetScore, gameTimeLeft);
        
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isGameOver) return;

                String key = KeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
                
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

        Timer timer = new Timer(50, e -> {
            if (isGameOver) return;

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
                    new MainMenu().setVisible(true);
                }
            }

            if (map.getOrderManager() != null) {
                map.getOrderManager().tick();
            }
            gamePanel.repaint();
        });
        timer.start();
        
        System.out.println("Game Started! Target: " + targetScore + ", Time: " + durationInSeconds + "s | Press 'B' to Switch Chefs.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu().setVisible(true);
        });
    }
}