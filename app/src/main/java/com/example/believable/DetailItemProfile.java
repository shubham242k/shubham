package com.example.believable;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailItemProfile extends AppCompatActivity {

    TextView name,price;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String url,sprice;
    ImageView image,addtoCart;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_item);
        auth =FirebaseAuth.getInstance();

        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        image = findViewById(R.id.image);
        addtoCart = findViewById(R.id.addtoCart);


        url = getIntent().getStringExtra("imageurl");
        Glide.with(this).load(getIntent().getStringExtra("imageurl")).into(image);
        name.setText(getIntent().getStringExtra("name"));
        sprice = getIntent().getStringExtra("price");
        price.setText(sprice+" $/kg");

        addtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore = FirebaseFirestore.getInstance();

                DocumentReference documentReference = firestore.collection("Users").document(auth.getCurrentUser().getUid())
                        .collection("Cart").document();
                Map<String,Object> cartItem = new HashMap<>();
                cartItem.put("name",name.getText().toString());
                cartItem.put("priceperkg",sprice);
                cartItem.put("imageurl",url);

                documentReference.set(cartItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent i = new Intent(getApplicationContext(),BaseActivity.class);
                        i.putExtra("which activity","to cart activity");
                        startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Not able to add into cart. Please try later", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
