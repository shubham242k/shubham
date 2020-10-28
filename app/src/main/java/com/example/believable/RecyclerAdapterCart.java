package com.example.believable;

import android.content.Context;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RecyclerAdapterCart extends RecyclerView.Adapter<RecyclerAdapterCart.viewHolder> {
    Context context;
    List<CartModel> cartModelList;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    public RecyclerAdapterCart(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        auth = FirebaseAuth.getInstance();
        Glide.with(context).load(cartModelList.get(position).getImage()).into(holder.image);
        holder.name.setText(cartModelList.get(position).getName());
        holder.price.setText(cartModelList.get(position).getPrice()+" $/kg");

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Function not yet added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{

        TextView name,price;
        ImageView image, delete;
        CardView cardView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
            delete = itemView.findViewById(R.id.deleteItem);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
