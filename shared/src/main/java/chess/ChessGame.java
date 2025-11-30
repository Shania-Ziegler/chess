package java.chess;
import chess.InvalidMoveException;

import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor currentTurn;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        currentTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }

        Collection<ChessMove> allMoves = piece.pieceMoves(board, startPosition);
        ArrayList<ChessMove> safeMoves = new ArrayList<>();

        for (ChessMove move : allMoves) {
            if (isMoveValid(piece, move)) {
                safeMoves.add(move);
            }
        }

        return safeMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());

        if (piece == null) {
            throw new InvalidMoveException("No piece at start position");
        }

        if (piece.getTeamColor() != currentTurn) {
            throw new InvalidMoveException("Not turn");
        }

        Collection<ChessMove> validMovesList = validMoves(move.getStartPosition());

        if (validMovesList == null || !validMovesList.contains(move)) {
            throw new InvalidMoveException("This is Invalid move");
        }

        executeMove(board, move);
        currentTurn = (currentTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);
        TeamColor opponentColor = getOpponentColor(teamColor);

        for (int row = 1; row <= 8; row++) {
            if (isKingUnderAttackInRow(row, kingPosition, opponentColor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        return !hasAnyValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        return !hasAnyValidMoves(teamColor);
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    // Private helper methods

    private boolean isMoveValid(ChessPiece piece, ChessMove move) {
        ChessBoard testBoard = copyBoard();
        executeMove(testBoard, move);
        return !isKingInCheckOnBoard(testBoard, piece.getTeamColor());
    }

    private boolean isKingUnderAttackInRow(int row, ChessPosition kingPosition,
                                           TeamColor opponentColor) {
        for (int col = 1; col <= 8; col++) {
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = board.getPiece(position);

            if (isOpponentPieceAttackingKing(piece, position, kingPosition, opponentColor)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOpponentPieceAttackingKing(ChessPiece piece, ChessPosition position,
                                                 ChessPosition kingPosition,
                                                 TeamColor opponentColor) {
        if (piece == null || piece.getTeamColor() != opponentColor) {
            return false;
        }

        Collection<ChessMove> moves = piece.pieceMoves(board, position);
        for (ChessMove move : moves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    private ChessPosition findKing(TeamColor team) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (isKingAtPosition(piece, team)) {
                    return position;
                }
            }
        }
        return null;
    }

    private boolean isKingAtPosition(ChessPiece piece, TeamColor team) {
        return piece != null &&
                piece.getPieceType() == ChessPiece.PieceType.KING &&
                piece.getTeamColor() == team;
    }

    private ChessPosition findKingOnBoard(ChessBoard testBoard, TeamColor team) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = testBoard.getPiece(position);
                if (isKingAtPosition(piece, team)) {
                    return position;
                }
            }
        }
        return null;
    }

    private boolean isKingInCheckOnBoard(ChessBoard testBoard, TeamColor teamColor) {
        ChessPosition kingPosition = findKingOnBoard(testBoard, teamColor);
        if (kingPosition == null) {
            return false;
        }

        TeamColor opponentColor = getOpponentColor(teamColor);

        for (int row = 1; row <= 8; row++) {
            if (isKingUnderAttackInRowOnBoard(testBoard, row, kingPosition, opponentColor)) {
                return true;
            }
        }
        return false;
    }

    private boolean isKingUnderAttackInRowOnBoard(ChessBoard testBoard, int row,
                                                  ChessPosition kingPosition,
                                                  TeamColor opponentColor) {
        for (int col = 1; col <= 8; col++) {
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = testBoard.getPiece(position);

            if (isOpponentPieceAttackingKingOnBoard(testBoard, piece, position,
                    kingPosition, opponentColor)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOpponentPieceAttackingKingOnBoard(ChessBoard testBoard,
                                                        ChessPiece piece,
                                                        ChessPosition position,
                                                        ChessPosition kingPosition,
                                                        TeamColor opponentColor) {
        if (piece == null || piece.getTeamColor() != opponentColor) {
            return false;
        }

        Collection<ChessMove> moves = piece.pieceMoves(testBoard, position);
        for (ChessMove move : moves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    private ChessBoard copyBoard() {
        ChessBoard newBoard = new ChessBoard();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null) {
                    ChessPiece newPiece = new ChessPiece(piece.getTeamColor(),
                            piece.getPieceType());
                    newBoard.addPiece(position, newPiece);
                }
            }
        }
        return newBoard;
    }

    private void executeMove(ChessBoard targetBoard, ChessMove move) {
        ChessPiece piece = targetBoard.getPiece(move.getStartPosition());
        targetBoard.addPiece(move.getStartPosition(), null);
        if (move.getPromotionPiece() != null) {
            piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }
        targetBoard.addPiece(move.getEndPosition(), piece);
    }

    private boolean hasAnyValidMoves(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (pieceHasValidMoves(piece, position, teamColor)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean pieceHasValidMoves(ChessPiece piece, ChessPosition position,
                                       TeamColor teamColor) {
        if (piece == null || piece.getTeamColor() != teamColor) {
            return false;
        }
        Collection<ChessMove> moves = validMoves(position);
        return moves != null && !moves.isEmpty();
    }

    private TeamColor getOpponentColor(TeamColor teamColor) {
        return (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame game = (ChessGame) o;
        return Objects.equals(board, game.board) && currentTurn == game.currentTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentTurn);
    }
}