package com.example.jmb00k;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class Library_BookDeActivity extends AppCompatActivity {

    Intent search = getIntent();
    String bookname = search.getStringExtra("search");
    private static final String IP = "192.168.0.4";
    private static final int PORT = 8350;
    String datain;
    PrintWriter out;
    BufferedReader in;
    Socket socket;
    private String activity = "bookde";


    TextView deTextView_bookname, deTextView_writer, deTextView_bookcompany, deTextView_location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_activity_bookde);

        deTextView_bookname = (TextView) findViewById(R.id.jmb_TextView_bookde_bookname);
        deTextView_writer = (TextView) findViewById(R.id.jmb_TextView_bookde_writer);
        deTextView_bookcompany = (TextView) findViewById(R.id.jmb_TextView_bookde_bookcompany);
        deTextView_location = (TextView) findViewById(R.id.jmb_TextView_bookde_location);

        out.println(bookname);

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
                        String data[] = datain.split(":");
                        if (data[0] == "bookdata"){
                            deTextView_bookname.setText(data[1]);
                            deTextView_writer.setText(data[2]);
                            deTextView_bookcompany.setText(data[3]);
                            deTextView_location.setText(data[4]);
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        worker.start();  //onResume()에서 실행.

    }
    public void onBackPressed(){
        if (activity == "bookde"){
            Intent bookde = new Intent(Library_BookDeActivity.this, Library_MainActivity.class);
            startActivity(bookde);
            finish();
        }
    }
}
