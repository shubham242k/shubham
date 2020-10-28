package com.example.believable;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExtendedCategoryPage extends AppCompatActivity {

    List<NewCardModel> newCardModelList;
    RecyclerAdapterNewItems recyclerAdapterNewItems;
    RecyclerView recyclerView;
    String clickedCategory;
    FirebaseFirestore firestore;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extended_category_page);

        clickedCategory = getIntent().getStringExtra("category");

        recyclerView = findViewById(R.id.recyclerView);



        recyclerView.setItemAnimator(new DefaultItemAnimator());
        cardLoading();

    }

    public void cardLoading(){
        newCardModelList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firestore.collection("Store");
        Query category = collectionReference.whereEqualTo("category",clickedCategory);

        category.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.d("Exception Failed", "onEvent: 0  " + error);
                }
                else {
                    for (QueryDocumentSnapshot snapshot : value){
                        DocumentReference documentReference = firestore.collection("Store").document(snapshot.getId());
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error!=null){
                                    Log.d("Exception Failed", "onEvent: 0  " + error);
                                }
                                else{
                                    newCardModelList.add(new NewCardModel(value.getString("name"),
                                    value.getString("priceperkg"),
                                    value.getString("imageurl"),
                                    value.getString("category")));
                            recyclerAdapterNewItems.notifyDataSetChanged();
                            recyclerView.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                }
            }
        });
        if (newCardModelList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
        }


        recyclerAdapterNewItems = new RecyclerAdapterNewItems(this,newCardModelList);
        recyclerView.setAdapter(recyclerAdapterNewItems);
    }
}
