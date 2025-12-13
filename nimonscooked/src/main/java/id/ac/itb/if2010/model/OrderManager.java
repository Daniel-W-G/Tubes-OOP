package id.ac.itb.if2010.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class OrderManager {
    private List<Order> activeOrders;
    private List<Recipe> availableRecipes;
    private int score;
    private int successfulOrders;
    private int failedOrders;
    private int wrongOrders;
    private Random random;

    public OrderManager(List<Recipe> levelRecipes) {
        this.activeOrders = new CopyOnWriteArrayList<>();
        this.availableRecipes = levelRecipes;
        this.score = 0;
        this.failedOrders = 0;
        this.random = new Random();
        
        spawnOrder();
        spawnOrder();
    }
    
    public void spawnOrder() {
        if (availableRecipes.isEmpty()) return;
        Recipe randomRecipe = availableRecipes.get(random.nextInt(availableRecipes.size()));
        
        if (activeOrders.size() < 5) {
            Order newOrder = new Order(randomRecipe.getName(), 600, 100);
            activeOrders.add(newOrder);
            System.out.println("NEW ORDER: " + randomRecipe.getName());
        }
    }

    public void tick() {
        List<Order> expired = new ArrayList<>();
        
        for (Order o : activeOrders) {
            if (o.tick()) {
                expired.add(o);
            }
        }
        
        for (Order o : expired) {
            activeOrders.remove(o);
            failedOrders++;
            score -= 10;
            if (score < 0) score = 0;
            System.out.println("ORDER FAILED: Late to serve " + o.getRecipeName());
            spawnOrder();
        }
    }
    
    public boolean deliverDish(String dishName) {
        if (dishName == null) return false;
        
        for (Order o : activeOrders) {
            if (o.getRecipeName().equalsIgnoreCase(dishName)) {
                score += o.getReward();
                activeOrders.remove(o);
                System.out.println("ORDER SUCCESS: Served " + dishName);
                successfulOrders++;
                spawnOrder();
                return true;
            }
        }
        
        System.out.println("ORDER FAIL: Customer didn't want " + dishName);
        wrongOrders++;
        score -= 5;
        if (score < 0) score = 0;
        return false;
    }
    
    public List<Order> getActiveOrders() { return activeOrders; }
    public int getScore() { return score; }
    public int getFailedOrders() { return failedOrders; }
    public int getSuccessfulOrders() { return successfulOrders; }
    public int getWrongOrders() { return wrongOrders; }

}