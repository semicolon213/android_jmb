package com.example.jmb00k;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Library_MainActivity extends AppCompatActivity {

    private String activity = "main";

    ImageView mImageView_user;
    Button mButton_borrow, mButton_search, mButton_usermanage;

    Socket socket;
    private static final String IP = "192.168.0.4";
    private static final int PORT = 8350;

    PrintWriter out;
    BufferedReader in;
    String datain;

    public void onBackPressed(){
        if (activity == "main"){
            AlertDialog.Builder builder = new AlertDialog.Builder(Library_MainActivity.this);
            builder.setTitle("종료").setMessage("정말로 종료 하시겠습니까?");
            builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            }).show();
        }
    }
    protected void onCreate(Bundle savedInstanceState) {   //앱 시작시  초기화설정
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_activity_main);


        mImageView_user = (ImageView) findViewById(R.id.jmb_ImageView_main_user);
        mButton_borrow = (Button) findViewById(R.id.jmb_Button_main_borrow);
        mButton_usermanage = (Button) findViewById(R.id.jmb_Button_main_usermanage);
        mButton_search = (Button) findViewById(R.id.jmb_Button_main_search);

        mButton_borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mborrow = new Intent(Library_MainActivity.this, Library_BooklistActivity.class);
                startActivity(mborrow);
                finish();
            }
        });

        mButton_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent msearch = new Intent(Library_MainActivity.this, Library_SearchActivity.class);
                startActivity(msearch);
                finish();
            }
        });

        mButton_usermanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent muser = new Intent(Library_MainActivity.this, Library_UserActivity.class);
                startActivity(muser);
                finish();
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

}