package com.example.believable;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NewItemTabFragment extends Fragment {

    List<NewCardModel> newCardModelList;
    RecyclerAdapterNewItems recyclerAdapterNewItems;
    FirebaseFirestore firestore;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newitem_tab_fragment,container,false);


        recyclerView = view.findViewById(R.id.recyclerView);

        firestore =FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        cardLoading();

        return view;
    }

    public void cardLoading(){
        newCardModelList = new ArrayList<>();
        CollectionReference collectionReference = firestore.collection("Store");
        collectionReference.addSnapshotListener((value, error) -> {
            if (error!=null){
                Log.d("Exception Failed", "onEvent: 0  " + error);

            }
            else{
                for (QueryDocumentSnapshot snapshot : value){
                    DocumentReference documentReference = firestore.collection("Store").document(snapshot.getId());
                    documentReference.addSnapshotListener((value1, error1) -> {
                        if (error1 !=null){
                            Log.d("Exception Failed", "onEvent: 0  " + error1);

                        }
                        else{
                            newCardModelList.add(new NewCardModel(value1.getString("name"),
                                    value1.getString("priceperkg"),
                                    value1.getString("imageurl"),
                                     value1.getString("category")));
                            recyclerAdapterNewItems.notifyDataSetChanged();
//                            Log.d("abclist",value1.getString("name")+" "+value1.getString("price")+" "+value1.getString("imageurl")+" "+value1.getString("category"));
                        }

                    });

                }
            }
        });

        recyclerAdapterNewItems = new RecyclerAdapterNewItems(getContext(),newCardModelList);
        recyclerView.setAdapter(recyclerAdapterNewItems);
    }
}
