package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * @return promotion piece type if pawn promotion,
     * or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessMove) {
            ChessMove other = (ChessMove) obj;
            return startPosition.equals(other.startPosition)
                    && endPosition.equals(other.endPosition)
                    && Objects.equals(promotionPiece, other.promotionPiece);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }

    @Override
    public String toString() {
        return startPosition + " -> " + endPosition +
                (promotionPiece != null ? " = " + promotionPiece : "");
    }
}
