package id.ac.itb.if2010;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.Timer;

import id.ac.itb.if2010.model.ChefPlayer;
import id.ac.itb.if2010.model.CookingStation;
import id.ac.itb.if2010.model.Direction;
import id.ac.itb.if2010.model.GameMap;
import id.ac.itb.if2010.model.Plate;
import id.ac.itb.if2010.model.Position;
import id.ac.itb.if2010.model.Station;
import id.ac.itb.if2010.view.GamePanel;

public class App {
    public static void main(String[] args) {
        GameMap map = new GameMap(); 
        
        ChefPlayer chef1 = new ChefPlayer("Chef 1", new Position(6, 4));
        chef1.setInventory(new Plate(chef1.getPosition()));
        map.addChef(chef1);
        
        JFrame window = new JFrame("Nimonscooked");
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
                String key = KeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
                
                Direction dir = null;
                if (key.equals("w")) dir = Direction.UP;
                if (key.equals("s")) dir = Direction.DOWN;
                if (key.equals("a")) dir = Direction.LEFT;
                if (key.equals("d")) dir = Direction.RIGHT;
                
                if (dir != null) {
                    int nextX = chef1.getPosition().getX();
                    int nextY = chef1.getPosition().getY();
                    if (dir == Direction.UP) nextY--;
                    if (dir == Direction.DOWN) nextY++;
                    if (dir == Direction.LEFT) nextX--;
                    if (dir == Direction.RIGHT) nextX++;

                    if (map.isValidPosition(nextX, nextY) && map.getStationAt(nextX, nextY) == null) {
                        chef1.move(dir);
                    } else {
                        chef1.setDirection(dir);
                    }
                }

                if (key.equals("e")) {
                    int tx = chef1.getPosition().getX();
                    int ty = chef1.getPosition().getY();
                    switch (chef1.getDirection()) {
                        case UP:    ty--; break;
                        case DOWN:  ty++; break;
                        case LEFT:  tx--; break;
                        case RIGHT: tx++; break;
                    }
                    Station s = map.getStationAt(tx, ty);
                    if (s != null) s.interact(chef1);
                }
                
                gamePanel.repaint();
            }
        });

        Timer timer = new Timer(50, e -> {
             for(int y=0; y<map.getRows(); y++) {
                for(int x=0; x<map.getCols(); x++) {
                    Station s = map.getStationAt(x, y);
                    if (s instanceof CookingStation) {
                        ((CookingStation) s).tick();
                    }
                }
            }
            gamePanel.repaint();
        });
        timer.start();
        
        System.out.println("Nimonscooked Started!");
    }
}