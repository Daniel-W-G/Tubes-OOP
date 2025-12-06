package id.ac.itb.if2010.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.lang.reflect.Field;
import java.util.List;

import javax.swing.JPanel;

import id.ac.itb.if2010.model.AssemblyStation;
import id.ac.itb.if2010.model.BoilingPot;
import id.ac.itb.if2010.model.ChefPlayer;
import id.ac.itb.if2010.model.CookingDevice;
import id.ac.itb.if2010.model.CookingStation;
import id.ac.itb.if2010.model.CuttingStation;
import id.ac.itb.if2010.model.FryingPan;
import id.ac.itb.if2010.model.GameMap;
import id.ac.itb.if2010.model.Ingredient;
import id.ac.itb.if2010.model.IngredientState;
import id.ac.itb.if2010.model.IngredientStorage;
import id.ac.itb.if2010.model.Item;
import id.ac.itb.if2010.model.KitchenUtensil;
import id.ac.itb.if2010.model.Order;
import id.ac.itb.if2010.model.Oven;
import id.ac.itb.if2010.model.Plate;
import id.ac.itb.if2010.model.PlateStorage;
import id.ac.itb.if2010.model.ServingCounter;
import id.ac.itb.if2010.model.Station;
import id.ac.itb.if2010.model.TrashStation;
import id.ac.itb.if2010.model.Wall;
import id.ac.itb.if2010.model.WashingStation;

public class GamePanel extends JPanel {
    private GameMap map;
    private final int TILE_SIZE = 64;
    private int activeChefIndex = 0;

    public GamePanel(GameMap map) {
        this.map = map;
        this.setPreferredSize(new Dimension(map.getCols() * TILE_SIZE, map.getRows() * TILE_SIZE + 100)); 
        this.setBackground(Color.BLACK);
    }
    
    public void setActiveChefIndex(int index) {
        this.activeChefIndex = index;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (int y = 0; y < map.getRows(); y++) {
            for (int x = 0; x < map.getCols(); x++) {
                drawTile(g, x, y);
            }
        }
        
        List<ChefPlayer> chefs = map.getChefs();
        for (int i = 0; i < chefs.size(); i++) {
            drawChef(g, chefs.get(i), i == activeChefIndex);
        }

        drawUI(g);
    }

    private void drawTile(Graphics g, int x, int y) {
        int sx = x * TILE_SIZE;
        int sy = y * TILE_SIZE + 50; 

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(sx, sy, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.WHITE);
        g.drawRect(sx, sy, TILE_SIZE, TILE_SIZE);

        Station station = map.getStationAt(x, y);
        if (station != null) {
            if (station instanceof IngredientStorage) {
                g.setColor(new Color(139, 69, 19)); 
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 10));
                
                String label = "CRATE";
                try {
                    Field f = IngredientStorage.class.getDeclaredField("ingredientName");
                    f.setAccessible(true);
                    label = (String) f.get(station);
                } catch (Exception e) { }
                g.drawString(label.toUpperCase(), sx+5, sy+35);
                
                if (((IngredientStorage)station).getItemOnTop() != null) {
                    drawItem(g, ((IngredientStorage)station).getItemOnTop(), sx+15, sy+10);
                }
            }
            
            else if (station instanceof CookingStation) {
                CookingStation cs = (CookingStation) station;
                CookingDevice device = cs.getDevice();
                
                g.setColor(Color.GRAY); 
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
                
                if (device != null) {
                    if (device instanceof Oven) {
                        g.setColor(Color.BLACK);
                        g.fillRect(sx+10, sy+10, 44, 44);
                        g.setColor(Color.ORANGE);
                        g.drawString("OVEN", sx+15, sy+35);
                    } 
                    else if (device instanceof BoilingPot) {
                        g.setColor(Color.DARK_GRAY);
                        g.fillOval(sx+10, sy+10, 44, 44);
                        g.setColor(Color.CYAN);
                        g.drawString("POT", sx+20, sy+35);
                    }
                    else if (device instanceof FryingPan) {
                        g.setColor(Color.BLACK);
                        g.fillOval(sx+10, sy+10, 44, 44);
                        g.setColor(Color.WHITE);
                        g.drawString("PAN", sx+20, sy+35);
                    }
                    
                    if (device.isCooking()) {
                        drawProgressBar(g, sx, sy, device.getProgress(), 24);
                    }
                } else {
                    g.setColor(Color.RED); 
                    g.drawOval(sx+15, sy+15, 34, 34);
                }
            }
            
            else if (station instanceof CuttingStation) {
                g.setColor(new Color(210, 180, 140)); 
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.BLACK);
                g.drawString("CUT", sx+20, sy+35);
                
                CuttingStation cut = (CuttingStation) station;
                
                if (cut.getCurrentItem() != null) {
                    drawItem(g, cut.getCurrentItem(), sx+15, sy+15);
                }
                
