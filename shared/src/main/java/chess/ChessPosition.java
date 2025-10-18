package chess;

/**
 * Represents a single square on a chess board.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int row;
    private final int col;

    /** Rows and columns are 1–8 (not 0-indexed). */
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /** @return which row this position is in (1 = bottom row) */
    public int getRow() {
        return row; }

    /** @return which column this position is in (1 = leftmost column) */
    public int getColumn() {
        return col; }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ChessPosition other)
                && this.row == other.row
                && this.col == other.col;
    }

    @Override
    public int hashCode() {
        return row * 8 + col;
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}