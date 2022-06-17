package org.techtown.rhythmgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button game_to_main_button =findViewById(R.id.game_to_main_bt);
        game_to_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_game_to_main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_game_to_main);
            }
        });
    }
}