                if (cut.getProgress() > 0) {
                    drawProgressBar(g, sx, sy, cut.getProgress(), 100);
                }
            }
            
            else if (station instanceof AssemblyStation) {
                g.setColor(new Color(222, 184, 135)); 
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.BLACK);
                g.drawString("TABLE", sx+10, sy+20);
                
                if (((AssemblyStation)station).getItem() != null) {
                    drawItem(g, ((AssemblyStation)station).getItem(), sx+15, sy+25);
                }
            }
            else if (station instanceof PlateStorage) {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.WHITE);
                g.drawString("PLATES", sx+5, sy+20);
                g.drawString(((PlateStorage)station).getStatus(), sx+5, sy+40);
            }
            else if (station instanceof WashingStation) {
                g.setColor(Color.CYAN); 
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.BLACK);
                g.drawString("SINK", sx+15, sy+20);
                WashingStation sink = (WashingStation) station;
                if (sink.hasPlates()) {
                     g.setColor(Color.WHITE);
                     g.fillOval(sx+20, sy+30, 20, 20);
                }
                if (sink.getProgress() > 0) {
                    drawProgressBar(g, sx, sy, sink.getProgress(), 100);
                }
            }
            else if (station instanceof TrashStation) {
                g.setColor(Color.BLACK);
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.WHITE);
                g.drawString("TRASH", sx+10, sy+35);
            }
            else if (station instanceof ServingCounter) {
                g.setColor(Color.MAGENTA);
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.WHITE);
                g.drawString("SERVE", sx+10, sy+35);
            }
            else if (station instanceof Wall) {
                g.setColor(Color.BLACK);
                g.fillRect(sx, sy, TILE_SIZE, TILE_SIZE);
                g.setColor(Color.WHITE);
                g.drawString("WALL", sx+20, sy+35);
            }
            else {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
            }
        }
    }

    private void drawProgressBar(Graphics g, int x, int y, int current, int max) {
        g.setColor(Color.WHITE);
        g.fillRect(x + 5, y - 5, 54, 8);
        float pct = (float) current / max;
        if (pct > 1.0f) pct = 1.0f;
        if (max == 24 && current > 12) g.setColor(Color.RED); 
        else g.setColor(Color.GREEN);
        g.fillRect(x + 5, y - 5, (int)(54 * pct), 8);
        g.setColor(Color.BLACK);
        g.drawRect(x + 5, y - 5, 54, 8);
    }

    private void drawChef(Graphics g, ChefPlayer chef, boolean isActive) {
        int x = chef.getPosition().getX() * TILE_SIZE;
        int y = chef.getPosition().getY() * TILE_SIZE + 50;

        if (isActive) {
            g.setColor(Color.YELLOW);
            int[] xPoints = {x + 32, x + 22, x + 42};
            int[] yPoints = {y - 5, y - 15, y - 15};
            g.fillPolygon(xPoints, yPoints, 3);
        }

        g.setColor(isActive ? Color.BLUE : Color.GRAY); 
        g.fillOval(x + 10, y + 10, TILE_SIZE - 20, TILE_SIZE - 20);
        
        if (chef.isBusy()) {
            g.setColor(Color.RED);
            g.drawOval(x + 8, y + 8, TILE_SIZE - 16, TILE_SIZE - 16);
        }

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
            if (!p.isClean()) label = "DIRT(" + p.getStackSize() + ")";
            else if (!p.getContents().isEmpty()) label = "MIX";
            else label = "PLATE";
        }
        else if (item instanceof KitchenUtensil) {
            KitchenUtensil k = (KitchenUtensil) item;
            if (!k.getContents().isEmpty()) label = "FULL";
            else label = item.getName().length() > 3 ? item.getName().substring(0,3) : item.getName();
        }
        else if (item instanceof Ingredient) {
            Ingredient ing = (Ingredient) item;
            if (ing.getState() == IngredientState.CHOPPED) label = "CHOP";
            else if (ing.getState() == IngredientState.COOKED) label = "COOK";
            else label = item.getName().length() > 4 ? item.getName().substring(0,4) : item.getName();
        }
        else {
             label = item.getName().length() > 4 ? item.getName().substring(0,4) : item.getName();
        }
        
        g.drawString(label, x + 2, y + 20);
    }

    private void drawUI(Graphics g) {
        g.setColor(new Color(50, 50, 50));
        g.fillRect(0, 0, getWidth(), 50);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        if (map.getOrderManager() != null) {
            int score = map.getOrderManager().getScore();
            g.drawString("Score: " + score, 20, 30);
            
            int xPos = 150;
            for (Order o : map.getOrderManager().getActiveOrders()) {
                g.setColor(new Color(255, 255, 200)); 
                g.fillRect(xPos, 5, 100, 40);
                
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.PLAIN, 12));
                g.drawString(o.getRecipeName(), xPos + 5, 20);
                
                g.setColor(Color.GRAY);
                g.fillRect(xPos + 5, 25, 90, 5);
                g.setColor(Color.GREEN);
                int barWidth = (int) (90 * ((double)o.getTimeLeft() / o.getMaxTime()));
                g.fillRect(xPos + 5, 25, barWidth, 5);
                
                xPos += 110;
            }
        }
    }
}