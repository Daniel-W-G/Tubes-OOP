package id.ac.itb.if2010.model;

public class BoilingPot extends KitchenUtensil implements CookingDevice {
    private boolean isCooking = false;
    private int progress = 0;
    private boolean isBurned = false;

    public BoilingPot(Position position) {
        super("Boiling Pot", position);
    }

    @Override public boolean isPortable() { return true; }
    @Override public int capacity() { return 1; }

    @Override
    public boolean canAccept(Preparable ingredient) {
        if (contents.size() >= capacity()) return false;

        if (ingredient instanceof Item) {
            String name = ((Item) ingredient).getName();
            boolean isRiceOrPasta = name.equalsIgnoreCase("Rice") || name.equalsIgnoreCase("Pasta");
            return !isBurned && isRiceOrPasta && ingredient.canBeCooked();
        }
        
        return false;
    }

    @Override
    public void addIngredient(Preparable ingredient) {
        if (canAccept(ingredient)) {
            contents.add(ingredient);
            System.out.println("Added " + ((Item)ingredient).getName() + " to Pot.");
        }
    }

    @Override
    public void startCooking() {
        if (!contents.isEmpty() && !isBurned) {
            this.isCooking = true;
            // set state jadi COOKING untuk semua ingredient di dalam pot
            for (Preparable p : contents) {
                if (p instanceof Ingredient) {
                    ((Ingredient) p).setCooking();
                }
            }
            System.out.println("Pot started boiling!");
        }
    }
    
    @Override public boolean isCooking() { return isCooking; }

    @Override
    public void processCooking() {
        if (isCooking) {
            progress++;
            if (progress % 20 == 0) System.out.println("Boiling... " + progress + "%");
            
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
                System.out.println("ALARM! : Pot Burned!");
            }
        }
    }
    
    @Override
    public void clearContents() {
        super.clearContents();
        this.isCooking = false;
        this.isBurned = false;
        this.progress = 0;
        System.out.println("Pot emptied.");
    }
}