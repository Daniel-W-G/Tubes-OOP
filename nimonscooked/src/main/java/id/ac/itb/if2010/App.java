package id.ac.itb.if2010;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import id.ac.itb.if2010.model.ChefPlayer;
import id.ac.itb.if2010.model.Direction;
import id.ac.itb.if2010.model.GameMap;
import id.ac.itb.if2010.model.Plate;
import id.ac.itb.if2010.model.Position;
import id.ac.itb.if2010.model.Station;
import id.ac.itb.if2010.view.GamePanel;

public class App {
    private static boolean isGameOver = false;
    private static int activeChefIndex = 0; 

    public static void main(String[] args) {
        GameMap map = new GameMap();
        ChefPlayer chef1 = new ChefPlayer("Chef 1", new Position(6, 5));
        chef1.setInventory(new Plate(chef1.getPosition()));
        map.addChef(chef1);
        
        ChefPlayer chef2 = new ChefPlayer("Chef 2", new Position(8, 5));
        map.addChef(chef2);
        
        JFrame window = new JFrame("Nimonscooked v2.0 - Multi Chef");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        
        GamePanel gamePanel = new GamePanel(map);
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
                    System.out.println("Switched to " + chefs.get(activeChefIndex).getName());
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

        Timer timer = new Timer(50, e -> {
            if (isGameOver) return;
            if (map.getOrderManager() != null) {
                map.getOrderManager().tick();
                if (map.getOrderManager().getFailedOrders() >= 5) {
                    isGameOver = true;
                    JOptionPane.showMessageDialog(window, "GAME OVER! Too many failed orders.");
                }
            }
            gamePanel.repaint();
        });
        timer.start();
        
        System.out.println("Game Started! Press 'B' to Switch Chefs.");
    }
}