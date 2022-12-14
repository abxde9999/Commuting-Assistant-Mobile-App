package com.example.biyahe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SOS extends AppCompatActivity {

    private static final String SOS_RECEIVER = "sos_receiver.txt";
    private static final String SOS_MESSAGE = "sos_message.txt";

    //Initialization
    EditText etPhone,etMessage,etReceiver;
    Button btSend,btChange;
    String rNumber;
    String mInput;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        //Assign Variable
        etReceiver = findViewById(R.id.et_receiver);
        etMessage = findViewById(R.id.et_message);
        btSend = findViewById(R.id.et_send);
        btChange = findViewById(R.id.et_change);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check permission
                if (ContextCompat.checkSelfPermission(SOS.this, Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED){
                    //When permission is granted
                    //Create a Method

                    sendSMS();
                }else{
                    //When Permission is not Granted
                    //Request for Permission
                    ActivityCompat.requestPermissions(SOS.this, new String[]{Manifest.permission.SEND_SMS},
                            100);
                }
            }
        });

        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenHome();
                Intent intent = new Intent(SOS.this, Home.class);
                startActivity(intent);
            }
        });

    }
    public void OpenHome() {

        String recNumber = etReceiver.getText().toString();
        FileOutputStream fosReceiver = null;

        Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();

        try {
            fosReceiver = openFileOutput(SOS_RECEIVER, MODE_PRIVATE);
            fosReceiver.write(recNumber.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fosReceiver != null){
                try {
                    fosReceiver.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String sosSMS = etMessage.getText().toString();
        FileOutputStream fosMessage = null;

        try {
            fosMessage = openFileOutput(SOS_MESSAGE, MODE_PRIVATE);
            fosMessage.write(sosSMS.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fosMessage != null){
                try {
                    fosMessage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check Condition
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //When permission is Granted
            //Call Method
            sendSMS();
        }else {
            //When permission is Denied
            //Display Toast
            Toast.makeText(this, "Permission Denied, Please Grant!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSMS() {


        //get Value from etText
        String message = etMessage.getText().toString();
        String receiver = etReceiver.getText().toString();

        //Check condition if String is empty
        if (!receiver.isEmpty() && !message.isEmpty()){
            //Initialize SMS Manager
            SmsManager smsManager = SmsManager.getDefault();
            //Send Msg
            smsManager.sendTextMessage(receiver,null,message, null, null);
            // Display Toast Msg
            Toast.makeText(this, "SOS Message Sent Successfully.", Toast.LENGTH_SHORT).show();

        }else{
            //When String is empty, Displays a Toast
            Toast.makeText(this, "Please enter a message.", Toast.LENGTH_SHORT).show();

        }
    }

}