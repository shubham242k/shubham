package com.example.believable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;



public class MyPagerAdapter extends FragmentPagerAdapter {

    CategoryTabFragment categoryTabFragment;
    NewItemTabFragment newItemTabFragment,itemTabFragment;
    BudgetTabFragment budgetTabFragment;

    String[] tabName = {"Category", "New arrrival","Budget friendly"};

    public MyPagerAdapter(@NonNull FragmentManager fm)
    {
        super(fm);
        categoryTabFragment = new CategoryTabFragment();
        newItemTabFragment = new NewItemTabFragment();
        itemTabFragment = new NewItemTabFragment();

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return categoryTabFragment;
            case 1:
                return newItemTabFragment;
            case 2:
                return itemTabFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabName.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabName[position];
    }
}

