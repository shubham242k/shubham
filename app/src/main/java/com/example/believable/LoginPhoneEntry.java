package com.example.believable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

public class LoginPhoneEntry extends AppCompatActivity {

    EditText phoneNumber;
    CountryCodePicker codeNumber;
    TextView errorText;
    Button generateotpButton;
    ProgressBar generateProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_entry_activity);

        phoneNumber = findViewById(R.id.phoneNumberLayout);
        codeNumber = findViewById(R.id.codeNumber);

        generateotpButton = findViewById(R.id.generateotpButton);
        errorText = findViewById(R.id.errorText);
        generateProgressBar = findViewById(R.id.generateProgressBar);
        codeNumber.registerCarrierNumberEditText(phoneNumber);

        generateotpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(phoneNumber.getText().toString())){
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("Please enter your phone number");
                }
                else if(phoneNumber.getText().toString().replace(" ","").length()>15 && phoneNumber.getText().toString().replace(" ","").length()<9){
                    errorText.setText("Invalid Phone number");
                    errorText.setVisibility(View.VISIBLE);

                }
                else {
                    generateProgressBar.setVisibility(View.VISIBLE);
                    Intent i = new Intent(getApplicationContext(), OtpActivityLogin.class);
                    i.putExtra("phone number",codeNumber.getFullNumberWithPlus().replace(" ",""));
                    finish();
                    startActivity(i);
                }
            }
        });

    }
}