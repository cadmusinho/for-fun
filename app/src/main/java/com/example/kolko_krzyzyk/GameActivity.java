package com.example.kolko_krzyzyk;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private ImageView[][] board = new ImageView[3][3];
    private boolean player1Turn = true; // Który gracz wykonuje aktualnie ruch
    private boolean player1Starts = true; // Który gracz rozpoczyna rundę
    private int player1Points = 0;
    private int player2Points = 0;
    private int player1IconRes;
    private int player2IconRes;

    private TextView scoreDisplay;
    private TextView currentTurnDisplay;
    private ImageView player1Icon;
    private ImageView player2Icon;
    private TextView player1Name;
    private TextView player2Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        scoreDisplay = findViewById(R.id.score_display);
        currentTurnDisplay = findViewById(R.id.current_turn_display);
        player1Icon = findViewById(R.id.player1_icon);
        player2Icon = findViewById(R.id.player2_icon);
        player1Name = findViewById(R.id.player1_name);
        player2Name = findViewById(R.id.player2_name);

        // Pobieranie danych graczy z MainActivity
        String name1 = getIntent().getStringExtra("PLAYER1_NAME");
        String name2 = getIntent().getStringExtra("PLAYER2_NAME");
        player1IconRes = getIntent().getIntExtra("PLAYER1_ICON", R.drawable.k1);
        player2IconRes = getIntent().getIntExtra("PLAYER2_ICON", R.drawable.o1);

        player1Name.setText(name1);
        player2Name.setText(name2);
        player1Icon.setImageResource(player1IconRes);
        player2Icon.setImageResource(player2IconRes);

        GridLayout gridLayout = findViewById(R.id.grid_layout);

        // Inicjalizacja planszy 3x3
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String fieldID = "image_" + i + j;
                int resID = getResources().getIdentifier(fieldID, "id", getPackageName());
                board[i][j] = findViewById(resID);
                int finalI = i;
                int finalJ = j;
                board[i][j].setOnClickListener(v -> onBoardClick(finalI, finalJ));
            }
        }

        findViewById(R.id.button_exit).setOnClickListener(v -> finish());

        updateTurnDisplay();
    }

    private void onBoardClick(int row, int col) {
        if (board[row][col].getDrawable() != null) {
            Toast.makeText(this, "Pole zajęte!", Toast.LENGTH_SHORT).show();
            return; // Pole jest już zajęte
        }

        if (player1Turn) {
            board[row][col].setImageResource(player1IconRes);
        } else {
            board[row][col].setImageResource(player2IconRes);
        }

        if (checkForWin()) {
            if (player1Turn) {
                player1Points++;
                player1Starts = false; // Gracz 2 zaczyna następną grę
                Toast.makeText(this, player1Name.getText() + " wygrywa!", Toast.LENGTH_SHORT).show();
            } else {
                player2Points++;
                player1Starts = true; // Gracz 1 zaczyna następną grę
                Toast.makeText(this, player2Name.getText() + " wygrywa!", Toast.LENGTH_SHORT).show();
            }
            updateScore();
            resetBoard();
        } else if (isBoardFull()) {
            Toast.makeText(this, "Remis!", Toast.LENGTH_SHORT).show();
            resetBoard(); // Remis - zachowujemy tego samego gracza rozpoczynającego
        } else {
            player1Turn = !player1Turn;
        }

        updateTurnDisplay();
    }

    private boolean checkForWin() {
        for (int i = 0; i < 3; i++) {
            if (isLineEqual(board[i][0], board[i][1], board[i][2])) {
                return true;
            }
            if (isLineEqual(board[0][i], board[1][i], board[2][i])) {
                return true;
            }
        }

        if (isLineEqual(board[0][0], board[1][1], board[2][2])) {
            return true;
        }

        if (isLineEqual(board[0][2], board[1][1], board[2][0])) {
            return true;
        }

        return false;
    }

    private boolean isLineEqual(ImageView a, ImageView b, ImageView c) {
        if (a.getDrawable() == null || b.getDrawable() == null || c.getDrawable() == null) {
            return false;
        }
        return a.getDrawable().getConstantState().equals(b.getDrawable().getConstantState()) &&
                a.getDrawable().getConstantState().equals(c.getDrawable().getConstantState());
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].getDrawable() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateScore() {
        scoreDisplay.setText(player1Points + " | " + player2Points);
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j].setImageDrawable(null);
            }
        }

        player1Turn = player1Starts; // Ustawiamy kolejnego rozpoczynającego gracza
        updateTurnDisplay();
    }

    private void updateTurnDisplay() {
        if (player1Turn) {
            currentTurnDisplay.setText("Ruch: " + player1Name.getText());
        } else {
            currentTurnDisplay.setText("Ruch: " + player2Name.getText());
        }
    }
}
