package com.example.kolko_krzyzyk;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kolko_krzyzyk.GameActivity;
import com.example.kolko_krzyzyk.R;

public class MainActivity extends AppCompatActivity {

    private ImageView player1Icon;
    private ImageView player2Icon;
    private EditText player1Name;
    private EditText player2Name;
    private Button playButton;

    // Dostępne skórki dla graczy
    private final int[] skins = {
            R.drawable.k1, R.drawable.o1, R.drawable.s1,
            R.drawable.s2, R.drawable.s3, R.drawable.s4
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicjalizacja widżetów
        player1Icon = findViewById(R.id.player1_icon);
        player2Icon = findViewById(R.id.player2_icon);
        player1Name = findViewById(R.id.editText1);
        player2Name = findViewById(R.id.editText2);
        playButton = findViewById(R.id.button1);

        ImageButton player1Dropdown = findViewById(R.id.player1_dropdown);
        ImageButton player2Dropdown = findViewById(R.id.player2_dropdown);

        // Obsługa rozwijanej listy dla Gracza 1
        player1Dropdown.setOnClickListener(view -> showPopupMenu(view, player1Icon));

        // Obsługa rozwijanej listy dla Gracza 2
        player2Dropdown.setOnClickListener(view -> showPopupMenu(view, player2Icon));

        // Obsługa przycisku "Graj"
        playButton.setOnClickListener(view -> {
            String name1 = player1Name.getText().toString().trim();
            String name2 = player2Name.getText().toString().trim();

            // Walidacja nazw graczy
            if (name1.isEmpty() || name2.isEmpty()) {
                Toast.makeText(this, "Wprowadź nazwy obu graczy!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("PLAYER1_NAME", name1);
            intent.putExtra("PLAYER2_NAME", name2);
            intent.putExtra("PLAYER1_ICON", skins[getCurrentSkin(player1Icon)]);
            intent.putExtra("PLAYER2_ICON", skins[getCurrentSkin(player2Icon)]);
            startActivity(intent);
        });
    }

    private void showPopupMenu(View anchorView, ImageView icon) {
        PopupMenu popupMenu = new PopupMenu(this, anchorView);
        popupMenu.getMenuInflater().inflate(R.menu.skin_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int selectedSkin = R.drawable.k1; // Default skin

            if (menuItem.getItemId() == R.id.skin_krzyzyk) {
                selectedSkin = R.drawable.k1;
            } else if (menuItem.getItemId() == R.id.skin_okrag) {
                selectedSkin = R.drawable.o1;
            } else if (menuItem.getItemId() == R.id.skin_burger) {
                selectedSkin = R.drawable.s1;
            } else if (menuItem.getItemId() == R.id.skin_piwo) {
                selectedSkin = R.drawable.s2;
            } else if (menuItem.getItemId() == R.id.skin_emotka) {
                selectedSkin = R.drawable.s3;
            } else if (menuItem.getItemId() == R.id.skin_fcb) {
                selectedSkin = R.drawable.s4;
            }

            icon.setImageResource(selectedSkin);
            icon.setTag(selectedSkin); // Zapisywanie wybranej skórki
            return true;
        });
        popupMenu.show();
    }

    private int getCurrentSkin(ImageView imageView) {
        for (int i = 0; i < skins.length; i++) {
            if (imageView.getDrawable() != null && imageView.getDrawable().getConstantState() == getResources().getDrawable(skins[i]).getConstantState()) {
                return i;
            }
        }
        return -1; // Zwróć -1, jeśli nie znaleziono dopasowania
    }

    public void exitApp(View view) {
        finishAffinity(); // Zamyka wszystkie aktywności i wyłącza aplikację
        System.exit(0);   // Kończy proces aplikacji
    }
}
