package id.ac.itb.if2010.model;

public class Ingredient extends Item implements Preparable {
    private IngredientState state;
    private int cuttingProgress; 

    public Ingredient(String name, Position position) {
        super(name, position);
        this.state = IngredientState.RAW;
        this.cuttingProgress = 0;
    }

    public IngredientState getState() { 
        return state; 
    }

    public void setState(IngredientState state) { 
        this.state = state; 
    }
   
    public int getCuttingProgress() { 
        return cuttingProgress; 
    }

    public void addCuttingProgress(int amount) { 
        this.cuttingProgress += amount; 
        if (this.cuttingProgress > 100) this.cuttingProgress = 100;
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
            this.cuttingProgress = 100; 
            System.out.println(this.name + " is now CHOPPED.");
        }
    }

    @Override
    public void cook() {
        if (canBeCooked()) {
            this.state = IngredientState.COOKED;
            System.out.println(this.name + " is now COOKED.");
        }
    }
}