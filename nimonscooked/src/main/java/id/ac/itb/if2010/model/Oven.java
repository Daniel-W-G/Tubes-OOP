package id.ac.itb.if2010.model;

public class Oven extends KitchenUtensil implements CookingDevice {
    private boolean isCooking = false;
    private int progress = 0;
    private boolean isBurned = false;
    private Thread cookingThread;
    
    @Override
    public int getProgress() {
        return progress;
    }

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
                this.progress = Math.max(0, this.progress - 3);
            }
        }
    }

    @Override
    public void startCooking() {
        if (!contents.isEmpty() && !isBurned && !isCooking) {
            this.isCooking = true;
            System.out.println("Oven started baking!");
            
            Runnable cookTask = () -> {
                try {
                    while (isCooking && !isBurned) {
                        Thread.sleep(1000);
                        progress++;
                        System.out.println("Oven: " + progress + "s");
                        if (progress == 12) {
                            for (Preparable p : contents) p.cook();
                            System.out.println("Oven: Pizza is BAKED!");
                        }
                        if (progress >= 24) burnFood();
                    }
                } catch (InterruptedException e) {
                    System.out.println("Oven baking stopped.");
                }
            };
            this.cookingThread = new Thread(cookTask);
            this.cookingThread.start();
        }
    }
    
    @Override
    public void stopCooking() {
        if (isCooking) {
            this.isCooking = false;
            if (cookingThread != null && cookingThread.isAlive()) {
                cookingThread.interrupt();
            }
            System.out.println("Oven turned off.");
        }
    }
    
    private void burnFood() {
        this.isBurned = true;
        this.isCooking = false;
        for (Preparable p : contents) {
            if (p instanceof Ingredient) ((Ingredient) p).setState(IngredientState.BURNED);
        }
        System.out.println("ALARM! Oven BURNED!");
    }

    @Override
    public void clearContents() {
        super.clearContents();
        stopCooking();
        this.isBurned = false;
        this.progress = 0;
        System.out.println("Oven cleaned.");
    }
}