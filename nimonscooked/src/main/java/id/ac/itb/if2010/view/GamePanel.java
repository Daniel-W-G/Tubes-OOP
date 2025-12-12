package id.ac.itb.if2010.view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import id.ac.itb.if2010.model.*;

public class GamePanel extends JPanel {
    private GameMap map;
    private final int TILE_SIZE = 64;
    private int activeChefIndex = 0;
    
    private int targetScore = 0;
    private boolean isPaused = false;
    
    private final int MAP_OFFSET_Y = 85; 
    
    private Rectangle pauseButtonRect = new Rectangle(0, 0, 0, 0); 
    private Rectangle resumeButtonRect = new Rectangle(0, 0, 0, 0);
    private Rectangle restartButtonRect = new Rectangle(0, 0, 0, 0);
    private Rectangle exitButtonRect = new Rectangle(0, 0, 0, 0);

    private Map<String, BufferedImage> assetMap = new HashMap<>();

    public GamePanel(GameMap map) {
        this.map = map;
        this.setPreferredSize(new Dimension(map.getCols() * TILE_SIZE, map.getRows() * TILE_SIZE + 120)); 
        this.setBackground(Color.BLACK);
        
        loadAssets();
    }
    
    private void loadAssets() {
        try {
            assetMap.put("Fish", loadImage("fish.png"));
            assetMap.put("Rice", loadImage("rice.png"));
            assetMap.put("Shrimp", loadImage("shrimp.png"));
            assetMap.put("Nori", loadImage("nori.png"));
            assetMap.put("Cucumber", loadImage("cucumber.png"));
            
            assetMap.put("Chopped Fish", loadImage("chopped fish.png"));
            assetMap.put("Chopped Shrimp", loadImage("chopped shrimp.png"));
            assetMap.put("Chopped Cucumber", loadImage("chopped cucumber.png"));
            assetMap.put("Cooked Rice", loadImage("cooked rice.png"));
            assetMap.put("Cooked Shrimp", loadImage("cooked shrimp.png"));
            
            assetMap.put("Plate", loadImage("clean plate.png"));
            assetMap.put("Dirty Plate", loadImage("dirty plate.png"));
            
            assetMap.put("Kappa Maki", loadImage("kappa.png"));
            assetMap.put("Sakana Maki", loadImage("sakana.png"));
            assetMap.put("Ebi Maki", loadImage("ebi maki.png"));
            
        } catch (Exception e) {
            System.out.println("Gagal load gambar: " + e.getMessage());
        }
    }
    
    private BufferedImage loadImage(String filename) {
        try {
            return ImageIO.read(getClass().getResourceAsStream("/assets/" + filename));
        } catch (IOException | IllegalArgumentException e) {
            return null;
        }
    }

    public void setGameInfo(int target) {
        this.targetScore = target;
    }
    
    public void setActiveChefIndex(int index) {
        this.activeChefIndex = index;
    }
    
    public void setPaused(boolean paused) {
        this.isPaused = paused;
        repaint();
    }
    
    public String checkClick(int mouseX, int mouseY) {
        if (isPaused) {
            if (resumeButtonRect.contains(mouseX, mouseY)) return "RESUME";
            if (restartButtonRect.contains(mouseX, mouseY)) return "RESTART";
            if (exitButtonRect.contains(mouseX, mouseY)) return "EXIT";
        } else {
            if (pauseButtonRect.contains(mouseX, mouseY)) return "PAUSE";
            
            if (map.getOrderManager() != null) {
                int xPos = 220;
                int orderBoxWidth = 110; 
                int orderBoxHeight = 70;
                
                for (Order o : map.getOrderManager().getActiveOrders()) {
                    Rectangle orderRect = new Rectangle(xPos, 5, orderBoxWidth, orderBoxHeight);
                    if (orderRect.contains(mouseX, mouseY)) {
                        showSingleRecipe(o.getRecipeName());
                        return "RECIPE_VIEW";
                    }
                    xPos += orderBoxWidth + 10;
                }
            }
        }
        return "NONE";
    }
    
