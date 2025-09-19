package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    //making final so I don't accident overwrite anything gives immutability
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col; //this initialized the fields
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
        // throw new RuntimeException("Not implemented");
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
        //throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessPosition) {
            ChessPosition other = (ChessPosition) obj;
            return this.row == other.row && this.col == other.col;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return row * 8 + col;
    }

    @Override
    public String toString() {
        return row + ""+ col;
    }
}