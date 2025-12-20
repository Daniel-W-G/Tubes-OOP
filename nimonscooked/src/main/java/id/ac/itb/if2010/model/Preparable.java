package id.ac.itb.if2010.model;

public interface Preparable {
    boolean canBeChopped();
    boolean canBeCooked();
    boolean canBePlacedOnPlate();
    void chop();
    void cook();
}