    private void showSingleRecipe(String dishName) {
        String content = "";
        
        if (dishName.equalsIgnoreCase("Kappa Maki")) {
            content = "<b>Ingredients:</b><br>- Nori (Raw)<br>- Rice (Cooked)<br>- Cucumber (Chopped)";
        } 
        else if (dishName.equalsIgnoreCase("Sakana Maki")) {
            content = "<b>Ingredients:</b><br>- Nori (Raw)<br>- Rice (Cooked)<br>- Fish (Raw)"; 
        } 
        else if (dishName.equalsIgnoreCase("Ebi Maki")) {
            content = "<b>Ingredients:</b><br>- Nori (Raw)<br>- Rice (Cooked)<br>- Shrimp (Cooked)";
        } 
        else {
            content = "Recipe not found.";
        }
        
        String message = "<html><h2>" + dishName + "</h2>" + content + "</html>";
        BufferedImage icon = assetMap.get(dishName);
        javax.swing.ImageIcon swingIcon = (icon != null) ? new javax.swing.ImageIcon(icon) : null;
        
        JOptionPane.showMessageDialog(this, message, "Recipe Info", JOptionPane.INFORMATION_MESSAGE, swingIcon);
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
        
        if (isPaused) {
            drawPauseOverlay(g);
        }
    }

