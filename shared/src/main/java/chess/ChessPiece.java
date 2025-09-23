package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    // Movement direction constants to eliminate duplication
    private static final int[][] STRAIGHT_DIRECTIONS = {{1,0}, {-1,0}, {0,1}, {0,-1}};
    private static final int[][] DIAGONAL_DIRECTIONS = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};
    private static final int[][] ALL_DIRECTIONS = {{1,0}, {-1,0}, {0,1}, {0,-1}, {1,1}, {1,-1}, {-1,1}, {-1,-1}};
    private static final int[][] KNIGHT_MOVES = {{2,1}, {2,-1}, {-2,1}, {-2,-1}, {1,2}, {1,-2}, {-1,2}, {-1,-2}};

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in danger
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);

        if (piece == null) return moves; // Safety check

        switch (piece.getPieceType()) {
            case ROOK:
                addDirectionalMoves(moves, board, myPosition, piece, STRAIGHT_DIRECTIONS);
                break;
            case BISHOP:
                addDirectionalMoves(moves, board, myPosition, piece, DIAGONAL_DIRECTIONS);
                break;
            case QUEEN:
                addDirectionalMoves(moves, board, myPosition, piece, ALL_DIRECTIONS);
                break;
            case KING:
                addSingleStepMoves(moves, board, myPosition, piece, ALL_DIRECTIONS);
                break;
            case KNIGHT:
                addSingleStepMoves(moves, board, myPosition, piece, KNIGHT_MOVES);
                break;
            case PAWN:
                addPawnMoves(moves, board, myPosition, piece);
                break;
        }
        return moves;
    }

    /**
     * Unified method for pieces that move in multiple directions until hitting something
     * Used by rook, bishop, and queen
     */
    private void addDirectionalMoves(ArrayList<ChessMove> moves, ChessBoard board,
                                     ChessPosition myPosition, ChessPiece piece, int[][] directions) {
        for (int[] dir : directions) {
            for (int i = 1; i < 8; i++) {
                int newRow = myPosition.getRow() + (dir[0] * i);
                int newCol = myPosition.getColumn() + (dir[1] * i);

                if (!isValidPosition(newRow, newCol)) break;

                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece target = board.getPiece(newPos);

                if (target == null) {
                    moves.add(new ChessMove(myPosition, newPos, null));
                } else {
                    if (target.getTeamColor() != piece.getTeamColor()) {
                        moves.add(new ChessMove(myPosition, newPos, null));
                    }
                    break; // Can't move past any piece
                }
            }
        }
    }

    /**
     * Unified method for pieces that move exactly one step
     * Used by king and knight
     */
    private void addSingleStepMoves(ArrayList<ChessMove> moves, ChessBoard board,
                                    ChessPosition myPosition, ChessPiece piece, int[][] moveOptions) {
        for (int[] move : moveOptions) {
            int newRow = myPosition.getRow() + move[0];
            int newCol = myPosition.getColumn() + move[1];

            if (isValidPosition(newRow, newCol)) {
                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece target = board.getPiece(newPos);

                if (target == null || target.getTeamColor() != piece.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, newPos, null));
                }
            }
        }
    }

    /**
     * Handles pawn movement rules (forward movement, diagonal captures, promotion)
     */
    private void addPawnMoves(ArrayList<ChessMove> moves, ChessBoard board,
                              ChessPosition myPosition, ChessPiece piece) {
        int direction = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 2 : 7;

        // Try moving forward one square
        int newRow = myPosition.getRow() + direction;
        if (isValidPosition(newRow, myPosition.getColumn())) {
            ChessPosition newPos = new ChessPosition(newRow, myPosition.getColumn());
            if (board.getPiece(newPos) == null) {
                addPawnMove(moves, myPosition, newPos);

                // Try moving forward two squares from starting position
                if (myPosition.getRow() == startRow) {
                    ChessPosition twoSquares = new ChessPosition(newRow + direction, myPosition.getColumn());
                    if (board.getPiece(twoSquares) == null) {
                        addPawnMove(moves, myPosition, twoSquares);
                    }
                }
            }
        }

        // Try capturing diagonally
        for (int colOffset : new int[]{-1, 1}) {
            int newCol = myPosition.getColumn() + colOffset;
            if (isValidPosition(newRow, newCol)) {
                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece target = board.getPiece(newPos);

                if (target != null && target.getTeamColor() != piece.getTeamColor()) {
                    addPawnMove(moves, myPosition, newPos);
                }
            }
        }
    }

    /**
     * Adds pawn move with promotion logic if reaching the end of the board
     */
    private void addPawnMove(ArrayList<ChessMove> moves, ChessPosition start, ChessPosition end) {
        if (end.getRow() == 1 || end.getRow() == 8) {
            // Pawn promotion - add all possible promotion pieces
            moves.add(new ChessMove(start, end, PieceType.QUEEN));
            moves.add(new ChessMove(start, end, PieceType.ROOK));
            moves.add(new ChessMove(start, end, PieceType.BISHOP));
            moves.add(new ChessMove(start, end, PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(start, end, null));
        }
    }

    /**
     * Checks if a position is within the chess board boundaries
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    /**
     * Compares two chess pieces for equality based on color and type
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessPiece) {
            ChessPiece other = (ChessPiece) obj;
            return this.pieceColor == other.pieceColor && this.type == other.type;
        }
        return false;
    }

    /**
     * Generates hash code for efficient collection storage
     */
    @Override
    public int hashCode() {
        return pieceColor.hashCode() + type.hashCode() * 31;
    }

    /**
     * Returns readable string representation for debugging
     */
    @Override
    public String toString() {
        return pieceColor + " " + type;
    }
}