package com.example.jmb00k;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Library_PasswordActivity extends AppCompatActivity {

    Intent getintent =new Intent(this.getIntent());
    String pw = getintent.getStringExtra("password");
    TextView pwTextView_password;
    Button pwButton_loginactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_activity_password);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        pwTextView_password = (TextView) findViewById(R.id.jmb_TextView);
        pwButton_loginactivity = (Button) findViewById(R.id.jmb_Button_password_loginback);

        pwTextView_password.setText(pw);

        pwButton_loginactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent password = new Intent(Library_PasswordActivity.this, Library_LoginActivity.class);
                startActivity(password);
            }
        });

    }
}
