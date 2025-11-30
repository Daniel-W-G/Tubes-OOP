package id.ac.itb.if2010.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class OrderManager {
    private List<Order> activeOrders;
    private List<Recipe> availableRecipes;
    private int score;
    private int failedOrders;
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
            System.out.println("ORDER FAILED: " + o.getRecipeName());
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
                spawnOrder();
                return true;
            }
        }
        
        System.out.println("ORDER FAIL: Customer didn't want " + dishName);
        return false;
    }
    
    public List<Order> getActiveOrders() { return activeOrders; }
    public int getScore() { return score; }
    public int getFailedOrders() { return failedOrders; }
}