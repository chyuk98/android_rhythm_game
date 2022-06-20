package org.techtown.rhythmgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    // DB
    SQLiteDatabase database;
    public String dbName;
    public String tableName;

    TextView score_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        dbName = "score_board";
        tableName = "person";
        createDatabase();
        createTable();

        Button score_to_main_button =findViewById(R.id.score_to_main_bt);
        score_to_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void createDatabase()
    {
        database= openOrCreateDatabase(dbName, MODE_PRIVATE, null);
    }

    public void createTable()
    {
        if(database == null)
        {
            return;
        }

        String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, name text, score integer)";
        database.execSQL(sql);
    }

    public void insertRecord()
    {
        if(tableName == null) {
            return;
        }
        String sql = "insert into " + tableName + "(name, score) values";
    }
}