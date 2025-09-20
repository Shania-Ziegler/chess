package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares;


    public ChessBoard() {
        squares = new ChessPiece[8][8]; //8x8 board standard chess size
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessBoard) {
            ChessBoard other = (ChessBoard) obj;
            return java.util.Arrays.deepEquals(this.squares, other.squares);
        }
    return false;
}

    @Override
    public int hashCode() {
        return java.util.Arrays.deepHashCode(squares);
}

    @Override
    public String toString() {
        return "ChessBoard with pieces";
    }
}



