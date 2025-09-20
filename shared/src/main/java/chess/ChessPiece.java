package chess;


import java.util.Collection;
import java.util.ArrayList; //implements collection


/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

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
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);

        if (piece == null) return moves; //small saftey check so if no piece exist return null

        PieceType type = piece.getPieceType();

        switch (type) { //if rook do straight if bishop do diagonal and so forth just adds what moves to do
            case ROOK:
                addStraightMoves(moves, board, myPosition, piece);
                break;
            case BISHOP:
                addDiagonalMoves(moves, board, myPosition, piece);
                break;
            case QUEEN:
                addStraightMoves(moves, board, myPosition, piece);
                addDiagonalMoves(moves, board, myPosition, piece);
                break;
            case KING:
                addKingMoves(moves, board, myPosition, piece);
                break;
            case KNIGHT:
                addKnightMoves(moves, board, myPosition, piece);
                break;
            case PAWN:
                addPawnMoves(moves, board, myPosition, piece);
                break;
        }

        return moves;
    }
    // Helper method for rook/queen straight line moves
    private void addStraightMoves(ArrayList<ChessMove> moves, ChessBoard board,
                                  ChessPosition myPosition, ChessPiece piece) {
        int[][] directions = {{1,0}, {-1,0}, {0,1}, {0,-1}}; //defines up, down, right, left
//try each direction
        for (int[] dir : directions) {
            for (int i = 1; i < 8; i++) {
                int newRow = myPosition.getRow() + (dir[0] * i);
                int newCol = myPosition.getColumn() + (dir[1] * i);

                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) break; //if going out of bounds stop

                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece target = board.getPiece(newPos);

                if (target == null) {
                    moves.add(new ChessMove(myPosition, newPos, null));
                } else {
                    if (target.getTeamColor() != piece.getTeamColor()) {
                        moves.add(new ChessMove(myPosition, newPos, null));
                    }
                    break; // can't move past piece if we hit it :)
                }
            }
        }
    }

    // Helper method for bishop/queen diagonal moves
    private void addDiagonalMoves(ArrayList<ChessMove> moves, ChessBoard board,
                                  ChessPosition myPosition, ChessPiece piece) {
        int[][] directions = {{1,1}, {1,-1}, {-1,1}, {-1,-1}}; // all diagonals
//same logic as straight but diagonally
        for (int[] dir : directions) {
            for (int i = 1; i < 8; i++) {
                int newRow = myPosition.getRow() + (dir[0] * i);
                int newCol = myPosition.getColumn() + (dir[1] * i);

                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) break;

                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece target = board.getPiece(newPos);

                if (target == null) {
                    moves.add(new ChessMove(myPosition, newPos, null));
                } else {
                    if (target.getTeamColor() != piece.getTeamColor()) {
                        moves.add(new ChessMove(myPosition, newPos, null));
                    }
                    break; //stops if hitting any piece
                }
            }
        }
    }

    // Helper method for king moves (one square in any direction)
    private void addKingMoves(ArrayList<ChessMove> moves, ChessBoard board,
                              ChessPosition myPosition, ChessPiece piece) {
        int[][] directions = {{1,0}, {-1,0}, {0,1}, {0,-1}, {1,1}, {1,-1}, {-1,1}, {-1,-1}}; //the moves of the king any 1 square

        for (int[] dir : directions) {
            int newRow = myPosition.getRow() + dir[0];
            int newCol = myPosition.getColumn() + dir[1];
//below checks bounds
            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece target = board.getPiece(newPos);

                if (target == null || target.getTeamColor() != piece.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, newPos, null));
                }
            }
        }
    }

    // Helper method for knight moves (L-shape moveset)
    private void addKnightMoves(ArrayList<ChessMove> moves, ChessBoard board,
                                ChessPosition myPosition, ChessPiece piece) {
        int[][] knightMoves = {{2,1}, {2,-1}, {-2,1}, {-2,-1}, {1,2}, {1,-2}, {-1,2}, {-1,-2}};

        for (int[] move : knightMoves) {
            int newRow = myPosition.getRow() + move[0];
            int newCol = myPosition.getColumn() + move[1];

            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece target = board.getPiece(newPos);
//knight can jump over pieces so below just checks the destenation square
                if (target == null || target.getTeamColor() != piece.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, newPos, null));
                }
            }
        }
    }

    // Helper method for pawn moves
    private void addPawnMoves(ArrayList<ChessMove> moves, ChessBoard board,
                              ChessPosition myPosition, ChessPiece piece) {
        int direction = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 2 : 7;

        // Move forward one square
        int newRow = myPosition.getRow() + direction;
        if (newRow >= 1 && newRow <= 8) {
            ChessPosition newPos = new ChessPosition(newRow, myPosition.getColumn());
            if (board.getPiece(newPos) == null) {
                addPawnMove(moves, myPosition, newPos);

                // Move forward two squares from starting position
                if (myPosition.getRow() == startRow) {
                    ChessPosition twoSquares = new ChessPosition(newRow + direction, myPosition.getColumn());
                    if (board.getPiece(twoSquares) == null) {
                        addPawnMove(moves, myPosition, twoSquares);
                    }
                }
            }
        }

        // Capture diagonally
        for (int colOffset : new int[]{-1, 1}) {
            newRow = myPosition.getRow() + direction;
            int newCol = myPosition.getColumn() + colOffset;

            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece target = board.getPiece(newPos);

                if (target != null && target.getTeamColor() != piece.getTeamColor()) {
                    addPawnMove(moves, myPosition, newPos);
                }
            }
        }
    }

    // Helper to add pawn moves with promotion logic
    private void addPawnMove(ArrayList<ChessMove> moves, ChessPosition start, ChessPosition end) {
        if (end.getRow() == 1 || end.getRow() == 8) {
            // Promotion
            moves.add(new ChessMove(start, end, PieceType.QUEEN));
            moves.add(new ChessMove(start, end, PieceType.ROOK));
            moves.add(new ChessMove(start, end, PieceType.BISHOP));
            moves.add(new ChessMove(start, end, PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(start, end, null)); //normal move no promotion of pawn
        }
    }

//check if other obj is chess piece
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessPiece) {
            ChessPiece other = (ChessPiece) obj;
            return this.pieceColor == other.pieceColor && this.type == other.type; //this states two pieces are equal if they have same color and same type
        }
        return false;
    }

    @Override
    public int hashCode() {
        return pieceColor.hashCode() + type.hashCode() * 31; //multiplied by 31 to improve efficiency 31 is v common
    }
    /**
     * Returns a string representation of ChessPiece
     * Makes debugging easier ie instead of seeing "ChessPiece@1a2b3c", see "WHITE QUEEN"
     */

    @Override
    public String toString() {
        return pieceColor + "" + type;
    }
}
