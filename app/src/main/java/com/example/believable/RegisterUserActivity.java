package com.example.believable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SIGN UP ACTIVITY" ;
    private EditText signUpemailEditText;
    private EditText signUpConfirmPasswordEditText;
    private EditText signUpPasswordEditText;
    private Button signUpButton;
    String email,password,phone;
    FirebaseFirestore firestore;
    ProgressBar progressBar;
    String from = "A";
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    TextView text_login;

    String stringImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user_activity);
        from = getIntent().getStringExtra("from activity");
        phone = getIntent().getStringExtra("phone number");
        Toast.makeText(this, from, Toast.LENGTH_SHORT).show();
        initializeUi();
    }
    private void initializeUi() {
        signUpemailEditText = findViewById(R.id.email_text_view);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        signUpConfirmPasswordEditText = findViewById(R.id.confirm_password_text_view2);
        signUpPasswordEditText  = findViewById(R.id.password_text_view2);
        signUpButton = findViewById(R.id.signup_button);
        text_login = findViewById(R.id.text_login);

        signUpButton.setOnClickListener(this);
        text_login.setOnClickListener(this);
        Toast.makeText(this, signUpButton.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signup_button) {
            if (from != null &&phone !=null&& from.equals("OtpActivityLogin")){
                linkWithPhone();
            }
            else{
                doSignUp();
                phone = "Add phone number";
            }

        }
        else if (view.getId() == R.id.text_login){
            Intent intent = new Intent(RegisterUserActivity.this, LoginUserActivity.class);
            startActivity(intent);
        }
    }

    private void linkWithPhone() {
        progressBar.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(signUpemailEditText.getText().toString())
                || TextUtils.isEmpty(signUpPasswordEditText.getText().toString().trim())
                || TextUtils.isEmpty(signUpConfirmPasswordEditText.getText().toString().trim())) {

            if(TextUtils.isEmpty(signUpemailEditText.getText().toString()))
                signUpemailEditText.setError("Enter the email");

            if(TextUtils.isEmpty(signUpPasswordEditText.getText().toString().trim()))
                signUpPasswordEditText.setError("Enter the password ");

            if(TextUtils.isEmpty(signUpConfirmPasswordEditText.getText().toString().trim()))
                signUpConfirmPasswordEditText.setError("Enter confirm password");


        }

        else if ( signUpPasswordEditText.getText().toString().trim().length() < 8){
            signUpPasswordEditText.setError("password length should be equal or more than 8 character");
        }

        else if(!signUpPasswordEditText.getText().toString().trim().equals(signUpConfirmPasswordEditText.getText().toString().trim())){


            signUpConfirmPasswordEditText.setError("password does not match");
            signUpPasswordEditText.setError("password does not match");
        }
        else{
            email = signUpemailEditText.getText().toString().trim();
            password = signUpConfirmPasswordEditText.getText().toString().trim();
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
            linkingWithPhone(credential);
            progressBar.setVisibility(View.GONE);
        }

    }
    private void linkingWithPhone(AuthCredential credential) {
        mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    updateFirebaseDatabase(mAuth.getCurrentUser());
                    Intent intent = new Intent(RegisterUserActivity.this, UserInfoEntry.class);
                    intent.putExtra("password",password);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void doSignUp() {
        if (TextUtils.isEmpty(signUpemailEditText.getText().toString())
                || TextUtils.isEmpty(signUpPasswordEditText.getText().toString().trim())
                || TextUtils.isEmpty(signUpConfirmPasswordEditText.getText().toString().trim())) {

            if(TextUtils.isEmpty(signUpemailEditText.getText().toString()))
                signUpemailEditText.setError("Enter the email");

            if(TextUtils.isEmpty(signUpPasswordEditText.getText().toString().trim()))
                signUpPasswordEditText.setError("Enter the password ");

            if(TextUtils.isEmpty(signUpConfirmPasswordEditText.getText().toString().trim()))
                signUpConfirmPasswordEditText.setError("Enter confirm password");


        }

        else if ( signUpPasswordEditText.getText().toString().trim().length() < 8){
            signUpPasswordEditText.setError("password length should be equal or more than 8 character");
        }

        else if(!signUpPasswordEditText.getText().toString().trim().equals(signUpConfirmPasswordEditText.getText().toString().trim())){


            signUpConfirmPasswordEditText.setError("password does not match");
            signUpPasswordEditText.setError("password does not match");
        }

        else {
            email = signUpemailEditText.getText().toString().trim();
            password = signUpConfirmPasswordEditText.getText().toString().trim();

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG, "onComplete: ");
                                /*
                                Trying to fix problem in crash
                                 */
                                Toast.makeText(RegisterUserActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                //TODO ADD INTENT HERE
                                updateFirebaseDatabase(mAuth.getCurrentUser());
                                startHome();

                            } else {
                                Toast.makeText(RegisterUserActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void updateFirebaseDatabase(final FirebaseUser user) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = firebaseFirestore.collection("Users").document(user.getUid());
        final Map<String,Object> userNew = new HashMap<>();

        StorageReference defaultFileRefernce  = FirebaseStorage.getInstance().getReference().child("default_profile.png");

        defaultFileRefernce.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                stringImageUri = uri.toString();
                Log.d(TAG, "onSuccess: " + stringImageUri);

                userNew.put("Display Name",email);
                userNew.put("Phone Number",phone);
                userNew.put("Email Address",email);
                userNew.put("Image Uri", uri.toString());
                userNew.put("User password",password);

                Log.d("user",userNew.toString());

                documentReference.set(userNew).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "firestore Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "firestore Updated failed", Toast.LENGTH_SHORT).show();
                    }
                });

//                final StorageReference ref = FirebaseStorage.getInstance().getReference().child(user.getUid()).child("profileImage.jpeg");
//                ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uriToSend) {
//
//                            }
//                        });
//                    }
//                });


            }
        });


    }

    private void startHome() {
        Intent i=new Intent(getApplicationContext(), PhoneEntryActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("password",password);
        startActivity(i);

    }

    protected void onStart() {
        super.onStart();

        if (mAuth != null) {
            mUser = mAuth.getCurrentUser();
        }
    }
}