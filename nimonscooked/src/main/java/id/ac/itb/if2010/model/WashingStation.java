package id.ac.itb.if2010.model;

import java.util.Stack;

public class WashingStation extends Station {
    private Stack<Plate> dirtyPlates;
    private int cleanPlatesReady;

    public WashingStation(Position position, PlateStorage storage) {
        super("Sink", position);
        this.dirtyPlates = new Stack<>();
        this.cleanPlatesReady = 0;
    }
    
    public boolean hasPlates() { return !dirtyPlates.isEmpty() || cleanPlatesReady > 0; }
    
    public void addDirtyPlate(Plate plate) {
        dirtyPlates.push(plate);
    }
    
    public int getProgress() {
        if (!dirtyPlates.isEmpty()) return dirtyPlates.peek().getWashProgress();
        return 0;
    }

    @Override
    public void interact(ChefPlayer chef) {
        if (chef.isBusy()) return;

        Item item = chef.getInventory();

        if (item instanceof Plate) {
            Plate p = (Plate) item;
            if (!p.isClean()) {
                int count = p.getStackSize();
                for(int i=0; i<count; i++) {
                    Plate singleDirty = new Plate(this.getPosition());
                    singleDirty.setClean(false);
                    dirtyPlates.push(singleDirty);
                }
                
                chef.setInventory(null);
                System.out.println("Added " + count + " dirty plates to sink. Total: " + dirtyPlates.size());
            } else {
                System.out.println("That plate is already clean!");
            }
        }
        else if (item == null) {
            if (cleanPlatesReady > 0) {
                Plate p = new Plate(chef.getPosition());
                chef.setInventory(p);
                cleanPlatesReady--;
                System.out.println("Picked up a washed plate.");
            }
            else if (!dirtyPlates.isEmpty()) {
                startWashing(chef);
            } else {
                System.out.println("Sink is empty.");
            }
        }
    }
    
    private void startWashing(ChefPlayer chef) {
        Plate plateToWash = dirtyPlates.peek();
        
        System.out.println("Washing plate... (" + plateToWash.getWashProgress() + "%)");
        
        Runnable task = () -> {
            try {
                while (plateToWash.getWashProgress() < 100) {
                    Thread.sleep(100);
                    plateToWash.addWashProgress(4); 
                }
                
                // Only pop if the plate at the top is still the one we washed
                if (!dirtyPlates.isEmpty() && dirtyPlates.peek() == plateToWash) {
                    dirtyPlates.pop();
                }
                
                cleanPlatesReady++;
                chef.setBusy(ChefAction.IDLE, null);
                
                System.out.println("Plate Cleaned!");
                
            } catch (InterruptedException e) {
                System.out.println("Washing paused.");
            }
        };
        
        Thread washThread = new Thread(task);
        washThread.start();
        
        chef.setBusy(ChefAction.BUSY_WASHING, () -> {
            if (washThread != null) washThread.interrupt();
        });
    }
}