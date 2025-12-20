package id.ac.itb.if2010.model;

public interface CookingDevice {
    boolean isPortable();
    int capacity();
    boolean canAccept(Preparable ingredient);
    void addIngredient(Preparable ingredient);
    void startCooking();
    void stopCooking(); 
    boolean isCooking();
    int getProgress();

}