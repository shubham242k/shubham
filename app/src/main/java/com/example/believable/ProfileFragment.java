package com.example.believable;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    private static final String TAG ="Detail Profile" ;
    TextView displayName,emailAddress,phoneNumber;
    ImageView profileImage , editButton ;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    String StringImageUri,name,email,phone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment,container,false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        displayName = view.findViewById(R.id.displayName);
        emailAddress = view.findViewById(R.id.emailAddress);
        profileImage = view.findViewById(R.id.profileImage);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        editButton = view.findViewById(R.id.editButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "This feature has not been added", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseUser.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.d(TAG,"Error:"+error.getMessage());
                }
                else {
                    name = value.getString("Display Name");
                    displayName.setText(name);

                    email =  value.getString("Email Address");
                    emailAddress.setText(email);

                    phone = value.getString("Phone Number");
                    phoneNumber.setText(phone);

                    StringImageUri = value.getString("Image Uri");
                    Glide.with(getContext()).load(StringImageUri).into(profileImage);

                    Log.i(TAG, "String: "+StringImageUri);
                }


            }
        });
    }
}
