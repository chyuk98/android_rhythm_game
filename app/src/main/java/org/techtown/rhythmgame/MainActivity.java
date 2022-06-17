package org.techtown.rhythmgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.techtown.rhythmgame.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("rhythmgame");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button main_to_game_button =findViewById(R.id.main_to_game_bt);
        main_to_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main_to_game = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent_main_to_game);

            }
        });
        Button main_to_score_button =findViewById(R.id.main_to_score_bt);
        main_to_score_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main_to_score = new Intent(getApplicationContext(), ScoreActivity.class);
                startActivity(intent_main_to_score);
            }
        });
    }

    /**
     * A native method that is implemented by the 'rythmgame' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}