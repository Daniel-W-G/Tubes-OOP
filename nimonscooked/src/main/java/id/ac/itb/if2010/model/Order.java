package id.ac.itb.if2010.model;

public class Order {
    private String recipeName; 
    private int timeLeft;      
    private int maxTime;       
    private int reward;        

    public Order(String recipeName, int duration, int reward) {
        this.recipeName = recipeName;
        this.maxTime = duration;
        this.timeLeft = duration;
        this.reward = reward;
    }

    public String getRecipeName() { return recipeName; }
    public int getReward() { return reward; }
    public int getTimeLeft() { return timeLeft; }
    public int getMaxTime() { return maxTime; }
    
    public boolean tick() {
        if (timeLeft > 0) timeLeft--;
        return timeLeft <= 0;
    }
}