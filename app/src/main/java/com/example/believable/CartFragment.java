package com.example.believable;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    List<CartModel> cartModelList;
    RecyclerAdapterCart recyclerAdapterCart;
    RecyclerView recyclerView;
    TextView bill;
    int Bill = 0;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    Button continueButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment,container,false);

        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        bill = view.findViewById(R.id.bill);
        continueButton = view.findViewById(R.id.continueButton);
        bill.setText(Integer.toString(Bill));


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Function not added yet", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        cardLoading();


        return view;
    }

    public void cardLoading(){
        firestore = FirebaseFirestore.getInstance();
       cartModelList = new ArrayList<>();
            CollectionReference collectionReference = firestore.collection("Users")
                    .document(firebaseAuth.getCurrentUser().getUid()).collection("Cart");
        collectionReference.addSnapshotListener((value, error) -> {
            if (error!=null){
                Log.d("Exception Failed", "onEvent: 0  " + error);

            }
            else{
                for (QueryDocumentSnapshot snapshot : value){
                    DocumentReference documentReference = firestore.collection("Users")
                            .document(firebaseAuth.getCurrentUser().getUid()).collection("Cart").document(snapshot.getId());
                    documentReference.addSnapshotListener((value1, error1) -> {
                        if (error1 !=null){
                            Log.d("Exception Failed", "onEvent: 0  " + error1);

                        }
                        else{
                            cartModelList.add(new CartModel(value1.getString("name"),
                                    value1.getString("priceperkg"),
                                    value1.getString("imageurl")
                            ,snapshot.getId()));
                            recyclerAdapterCart.notifyDataSetChanged();
                            if (!TextUtils.isEmpty(value1.getString("priceperkg")) && TextUtils.isDigitsOnly(value1.getString("priceperkg"))) {
                                Bill =Bill+  Integer.parseInt( value1.getString("priceperkg"));
                            }


                            bill.setText(Integer.toString(Bill));

//                            Log.d("abclist",value1.getString("name")+" "+value1.getString("price")+" "+value1.getString("imageurl")+" "+value1.getString("category"));
                        }

                    });

                }

            }
        });

        recyclerAdapterCart= new RecyclerAdapterCart(getContext(),cartModelList);
        recyclerView.setAdapter(recyclerAdapterCart);
    }
}
