package id.ac.itb.if2010.model;

public class AssemblyStation extends Station {
    protected Item itemOnTable;

    public AssemblyStation(Position position) {
        super("Assembly Table", position);
        this.itemOnTable = null;
    }
    
    public Item getItem() { return itemOnTable; }
    
    public void setItem(Item item) {
        this.itemOnTable = item;
    }

    @Override
    public void interact(ChefPlayer chef) {
        if (chef.isBusy()) return;

        Item handItem = chef.getInventory();

        if (itemOnTable == null) {
            if (handItem != null) {
                itemOnTable = handItem;
                chef.setInventory(null);
                System.out.println("Placed " + itemOnTable.getName() + " on table.");
            } else {
                System.out.println("Nothing to place here.");
            }
        }
        
        else {
            if (handItem == null) {
                chef.setInventory(itemOnTable);
                itemOnTable = null;
                System.out.println("Picked up " + chef.getInventory().getName());
            } 
            
            else if (handItem instanceof Plate && itemOnTable instanceof Plate) {
                Plate handPlate = (Plate) handItem;
                Plate tablePlate = (Plate) itemOnTable;
                if (!handPlate.isClean() && !tablePlate.isClean()) {
                    int combined = tablePlate.getStackSize() + handPlate.getStackSize();
                    tablePlate.setStackSize(combined);
                    chef.setInventory(null);
                    System.out.println("Stacked dirty plates. Total: " + combined);
                } else {
                    System.out.println("Can't stack clean/dirty mix!");
                }
            }
            
            else if (handItem instanceof Plate && itemOnTable instanceof Preparable) {
                Plate plate = (Plate) handItem;
                if (plate.canAccept((Preparable) itemOnTable)) {
                    plate.addIngredient((Preparable) itemOnTable);
                    itemOnTable = null; 
                } else {
                    System.out.println("Plate can't accept that.");
                }
            }
            
            
            else if (handItem instanceof Preparable && itemOnTable instanceof Plate) {
                Plate plate = (Plate) itemOnTable;
                if (plate.canAccept((Preparable) handItem)) {
                    plate.addIngredient((Preparable) handItem);
                    chef.setInventory(null); 
                } else {
                    System.out.println("Plate can't accept that.");
                }
            }
            
            else if (handItem instanceof KitchenUtensil && itemOnTable instanceof Plate) {
                KitchenUtensil utensil = (KitchenUtensil) handItem;
                Plate plate = (Plate) itemOnTable;
                java.util.List<Preparable> moved = new java.util.ArrayList<>();
                for (Preparable p : utensil.getContents()) {
                    if (plate.canAccept(p)) moved.add(p);
                }
                
                if (!moved.isEmpty()) {
                    for (Preparable p : moved) plate.addIngredient(p);
                    utensil.clearContents();
                    System.out.println("Poured food onto plate.");
                } else {
                    System.out.println("Nothing to pour or plate full.");
                }
            }
            
            else {
                System.out.println("Can't merge these items!");
            }
        }
    }
}