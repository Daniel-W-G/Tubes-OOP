package id.ac.itb.if2010.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.lang.reflect.Field;

import javax.swing.JPanel;

import id.ac.itb.if2010.model.AssemblyStation;
import id.ac.itb.if2010.model.BoilingPot;
import id.ac.itb.if2010.model.ChefPlayer;
import id.ac.itb.if2010.model.CookingDevice;
import id.ac.itb.if2010.model.CookingStation;
import id.ac.itb.if2010.model.CuttingStation;
import id.ac.itb.if2010.model.FryingPan;
import id.ac.itb.if2010.model.GameMap;
import id.ac.itb.if2010.model.IngredientStorage;
import id.ac.itb.if2010.model.Item;
import id.ac.itb.if2010.model.KitchenUtensil;
import id.ac.itb.if2010.model.Oven;
import id.ac.itb.if2010.model.Plate;
import id.ac.itb.if2010.model.PlateStorage;
import id.ac.itb.if2010.model.Station;
import id.ac.itb.if2010.model.TrashStation;
import id.ac.itb.if2010.model.WashingStation;

public class GamePanel extends JPanel {
    private GameMap map;
    private final int TILE_SIZE = 64;

    public GamePanel(GameMap map) {
        this.map = map;
        this.setPreferredSize(new Dimension(map.getCols() * TILE_SIZE, map.getRows() * TILE_SIZE));
        this.setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int y = 0; y < map.getRows(); y++) {
            for (int x = 0; x < map.getCols(); x++) {
                drawTile(g, x, y);
            }
        }
        for (ChefPlayer chef : map.getChefs()) {
            drawChef(g, chef);
        }
    }

    private void drawTile(Graphics g, int x, int y) {
        int screenX = x * TILE_SIZE;
        int screenY = y * TILE_SIZE;

        // Floor
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(screenX, screenY, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.WHITE);
        g.drawRect(screenX, screenY, TILE_SIZE, TILE_SIZE);

        Station station = map.getStationAt(x, y);
        if (station != null) {
            if (station instanceof IngredientStorage) {
                g.setColor(new Color(139, 69, 19)); 
                g.fillRect(screenX + 2, screenY + 2, TILE_SIZE - 4, TILE_SIZE - 4);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 10));
                
                String label = "CRATE";
                try {
                    Field f = IngredientStorage.class.getDeclaredField("ingredientName");
                    f.setAccessible(true);
                    label = (String) f.get(station);
                } catch (Exception e) { }
                
                g.drawString(label.toUpperCase(), screenX + 5, screenY + 35);
            }
            else if (station instanceof CookingStation) {
                CookingStation cs = (CookingStation) station;
                CookingDevice device = cs.getDevice();
                
                g.setColor(Color.GRAY); 
                g.fillRect(screenX + 2, screenY + 2, TILE_SIZE - 4, TILE_SIZE - 4);
                
                if (device != null) {
                    if (device instanceof Oven) {
                        g.setColor(Color.BLACK);
                        g.fillRect(screenX + 10, screenY + 10, 44, 44);
                        g.setColor(Color.ORANGE);
                        g.drawString("OVEN", screenX + 15, screenY + 35);
                    } 
                    else if (device instanceof BoilingPot) {
                        g.setColor(Color.DARK_GRAY);
                        g.fillOval(screenX + 10, screenY + 10, 44, 44);
                        g.setColor(Color.CYAN);
                        g.drawString("POT", screenX + 20, screenY + 35);
                    }
                    else if (device instanceof FryingPan) {
                        g.setColor(Color.BLACK);
                        g.fillOval(screenX + 10, screenY + 10, 44, 44);
                        g.setColor(Color.WHITE);
                        g.drawString("PAN", screenX + 20, screenY + 35);
                    }
                    
                    if (device.isCooking()) {
                        g.setColor(Color.YELLOW);
                        g.drawString("♨", screenX + 45, screenY + 20);
                    }
                } else {
                    g.setColor(Color.RED); 
                    g.drawOval(screenX + 15, screenY + 15, 34, 34);
                }
            }
            else if (station instanceof CuttingStation) {
                g.setColor(new Color(210, 180, 140)); 
                g.fillRect(screenX + 2, screenY + 2, TILE_SIZE - 4, TILE_SIZE - 4);
                g.setColor(Color.BLACK);
                g.drawString("CUT", screenX + 20, screenY + 35);
            }
            else if (station instanceof AssemblyStation) {
                g.setColor(new Color(222, 184, 135)); 
                g.fillRect(screenX + 2, screenY + 2, TILE_SIZE - 4, TILE_SIZE - 4);
                
                AssemblyStation table = (AssemblyStation) station;
                if (table.getItem() != null) {
                    drawItem(g, table.getItem(), screenX + 15, screenY + 15);
                }
            }
            else if (station instanceof TrashStation) {
                g.setColor(Color.BLACK);
                g.fillRect(screenX + 2, screenY + 2, TILE_SIZE - 4, TILE_SIZE - 4);
                g.setColor(Color.WHITE);
                g.drawString("TRASH", screenX + 10, screenY + 35);
            }
            else if (station instanceof PlateStorage) {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(screenX+2, screenY+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.WHITE);
                g.drawString("PLATES", screenX+5, screenY+20);
                g.drawString(((PlateStorage)station).getStatus(), screenX+5, screenY+40);
            }
            
            else if (station instanceof WashingStation) {
                g.setColor(Color.CYAN); // Water color
                g.fillRect(screenX+2, screenY+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.BLACK);
                g.drawString("SINK", screenX+15, screenY+20);
                
                WashingStation sink = (WashingStation) station;
                if (sink.hasPlates()) {
                     g.setColor(Color.WHITE);
                     g.fillOval(screenX+20, screenY+30, 20, 20); 
                }
            }
            
            else if (station instanceof TrashStation) {
                g.setColor(Color.BLACK);
                g.fillRect(screenX+2, screenY+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.WHITE);
                g.drawString("TRASH", screenX+10, screenY+35);
            }

        }
    }

    private void drawChef(Graphics g, ChefPlayer chef) {
        int x = chef.getPosition().getX() * TILE_SIZE;
        int y = chef.getPosition().getY() * TILE_SIZE;

        g.setColor(Color.BLUE);
        g.fillOval(x + 10, y + 10, TILE_SIZE - 20, TILE_SIZE - 20);
        g.setColor(Color.WHITE);
        g.drawString(chef.getDirection().toString().substring(0,1), x+28, y+35); 

        if (chef.getInventory() != null) {
            drawItem(g, chef.getInventory(), x + 30, y - 10);
        }
    }

    private void drawItem(Graphics g, Item item, int x, int y) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, 30, 30); 
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 9));
        
        String label = item.getName();
        if (item instanceof Plate) {
            Plate p = (Plate) item;
            if (p.getPlatedDish() != null) label = "DISH"; 
            else if (!p.getContents().isEmpty()) label = "MIX"; 
            else label = "PLATE";
        }
        else if (item instanceof KitchenUtensil) {
            KitchenUtensil k = (KitchenUtensil) item;
            if (!k.getContents().isEmpty()) label = "FULL";
            else label = item.getName().substring(0, 3);
        }
        
        g.drawString(label.length() > 4 ? label.substring(0,4) : label, x + 2, y + 20);
    }
}