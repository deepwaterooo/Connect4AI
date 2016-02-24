/**
 * A single slot in a Connect 4 board. A slot can be either empty or filled, and it can
 * be filled with either a red token or a blue token.
 * 
 * You should not modify this class, but you will need to use methods within it.
 */
public class Connect4Slot {
    private boolean isFilled;
    private boolean isRed;
    private boolean isHighlighted;
    
    public Connect4Slot() {
        this.isFilled = false;
        this.isRed = false;
    }

    public Connect4Slot(Connect4Slot slot) {
        this.isFilled = slot.getIsFilled();
        this.isRed = slot.getIsRed();
    }

    public boolean getIsFilled() {
        return isFilled;
    }

    public boolean getIsRed() {
        return isRed;
    }

    public void addRed() {
        if (!isFilled) {
            this.isFilled = true;
            this.isRed = true;
        }
    }

    public void addBlue() {   
        if (!isFilled) {
            this.isFilled = true;
            this.isRed = false;
        }
    }
    
    public boolean getIsHighlighted() {
        return isHighlighted;
    }

    public void highlight() {
        this.isHighlighted = true;
    }

    public void clear() {
        this.isFilled = false;
        this.isRed = false;
        this.isHighlighted = false;
    }
}
