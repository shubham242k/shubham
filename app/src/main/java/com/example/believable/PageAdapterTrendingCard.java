package com.example.believable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

public class PageAdapterTrendingCard extends PagerAdapter {

    private Context context;
    private List<TrendingCardModel> trendingCardModelArrayList;

    public PageAdapterTrendingCard(Context context, List<TrendingCardModel> trendingCardModelArrayList) {
        this.context = context;
        this.trendingCardModelArrayList = trendingCardModelArrayList;
    }

    @Override
    public int getCount() {
        if (trendingCardModelArrayList == null) {
            return 0;
        }
        return trendingCardModelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.trending_card_item,container,false);
        ImageView backgroundImage = view.findViewById(R.id.backgroundImage);
        final TextView name = view.findViewById(R.id.name);
        TextView price = view.findViewById(R.id.price);

        TrendingCardModel trendingCardModel = trendingCardModelArrayList.get(position);
        final String itemname = trendingCardModel.getName();
        final String itemprice = trendingCardModel.getPrice();
        String imageUrl = trendingCardModel.getBackgroundImageUrl();


        Glide.with(context).load(imageUrl).into(backgroundImage);
        name.setText(itemname);
        price.setText(itemprice+"$/kg");



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Toast.makeText(context, position+itemname+itemprice, Toast.LENGTH_SHORT).show();

            }
        });

        
        container.addView(view,0);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);

    }
}
