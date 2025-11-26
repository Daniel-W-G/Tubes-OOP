package id.ac.itb.if2010.model;

import java.util.ArrayList;
import java.util.List;

public class AssemblyStation extends Station {
    private Item itemOnTable;

    public AssemblyStation(Position position) {
        super("Assembly Table", position);
        this.itemOnTable = null;
    }
    
    public Item getItem() { return itemOnTable; }

    @Override
    public void interact(ChefPlayer chef) {
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
                System.out.println("Picked up item from table.");
            } 
            else if (handItem instanceof Preparable && itemOnTable instanceof Plate) {
                Plate plate = (Plate) itemOnTable;
                if (plate.canAccept((Preparable) handItem)) {
                    plate.addIngredient((Preparable) handItem);
                    chef.setInventory(null);
                } else {
                    System.out.println("Cannot place that on this plate!");
                }
            }
            else if (handItem instanceof KitchenUtensil && itemOnTable instanceof Plate) {
                KitchenUtensil utensil = (KitchenUtensil) handItem;
                Plate plate = (Plate) itemOnTable;
                
                List<Preparable> itemsToMove = new ArrayList<>();
                
                for (Preparable food : utensil.getContents()) {
                    if (plate.canAccept(food)) {
                        itemsToMove.add(food);
                    }
                }
                
                if (!itemsToMove.isEmpty()) {
                    for (Preparable food : itemsToMove) {
                        plate.addIngredient(food); 
                    }
                    utensil.clearContents(); 
                    System.out.println("Transferred food from " + utensil.getName() + " to Plate.");
                } else {
                    System.out.println("Nothing in the " + utensil.getName() + " can go on this plate.");
                }
            }
            else if (handItem instanceof Plate && itemOnTable instanceof Preparable) {
                 Plate plate = (Plate) handItem;
                 if (plate.canAccept((Preparable) itemOnTable)) {
                     plate.addIngredient((Preparable) itemOnTable);
                     itemOnTable = null;
                 }
            }
        }
    }
}