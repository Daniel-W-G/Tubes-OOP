package id.ac.itb.if2010.model;

import java.util.Stack;

public class WashingStation extends Station {
    private Stack<Plate> dirtyPlates;
    private int cleanPlatesReady;
    private Thread washThread;
    
    private Plate currentWashingPlate;

    public WashingStation(Position position) {
        super("Sink", position);
        this.dirtyPlates = new Stack<>();
        this.cleanPlatesReady = 0;
    }
    
    public boolean hasPlates() { return !dirtyPlates.isEmpty() || cleanPlatesReady > 0; }
    
    public int getProgress() {
        if (currentWashingPlate != null) return currentWashingPlate.getWashProgress();
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
        this.currentWashingPlate = dirtyPlates.peek();
        
        System.out.println("Washing plate... (" + currentWashingPlate.getWashProgress() + "%)");
        
        Runnable task = () -> {
            try {
                while (currentWashingPlate.getWashProgress() < 100) {
                    Thread.sleep(100);
                    currentWashingPlate.addWashProgress(4); 
                }
                dirtyPlates.pop(); 
                cleanPlatesReady++; 
                chef.setBusy(ChefAction.IDLE, null);
                this.currentWashingPlate = null;
                
                System.out.println("Plate Cleaned!");
                
            } catch (InterruptedException e) {
                System.out.println("Washing paused.");
                this.currentWashingPlate = null;
            }
        };
        
        this.washThread = new Thread(task);
        this.washThread.start();
        
        chef.setBusy(ChefAction.BUSY_WASHING, () -> {
            if (washThread != null) washThread.interrupt();
        });
    }
}