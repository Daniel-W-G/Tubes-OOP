package id.ac.itb.if2010.model;

public class BoilingPot extends KitchenUtensil implements CookingDevice {
    private boolean isCooking = false;
    private int progress = 0;
    private boolean isBurned = false;
    private Thread cookingThread;

    @Override
    public int getProgress() {
        return progress;
    }

    public BoilingPot(Position position) {
        super("Boiling Pot", position);
    }

    @Override public boolean isPortable() { return true; }
    @Override public int capacity() { return 1; }
    @Override public boolean isCooking() { return isCooking; }

    @Override
    public boolean canAccept(Preparable ingredient) {
        if (isBurned || contents.size() >= capacity()) return false;
        if (ingredient instanceof Item) {
            String name = ((Item) ingredient).getName();
            boolean isRiceOrPasta = name.equalsIgnoreCase("Rice") || name.equalsIgnoreCase("Pasta");
            return isRiceOrPasta && ingredient.canBeCooked();
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
        if (!contents.isEmpty() && !isBurned && !isCooking) {
            this.isCooking = true;
            System.out.println("Pot started boiling!");
            
            Runnable cookTask = () -> {
                try {
                    while (isCooking && !isBurned) {
                        Thread.sleep(1000);
                        progress++;
                        System.out.println("Boiling Pot: " + progress + "s");
                        if (progress == 12) {
                            for (Preparable p : contents) p.cook();
                            System.out.println("Boiling Pot: Food is COOKED!");
                        }
                        if (progress >= 24) burnFood();
                    }
                } catch (InterruptedException e) {
                    System.out.println("Pot boiling stopped.");
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
            System.out.println("Pot removed from heat.");
        }
    }
    
    private void burnFood() {
        this.isBurned = true;
        this.isCooking = false;
        for (Preparable p : contents) {
            if (p instanceof Ingredient) ((Ingredient) p).setState(IngredientState.BURNED);
        }
        System.out.println("ALARM! Pot contents BURNED!");
    }

    @Override
    public void clearContents() {
        super.clearContents();
        stopCooking();
        this.isBurned = false;
        this.progress = 0;
        System.out.println("Boiling Pot cleaned and reset.");
    }
}