package id.ac.itb.if2010.model;

public class Ingredient extends Item implements Preparable {
    private IngredientState state;

    public Ingredient(String name, Position position) {
        super(name, position);
        this.state = IngredientState.RAW; 
    }

    public IngredientState getState() {
        return state;
    }

    public void setState(IngredientState state) {
        this.state = state;
    }

    @Override
    public boolean canBeChopped() {
        return this.state == IngredientState.RAW;
    }

    @Override
    public boolean canBeCooked() {
        return this.state == IngredientState.CHOPPED || this.state == IngredientState.RAW; 
    }

    @Override
    public boolean canBePlacedOnPlate() {
        return true;
    }

    @Override
    public void chop() {
        if (canBeChopped()) {
            this.state = IngredientState.CHOPPED;
            System.out.println(this.name + " is now CHOPPED.");
        } else {
            System.out.println("Cannot chop " + this.name + "!");
        }
    }

    @Override
    public void cook() {
        if (canBeCooked()) {
            this.state = IngredientState.COOKED;
            System.out.println(this.name + " is now COOKED.");
        } else {
            System.out.println("Cannot cook " + this.name + "!");
        }
    }
}