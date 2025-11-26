package id.ac.itb.if2010.model;

public class Oven extends KitchenUtensil implements CookingDevice {
    private boolean isCooking = false;
    private int progress = 0;
    private boolean isBurned = false;

    public Oven(Position position) {
        super("Oven", position);
    }

    @Override public boolean isPortable() { return false; }
    @Override public int capacity() { return 3; } 
    @Override public boolean isCooking() { return isCooking; }

    @Override
    public boolean canAccept(Preparable ingredient) {
        return !isBurned && contents.size() < capacity() && ingredient.canBeCooked();
    }

    @Override
    public void addIngredient(Preparable ingredient) {
        if (canAccept(ingredient)) {
            contents.add(ingredient);
            System.out.println("Added " + ((Item)ingredient).getName() + " to Oven.");
            if (this.progress > 0) {
                this.progress = Math.max(0, this.progress - 20);
                System.out.println("Ingredients added! Progress reduced to " + this.progress + "%");
            }
        }
    }

    @Override
    public void startCooking() {
        if (!contents.isEmpty() && !isBurned) {
            this.isCooking = true;
            System.out.println("Oven started heating up!");
        }
    }

    @Override
    public void processCooking() {
        if (isCooking) {
            progress++;
            if (progress % 20 == 0) System.out.println("Baking... " + progress + "%");

            if (progress == 100) {
                for (Preparable p : contents) {
                    p.cook(); 
                }
                System.out.println("Oven finished baking! (Ding!)");
            }

            if (progress >= 150) {
                this.isBurned = true;
                this.isCooking = false;
                for (Preparable p : contents) {
                    if (p instanceof Ingredient) ((Ingredient) p).setState(IngredientState.BURNED);
                }
                System.out.println("Oven is smoking! Food is BURNED!");
            }
        }
    }
    
    @Override
    public void clearContents() {
        super.clearContents();
        this.isCooking = false;
        this.isBurned = false;
        this.progress = 0;
        System.out.println("Oven cleaned.");
    }
}