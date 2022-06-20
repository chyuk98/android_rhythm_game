package org.techtown.rhythmgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
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

    public TextView score_view;
    public int[] score_view_index = new int[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        {
            score_view_index[0] = R.id.score_01;
            score_view_index[1] = R.id.score_02;
            score_view_index[2] = R.id.score_03;
            score_view_index[3] = R.id.score_04;
            score_view_index[4] = R.id.score_05;
            score_view_index[5] = R.id.score_06;
            score_view_index[6] = R.id.score_07;
            score_view_index[7] = R.id.score_08;
            score_view_index[8] = R.id.score_09;
            score_view_index[9] = R.id.score_10;
        }

        /**db 세팅**/
        dbName = "score_board";
        tableName = "person";
        createDatabase();
        createTable();
        insertRecord();

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
        String sql = "select _id, name, score from  " + tableName + " order by score desc";
        Cursor cursor = database.rawQuery(sql, null);
        int recordCount = cursor.getCount();

        for(int i=0; i<recordCount; i++)
        {
            cursor.moveToNext();

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int score = cursor.getInt(2);

            score_view = (TextView) findViewById(score_view_index[i]);
            score_view.setText(name + " : " + Integer.toString(score));

            if(i==9)
                break;
        }
        cursor.close();
    }
}