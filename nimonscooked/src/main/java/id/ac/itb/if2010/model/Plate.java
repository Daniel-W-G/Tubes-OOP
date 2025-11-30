package id.ac.itb.if2010.model;

public class Plate extends KitchenUtensil {
    private boolean isClean;
    private int stackSize;
    private int washProgress; 

    public Plate(Position position) {
        super("Plate", position);
        this.isClean = true;
        this.stackSize = 1;
        this.washProgress = 0;
        this.spriteName = "plate_clean";
    }

    public boolean isClean() { return isClean; }
    
    public void setClean(boolean clean) { 
        this.isClean = clean; 
        this.spriteName = isClean ? "plate_clean" : "plate_dirty";
        this.washProgress = 0; 
        if (isClean) { 
            this.contents.clear(); 
            this.stackSize = 1; 
        }
    }
    
    public int getWashProgress() { return washProgress; }
    public void addWashProgress(int amount) {
        this.washProgress += amount;
        if (this.washProgress > 100) this.washProgress = 100;
    }
    
    public int getStackSize() { return stackSize; }
    public void setStackSize(int n) { this.stackSize = n; }
    public void addPlate() { this.stackSize++; }

    @Override
    public boolean hasItems() { return !contents.isEmpty(); }

    @Override
    public void clearContents() {
        if (!contents.isEmpty()) {
            super.clearContents();
            this.setClean(false);
            System.out.println("Plate is now DIRTY.");
        }
    }

    public boolean canAccept(Preparable ingredient) {
        return isClean && stackSize == 1;
    }

    public void addIngredient(Preparable ingredient) {
        if (canAccept(ingredient)) {
            this.contents.add(ingredient);
            System.out.println("Added " + ((Item)ingredient).getName() + " to Plate.");
        }
    }
}