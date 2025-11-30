package id.ac.itb.if2010.model;

public class CuttingStation extends Station {
    private Thread cuttingThread;
    private Item currentItem; 

    public CuttingStation(Position position) {
        super("Cutting Board", position);
    }
    
    public int getProgress() {
        if (currentItem instanceof Ingredient) {
            return ((Ingredient)currentItem).getCuttingProgress();
        }
        return 0;
    }

    public Item getCurrentItem() {
        return this.currentItem;
    }
    
    private boolean isCuttable(Item item) {
        if (item instanceof Ingredient) {
            Ingredient ing = (Ingredient) item;
            return ing.canBeChopped() && ing.getState() == IngredientState.RAW;
        }
        return false;
    }

    @Override
    public void interact(ChefPlayer chef) {
        if (chef.isBusy()) return;
        Item handItem = chef.getInventory();

        if (currentItem == null) {
            if (handItem != null) {
                this.currentItem = handItem;
                chef.setInventory(null);
                System.out.println("Placed " + currentItem.getName() + " on Cutting Board.");
            } else {
                System.out.println("Nothing to place.");
            }
        }
        
        else {
            if (isCuttable(currentItem) && handItem == null) {
                startCuttingProcess(chef, (Ingredient) currentItem);
                return;
            }
            handleAssemblyInteraction(chef, handItem);
        }
    }
    
    private void handleAssemblyInteraction(ChefPlayer chef, Item handItem) {
        if (handItem == null) {
            chef.setInventory(currentItem);
            currentItem = null;
            System.out.println("Picked up " + chef.getInventory().getName());
        }
        else if (handItem instanceof Plate && currentItem instanceof Plate) {
            Plate handPlate = (Plate) handItem;
            Plate tablePlate = (Plate) currentItem;
            if (!handPlate.isClean() && !tablePlate.isClean()) {
                int combined = tablePlate.getStackSize() + handPlate.getStackSize();
                tablePlate.setStackSize(combined);
                chef.setInventory(null);
                System.out.println("Stacked dirty plates on Board.");
            }
        }
        else if (handItem instanceof Plate && currentItem instanceof Preparable) {
            Plate plate = (Plate) handItem;
            if (plate.canAccept((Preparable) currentItem)) {
                plate.addIngredient((Preparable) currentItem);
                currentItem = null; 
            }
        }
        else if (handItem instanceof Preparable && currentItem instanceof Plate) {
            Plate plate = (Plate) currentItem;
            if (plate.canAccept((Preparable) handItem)) {
                plate.addIngredient((Preparable) handItem);
                chef.setInventory(null); 
            }
        }
        else if (handItem instanceof KitchenUtensil && currentItem instanceof Plate) {
            KitchenUtensil utensil = (KitchenUtensil) handItem;
            Plate plate = (Plate) currentItem;
            java.util.List<Preparable> moved = new java.util.ArrayList<>();
            for (Preparable p : utensil.getContents()) {
                if (plate.canAccept(p)) moved.add(p);
            }
            if (!moved.isEmpty()) {
                for (Preparable p : moved) plate.addIngredient(p);
                utensil.clearContents();
                System.out.println("Poured food onto plate (on Board).");
            }
        }
        else {
            System.out.println("Can't do that here!");
        }
    }

    private void startCuttingProcess(ChefPlayer chef, Ingredient ingredient) {
        System.out.println("Resuming chop for " + ingredient.getName() + " (" + ingredient.getCuttingProgress() + "%)");
        
        Runnable task = () -> {
            try {
                while (ingredient.getCuttingProgress() < 100) {
                    Thread.sleep(100); 
                    ingredient.addCuttingProgress(4); 
                }
                ingredient.chop();
                chef.setBusy(ChefAction.IDLE, null);
                System.out.println("Chopping finished! Pick up your item.");
            } catch (InterruptedException e) {
                System.out.println("Chopping paused.");
            }
        };

        this.cuttingThread = new Thread(task);
        this.cuttingThread.start();

        chef.setBusy(ChefAction.BUSY_CUTTING, () -> {
            if (cuttingThread != null) cuttingThread.interrupt();
        });
    }
}