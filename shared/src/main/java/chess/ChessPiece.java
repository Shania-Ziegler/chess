package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 * Represents a single chess piece.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    // Movement direction constants
    private static final int[][] STRAIGHT = {{1,0}, {-1,0}, {0,1}, {0,-1}};
    private static final int[][] DIAGONAL = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};
    private static final int[][] ALL_DIRS = { {1,0}, {-1,0}, {0,1}, {0,-1},
            {1,1}, {1,-1}, {-1,1}, {-1,-1}
    };
    private static final int[][] KNIGHT = {
            {2,1}, {2,-1}, {-2,1}, {-2,-1},
            {1,2}, {1,-2}, {-1,2}, {-1,-2}
    };

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /** The various different chess piece options */
    public enum PieceType {KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN }

    public ChessGame.TeamColor getTeamColor() {
        return pieceColor; }

    public PieceType getPieceType() {
        return type; }

    /**
     * Calculates all the positions a chess piece can move to.
     * Does not check for king safety rules.
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);
        if (piece == null) return moves;

        switch (piece.type) {
            case ROOK -> addDirectionalMoves(moves, board, myPosition, STRAIGHT);
            case BISHOP -> addDirectionalMoves(moves, board, myPosition, DIAGONAL);
            case QUEEN -> addDirectionalMoves(moves, board, myPosition, ALL_DIRS);
            case KING -> addSingleStepMoves(moves, board, myPosition, ALL_DIRS);
            case KNIGHT -> addSingleStepMoves(moves, board, myPosition, KNIGHT);
            case PAWN -> addPawnMoves(moves, board, myPosition, piece);
        }
        return moves;
    }

    /** For rook, bishop, queen (multi-step until blocked) */
    private void addDirectionalMoves(List<ChessMove> moves, ChessBoard board,
                                     ChessPosition start, int[][] dirs) {
        for (int[] d : dirs) {
            for (int i = 1; i < 8; i++) {
                int r = start.getRow() + d[0]*i;
                int c = start.getColumn() + d[1]*i;
                if (!isValid(r, c)) break;

                ChessPosition pos = new ChessPosition(r, c);
                ChessPiece target = board.getPiece(pos);

                if (target == null) {
                    moves.add(new ChessMove(start, pos, null));
                } else {
                    if (target.pieceColor != this.pieceColor) {
                        moves.add(new ChessMove(start, pos, null));
                    }
                    break; // stop in this direction
                }
            }
        }
    }

    /** For king, knight (single-step moves only) */
    private void addSingleStepMoves(List<ChessMove> moves, ChessBoard board,
                                    ChessPosition start, int[][] steps) {
        for (int[] s : steps) {
            int r = start.getRow() + s[0];
            int c = start.getColumn() + s[1];
            if (!isValid(r, c)) continue;

            ChessPosition pos = new ChessPosition(r, c);
            ChessPiece target = board.getPiece(pos);

            if (target == null || target.pieceColor != this.pieceColor) {
                moves.add(new ChessMove(start, pos, null));
            }
        }
    }

    /** Pawn movement rules */
    private void addPawnMoves(List<ChessMove> moves, ChessBoard board,
                              ChessPosition start, ChessPiece pawn) {
        int dir = (pawn.pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (pawn.pieceColor == ChessGame.TeamColor.WHITE) ? 2 : 7;

        // forward one
        int r = start.getRow() + dir;
        int c = start.getColumn();
        if (isValid(r, c) && board.getPiece(new ChessPosition(r, c)) == null) {
            addPawnMove(moves, start, new ChessPosition(r, c));

            // forward two from starting row
            if (start.getRow() == startRow) {
                int r2 = r + dir;
                ChessPosition pos2 = new ChessPosition(r2, c);
                if (isValid(r2, c) && board.getPiece(pos2) == null) {
                    addPawnMove(moves, start, pos2);
                }
            }
        }

        // captures
        for (int dc : new int[]{-1, 1}) {
            int nc = start.getColumn() + dc;
            if (isValid(r, nc)) {
                ChessPosition pos = new ChessPosition(r, nc);
                ChessPiece target = board.getPiece(pos);
                if (target != null && target.pieceColor != pawn.pieceColor) {
                    addPawnMove(moves, start, pos);
                }
            }
        }
    }

    /** Handle pawn promotions */
    private void addPawnMove(List<ChessMove> moves, ChessPosition start, ChessPosition end) {
        if (end.getRow() == 1 || end.getRow() == 8) {
            moves.add(new ChessMove(start, end, PieceType.QUEEN));
            moves.add(new ChessMove(start, end, PieceType.ROOK));
            moves.add(new ChessMove(start, end, PieceType.BISHOP));
            moves.add(new ChessMove(start, end, PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(start, end, null));
        }
    }

    private boolean isValid(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ChessPiece other)
                && this.pieceColor == other.pieceColor
                && this.type == other.type;
    }

    @Override
    public int hashCode() {
        return pieceColor.hashCode() * 31 + type.hashCode();
    }

    @Override
    public String toString() {
        return pieceColor + " " + type;
    }
}
