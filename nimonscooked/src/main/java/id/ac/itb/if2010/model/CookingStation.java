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
        if (chef.isBusy()) return;

        Item itemInHand = chef.getInventory();

        if (currentDevice == null) {
            if (itemInHand instanceof CookingDevice) {
                System.out.println("Placed " + itemInHand.getName() + " on cooking station.");
                this.currentDevice = (CookingDevice) itemInHand;
                chef.setInventory(null);
                
                if (currentDevice instanceof KitchenUtensil && !((KitchenUtensil)currentDevice).getContents().isEmpty()) {
                    currentDevice.startCooking();
                }
            } else {
                System.out.println("You need a Cooking Device here!");
            }
        } 
        
        else {
            if (itemInHand instanceof Plate && currentDevice instanceof KitchenUtensil) {
                Plate plate = (Plate) itemInHand;
                KitchenUtensil device = (KitchenUtensil) currentDevice;
                
                if (!device.getContents().isEmpty()) {
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
                        currentDevice.stopCooking();
                        device.clearContents(); 
                        
                        System.out.println("Instantly plated food from " + device.getName() + ".");
                        return; 
                    } else {
                        System.out.println("Plate is full or dirty.");
                    }
                } else {
                    System.out.println("Device is empty.");
                }
                return; 
            }

            if (itemInHand == null) {
                if (currentDevice.isPortable() && currentDevice instanceof Item) {
                     Item deviceToPickUp = (Item) currentDevice;
                     if (currentDevice instanceof CookingDevice) currentDevice.stopCooking();

                     chef.setInventory(deviceToPickUp);
                     this.currentDevice = null;
                     System.out.println("Picked up the " + deviceToPickUp.getName());
                }
                else if (!currentDevice.isPortable()) {
                     System.out.println("It's hot! Use a Plate to scoop the food.");
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
        }
    }
}