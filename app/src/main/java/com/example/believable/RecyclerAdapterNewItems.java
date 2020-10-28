package com.example.believable;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerAdapterNewItems extends RecyclerView.Adapter<RecyclerAdapterNewItems.viewHolder> {
    Context context;
    List<NewCardModel> newCardModelList;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public RecyclerAdapterNewItems(Context context, List<NewCardModel> newCardModelList) {
        this.context = context;
        this.newCardModelList = newCardModelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        auth = FirebaseAuth.getInstance();
        Glide.with(context).load(newCardModelList.get(position).getImageUrl()).into(holder.image);
        holder.name.setText(newCardModelList.get(position).getName());
        String p = newCardModelList.get(position).getPrice();
        holder.price.setText(p+" $/kg");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),DetailItemProfile.class);
                i.putExtra("name",newCardModelList.get(position).getName());
                i.putExtra("price",newCardModelList.get(position).getPrice());
                i.putExtra("imageurl",newCardModelList.get(position).getImageUrl());
               v.getContext().startActivity(i);
            }
        });

        holder.addtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore =FirebaseFirestore.getInstance();

                DocumentReference documentReference = firestore.collection("Users").document(auth.getCurrentUser().getUid())
                        .collection("Cart").document();
                Map<String,Object> cartItem = new HashMap<>();
                cartItem.put("name",newCardModelList.get(position).getName());
                cartItem.put("priceperkg",newCardModelList.get(position).getPrice());
                cartItem.put("imageurl",newCardModelList.get(position).getImageUrl());

                documentReference.set(cartItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent i = new Intent(v.getContext(),BaseActivity.class);
                       i.putExtra("which activity","to cart activity");
                        v.getContext().startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Not able to add into cart. Please try later", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });
    }

    @Override
    public int getItemCount() {
        return newCardModelList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder{
        TextView name,price;
        ImageView image, addtoCart;
        CardView cardView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
            addtoCart = itemView.findViewById(R.id.addtoCart);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }


}
