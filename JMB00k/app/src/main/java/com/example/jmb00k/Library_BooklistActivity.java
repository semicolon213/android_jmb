package com.example.jmb00k;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Library_BooklistActivity extends AppCompatActivity {

    Intent input = getIntent();

    TextView blistTextView_OX;
    TextView blistTextView_num;
    ListView blistListView_list;

    Socket socket;
    PrintWriter out;
    BufferedReader in;
    private static final String IP = "192.168.0.4";
    private static final int PORT = 8350;
    String datain;
    private String activity = "booklist";

    boolean borrowstate = true;

    ArrayList<HashMap<String, String>> blistArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_activity_booklist);

        blistTextView_OX = (TextView) findViewById(R.id.jmb_TextView_booklist_OX);
        blistTextView_num = (TextView) findViewById(R.id.jmb_TextView_booklist_num);
        blistListView_list = (ListView) findViewById(R.id.jmb_ListView_booklist_list);

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
                        datain = in.readLine();
                        JSONArray jsonArray = new JSONArray(datain);
                        if (jsonArray.length()>=6){
                            borrowstate = false;
                        }
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObject =jsonArray.getJSONObject(i);
                            String bookname = jsonObject.getString("bookname");
                            String deadline = jsonObject.getString("deadline");
                            String bstate = jsonObject.getString("bstate");
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("bookname", bookname);
                            hashMap.put("deadline", deadline);
                            hashMap.put("bstate", bstate);
                            blistArrayList.add(hashMap);
                            if (bstate == "no") {
                                borrowstate = false;
                            }
                        }
                        ListAdapter adapter = new SimpleAdapter(Library_BooklistActivity.this, blistArrayList, R.layout.library_booklist_item, new String[]{"bookname", "deadline", "bstate"}, new int[]{R.id.jmb_TextView_bookitem_bookname, R.id.jmb_TextView_bookitem_deadline, R.id.jmb_TextView_bookitem_bstate});
                        blistListView_list.setAdapter(adapter);
                        if (borrowstate == true){
                            blistTextView_OX.setText("O");
                        } else {
                            blistTextView_OX.setText("X");
                        }
                        blistTextView_num.setText(6-jsonArray.length());
                    }
                } catch (Exception e){ }
            }
        };
        worker.start();  //onResume()에서 실행.
    }
    public void onBackPressed(){
        if (activity == "booklist"){
            Intent back = new Intent(Library_BooklistActivity.this, Library_MainActivity.class);
            startActivity(back);
            finish();
        }
    }

    @Override
    protected void onStop(){  //앱 종료시
        super.onStop();
        try {
            socket.close(); //소켓을 닫는다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}