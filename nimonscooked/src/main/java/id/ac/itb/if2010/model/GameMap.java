package id.ac.itb.if2010.model;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private int rows = 10;
    private int cols = 14;
    private Station[][] grid;
    private List<ChefPlayer> chefs;

    public GameMap() {
        this.grid = new Station[rows][cols];
        this.chefs = new ArrayList<>();
        initializeUltimateMap();
    }

    private void initializeUltimateMap() {
        System.out.println("Initializing Ultimate Test Kitchen...");

        grid[1][1] = new IngredientStorage(new Position(1, 1), "Rice");
        grid[1][2] = new IngredientStorage(new Position(2, 1), "Nori");
        grid[1][3] = new IngredientStorage(new Position(3, 1), "Cucumber");

        CookingStation riceStove = new CookingStation(new Position(2, 2));
        riceStove.setDevice(new BoilingPot(new Position(2, 2))); 
        grid[2][2] = riceStove;

        grid[1][5] = new IngredientStorage(new Position(5, 1), "Meat");
        grid[1][6] = new IngredientStorage(new Position(6, 1), "Bun");
        grid[1][7] = new IngredientStorage(new Position(7, 1), "Cheese");
        CookingStation meatStove = new CookingStation(new Position(6, 2));
        meatStove.setDevice(new FryingPan(new Position(6, 2)));
        grid[2][6] = meatStove;

        grid[1][9] = new IngredientStorage(new Position(9, 1), "Dough");
        grid[1][10] = new IngredientStorage(new Position(10, 1), "Tomato");

        CookingStation ovenStation = new CookingStation(new Position(10, 2));
        ovenStation.setDevice(new Oven(new Position(10, 2))); 
        grid[2][10] = ovenStation;

        grid[6][2] = new CuttingStation(new Position(2, 6));
        grid[6][4] = new AssemblyStation(new Position(4, 6));
        grid[6][3] = new AssemblyStation(new Position(3, 6));


        grid[6][8] = new PlateStorage(new Position(8, 6));
        grid[6][10] = new WashingStation(new Position(10, 6));
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