package com.example.jmb00k;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Library_SearchActivity extends AppCompatActivity {

    Socket socket;
    private static final String IP = "192.168.0.4";
    private static final int PORT = 8350;
    PrintWriter out;
    BufferedReader in;
    String datain;

    EditText sEditText_book;
    Button sButton_search;
    ListView sListView_searchlist;
    EditText bookname;
    String searchinfor;

    ArrayList<HashMap<String, String>> sArrayList = new ArrayList<>();

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            bookname = (EditText)view.findViewById(R.id.jmb_EditText_search_book);
            searchinfor = bookname.getText().toString();
            Intent search = new Intent(Library_SearchActivity.this, Library_BookDeActivity.class);
            search.putExtra("search", searchinfor);
            startActivity(search);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_activity_search);

        sEditText_book = (EditText) findViewById(R.id.jmb_EditText_search_book);
        sButton_search = (Button) findViewById(R.id.jmb_Button_search_search);
        sListView_searchlist = (ListView) findViewById(R.id.jmb_ListView_search_searchlist);
        sButton_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                out.println("search" + sEditText_book.getText().toString());
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
                        Toast.makeText(Library_SearchActivity.this, datain, Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = new JSONArray(datain);
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String bookname = jsonObject.getString("bookname");
                            String writer = jsonObject.getString("writer");
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("bookname", bookname);
                            hashMap.put("writer", writer);
                            sArrayList.add(hashMap);
                        }
                        ListAdapter adapter = new SimpleAdapter(Library_SearchActivity.this, sArrayList, R.layout.library_boodinfer_listitem, new String[]{"bookname","writer"}, new int[]{R.id.jmb_TextView_boodinfer_bookname, R.id.jmb_TextView_boodinfer_writer});
                        sListView_searchlist.setAdapter(adapter);
                        sListView_searchlist.setOnItemClickListener(onItemClickListener);
                    }
                } catch (Exception e) {
                }
            }
        };
        worker.start();  //onResume()에서 실행.
    }

}