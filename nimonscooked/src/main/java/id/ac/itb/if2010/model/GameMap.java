package id.ac.itb.if2010.model;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private int rows = 10;
    private int cols = 14;
    private Station[][] grid;
    private List<ChefPlayer> chefs;
    private OrderManager orderManager;

    public GameMap() {
        this.grid = new Station[rows][cols];
        this.chefs = new ArrayList<>();
        
        this.orderManager = new OrderManager(RecipeBook.getAllRecipes());
        
        initializeUltimateMap();
    }
    
    public OrderManager getOrderManager() { return orderManager; }

    private void initializeUltimateMap() {
        System.out.println("Initializing Ultimate Test Kitchen...");

        grid[1][1] = new IngredientStorage(new Position(1, 1), "Rice");
        grid[1][2] = new IngredientStorage(new Position(2, 1), "Nori");
        grid[1][3] = new IngredientStorage(new Position(3, 1), "Cucumber");
        grid[1][4] = new IngredientStorage(new Position(4, 1), "Meat");
        grid[1][5] = new IngredientStorage(new Position(5, 1), "Bun");
        grid[1][6] = new IngredientStorage(new Position(6, 1), "Cheese");
        grid[1][7] = new IngredientStorage(new Position(7, 1), "Dough");
        grid[1][8] = new IngredientStorage(new Position(8, 1), "Tomato");

        

        CookingStation riceStove = new CookingStation(new Position(2, 3));
        riceStove.setDevice(new BoilingPot(new Position(2, 3)));
        grid[3][2] = riceStove;
        

        CookingStation meatStove = new CookingStation(new Position(4, 3));
        meatStove.setDevice(new FryingPan(new Position(4, 3)));
        grid[3][4] = meatStove;
        

        CookingStation ovenStation = new CookingStation(new Position(6, 3));
        ovenStation.setDevice(new Oven(new Position(6, 3)));
        grid[3][6] = ovenStation;
        
        grid[6][2] = new CuttingStation(new Position(2, 6));
        grid[6][4] = new CuttingStation(new Position(4, 6));
        
        grid[6][6] = new AssemblyStation(new Position(6, 6));
        grid[6][7] = new AssemblyStation(new Position(7, 6));
        
        PlateStorage plateStorage = new PlateStorage(new Position(10, 6));
        grid[6][10] = plateStorage;
        
        grid[6][11] = new WashingStation(new Position(11, 6));
        
        grid[4][12] = new ServingCounter(new Position(12, 4), orderManager, plateStorage);
        
        grid[6][12] = new TrashStation(new Position(12, 6));
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
}