    private void drawTile(Graphics g, int x, int y) {
        int sx = x * TILE_SIZE;
        int sy = y * TILE_SIZE + MAP_OFFSET_Y;

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
                    drawItem(g, ((IngredientStorage)station).getItemOnTop(), sx+10, sy+10, 44);
                }
            }
            else if (station instanceof CookingStation) {
                CookingStation cs = (CookingStation) station;
                CookingDevice device = cs.getDevice();
                g.setColor(Color.GRAY); 
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
                if (device != null) {
                    if (device instanceof Oven) {
                        g.setColor(Color.BLACK); g.fillRect(sx+10, sy+10, 44, 44);
                        g.setColor(Color.ORANGE); g.drawString("OVEN", sx+15, sy+35);
                    } 
                    else if (device instanceof BoilingPot) {
                        g.setColor(Color.DARK_GRAY); g.fillOval(sx+10, sy+10, 44, 44);
                        g.setColor(Color.CYAN); g.drawString("POT", sx+20, sy+35);
                    }
                    else if (device instanceof FryingPan) {
                        g.setColor(Color.BLACK); g.fillOval(sx+10, sy+10, 44, 44);
                        g.setColor(Color.WHITE); g.drawString("PAN", sx+20, sy+35);
                    }
                    if (device.isCooking()) drawProgressBar(g, sx, sy, device.getProgress(), 24);
                }
            }
            else if (station instanceof CuttingStation) {
                g.setColor(new Color(210, 180, 140)); 
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.BLACK); g.drawString("CUT", sx+20, sy+35);
                
                CuttingStation cut = (CuttingStation) station;
                if (cut.getCurrentItem() != null) drawItem(g, cut.getCurrentItem(), sx+10, sy+10, 44);
                if (cut.getProgress() > 0) drawProgressBar(g, sx, sy, cut.getProgress(), 100);
            }
            else if (station instanceof AssemblyStation) {
                g.setColor(new Color(222, 184, 135)); 
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.BLACK); g.drawString("TABLE", sx+10, sy+20);
                if (((AssemblyStation)station).getItem() != null) drawItem(g, ((AssemblyStation)station).getItem(), sx+10, sy+10, 44);
            }
            else if (station instanceof PlateStorage) {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.WHITE);
                g.drawString("PLATES", sx+5, sy+20);
            }
            else if (station instanceof WashingStation) {
                g.setColor(Color.CYAN); 
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.BLACK); g.drawString("SINK", sx+15, sy+20);
                WashingStation sink = (WashingStation) station;
                if (sink.hasPlates()) { 
                    BufferedImage img = assetMap.get("Dirty Plate");
                    if (img != null) g.drawImage(img, sx+15, sy+15, 34, 34, null);
                    else { g.setColor(Color.WHITE); g.fillOval(sx+20, sy+30, 20, 20); }
                }
                if (sink.getProgress() > 0) drawProgressBar(g, sx, sy, sink.getProgress(), 100);
            }
            else if (station instanceof TrashStation) {
                g.setColor(Color.BLACK);
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.WHITE); g.drawString("TRASH", sx+10, sy+35);
            }
            else if (station instanceof ServingCounter) {
                g.setColor(Color.MAGENTA);
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
                g.setColor(Color.WHITE); g.drawString("SERVE", sx+10, sy+35);
            }
            else if (station instanceof Wall) {
                g.setColor(Color.BLACK);
                g.fillRect(sx, sy, TILE_SIZE, TILE_SIZE);
                g.setColor(Color.WHITE); g.drawString("WALL", sx+20, sy+35);
            }
            else {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(sx+2, sy+2, TILE_SIZE-4, TILE_SIZE-4);
            }
        }
    }

    private void drawProgressBar(Graphics g, int x, int y, int current, int max) {
        g.setColor(Color.WHITE); g.fillRect(x + 5, y - 5, 54, 8);
        float pct = (float) current / max; if (pct > 1.0f) pct = 1.0f;
        if (max == 24 && current > 12) g.setColor(Color.RED); else g.setColor(Color.GREEN);
        g.fillRect(x + 5, y - 5, (int)(54 * pct), 8);
        g.setColor(Color.BLACK); g.drawRect(x + 5, y - 5, 54, 8);
    }

    private void drawChef(Graphics g, ChefPlayer chef, boolean isActive) {
        int x = chef.getPosition().getX() * TILE_SIZE;
        int y = chef.getPosition().getY() * TILE_SIZE + MAP_OFFSET_Y;
        
        if (isActive) {
            g.setColor(Color.YELLOW);
            int[] xPoints = {x + 32, x + 22, x + 42}; int[] yPoints = {y - 5, y - 15, y - 15};
            g.fillPolygon(xPoints, yPoints, 3);
        }
        
        g.setColor(isActive ? Color.BLUE : Color.GRAY); 
        g.fillOval(x + 10, y + 10, TILE_SIZE - 20, TILE_SIZE - 20);
        
        if (chef.isBusy()) { g.setColor(Color.RED); g.drawOval(x + 8, y + 8, TILE_SIZE - 16, TILE_SIZE - 16); }
        
        if (chef.getInventory() != null) {
            drawItem(g, chef.getInventory(), x + 30, y + 30, 30);
        }
    }

    private void drawItem(Graphics g, Item item, int x, int y, int size) {
        String key = item.getName();
        
        if (item instanceof Plate) {
            Plate p = (Plate) item;
            if (!p.isClean()) key = "Dirty Plate";
            else key = "Plate";
        }
        else if (item instanceof Ingredient) {
            Ingredient ing = (Ingredient) item;
            key = ing.getName(); 
            if (ing.getState() == IngredientState.CHOPPED) key = "Chopped " + key;
            if (ing.getState() == IngredientState.COOKED) key = "Cooked " + key;
        }

        BufferedImage img = assetMap.get(key);
        
        if (img != null) {
            g.drawImage(img, x, y, size, size, null);
        } else {
            g.setColor(Color.WHITE); 
            g.fillOval(x, y, size, size);
            g.setColor(Color.BLACK); 
            g.setFont(new Font("Arial", Font.PLAIN, 9));
            String label = key.length() > 4 ? key.substring(0, 4) : key;
            g.drawString(label, x + 2, y + size/2);
        }
    }

    private void drawUI(Graphics g) {
        g.setColor(new Color(50, 50, 50));
        g.fillRect(0, 0, getWidth(), 80);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        
        if (map.getOrderManager() != null) {
            int currentScore = map.getOrderManager().getScore();
            g.drawString("Score: " + currentScore + " / " + targetScore, 20, 25);
            
            int failed = map.getOrderManager().getFailedOrders();
            int lives = 5 - failed;
            if (lives < 0) lives = 0;
            
            g.setFont(new Font("SansSerif", Font.PLAIN, 30)); 
            int heartX = 20;
            int heartY = 60;
            
            for (int i = 0; i < 5; i++) {
                if (i < lives) {
                    g.setColor(Color.RED); 
                    g.drawString("❤", heartX, heartY); 
                } else {
                    g.setColor(Color.GRAY);
                    g.drawString("❤", heartX, heartY); 
                }
                heartX += 35; 
            }

            g.setColor(new Color(231, 76, 60)); 
            int btnW = 50; 
            int btnH = 40;
            int btnX = getWidth() - btnW - 20;
            int btnY = 15;
            g.fillRect(btnX, btnY, btnW, btnH);
            pauseButtonRect.setBounds(btnX, btnY, btnW, btnH);
            
            g.setColor(Color.WHITE);
            g.fillRect(btnX + 15, btnY + 10, 6, 20);          
            g.fillRect(btnX + 29, btnY + 10, 6, 20); 

            int xPos = 220; 
            int orderBoxWidth = 110; 
            int orderBoxHeight = 70; 
            
            for (Order o : map.getOrderManager().getActiveOrders()) {
                g.setColor(new Color(255, 255, 200)); 
                g.fillRect(xPos, 5, orderBoxWidth, orderBoxHeight);
                
                BufferedImage icon = assetMap.get(o.getRecipeName());
                if (icon != null) {
                    g.drawImage(icon, xPos + (orderBoxWidth - 40)/2, 10, 40, 40, null);
                }
                
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 11));
                String text = o.getRecipeName();
                int textW = g.getFontMetrics().stringWidth(text);
                g.drawString(text, xPos + (orderBoxWidth - textW)/2, 60);

                g.setColor(Color.GRAY); 
                g.fillRect(xPos, 70, orderBoxWidth, 5); 
                g.setColor(Color.GREEN);
                int barWidth = (int) (orderBoxWidth * ((double)o.getTimeLeft() / o.getMaxTime()));
                g.fillRect(xPos, 70, barWidth, 5); 
                
                xPos += orderBoxWidth + 10; 
            }
        }
    }

    private void drawPauseOverlay(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150)); 
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "GAME PAUSED";
        int titleW = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (getWidth() - titleW) / 2, getHeight() / 2 - 100);
        
        int btnW = 200;
        int btnH = 50;
        int btnX = (getWidth() - btnW) / 2;
        int startY = getHeight() / 2 - 40;
        
        g.setColor(new Color(255, 204, 0)); 
        g.fillRect(btnX, startY, btnW, btnH);
        resumeButtonRect.setBounds(btnX, startY, btnW, btnH);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        drawStringCentered(g, "RESUME", btnX, startY, btnW, btnH);
        
        int btnY2 = startY + 70;
        g.setColor(new Color(255, 204, 0)); 
        g.fillRect(btnX, btnY2, btnW, btnH);
        restartButtonRect.setBounds(btnX, btnY2, btnW, btnH);
        
        g.setColor(Color.BLACK);
        drawStringCentered(g, "RESTART", btnX, btnY2, btnW, btnH);

        int btnY3 = btnY2 + 70;
        g.setColor(new Color(231, 76, 60)); 
        g.fillRect(btnX, btnY3, btnW, btnH);
        exitButtonRect.setBounds(btnX, btnY3, btnW, btnH);
        
        g.setColor(Color.WHITE);
        drawStringCentered(g, "EXIT GAME", btnX, btnY3, btnW, btnH);
    }
    
    private void drawStringCentered(Graphics g, String text, int x, int y, int w, int h) {
        FontMetrics fm = g.getFontMetrics();
        int tx = x + (w - fm.stringWidth(text)) / 2;
        int ty = y + (h - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, tx, ty);
    }
}