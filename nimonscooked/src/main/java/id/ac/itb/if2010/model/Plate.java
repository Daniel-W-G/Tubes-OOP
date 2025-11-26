package id.ac.itb.if2010.model;

public class Plate extends KitchenUtensil {
    private boolean isClean;
    private Dish platedDish; 

    public Plate(Position position) {
        super("Plate", position);
        this.isClean = true;
        this.platedDish = null;
        this.spriteName = "plate_clean";
    }

    public boolean isClean() { return isClean; }
    public void setClean(boolean clean) { 
        this.isClean = clean; 
        this.spriteName = isClean ? "plate_clean" : "plate_dirty";
        if (isClean) { this.platedDish = null; this.contents.clear(); }
    }
    public Dish getPlatedDish() { return platedDish; }

    @Override
    public boolean hasItems() {
        return !contents.isEmpty() || platedDish != null;
    }

    @Override
    public void clearContents() {
        super.clearContents(); 
        this.platedDish = null; 
        System.out.println("Plate scraped clean.");
    }

    public boolean canAccept(Preparable ingredient) {
        return isClean && platedDish == null;
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