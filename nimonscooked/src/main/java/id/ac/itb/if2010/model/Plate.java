package id.ac.itb.if2010.model;

public class Plate extends KitchenUtensil {
    private boolean isClean;
    private Dish platedDish; 
    private int stackSize; 

    public Plate(Position position) {
        super("Plate", position);
        this.isClean = true;
        this.platedDish = null;
        this.stackSize = 1; 
        this.spriteName = "plate_clean";
    }

    public boolean isClean() { return isClean; }
    
    public void setClean(boolean clean) { 
        this.isClean = clean; 
        this.spriteName = isClean ? "plate_clean" : "plate_dirty";
        if (isClean) { 
            this.platedDish = null; 
            this.contents.clear(); 
            this.stackSize = 1; 
        }
    }
    
    public int getStackSize() { return stackSize; }
    public void setStackSize(int n) { this.stackSize = n; }
    public void addPlate() { this.stackSize++; }

    public Dish getPlatedDish() { return platedDish; }

    @Override
    public boolean hasItems() {
        return !contents.isEmpty() || platedDish != null;
    }

    @Override
    public void clearContents() {
        if (this.platedDish != null) {
            this.platedDish = null;
            System.out.println("Dish trashed. The plate is now DIRTY.");
            this.setClean(false); 
        } else {
            super.clearContents();
            System.out.println("Ingredients cleared. Plate remains clean.");
        }
    }

    public boolean canAccept(Preparable ingredient) {
        return isClean && platedDish == null && stackSize == 1;
    }

    public void addIngredient(Preparable ingredient) {
        if (canAccept(ingredient)) {
            this.contents.add(ingredient);
            System.out.println("Added " + ((Item)ingredient).getName() + " to Plate.");
            
            Dish result = RecipeBook.createDishIfValid(this.contents, this.position);
            
            if (result != null) {
                this.platedDish = result;
                this.contents.clear(); 
                System.out.println("Wow! You created " + result.getName() + "!");
                this.spriteName = result.getSpriteName(); 
            }
        }
    }
}