package id.ac.itb.if2010.model;

public class Wall extends Station {
    public Wall(Position position) {
        super("Wall", position);
    }

    @Override 
    public void interact(ChefPlayer chef) {
        return;
    }
    
}
