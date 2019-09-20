package com.example.jmb00k;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;

public class Library_LoginActivity extends AppCompatActivity {    //메인 activity 시작!

    private Socket socket;
    private static final String IP = "192.168.0.4";
    private static final int PORT = 8350;

    EditText logEditText_id;
    EditText logEditText_pw;

    Button logButton_login;
    Button logButton_pwsearch;
    String datain;
    String dataout;

    PrintWriter out;
    BufferedReader in;
    String id;
    String pw;
    private BackPressCloseHandler backPressCloseHandler;
    private String activity = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {   //앱 시작시  초기화설정
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_activity_login);

        logEditText_id = (EditText) findViewById(R.id.jmb_EditText_login_id);
        logEditText_pw = (EditText) findViewById(R.id.jmb_EditText_login_pw);

        logButton_login = (Button) findViewById(R.id.jmb_Button_login_login);
        logButton_pwsearch = (Button) findViewById(R.id.jmb_Button_login_pwforget);

        backPressCloseHandler = new BackPressCloseHandler(this);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;

        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // 버튼을 누르는 이벤트 발생, 이벤트 제어문이기 때문에 이벤트 발생 때마다 발동된다. 시스템이 처리하는 부분이 무한루프문에
        //있더라도 이벤트가 발생하면 자동으로 실행된다.
        logButton_login.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {                       //버튼이 클릭되면 소켓에 데이터를 출력한다.
                id = logEditText_id.getText().toString();  //글자입력칸에 있는 글자를 String 형태로 받아서 data에 저장
                pw = logEditText_pw.getText().toString();

                try {
                    if (!(id.equals("")) && !(pw.equals(""))) { //만약 데이타가 아무것도 입력된 것이 아니라면
                        dataout = "login:" + id + ":" + pw;
                        out.println(dataout);
                        Intent login = new Intent(Library_LoginActivity.this, Library_MainActivity.class);
                        startActivity(login);
                        finish();
                    } else {
                        if (id.equals("")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(Library_LoginActivity.this);
                            builder.setTitle("로그인").setMessage("아이디를 입력해주세요");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) { }
                            }).show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Library_LoginActivity.this);
                            builder.setTitle("로그인").setMessage("비밀번호를 입력해주세요");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) { }
                            }).show();
                        }
                    }
                } catch (Exception e){
                    Toast.makeText(Library_LoginActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
        logButton_pwsearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(Library_LoginActivity.this, Library_PwsearchActivity.class);
                startActivity(login);
                finish();
                try {
                    socket.close();
                } catch (Exception e) { }
            }
        });

            Thread worker = new Thread() {    //worker 를 Thread 로 생성
                public void run() { //스레드 실행구문
                    try {
    //소켓을 생성하고 입출력 스트립을 소켓에 연결한다.
                        socket = new Socket(IP,PORT); //소켓생성
                        out = new PrintWriter(socket.getOutputStream(), true); //데이터를 전송시 stream 형태로 변환하여                                                                                                                       //전송한다.
                        in = new BufferedReader(new InputStreamReader(
                                socket.getInputStream())); //데이터 수신시 stream을 받아들인다.

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

    //소켓에서 데이터를 읽어서 화면에 표시한다.
                    try {
                        while (true) {
                            datain = in.readLine(); // in으로 받은 데이타를 String 형태로 읽어 data 에 저장
                            if (datain.equals("yes")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Library_LoginActivity.this);
                                builder.setTitle("로그인").setMessage("로그인 되었습니다");
                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent login = new Intent(Library_LoginActivity.this, Library_MainActivity.class);
                                        login.putExtra("id", id);
                                        login.putExtra("pw", pw);
                                        startActivity(login);
                                    }
                                }).show();
                            } else if (datain.equals("no")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Library_LoginActivity.this);
                                builder.setTitle("로그인").setMessage("회원정보가 올바르지 않습니다.");
                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        logEditText_pw.setBackgroundColor(Color.RED);
                                        logEditText_pw.setText("");
                                    }
                                }).show();
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            };
            worker.start();  //onResume()에서 실행.
    }

    @Override
    protected void onStop() {  //앱 종료시
        super.onStop();
        try {
            socket.close(); //소켓을 닫는다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed(){
        if (activity == "login") {
            backPressCloseHandler.onBackPressed();
        }
    }
    public class BackPressCloseHandler {

        private long backKeyPressedTime = 0;
        private Toast toast;

        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                activity.finish();
                toast.cancel();
            }
        }

        public void showGuide() {
            toast = Toast.makeText(activity,
                    "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
