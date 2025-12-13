package id.ac.itb.if2010.model;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private final int rows = 10;
    private final int cols = 14;
    private Station[][] grid;
    private List<ChefPlayer> chefs;
    private OrderManager orderManager;
    private List<Item> thrownItems;

    public GameMap() {
        this.grid = new Station[rows][cols];
        this.chefs = new ArrayList<>();
        this.thrownItems = new ArrayList<>();
        
        this.orderManager = new OrderManager(RecipeBook.getAllRecipes());
        
        initializeUltimateMap();
    }
    
    public OrderManager getOrderManager() { return orderManager; }

    private void initializeUltimateMap() {
        System.out.println("Initializing Ultimate Test Kitchen...");


        for (int i = 0; i < 10; i++) {
            grid [0][i] = new Wall(new Position(i, 0));

        }
        for (int i = 0; i < 14; i++) {
            grid [9][i] = new Wall(new Position(13, i));
        }
        grid[8][0] = new Wall(new Position(0, 8));
        grid[8][9] = new Wall(new Position(9, 8));
        grid[8][13] = new Wall(new Position(13, 8));
        grid[7][3] = new Wall(new Position(3, 7));
        grid[7][13] = new Wall(new Position(13, 7));



        grid[3][13] = new IngredientStorage(new Position(13, 3), "Rice");
        grid[4][13] = new IngredientStorage(new Position(13, 4), "Nori");
        grid[3][0] = new IngredientStorage(new Position(0, 3), "Fish");
        grid[4][0] = new IngredientStorage(new Position(0, 4), "Shrimp");
        grid[5][0] = new IngredientStorage(new Position(0, 5), "Cucumber");

        grid[0][9] = new AssemblyStation(new Position(9, 0));
        grid[0][13] = new AssemblyStation(new Position(13, 0));
        grid[1][0] = new AssemblyStation(new Position(0, 1));
        grid[1][2] = new AssemblyStation(new Position(2, 1));
        grid[1][4] = new AssemblyStation(new Position(4, 1));
        grid[1][6] = new AssemblyStation(new Position(6, 1));
        grid[1][13] = new AssemblyStation(new Position(13, 1));
        grid[2][0] = new AssemblyStation(new Position(0, 2));
        grid[2][6] = new AssemblyStation(new Position(6, 2));
        grid[2][13] = new AssemblyStation(new Position(13, 2));
        grid[3][6] = new AssemblyStation(new Position(6, 3));
        grid[4][3] = new AssemblyStation(new Position(3, 4));
        grid[4][6] = new AssemblyStation(new Position(6, 4));
        grid[5][3] = new AssemblyStation(new Position(3, 5));
        grid[5][6] = new AssemblyStation(new Position(6, 5));
        grid[5][13] = new AssemblyStation(new Position(13, 5));
        grid[6][3] = new AssemblyStation(new Position(6, 3));
        grid[6][6] = new AssemblyStation(new Position(6, 6));
        grid[6][13] = new AssemblyStation(new Position(6, 13));

        
        CookingStation Stove1 = new CookingStation(new Position(10, 0));
        Stove1.setDevice(new BoilingPot(new Position(10, 0)));
        grid[0][10] = Stove1;
        

        CookingStation Stove2 = new CookingStation(new Position(11, 0));
        Stove2.setDevice(new BoilingPot(new Position(11, 0)));
        grid[0][11] = Stove2;

        
        CookingStation Stove3 = new CookingStation(new Position(12, 0));
        Stove3.setDevice(new FryingPan(new Position(12, 0)));
        grid[0][12] = Stove3;
        

        grid[1][1] = new CuttingStation(new Position(1, 1));
        grid[1][3] = new CuttingStation(new Position(3, 1));
        grid[1][5] = new CuttingStation(new Position(5, 1));
        
        PlateStorage plateStorage = new PlateStorage(new Position(11, 7));
        grid[7][11] = plateStorage;
        
        grid[7][9] = new WashingStation(new Position(9, 7), plateStorage);
        grid[7][10] = new WashingStation(new Position(10, 7), plateStorage);
        
        grid[6][0] = new ServingCounter(new Position(0, 6), orderManager, plateStorage);
        grid[7][0] = new ServingCounter(new Position(0, 7), orderManager, plateStorage);
        
        grid[7][6] = new TrashStation(new Position(6, 7), plateStorage);
    }

    public Station getStationAt(int x, int y) {
        if (isValidPosition(x, y)) return grid[y][x];
        return null;
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < cols && y >= 0 && y < rows;
    }

    public void addChef(ChefPlayer chef) { chefs.add(chef); }
    public List<ChefPlayer> getChefs() { return chefs; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public void addThrownItem(Item item) {
        if (item != null) thrownItems.add(item);
    }

    public Item getThrownItemAt(int x, int y) {
        for (Item it : thrownItems) {
            if (it.getPosition() != null && it.getPosition().getX() == x && it.getPosition().getY() == y) {
                return it;
            }
        }
        return null;
    }

    public Item tryPickupThrownItem(ChefPlayer chef) {
        Item it = getThrownItemAt(chef.getPosition().getX(), chef.getPosition().getY());
        if (it != null) {
            thrownItems.remove(it);
        }
        return it;
    }

    public java.util.List<Item> getThrownItems() {
        return thrownItems;
    }
}