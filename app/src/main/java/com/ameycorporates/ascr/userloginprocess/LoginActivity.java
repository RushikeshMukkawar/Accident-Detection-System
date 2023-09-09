package com.ameycorporates.ascr.userloginprocess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ameycorporates.ascr.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        EditText email=(EditText)findViewById(R.id.emailtextview);
        EditText pass=(EditText)findViewById(R.id.passwordtextview);
        TextView newregister=(TextView)findViewById(R.id.newuserregister);
        Button loginbtn=(Button)findViewById(R.id.loginbtn);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //login process
            }
        });

        newregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                            Intent it=new Intent(LoginActivity.this, RegisterActivity.class);
                            startActivity(it);
                            finish();
            }
        });



    }
}