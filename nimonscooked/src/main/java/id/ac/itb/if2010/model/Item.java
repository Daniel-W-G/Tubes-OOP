package id.ac.itb.if2010.model;

public abstract class Item {
    protected String name;
    protected Position position;
    protected String spriteName;

    public Item(String name, Position position) {
        this.name = name;
        this.position = position;
        this.spriteName = name.toLowerCase().replace(" ", "_");
    }
    
    public String getSpriteName() {
        return spriteName;
    }
    
    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}