package org.techtown.rhythmgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {


    // timer (task 관리용)
    Timer t = new Timer();
    
    
    // Layout
    TextView view_gametime;
    TextView view_gamescore;
    TextView view_gamecombo;
    public ImageView view_img[] = new ImageView[9];
    // Layout 중 환경설정으로 바뀔수 있는 image-mapping value
    public int image_R_ID[]  = new int[9];
    // 채점 관련 img_view
    public ImageView img_grade;


    // LED
    public int led_wrong;

    // DOT
    public int dot_combo;

    // Step Motor
    public int action;
    public int direction;
    public int speed = 10;      // speed : 10 고정

    // LCD
    public int gamescore;

    // Push Button
    public int button_fixed[] = {1, 2, 4, 8, 16, 32, 64, 128, 256}; //: 버튼들이 가지고 있는 고정 값
    int push_check; // : push button이 루틴중에 한번이라도 눌리면 체크
    int push_store; // : push button의 값을 루틴중에만 저장
    class But_task1 extends TimerTask {
        public int task_button_value;
        public But_task1(int temp)
        {
            task_button_value = temp;
        }

        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    img_grade.setVisibility(View.INVISIBLE);
                    view_img[task_button_value].setImageResource(R.drawable.black);
                    if (push_store != 0 && push_store == button_fixed[task_button_value]){
                        img_grade.setVisibility(View.VISIBLE);
                        img_grade.setImageResource(R.drawable.miss_img);
                        switch_fail();
                    }
                }
            });
        }
    }
    class But_task2 extends TimerTask {
        public int task_button_value;
        public But_task2(int temp)
        {
            task_button_value = temp;
        }

        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view_img[task_button_value].setImageResource(R.drawable.red);
                    if (push_check !=1) {
                        if (push_store != 0) {

                            img_grade.setVisibility(View.VISIBLE);
                            img_grade.setImageResource(R.drawable.miss_img);
                            switch_fail();
                        }
                    }
                }
            });
        }
    }
    class But_task3 extends TimerTask {
        public int task_button_value;
        public But_task3(int temp)
        {
            task_button_value = temp;
        }

        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view_img[task_button_value].setImageResource(R.drawable.yellow);
                    if (push_check != 1) {

                        if (push_store != 0) {
                            img_grade.setVisibility(View.VISIBLE);
                            img_grade.setImageResource(R.drawable.miss_img);
                            switch_fail();
                        }
                    }
                }
            });
        }
    }
    class But_task4 extends TimerTask {
        public int task_button_value;
        public But_task4(int temp)
        {
            task_button_value = temp;
        }

        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view_img[task_button_value].setImageResource(R.drawable.green);
                    if (push_check != 1) {
                        if (push_store != 0 && push_store == button_fixed[task_button_value]) {
                            img_grade.setVisibility(View.VISIBLE);
                            img_grade.setImageResource(R.drawable.good_img);
                            switch_success();
                        }
                    }
                }
            });
        }
    }
    class But_task5 extends TimerTask {
        public int task_button_value;
        public But_task5(int temp)
        {
            task_button_value = temp;
        }
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    img_grade.setVisibility(View.VISIBLE);
                    view_img[task_button_value].setImageResource(R.drawable.black);
                    if (push_check != 1) {
                        if (push_store != 0 && push_store == button_fixed[task_button_value]) {
                            img_grade.setImageResource(R.drawable.perfect_img);
                            switch_success();
                        }
                    }
                    else if(push_store == 0)
                    {
                        img_grade.setImageResource(R.drawable.miss_img);
                    }
                    push_store = 0;
                    push_check = 0;
                }
            });
        }
    }

    Timer g = new Timer(true);


    // FND
    public int gamecombo;

    // Used to load the 'jni_test' library on application startup.
    static {
        System.loadLibrary("fpga-led-jni");
        System.loadLibrary("fpga-dot-jni");
        System.loadLibrary("fpga-step-motor-jni");
        System.loadLibrary("fpga-text-lcd-jni");
        System.loadLibrary("fpga-push-switch-jni");
        System.loadLibrary("fpga-fnd-jni");
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /********** Layout setting **********
         * 시간, 점수, 콤보 화면 초기화
         *********************************/
        view_gametime = (TextView) findViewById(R.id.game_time);
        view_gamescore = (TextView) findViewById(R.id.game_score);
        view_gamecombo = (TextView) findViewById(R.id.game_combo);
        {
            image_R_ID[0] = R.id.img_1;
            image_R_ID[1] = R.id.img_2;
            image_R_ID[2] = R.id.img_3;
            image_R_ID[3] = R.id.img_4;
            image_R_ID[4] = R.id.img_5;
            image_R_ID[5] = R.id.img_6;
            image_R_ID[6] = R.id.img_7;
            image_R_ID[7] = R.id.img_8;
            image_R_ID[8] = R.id.img_9;
        }
        for(int i = 0; i < 9; i++)
        {
            view_img[i] = (ImageView)findViewById(image_R_ID[i]);
        }


        img_grade = (ImageView) findViewById(R.id.img_grade);
        push_check = 0;

        view_gametime.setText("00:00");
        view_gamescore.setText("0");
        view_gamecombo.setText("0");


        /********** Layout setting **********
         * divice data 초기화
         *********************************/

        led_wrong = 0;
        ReceiveLedValue(led_wrong);
        dot_combo = 0;
        ReceiveDotValue(dot_combo);
        gamescore = 0;
        ReceiveTextLcdValue(Integer.toString(gamescore), " ");
        gamecombo = 0;
        ReceiveFndValue(Integer.toString(gamecombo));

        showNameDialog();


        Button game_to_main_button =findViewById(R.id.game_to_main_bt);
        game_to_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.cancel();
                g.cancel();
                finish();
            }
        });


        /********** Push Switch **********
         * 누른 Switch 의 값을 TextView 로 출력
         *********************************/

        TimerTask task = new TimerTask() {
            Handler mHandler = new Handler();

            public void run() {
                mHandler.postDelayed(new Runnable() {

                    public void run() {

                        int value;
                        value = DeviceOpen();

                        if(value!=-1)
                            value = ReceivePushSwitchValue();
                            if(push_store == 0) {
                                push_store = value;
                            }
                            System.out.println(push_store);

                        if(value!=-1)
                            DeviceClose();


                    }
                }, 100);
            }
        };


        t.schedule(task, 100, 100);
        

        
    }

    private void showNameDialog(){
        LayoutInflater vi =(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout loginLayout = (LinearLayout)vi.inflate(R.layout.game_login, null);

        final EditText name = (EditText) loginLayout.findViewById(R.id.name);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("로그인");
        adb.setView(loginLayout);
        adb.setNeutralButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(GameActivity.this, "NAME : " + name.getText().toString(), Toast.LENGTH_LONG).show();
                showCountDialog();
            }
        }).show();
    }

    private void showCountDialog(){
        final Dialog count_Dialog = new Dialog(this);
        count_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        count_Dialog.setContentView(R.layout.game_count);
        count_Dialog.setCanceledOnTouchOutside(false);

        final TextView countNum = (TextView) count_Dialog.findViewById(R.id.count_num);

        final CountDownTimer cntTimer = new CountDownTimer(4000,1000) {
            @Override
            public void onTick(long millis) {
                countNum.setText(String.valueOf(millis/1000));
            }

            @Override
            public void onFinish() {
                count_Dialog.dismiss();
/******************게임 시작****************/
                game_start();
                //changeImage();
            }
        };
        count_Dialog.show();
        cntTimer.start();

    }

    public void game_start()
    {
        game_mapping(7, 3000, 1000);
        game_mapping(8, 10000, 333);
        game_mapping(9, 17000, 500);


    }

    public int game_mapping(int quest_button, long delay_time, long interval_time)
    {
        if(quest_button > 9 || quest_button < 1)
        {
            return 0; // quest_button이 9초과 1미만으로 오류 값
        }
        quest_button--;


        TimerTask task1 = new But_task1(quest_button);
        TimerTask task2 = new But_task2(quest_button);
        TimerTask task3 = new But_task3(quest_button);
        TimerTask task4 = new But_task4(quest_button);
        TimerTask task5 = new But_task5(quest_button);


        g.schedule(task1, delay_time);
        g.schedule(task2, delay_time + interval_time);
        g.schedule(task3, delay_time + interval_time * 2);
        g.schedule(task4, delay_time + interval_time * 3);
        g.schedule(task5, delay_time + interval_time * 4);

        return 0;
    }


    public void switch_success()
    {
        /** score, combo 계산 **/
        push_check = 1;

        gamecombo++;
        if(gamecombo >= 50)
        {
            gamescore += 5;
        }
        else if(gamecombo >= 10)
        {
            gamescore += (gamecombo / 10);
        }
        else
        {
            gamescore++;
        }

        dot_combo++;
        if(dot_combo>8)
            dot_combo = 0;


        String g_combo = Integer.toString(gamecombo);

        if (g_combo.length()==1)
            g_combo="000"+g_combo;
        else if (g_combo.length()==2)
            g_combo="00"+g_combo;
        else if (g_combo.length()==3)
            g_combo="0"+g_combo;

        ReceiveDotValue(dot_combo);
        ReceiveTextLcdValue(Integer.toString(gamescore), " ");
        ReceiveFndValue(g_combo);
        view_gamescore.setText(Integer.toString(gamescore));
        view_gamecombo.setText(Integer.toString(gamecombo));
    }

    public void switch_fail()
    {
        /** score, combo 계산 **/
        push_check = 1;

        gamecombo = 0;
        dot_combo = 0;
        led_wrong++;
        if(led_wrong > 8)
            led_wrong = 8;

        ReceiveLedValue(led_wrong);
        ReceiveDotValue(dot_combo);
        ReceiveFndValue(Integer.toString(gamecombo));
        view_gamecombo.setText(Integer.toString(gamecombo));
    }



    /**
     * A native method that is implemented by the 'jni_test' native library,
     * which is packaged with this application.
     */
    public native String ReceiveLedValue(int checkLED);
    public native String ReceiveDotValue(int checkDOT);
    public native String SetMotorState(int action, int direction, int speed);
    public native int ReceiveTextLcdValue(String ptr1, String ptr2);
    public native int DeviceOpen();
    public native int DeviceClose();
    public native int ReceivePushSwitchValue();
    public native int ReceiveFndValue(String ptr);
}