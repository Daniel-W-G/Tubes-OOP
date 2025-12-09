package id.ac.itb.if2010.model;

public class ChefPlayer {
    private String name;
    private Position position;
    private Direction direction;
    private Item inventory;
    private ChefAction currentAction;
    private Runnable cancelCallback; 

    public ChefPlayer(String name, Position position) {
        this.name = name;
        this.position = position;
        this.direction = Direction.RIGHT;
        this.inventory = null;
        this.currentAction = ChefAction.IDLE;
    }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public Direction getDirection() { return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }
    public Item getInventory() { return inventory; }
    public void setInventory(Item item) { this.inventory = item; }
    public String getName() { return name; }
    public ChefAction getCurrentAction() { return currentAction; }

    public boolean isBusy() {
        return currentAction == ChefAction.BUSY_CUTTING || currentAction == ChefAction.BUSY_WASHING;
    }
    
    public void setBusy(ChefAction action, Runnable cancelCallback) {
        this.currentAction = action;
        this.cancelCallback = cancelCallback;
        if (isBusy()) {
            System.out.println(name + " is now " + action);
        } else {
            System.out.println(name + " is free.");
        }
    }

    public void move(Direction dir) {
        if (isBusy()) {
            if (cancelCallback != null) {
                System.out.println(name + " stopped working to move.");
                cancelCallback.run(); 
            }
            this.currentAction = ChefAction.IDLE;
            this.cancelCallback = null;
        }

        this.direction = dir; 
        
        int newX = position.getX();
        int newY = position.getY();

        switch (dir) {
            case UP:    newY--; break;
            case DOWN:  newY++; break;
            case LEFT:  newX--; break;
            case RIGHT: newX++; break;
        }
        
        this.position.setX(newX);
        this.position.setY(newY);
    }
}
