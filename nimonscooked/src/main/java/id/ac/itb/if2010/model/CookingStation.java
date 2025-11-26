package id.ac.itb.if2010.model;

import java.util.ArrayList;
import java.util.List;

public class CookingStation extends Station {
    private CookingDevice currentDevice;

    public CookingStation(Position position) {
        super("Stove", position);
        this.currentDevice = null;
    }
    
    public void setDevice(CookingDevice device) {
        this.currentDevice = device;
    }
    
    public CookingDevice getDevice() {
        return currentDevice;
    }

    @Override
    public void interact(ChefPlayer chef) {
        Item itemInHand = chef.getInventory();

        if (currentDevice == null) {
            if (itemInHand instanceof CookingDevice) {
                System.out.println("Placed " + itemInHand.getName() + " on cooking station.");
                this.currentDevice = (CookingDevice) itemInHand;
                chef.setInventory(null);
            } else {
                System.out.println("You need a Cooking Device here!");
            }
        } 
        
        else {
            if (itemInHand == null) {
                if (currentDevice.isPortable() && currentDevice instanceof Item) {
                     Item deviceToPickUp = (Item) currentDevice;
                     
                     chef.setInventory(deviceToPickUp);
                     this.currentDevice = null;
                     
                     System.out.println("Picked up the " + deviceToPickUp.getName());
                }
                else if (!currentDevice.isPortable() && currentDevice instanceof KitchenUtensil) {
                     KitchenUtensil device = (KitchenUtensil) currentDevice;
                     
                     if (device.getContents().isEmpty()) {
                         System.out.println("The " + device.getName() + " is empty.");
                         return;
                     }

                     Dish dish = RecipeBook.createDishIfValid(device.getContents(), chef.getPosition());
                     
                     if (dish != null) {
                         chef.setInventory(dish);
                         device.clearContents();
                         System.out.println("Took " + dish.getName() + " from " + device.getName());
                     } else {
                         if (device.getContents().size() == 1) {
                             Item content = (Item) device.getContents().get(0);
                             chef.setInventory(content);
                             device.clearContents();
                             System.out.println("Took " + content.getName() + " from " + device.getName());
                         } else {
                             System.out.println("It's a mess of ingredients... (Try adding a Plate to scoop it?)");
                         }
                     }
                }
            } 
            else if (itemInHand instanceof Preparable) {
                Preparable ingredient = (Preparable) itemInHand;
                if (currentDevice.canAccept(ingredient)) {
                    currentDevice.addIngredient(ingredient);
                    chef.setInventory(null);
                    currentDevice.startCooking();
                } else {
                    System.out.println("Can't put that in!");
                }
            }
            else if (itemInHand instanceof Plate && !currentDevice.isPortable() && currentDevice instanceof KitchenUtensil) {
                Plate plate = (Plate) itemInHand;
                KitchenUtensil device = (KitchenUtensil) currentDevice;
                
                List<Preparable> itemsToMove = new ArrayList<>();
                for (Preparable food : device.getContents()) {
                    if (plate.canAccept(food)) {
                        itemsToMove.add(food);
                    }
                }
                
                if (!itemsToMove.isEmpty()) {
                    for (Preparable food : itemsToMove) {
                        plate.addIngredient(food);
                    }
                    device.clearContents();
                    System.out.println("Scooped food from " + device.getName() + " onto Plate.");
                } else {
                    System.out.println("Nothing to scoop or plate is full/dirty.");
                }
            }
        }
    }
    
    public void tick() {
        if (currentDevice != null && currentDevice.isCooking()) {
            currentDevice.processCooking();
        }
    }
}