package oeroaq.moviles.unal.tictactoe;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.zip.Inflater;

public class TicTacToeGame {
    private char mBoard[] = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    private final int BOARD_SIZE = 9;

    // The computer's difficulty levels
    public enum DifficultyLevel {
        Easy, Harder, Expert
    }
    // Current difficulty level
    private DifficultyLevel mDifficultyLevel = DifficultyLevel.Expert;
    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    private View view;
    private boolean lock;

    private Random mRand;

    public TicTacToeGame(View _view) {
        // Seed the random number generator
        mRand = new Random();
        view = _view;
        drawBoard("");
        lock = false;
    }

    public void displayBoard(boolean turn) {
        int win = checkForWinner();
        // Report the winner
        if (win == 1) {
            drawBoard("It's a tie.");
            lock = true;
        } else if (win == 2) {
            drawBoard(HUMAN_PLAYER + " wins!");
            lock = true;
        } else if (win == 3) {
            drawBoard(COMPUTER_PLAYER + " wins!");
            lock = true;
        } else if (turn) {
            // First see if there's a move O can make to win
            getComputerMove();
            displayBoard(false);
        } else {
            drawBoard("");
            lock = false;
        }
    }

    public void drawBoard(String textWin) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            String text = String.valueOf(mBoard[i]);
            switch (i) {
                case 1:
                    ((Button) view.findViewById(R.id._1)).setText(text);
                    break;
                case 2:
                    ((Button) view.findViewById(R.id._2)).setText(text);
                    break;
                case 3:
                    ((Button) view.findViewById(R.id._3)).setText(text);
                    break;
                case 4:
                    ((Button) view.findViewById(R.id._4)).setText(text);
                    break;
                case 5:
                    ((Button) view.findViewById(R.id._5)).setText(text);
                    break;
                case 6:
                    ((Button) view.findViewById(R.id._6)).setText(text);
                    break;
                case 7:
                    ((Button) view.findViewById(R.id._7)).setText(text);
                    break;
                case 8:
                    ((Button) view.findViewById(R.id._8)).setText(text);
                    break;
                default:
                    ((Button) view.findViewById(R.id._0)).setText(text);
                    break;
            }
        }
        ((TextView) view.findViewById(R.id.information)).setText(textWin);
    }

    // Check for a winner.  Return
    //  0 if no winner or tie yet
    //  1 if it's a tie
    //  2 if X won
    //  3 if O won
    private int checkForWinner() {

        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3) {
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i + 1] == HUMAN_PLAYER &&
                    mBoard[i + 2] == HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i + 1] == COMPUTER_PLAYER &&
                    mBoard[i + 2] == COMPUTER_PLAYER)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i + 3] == HUMAN_PLAYER &&
                    mBoard[i + 6] == HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i + 3] == COMPUTER_PLAYER &&
                    mBoard[i + 6] == COMPUTER_PLAYER)
                return 3;
        }

        // Check for diagonal wins
        if ((mBoard[0] == HUMAN_PLAYER &&
                mBoard[4] == HUMAN_PLAYER &&
                mBoard[8] == HUMAN_PLAYER) ||
                (mBoard[2] == HUMAN_PLAYER &&
                        mBoard[4] == HUMAN_PLAYER &&
                        mBoard[6] == HUMAN_PLAYER))
            return 2;
        if ((mBoard[0] == COMPUTER_PLAYER &&
                mBoard[4] == COMPUTER_PLAYER &&
                mBoard[8] == COMPUTER_PLAYER) ||
                (mBoard[2] == COMPUTER_PLAYER &&
                        mBoard[4] == COMPUTER_PLAYER &&
                        mBoard[6] == COMPUTER_PLAYER))
            return 3;

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER)
                return 0;
        }

        // If we make it through the previous loop, all places are taken, so it's a tie
        return 1;
    }

    public void getUserMove(int move) {
        if (!lock) {
            if (mBoard[move] != HUMAN_PLAYER && mBoard[move] != COMPUTER_PLAYER)
                mBoard[move] = HUMAN_PLAYER;
            displayBoard(true);
        }
    }


    private int getRandomMove() {
        int move = -1;
        do {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mBoard[move] == HUMAN_PLAYER || mBoard[move] == COMPUTER_PLAYER);
        return move;
    }

    private int getWinningMove() {
        int move;
        move = getRandomMove();
        return move;
    }

    private int getBlockingMove() {
        return getWinningMove();
    }

    public void getComputerMove() {

        int move = -1;
        // First see if there's a move O can make to win
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];
                mBoard[i] = COMPUTER_PLAYER;
                if (checkForWinner() == 3) {
                    return;
                } else
                    mBoard[i] = curr;
            }
        }

        // See if there's a move O can make to block X from winning
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];   // Save the current number
                mBoard[i] = HUMAN_PLAYER;
                if (checkForWinner() == 2) {
                    mBoard[i] = COMPUTER_PLAYER;
                    return;
                } else
                    mBoard[i] = curr;
            }
        }

        if (mDifficultyLevel == DifficultyLevel.Easy)
            move = getRandomMove();
        else if (mDifficultyLevel == DifficultyLevel.Harder) {
            move = getWinningMove();
            if (move == -1)
                move = getRandomMove();
        } else if (mDifficultyLevel == DifficultyLevel.Expert) {

            // Try to win, but if that's not possible, block.
            // If that's not possible, move anywhere.
            move = getWinningMove();
            if (move == -1)
                move = getBlockingMove();
            if (move == -1)
                move = getRandomMove();
        }

        if(move != -1)
            mBoard[move] = COMPUTER_PLAYER;
    }

    public DifficultyLevel getDifficultyLevel() {
        return mDifficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        mDifficultyLevel = difficultyLevel;
    }

}
