package com.ameycorporates.ascr.maingui;

import static android.graphics.Color.TRANSPARENT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ameycorporates.ascr.R;
import com.ameycorporates.ascr.allservices.BluetoothService;
import com.ameycorporates.ascr.allservices.MyService;
import com.ameycorporates.ascr.database.DatabaseHandler;
import com.ameycorporates.ascr.userloginprocess.RegisterActivity;

import org.w3c.dom.Text;

import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;
    public static BluetoothDevice bluetoothDevice;
    BroadcastReceiver br;
    Button connect;
    TextView connectionmsg;
    static int flagconnect=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();

        takeallpermission();

        String id=getIntent().getStringExtra("myid");
        DatabaseHandler db=new DatabaseHandler();
        Cursor cr=db.getData("select * from user_master where id="+id+";");
        while(cr.moveToNext())
        {
            TextView txt=(TextView) findViewById(R.id.usernametextview);
            txt.setText(cr.getString(1).toUpperCase());
            Log.e("message","Data is :"+cr.getString(0)+" : "+cr.getString(1));
        }




        connect=(Button)findViewById(R.id.connectbtn);
        connect.setText("Checking..");

        connectionmsg=(TextView)findViewById(R.id.connectmsg);
        connectionmsg.setText(" Connecting to HC-05");

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            //can't run the service with this device
        } else {
            if (bluetoothAdapter.isEnabled()) {
                processAfterOn();
            } else {
                new AlertDialog.Builder(this).setTitle("On Bluetooth")
                        .setMessage("Please turn on your bluetooth to connect with the hardware")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent it = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(it, 20);
                            }
                        })
                        .show();
            }
        }

        registrerreciver();

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processAfterOn();
            }
        });

            workWithContact();



    }

    private void workWithContact()
    {
        DatabaseHandler db=new DatabaseHandler();
        Cursor cursor=db.getData("select * from contact_master;");
        String ids[]=new String[3];
        String names[]=new String[3];
        String contacts[]=new String[3];
        String emails[]=new String[3];
        int i=0;
        while(cursor.moveToNext())
        {
            ids[i]=cursor.getString(0);
            names[i]=cursor.getString(1);
            contacts[i]=cursor.getString(2);
            emails[i]=cursor.getString(3);

            i++;
        }
        TextView contacttxt1=(TextView) findViewById(R.id.contacttxt1);
        TextView contacttxt2=(TextView) findViewById(R.id.contacttxt2);
        TextView contacttxt3=(TextView) findViewById(R.id.contacttxt3);

        contacttxt1.setText(names[0].toUpperCase());
        contacttxt2.setText(names[1].toUpperCase());
        contacttxt3.setText(names[2].toUpperCase());

        LinearLayout contact1=(LinearLayout) findViewById(R.id.contact1);
        contact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(DashboardActivity.this);
                dialog.setContentView(R.layout.update_contact_layout);
                dialog.setCancelable(true);
                WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width=WindowManager.LayoutParams.MATCH_PARENT;
                lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
                dialog.show();
                dialog.getWindow().setAttributes(lp);

                EditText contactname=(EditText) dialog.findViewById(R.id.nametextview);
                EditText contactphone=(EditText) dialog.findViewById(R.id.phonenotextview);
                EditText contactemail=(EditText) dialog.findViewById(R.id.emailtextview);
                TextView txt=(TextView)dialog.findViewById(R.id.texttoshow);
                txt.setText("Contact details for "+names[0]);

                contactname.setText(names[0]);
                contactphone.setText(contacts[0]);
                contactemail.setText(emails[0]);


                Button update=(Button) dialog.findViewById(R.id.updatebtn);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseHandler db=new DatabaseHandler();
                        db.insertData("update contact_master set contact_name='"+contactname.getText().toString()+"' , phone_no='"+contactphone.getText().toString()+"' , email_id='"+contactemail.getText().toString()+"' where id="+ids[0]+";");
                        dialog.hide();
                        new AlertDialog.Builder(DashboardActivity.this).setTitle("Successfull")
                                .setMessage("Updated Successfully")
                                .show();

                        workWithContact();
                    }
                });




            }
        });

        LinearLayout contact2=(LinearLayout) findViewById(R.id.contact2);
        contact2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dialog dialog = new Dialog(DashboardActivity.this);
                dialog.setContentView(R.layout.update_contact_layout);
                dialog.setCancelable(true);
                WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width=WindowManager.LayoutParams.MATCH_PARENT;
                lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
                dialog.show();
                dialog.getWindow().setAttributes(lp);

                EditText contactname=(EditText) dialog.findViewById(R.id.nametextview);
                EditText contactphone=(EditText) dialog.findViewById(R.id.phonenotextview);
                EditText contactemail=(EditText) dialog.findViewById(R.id.emailtextview);

                TextView txt=(TextView)dialog.findViewById(R.id.texttoshow);
                txt.setText("Contact details for "+names[1]);

                contactname.setText(names[1]);
                contactphone.setText(contacts[1]);
                contactemail.setText(emails[1]);


                Button update=(Button) dialog.findViewById(R.id.updatebtn);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseHandler db=new DatabaseHandler();
                        db.insertData("update contact_master set contact_name='"+contactname.getText().toString()+"' , phone_no='"+contactphone.getText().toString()+"' , email_id='"+contactemail.getText().toString()+"' where id="+ids[1]+";");
                        dialog.hide();
                        new AlertDialog.Builder(DashboardActivity.this).setTitle("Successfull")
                                .setMessage("Updated Successfully")
                                .show();

                        workWithContact();
                    }
                });


            }
        });

        LinearLayout contact3=(LinearLayout) findViewById(R.id.contact3);
        contact3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dialog dialog = new Dialog(DashboardActivity.this);
                dialog.setContentView(R.layout.update_contact_layout);
                dialog.setCancelable(true);
                WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width=WindowManager.LayoutParams.MATCH_PARENT;
                lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
                dialog.show();
                dialog.getWindow().setAttributes(lp);

                EditText contactname=(EditText) dialog.findViewById(R.id.nametextview);
                EditText contactphone=(EditText) dialog.findViewById(R.id.phonenotextview);
                EditText contactemail=(EditText) dialog.findViewById(R.id.emailtextview);

                TextView txt=(TextView)dialog.findViewById(R.id.texttoshow);
                txt.setText("Contact details for "+names[2]);

                contactname.setText(names[2]);
                contactphone.setText(contacts[2]);
                contactemail.setText(emails[2]);


                Button update=(Button) dialog.findViewById(R.id.updatebtn);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseHandler db=new DatabaseHandler();
                        db.insertData("update contact_master set contact_name='"+contactname.getText().toString()+"' , phone_no='"+contactphone.getText().toString()+"' , email_id='"+contactemail.getText().toString()+"' where id="+ids[2]+";");
                        dialog.hide();
                        new AlertDialog.Builder(DashboardActivity.this).setTitle("Successfull")
                                .setMessage("Updated Successfully")
                                .show();

                        workWithContact();
                    }
                });

            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        registrerreciver();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(br);
        super.onStop();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 20) {
            if (resultCode == RESULT_OK) {
                processAfterOn();
            } else {
                new AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("App can't process further..!")
                        .setCancelable(false)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).show();
            }
        }
    }



    private void takeallpermission() {
        if(!Settings.System.canWrite(getApplicationContext()))
        {
            Intent i=new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:"+getPackageName()));
            startActivity(i);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{ Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ANSWER_PHONE_CALLS,Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_FINE_LOCATION}, 20);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 20) {
            for (int i = 0; i < permissions.length; i++) {
                Log.e("Result"," Result of "+permissions[i]+" is :"+grantResults[i]);
                if (grantResults[i] == -1){
                    new AlertDialog.Builder(this).setTitle("Error")
                            .setMessage("Please grant all permission")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).show();
                }
            }

        }
    }

    private boolean checkBluetoothService()
    {

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if(BluetoothService.class.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void processAfterOn() {

        if(checkBluetoothService())
        {
            return;
        }


        bluetoothDevice = null;
            int flag=1;

        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
           if (device.getName().equals("HC-05")) {

                flag=2;
                bluetoothDevice=device;
                Bundle b=new Bundle();
                b.putParcelable("data",bluetoothDevice);
                Intent it=new Intent(DashboardActivity.this, BluetoothService.class);
                it.putExtra("mydata",b);
                startForegroundService(it);
                break;

            }
        }

        if(flag==1)
        {
            new AlertDialog.Builder(this).setTitle("Pair the Device")
                    .setMessage("Please go into your settings and pair the device to processed further")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).show();
        }


    }

        public  void registrerreciver()
        {
            br= new BroadcastReceiver(){

                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.hasExtra("message")) {
                        String msg=intent.getStringExtra("message");
                        if(msg.equals("connected"))
                        {
                            connect.setText("Connected");
                            connectionmsg.setText("Paired with HC-05");
                            flagconnect=1;
                        }
                        else if(msg.equals("disconnected"))
                        {
                            flagconnect=2;
                            connect.setText("Disconnected");
                            connectionmsg.setText("please ON your HC-05");
                            stopService(new Intent(DashboardActivity.this,BluetoothService.class));
                            new Handler().postDelayed(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void run() {
                                    processAfterOn();
                                }
                            },300000);
                        }


                    }

                }
            };
            IntentFilter intentFilter=new IntentFilter();
            intentFilter.addAction("pass");
            registerReceiver(br,intentFilter);

        }

    @Override
    protected void onDestroy() {
        if(flagconnect==2)
        {
            stopService(new Intent(DashboardActivity.this,BluetoothService.class));
        }
        super.onDestroy();

    }
}