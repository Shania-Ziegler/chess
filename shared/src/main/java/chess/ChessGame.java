package chess;
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
    private TeamColor currentTurn; //initalize both fields in constructor

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        currentTurn = TeamColor.WHITE; //white goes first in classical chess rules always :D
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn; //return field that stores whos turn it is
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTurn = team; //store parameter <team> into field :)
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

        // Get all possible moves from the piece
        Collection<ChessMove> allMoves = piece.pieceMoves(board, startPosition);
        ArrayList<ChessMove> safeMoves = new ArrayList<>();

        for (ChessMove move : allMoves) {
            ChessBoard testBoard = copyBoard();
            executeMove(testBoard, move);
            if (!isKingInCheckOnBoard(testBoard, piece.getTeamColor())) {
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

        // Check if a piece is at the start position
        if (piece == null) {
            throw new InvalidMoveException("No piece at start position");
        }

        // Check if correct team turn
        if (piece.getTeamColor() != currentTurn) {
            throw new InvalidMoveException("Not turn");
        }

        // Get valid moves for this piece
        Collection<ChessMove> validMovesList = validMoves(move.getStartPosition());

        // Check if the move is in the valid moves list
        if (validMovesList == null || !validMovesList.contains(move)) {
            throw new InvalidMoveException("This is Invalid move");
        }

        // Move is valid, execute it
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

        TeamColor opponentColor; //creates variable opponent color has no value

        if (teamColor == TeamColor.WHITE) { //this actually will give value to Opponet color good
            opponentColor = TeamColor.BLACK;
        } else {
            opponentColor = TeamColor.WHITE;
        }
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                // so this ask Is this an opponent piece?
                if (piece != null && piece.getTeamColor() == opponentColor) {
                    // Get all moves this opponent piece can make
                    Collection<ChessMove> moves = piece.pieceMoves(board, position);

                    // Check if any move attacks the king
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;  // King is in check!
                        }
                    }
                }
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
//check the not in check and no valid moves kinda vibe

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board; //store parameter into field
    }


    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }



    //private helper method(s)

    private ChessPosition findKing(TeamColor team) {
        //loop for all row
        for (int row = 1; row <= 8; row++) {
            //loop all columns
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);   //create chess pos for square
                ChessPiece piece = board.getPiece(position); //get the piece at this position
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == team) {
                    return position;
                }


            }
        }
        return null;//for some reason if king no where to be found saftey check here so it returns null
    }

    private ChessPosition findKingOnBoard(ChessBoard testBoard, TeamColor team) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = testBoard.getPiece(position);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == team) {
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

        TeamColor opponentColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = testBoard.getPiece(position);

                if (piece != null && piece.getTeamColor() == opponentColor) {
                    Collection<ChessMove> moves = piece.pieceMoves(testBoard, position);

                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
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
                    ChessPiece newPiece = new ChessPiece(piece.getTeamColor(), piece.getPieceType());
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
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(position);
                    if (moves != null && !moves.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

//add my equals and hashcode + maybe other ovverrides

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame game = (ChessGame) o;
        return Objects.equals(board, game.board) &&
                currentTurn == game.currentTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentTurn);
    }
}