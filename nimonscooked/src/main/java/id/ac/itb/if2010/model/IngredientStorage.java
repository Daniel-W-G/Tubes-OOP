package id.ac.itb.if2010.model;

public class IngredientStorage extends Station {
    private String ingredientName;
    private Item itemOnTop; 

    public IngredientStorage(Position position, String ingredientName) {
        super("Crate of " + ingredientName, position);
        this.ingredientName = ingredientName;
        this.itemOnTop = null;
    }
    
    public String getIngredientName() { return ingredientName; }
    public Item getItemOnTop() { return itemOnTop; }

    @Override
    public void interact(ChefPlayer chef) {
        if (chef.isBusy()) return;

        Item handItem = chef.getInventory();

        if (handItem != null) {
            if (itemOnTop == null) {
                itemOnTop = handItem;
                chef.setInventory(null);
                System.out.println("Placed " + itemOnTop.getName() + " on top of " + ingredientName + " crate.");
            } else {
                handleAssemblyInteraction(chef, handItem);
            }
        }
        
        else {
            if (itemOnTop != null) {
                chef.setInventory(itemOnTop);
                itemOnTop = null;
                System.out.println("Picked up " + chef.getInventory().getName() + " from top of crate.");
            }
            else {
                Ingredient newIng = new Ingredient(ingredientName, chef.getPosition());
                chef.setInventory(newIng);
                System.out.println("Grabbed " + ingredientName + " from storage.");
            }
        }
    }
    
    private void handleAssemblyInteraction(ChefPlayer chef, Item handItem) {
        if (handItem instanceof Plate && itemOnTop instanceof Plate) {
            Plate handPlate = (Plate) handItem;
            Plate tablePlate = (Plate) itemOnTop;

            if (!handPlate.isClean() && !tablePlate.isClean()) {
                int combined = tablePlate.getStackSize() + handPlate.getStackSize();
                tablePlate.setStackSize(combined);
                chef.setInventory(null);
                System.out.println("Stacked dirty plates on Crate. Total: " + combined);
            } else {
                System.out.println("Can't stack clean/dirty mix!");
            }
        }
        else if (handItem instanceof Plate && itemOnTop instanceof Preparable) {
            Plate plate = (Plate) handItem;
            if (plate.canAccept((Preparable) itemOnTop)) {
                plate.addIngredient((Preparable) itemOnTop);
                itemOnTop = null; 
            } else {
                System.out.println("Plate can't accept that.");
            }
        }
        else if (handItem instanceof Preparable && itemOnTop instanceof Plate) {
            Plate plate = (Plate) itemOnTop;
            if (plate.canAccept((Preparable) handItem)) {
                plate.addIngredient((Preparable) handItem);
                chef.setInventory(null); 
            } else {
                System.out.println("Plate can't accept that.");
            }
        }
        else if (handItem instanceof KitchenUtensil && itemOnTop instanceof Plate) {
            KitchenUtensil utensil = (KitchenUtensil) handItem;
            Plate plate = (Plate) itemOnTop;
            
            java.util.List<Preparable> moved = new java.util.ArrayList<>();
            for (Preparable p : utensil.getContents()) {
                if (plate.canAccept(p)) moved.add(p);
            }
            
            if (!moved.isEmpty()) {
                for (Preparable p : moved) plate.addIngredient(p);
                utensil.clearContents();
                System.out.println("Poured food onto plate (on Crate).");
            } else {
                System.out.println("Nothing to pour or plate full.");
            }
        }
        
        else {
            System.out.println("Crate top is full!");
        }
    }
}