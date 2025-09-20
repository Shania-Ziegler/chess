package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board;


    public ChessBoard() {
        board = new ChessPiece[8][8]; //8x8 board standard chess size 2darray representation
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
        // Convert from chess rules on 8x8 (1-8) to array indices (0-7)
        //ie Chess positions start at 1, but arrays start at 0
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];

    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Clear the board first
        board = new ChessPiece[8][8];

        // Set up starting positions for both teams
        setupWhitePieces();
        setupBlackPieces();
    }

    /**
     * Places white pieces in their starting positions (rows 1-2)
     */
    private void setupWhitePieces() {
        setupBackRow(0, ChessGame.TeamColor.WHITE);
        setupPawnRow(1, ChessGame.TeamColor.WHITE);
    }

    /**
     * Places black pieces in their starting positions (rows 7-8)
     */
    private void setupBlackPieces() {
        setupBackRow(7, ChessGame.TeamColor.BLACK);
        setupPawnRow(6, ChessGame.TeamColor.BLACK);
    }

    /**
     * Sets up the back row pieces (rook, knight, bishop, queen, king, bishop, knight, rook)
     * @param row The row to place pieces (0 for white, 7 for black)
     * @param color The team color of the pieces
     */
    private void setupBackRow(int row, ChessGame.TeamColor color) {
        ChessPiece.PieceType[] backRowOrder = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };

        for (int col = 0; col < 8; col++) {
            board[row][col] = new ChessPiece(color, backRowOrder[col]);
        }
    }

    /**
     * Sets up a row of pawns
     * @param row The row to place pawns (1 for white, 6 for black)
     * @param color The team color of the pawns
     */
    private void setupPawnRow(int row, ChessGame.TeamColor color) {
        for (int col = 0; col < 8; col++) {
            board[row][col] = new ChessPiece(color, ChessPiece.PieceType.PAWN);
        }
    }


     // Compares two chess boards for equality

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessBoard) {
            ChessBoard other = (ChessBoard) obj;
            return java.util.Arrays.deepEquals(this.board, other.board);
        }
    return false;
}

    @Override
    public int hashCode() {
        return java.util.Arrays.deepHashCode(board);
}

   //Returns a simple string representation of the board
     // Could be improved to show actual board layout for debugging

    @Override
    public String toString() {
        return "ChessBoard with pieces";
    }
}



