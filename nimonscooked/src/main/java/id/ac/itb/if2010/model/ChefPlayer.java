package id.ac.itb.if2010.model;

public class ChefPlayer {
    private String name;
    private Position position;
    private Direction direction;
    private Item inventory;

    public ChefPlayer(String name, Position position) {
        this.name = name;
        this.position = position;
        this.direction = Direction.RIGHT; 
        this.inventory = null; 
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Item getInventory() {
        return inventory;
    }

    public void setInventory(Item item) {
        this.inventory = item;
    }

    public void move(Direction dir) {
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
        
        System.out.println(name + " moved " + dir + " to (" + newX + ", " + newY + ")");
    }
}