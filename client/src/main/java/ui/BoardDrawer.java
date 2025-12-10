package ui;

import chess.ChessGame;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import chess.*;
import static ui.EscapeSequences.*;

public class BoardDrawer {

    // Board dimensions
    private static final int BOARD_SIZE = 8;

    // Custom color
    private static final String SET_BG_COLOR_LIGHT_BROWN = "\u001b[48;2;205;170;125m"; // RGB(205,170,125)
    private static final String SET_BG_COLOR_OFF_WHITE = "\u001b[48;2;255;248;220m"; // RGB(255,248,220) - Cornsilk
    private static final String SET_TEXT_COLOR_BLACK_PIECE = "\u001b[38;2;50;50;50m"; // Dark gray for black pieces
    private static final String SET_TEXT_COLOR_WHITE_PIECE = "\u001b[38;2;255;255;255m"; // White for white pieces
    private static final String SET_TEXT_BOLD = "\u001b[1m";

    private static PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public static void drawBoard(ChessGame game, ChessGame.TeamColor perspective) {
        ChessBoard board = game.getBoard();

        out.print(ERASE_SCREEN);
        out.print(moveCursor(1, 1));

        drawBoardWithPerspective(board, perspective);

        // Reset colors
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    public static void drawBoardWithPerspective(ChessBoard board, ChessGame.TeamColor perspective) {
        if (perspective == ChessGame.TeamColor.WHITE) {
            drawWhitePerspective(board);
        } else {
            drawBlackPerspective(board);
        }
    }

    private static void drawWhitePerspective(ChessBoard board) {
        drawColumnHeaders("  a  b  c  d  e  f  g  h  ");

        for (int row = 8; row >= 1; row--) {
            drawRow(board, row, true);
        }

        drawColumnHeaders("  a  b  c  d  e  f  g  h  ");
    }

    private static void drawBlackPerspective(ChessBoard board) {
        drawColumnHeaders("  h  g  f  e  d  c  b  a  ");

        for (int row = 1; row <= 8; row++) {
            drawRow(board, row, false);
        }

        drawColumnHeaders("  h  g  f  e  d  c  b  a  ");
    }

    private static void drawColumnHeaders(String headers) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(headers);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.println();
    }

    private static void drawRow(ChessBoard board, int row, boolean whitePerspective) {
        // Row number on left
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(row + " ");

        // Draw squares
        int startCol = whitePerspective ? 1 : 8;
        int endCol = whitePerspective ? 9 : 0;
        int colIncrement = whitePerspective ? 1 : -1;

        for (int col = startCol; col != endCol; col += colIncrement) {
            boolean isLightSquare = (row + col) % 2 == 0;
            drawSquare(board, row, col, isLightSquare);
        }

        // Row number on right
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(" " + row);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.println();
    }

    private static void drawSquare(ChessBoard board, int row, int col, boolean isLightSquare) {
        // Set background color
        if (isLightSquare) {
            out.print(SET_BG_COLOR_OFF_WHITE);
        } else {
            out.print(SET_BG_COLOR_LIGHT_BROWN);
        }

        ChessPosition position = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(position);

        if (piece != null) {
            drawPiece(piece);
        } else {
            out.print("   ");
        }
    }

    private static void drawPiece(ChessPiece piece) {
        // Set text color based on team
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_WHITE_PIECE);
        } else {
            out.print(SET_TEXT_COLOR_BLACK_PIECE);
        }

        out.print(SET_TEXT_BOLD);
        out.print(" " + getPieceSymbol(piece) + " ");
    }

    private static String getPieceSymbol(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return switch (piece.getPieceType()) {
                case KING -> "♔";
                case QUEEN -> "♕";
                case BISHOP -> "♗";
                case KNIGHT -> "♘";
                case ROOK -> "♖";
                case PAWN -> "♙";
            };
        } else {
            return switch (piece.getPieceType()) {
                case KING -> "♚";
                case QUEEN -> "♛";
                case BISHOP -> "♝";
                case KNIGHT -> "♞";
                case ROOK -> "♜";
                case PAWN -> "♟";
            };
        }
    }

    private static String moveCursor(int row, int col) {
        return "\u001b[" + row + ";" + col + "H";
    }
}