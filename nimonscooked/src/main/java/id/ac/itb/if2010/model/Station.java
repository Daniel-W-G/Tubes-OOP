package id.ac.itb.if2010.model;

public abstract class Station {
    protected String name;
    protected Position position;

    public Station(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public abstract void interact(ChefPlayer chef);
}