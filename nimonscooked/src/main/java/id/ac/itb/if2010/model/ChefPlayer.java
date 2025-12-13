package id.ac.itb.if2010.model;

public class ChefPlayer {
    private String name;
    private Position position;
    private Direction direction;
    private Item inventory;
    private ChefAction currentAction;
    private Runnable cancelCallback; 

    // Dash Throw State
    private long lastDashTime = 0;
    private static final long DASH_COOLDOWN = 2000; 
    private static final int DASH_DISTANCE = 3; 
    private static final int THROW_DISTANCE = 4; 

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

    public void dash(Direction dir, GameMap map, java.util.List<ChefPlayer> allChefs) {
        long now = System.currentTimeMillis();
        if (now - lastDashTime < DASH_COOLDOWN) {
            System.out.println(name + " dash on cooldown.");
            return;
        }

        this.direction = dir;
        int newX = position.getX();
        int newY = position.getY();

        for (int i = 0; i < DASH_DISTANCE; i++) {
            int nextX = newX, nextY = newY;
            switch (dir) {
                case UP:    nextY--; break;
                case DOWN:  nextY++; break;
                case LEFT:  nextX--; break;
                case RIGHT: nextX++; break;
            }

            if (!map.isValidPosition(nextX, nextY)) break;
            Station st = map.getStationAt(nextX, nextY);
            if (st instanceof Wall) break;

            boolean blocked = false;
            for (ChefPlayer other : allChefs) {
                if (other != this && other.getPosition().getX() == nextX && other.getPosition().getY() == nextY) {
                    blocked = true; break;
                }
            }
            if (blocked) break;

            newX = nextX; newY = nextY;
        }

        this.position.setX(newX);
        this.position.setY(newY);
        lastDashTime = System.currentTimeMillis();
        System.out.println(name + " dashed to (" + newX + "," + newY + ")");
    }

    public Item throwItem(Direction dir, GameMap map) {
        if (inventory == null) {
            System.out.println(name + " has nothing to throw.");
            return null;
        }
        if (!(inventory instanceof Ingredient)) {
            System.out.println(name + " cannot throw this item.");
            return null;
        }

        Ingredient ing = (Ingredient) inventory;
        if (ing.getState() != IngredientState.RAW && ing.getState() != IngredientState.CHOPPED) {
            System.out.println(name + " can't throw cooked ingredient.");
            return null;
        }

        this.direction = dir;
        int landX = position.getX(), landY = position.getY();

        for (int i = 0; i < THROW_DISTANCE; i++) {
            int nextX = landX, nextY = landY;
            switch (dir) {
                case UP:    nextY--; break;
                case DOWN:  nextY++; break;
                case LEFT:  nextX--; break;
                case RIGHT: nextX++; break;
            }
            if (!map.isValidPosition(nextX, nextY)) break;
            Station st = map.getStationAt(nextX, nextY);
            if (st instanceof Wall) break;
            landX = nextX; landY = nextY;
        }

        Ingredient thrown = new Ingredient(inventory.getName(), new Position(landX, landY));
        thrown.setState(ing.getState());
        this.inventory = null;
        System.out.println(name + " threw " + thrown.getName() + " to (" + landX + "," + landY + ")");
        return thrown;
    }
}
