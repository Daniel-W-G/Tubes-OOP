package id.ac.itb.if2010.model;

public class FryingPan extends KitchenUtensil implements CookingDevice {
    private boolean isCooking = false;
    private int progress = 0;
    private boolean isBurned = false; 

    public FryingPan(Position position) {
        super("Frying Pan", position);
    }

    @Override public boolean isPortable() { return true; }
    @Override public int capacity() { return 1; }

    @Override
    public boolean canAccept(Preparable ingredient) {
        return !isBurned && contents.size() < capacity() && ingredient.canBeCooked();
    }

    @Override
    public void addIngredient(Preparable ingredient) {
        if (canAccept(ingredient)) {
            contents.add(ingredient);
            System.out.println("Added " + ((Item)ingredient).getName() + " to Pan.");
        }
    }

    @Override
    public void startCooking() {
        if (!contents.isEmpty() && !isBurned) {
            this.isCooking = true;
            System.out.println("Pan started sizzling!");
        }
    }

    @Override public boolean isCooking() { return isCooking; }
    
    @Override
    public void processCooking() {
        if (isCooking) {
            progress++;
            
            if (progress % 20 == 0) System.out.println("Frying... " + progress + "%");

            if (progress == 100) {
                for (Preparable p : contents) {
                    p.cook(); 
                }
                System.out.println("Cooking Done! (Take it off now!)");
            }

            if (progress >= 150) {
                this.isBurned = true;
                this.isCooking = false; 
                
                for (Preparable p : contents) {
                    if (p instanceof Ingredient) {
                        ((Ingredient) p).setState(IngredientState.BURNED);
                    }
                }
                System.out.println("ALARM! Pan burned!");
            }
        }
    }

    @Override
    public void clearContents() {
        super.clearContents();
        this.isCooking = false;
        this.isBurned = false; 
        this.progress = 0;
        System.out.println("Frying Pan emptied.");
    }
}