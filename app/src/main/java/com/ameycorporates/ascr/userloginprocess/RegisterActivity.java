package com.ameycorporates.ascr.userloginprocess;

import static android.graphics.Color.TRANSPARENT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ameycorporates.ascr.R;
import com.ameycorporates.ascr.database.DatabaseHandler;
import com.ameycorporates.ascr.maingui.MainActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        EditText name=(EditText) findViewById(R.id.nametextview);
        EditText phoneno=(EditText) findViewById(R.id.phonenotextview);
        EditText emailid=(EditText) findViewById(R.id.emailtextview);
        EditText  pass=(EditText) findViewById(R.id.passwordtextview);
        EditText cpass=(EditText) findViewById(R.id.confirmpasswordtextview);
        
        Button register=(Button) findViewById(R.id.registerbtn);



        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //registration process

                if(name.getText().toString().equals(""))
                {
                    name.setError("please enter name");
                    return;
                }
                else if(phoneno.getText().toString().equals(""))
                {
                    phoneno.setError("please enter phone number ");
                    return;
                }
                else if(emailid.getText().toString().equals(""))
                {
                    emailid.setError("please enter mail id");
                    return;
                }
                else if(pass.getText().toString().equals(""))
                {
                    pass.setError("please create a password");
                    return;
                }
                else if(cpass.getText().toString().equals(""))
                {
                    cpass.getText().toString().equals("please confirm the password");
                    return;
                }
                if(!pass.getText().toString().equals(cpass.getText().toString()))
                {
                    new android.app.AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Password Error")
                            .setMessage("password don't match \n not to worry try again.!!")
                            .setPositiveButton("okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //
                                }
                            }).show();
                }
                else {

                    int id = 1, flag = 1;
                    String query;
                    DatabaseHandler db = new DatabaseHandler();
                    query = "insert into user_master values(1,'" + name.getText().toString() + "','" + phoneno.getText().toString() + "','" + emailid.getText().toString() + "','" + pass.getText().toString() + "');";
                    db.insertData(query);

                    new AlertDialog.Builder(RegisterActivity.this).setTitle("Successfull")
                            .setMessage("Registration completed")
                            .setCancelable(false)
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    takecontact();

                                }
                            }).show();
                }
            }
        });

    }


    private int counter=1;
    private void takecontact()
    {
        if(counter<4) {
            Dialog dialog = new Dialog(RegisterActivity.this);
            dialog.setContentView(R.layout.contact_layout);
            dialog.setCancelable(false);
            Button btn=(Button)dialog.findViewById(R.id.addbtn);
            EditText contactname=(EditText) dialog.findViewById(R.id.nametextview);
            EditText contactphone=(EditText) dialog.findViewById(R.id.phonenotextview);
            EditText contactemail=(EditText) dialog.findViewById(R.id.emailtextview);
            TextView txt=(TextView) dialog.findViewById(R.id.texttoshow);
            txt.setText("Contact List "+counter+"/3");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(contactname.getText().equals(""))
                    {
                        contactname.setError("please enter the name");
                    }
                    else if(contactphone.getText().equals(""))
                    {
                        contactphone.setError("please enter phone number");
                    }
                    else if(contactemail.getText().equals(""))
                    {
                        contactemail.setError("please enter email id");
                    }
                    else
                    {
                        DatabaseHandler db=new DatabaseHandler();
                        String query="insert into contact_master(id,contact_name,phone_no,email_id) values("+counter+",'"+contactname.getText().toString()+"','"+contactphone.getText().toString()+"','"+contactemail.getText().toString()+"');";
                        db.insertData(query);

                        counter++;
                        dialog.hide();
                        takecontact();

                    }
                }
            });

            WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width=WindowManager.LayoutParams.MATCH_PARENT;
            lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
            dialog.show();
            dialog.getWindow().setAttributes(lp);

        }
        else
        {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();

        }
    }

}