package com.example.believable;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class CategoryTabFragment extends Fragment implements View.OnClickListener {
   String clickedCategory;
   CardView fruitCategory,vegetableCategory,grainCategory,masalaCategory,
           snacksCategory,packageCategory,milkCategory,stapleCategory,
           beverageCategory;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_tab_fragment,container,false);

        fruitCategory = view.findViewById(R.id.fruitCategory);
        vegetableCategory = view.findViewById(R.id.vegetableCategory);
        grainCategory = view.findViewById(R.id.grainCategory);
        masalaCategory = view.findViewById(R.id.masalaCategory);
        snacksCategory = view.findViewById(R.id.snacksCategory);
        stapleCategory = view.findViewById(R.id.stapleCategory);
        milkCategory = view.findViewById(R.id.milkCategory);
        packageCategory = view.findViewById(R.id.packageCategory);
        beverageCategory = view.findViewById(R.id.beverageCategory);


        fruitCategory.setOnClickListener(this);
        vegetableCategory.setOnClickListener(this);
        grainCategory.setOnClickListener(this);
        masalaCategory.setOnClickListener(this);
        snacksCategory.setOnClickListener(this);
        stapleCategory.setOnClickListener(this);
        milkCategory.setOnClickListener(this);
        packageCategory.setOnClickListener(this);
        beverageCategory.setOnClickListener(this);
        return  view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fruitCategory:
                clickedCategory = "fruits";
                break;
            case R.id.vegetableCategory:
                clickedCategory = "vegetables";
                break;
            case R.id.grainCategory:
                clickedCategory = "grains";
                break;
            case R.id.masalaCategory:
                clickedCategory = "masalas";
                break;
            case R.id.snacksCategory:
                clickedCategory = "snacks";
                break;
            case R.id.packageCategory:
                clickedCategory = "package";
                break;
            case R.id.milkCategory:
                clickedCategory = "milk product";
                break;
            case R.id.stapleCategory:
                clickedCategory = "staple";
                break;
            case R.id.beverageCategory:
                clickedCategory = "beverages";
                break;
        }

        Intent i =  new Intent(getContext(),ExtendedCategoryPage.class);
        i.putExtra("category",clickedCategory);
        startActivity(i);
    }
}
