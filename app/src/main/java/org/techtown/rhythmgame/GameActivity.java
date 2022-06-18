package org.techtown.rhythmgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
    public int button_value[] = {1, 2, 4, 8, 16, 32, 64, 128, 256};
    public TextView img_1;
    long curt ;
    int check;
    int button;
    class but1_1 extends TimerTask {

        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    img_1.setText(""); //  암것도 안나오게
                    if (button != 0 && button == 1){
                        img_1.setText("miss");// miss 이미지
                        switch_fail();
                    }
                }
            });
        }
    }
    class but1_2 extends TimerTask {
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (check !=1) {
                        img_1.setText("bad");
                        if (button != 0 && button == 1) {
                            img_1.setText("miss");
                            switch_success();
                        }
                    }
                }
            });
        }
    }
    class but1_3 extends TimerTask {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (check != 1) {
                        img_1.setText("good");//img = perfect
                        if (button != 0 && button == 1) {
                            img_1.setText("bad check");//  3x3 화면에 5번 픽셀에 2번 이미지
                            switch_success();
                        }
                    }
                }
            });
        }
    }
    class but1_4 extends TimerTask {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (check != 1) {
                        img_1.setText("perfect");
                        if (button != 0 && button == 1) {
                            img_1.setText("good check");//3x3 화면에 5번 픽셀에 3번 이미지
                            switch_success();
                        }
                    }
                }
            });
        }
    }
    class but1_5 extends TimerTask {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (check != 1) {
                        img_1.setText("miss");
                        if (button != 0) {
                            img_1.setText("perfect check");// 화면에 5번 픽셀에 4번 이미지
                            switch_success();
                        }

                    }
                    check = 0;
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

        img_1 = (TextView) findViewById(R.id.t1);
        check = 0;

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
                            if(button == 0) {
                                button = value;
                            }
                            System.out.println(button);

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
        TimerTask task1 = new but1_1();
        TimerTask task2 = new but1_2();
        TimerTask task3 = new but1_3();
        TimerTask task4 = new but1_4();
        TimerTask task5 = new but1_5();


        g.schedule(task1,6000);
        g.schedule(task2,7000);
        g.schedule(task3,8000);
        g.schedule(task4,9000);
        g.schedule(task5,10000);

    }


    public void switch_success()
    {
        /** score, combo 계산 **/
        check = 1;

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

        ReceiveDotValue(dot_combo);
        ReceiveTextLcdValue(Integer.toString(gamescore), " ");
        ReceiveFndValue(Integer.toString(gamecombo));
        view_gamescore.setText(Integer.toString(gamescore));
        view_gamecombo.setText(Integer.toString(gamecombo));
    }

    public void switch_fail()
    {
        /** score, combo 계산 **/
        check = 1;

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