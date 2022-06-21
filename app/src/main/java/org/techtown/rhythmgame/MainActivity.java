package org.techtown.rhythmgame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    /**
     * difficulty_radio_fix : easy = [0]
     * difficulty_radio_fix : middle = [1]
     * difficulty_radio_fix : hard = [2]
     * **/

    int[] difficulty_radio_fix = new int[3];
    public int difficulty;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        {
            difficulty_radio_fix[0] = R.id.radioButton_easy;
            difficulty_radio_fix[1] = R.id.radioButton_normal;
            difficulty_radio_fix[2] = R.id.radioButton_hard;
        }
        difficulty = 0;

        ImageButton option_button = (ImageButton)findViewById(R.id.option_button);
        option_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptionDialog();
            }
        });

        Button main_to_game_button = findViewById(R.id.main_to_game_bt);
        main_to_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main_to_game = new Intent(getApplicationContext(), GameActivity.class);
                intent_main_to_game.putExtra("difficulty", difficulty);
                startActivity(intent_main_to_game);

            }
        });
        Button main_to_score_button = findViewById(R.id.main_to_score_bt);
        main_to_score_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main_to_score = new Intent(getApplicationContext(), ScoreActivity.class);
                startActivity(intent_main_to_score);
            }
        });
    }

    private void showOptionDialog(){
        LayoutInflater vi_option =(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout optionLayout = (LinearLayout)vi_option.inflate(R.layout.main_option, null);

        AlertDialog.Builder adb_option = new AlertDialog.Builder(this);
        adb_option.setCancelable(false);
        adb_option.setTitle("난이도 선택");
        adb_option.setView(optionLayout);
        adb_option.setNeutralButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RadioGroup radioGroup_option = (RadioGroup)optionLayout.findViewById(R.id.radio_group_option);
                int checkedId= radioGroup_option.getCheckedRadioButtonId();
                RadioButton rb= (RadioButton)radioGroup_option.findViewById(checkedId);
                for(int temp=0; temp<3; temp++)
                {
                    if(difficulty_radio_fix[temp] == checkedId)
                    {
                        difficulty = temp;
                        break;
                    }
                }

                Toast.makeText(MainActivity.this, Integer.toString(checkedId), Toast.LENGTH_LONG).show();
            }
        });
        adb_option.show();
    }


}