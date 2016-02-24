/**
 * A single column in a Connect 4 game. A column stores a number of individual slots.
 * 
 * You should not modify this class, but you will need to use methods within it.
 */
public class Connect4Column {
    private Connect4Slot[] slots;
    
    public Connect4Column(int height) {
        slots = new Connect4Slot[height];
        for (int i = 0; i < height; i++) {
            slots[i] = new Connect4Slot();
        }
    }

    public Connect4Column(Connect4Column column) {
        this.slots = new Connect4Slot[column.getRowCount()];
        for (int i = 0; i < column.getRowCount(); i++) {
            slots[i] = new Connect4Slot(column.getSlot(i));
        }
    }

    // this method should go to Column class ????????????????????????????????????
    /**
     * Returns the index of the top empty slot in a particular column, or -1 if the column is already full.
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++) {
            if (!column.getSlot(i).getIsFilled()) {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    
    public Connect4Slot getSlot(int i) {
        if (i < slots.length && i >= 0) {
            return slots[i];
        } else {
            return null;
        }
    }

    public boolean getIsFull() {
        for (Connect4Slot slot : slots) {
            if (!slot.getIsFilled()) {
                return false;
            }
        }
        return true;
    }

    public int getRowCount() {
        return slots.length;
    }
}
