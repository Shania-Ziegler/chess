package chess;
import java.chess.ChessGame;
import java.util.Arrays;
/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board;
    public ChessBoard() {
        board = new ChessPiece[8][8]; // standard 8x8 chessboard
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[toIndex(position.getRow())][toIndex(position.getColumn())] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if none
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[toIndex(position.getRow())][toIndex(position.getColumn())];
    }

    /**
     * Resets the board to the default starting layout.
     */
    public void resetBoard() {
        board = new ChessPiece[8][8];
        setupBackRow(0, ChessGame.TeamColor.WHITE);
        setupPawnRow(1, ChessGame.TeamColor.WHITE);
        setupBackRow(7, ChessGame.TeamColor.BLACK);
        setupPawnRow(6, ChessGame.TeamColor.BLACK);
    }

    /** Sets up the back row pieces. */
    private void setupBackRow(int row, ChessGame.TeamColor color) {
        ChessPiece.PieceType[] order = {
                ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK
        };
        for (int col = 0; col < 8; col++) {
            board[row][col] = new ChessPiece(color, order[col]);
        }
    }

    /** Sets up a row of pawns. */
    private void setupPawnRow(int row, ChessGame.TeamColor color) {
        for (int col = 0; col < 8; col++) {
            board[row][col] = new ChessPiece(color, ChessPiece.PieceType.PAWN);
        }
    }

    /** Helper: convert chess row/col (1–8) to array index (0–7). */
    private int toIndex(int num) {
        return num - 1;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ChessBoard)
                && Arrays.deepEquals(board, ((ChessBoard) obj).board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        return "ChessBoard with pieces";
    }
}
