package com.example.jmb00k;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Library_PwsearchActivity extends AppCompatActivity {

    EditText psEditText_id, psEditText_name, psEditText_birth, psEditText_phone, psEditText_A;
    Spinner psSpinner_Q;
    Button psButton_pwre;

    private Socket socket;
    private static final String IP = "192.168.0.4";
    private static final int PORT = 8350;
    private String activity = "pwsearch";


    PrintWriter out;
    BufferedReader in;

    String datain;
    public void onBackPressed(){
        if (activity == "pwsearch"){
            Intent back = new Intent(Library_PwsearchActivity.this, Library_LoginActivity.class);
            finish();
            startActivity(back);
            try {
                socket.close();
            } catch (Exception e){ }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_activity_pwsearch);

        psEditText_id = (EditText) findViewById(R.id.jmb_EditText_pwsearch_ID);
        psEditText_name = (EditText) findViewById(R.id.jmb_EditText_pwsearch_name);
        psEditText_birth = (EditText) findViewById(R.id.jmb_EditText_pwsearch_birth);
        psEditText_phone = (EditText) findViewById(R.id.jmb_EditText_pwsearch_phone);
        psEditText_A = (EditText) findViewById(R.id.jmb_EditText_pwsearch_A);
        psSpinner_Q = (Spinner) findViewById(R.id.jmb_Spinner_pwsearch_Q);

        psButton_pwre = (Button) findViewById(R.id.jmb_Button_pwsearch_pwre);

        psButton_pwre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id, name, birth, phone, answer, question;
                id = psEditText_id.getText().toString();
                name = psEditText_name.getText().toString();
                birth = psEditText_birth.getText().toString();
                phone = psEditText_phone.getText().toString();
                answer = psEditText_A.getText().toString();
                question = psSpinner_Q.getSelectedItem().toString();
                String dataout;
                try {
                    if ((!id.equals("")) && (!name.equals("")) && (!birth.equals("")) && (!phone.equals("")) && (!answer.equals("")) && (!question.equals("질문을 선택해주세요"))) {
                        dataout = "re_pass" + ":" + id + ":" + name + ":" + birth + ":" + phone + ":" + question + ":" + answer;
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("re_pass", dataout);
                        out.println(dataout);
                    } else if (id.equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Library_PwsearchActivity.this);
                        builder.setTitle("입력").setMessage("ID를 입력해주세요");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        }).show();
                    } else if (name.equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Library_PwsearchActivity.this);
                        builder.setTitle("입력").setMessage("이름을 입력해주세요");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        }).show();
                    } else if (birth.equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Library_PwsearchActivity.this);
                        builder.setTitle("입력").setMessage("생년월일을 입력해주세요");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        }).show();
                    } else if (phone.equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Library_PwsearchActivity.this);
                        builder.setTitle("입력").setMessage("전화번호를 입력해주세요");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        }).show();
                    } else if (question.equals("질문을 선택해주세요")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Library_PwsearchActivity.this);
                        builder.setTitle("입력").setMessage("질문을 선택해주세요");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        }).show();
                    } else if (answer.equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Library_PwsearchActivity.this);
                        builder.setTitle("입력").setMessage("답을 입력해주세요");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        }).show();
                    }
                } catch (Exception e){
                    Toast.makeText(Library_PwsearchActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Thread worker = new Thread() {    //worker 를 Thread 로 생성
            public void run() { //스레드 실행구문
                try {
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(Library_PwsearchActivity.this);
                            builder.setTitle("비밀번호").setMessage("정보가 확인되었습니다");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent pwsearch = new Intent(Library_PwsearchActivity.this, Library_PasswordActivity.class);
                                    pwsearch.putExtra("password", datain);
                                    startActivity(pwsearch);
                                    try {
                                        socket.close();
                                    } catch (Exception e) { }
                                }
                            }).show();
                        } else if (datain.equals("no")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Library_PwsearchActivity.this);
                            builder.setTitle("비밀번호").setMessage("회원정보를 찾지 못하였습니다");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {}
                            }).show();
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        worker.start();  //onResume()에서 실행.
    }
    protected void onStop() {  //앱 종료시
        super.onStop();
        try {
            socket.close(); //소켓을 닫는다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}