package com.example.believable;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
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
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;
    public ViewPager viewPager2;
    FirebaseFirestore firestore;
    private PageAdapterTrendingCard pageAdapterTrendingCard;
    private List<TrendingCardModel> trendingCardModelList;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;
    private MyPagerAdapter myPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);



        viewPager = view.findViewById(R.id.viewPager);
        firestore =FirebaseFirestore.getInstance();
        trendingCardDataLoading();
        autoScroll();

        tabLayout = view.findViewById(R.id.tab_bar);
        viewPager2 = view.findViewById(R.id.viewPager2);
        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        tabLayout.setupWithViewPager(viewPager2);
        viewPager2.setAdapter(myPagerAdapter);


        return view;
    }

    public void trendingCardDataLoading(){

        trendingCardModelList = new ArrayList<>();

        CollectionReference collectionReference = firestore.collection("Store");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.d("Exception Failed", "onEvent: 0  " + error);

                }
                else{
                    for (QueryDocumentSnapshot snapshot : value){
                        DocumentReference documentReference = firestore.collection("Store").document(snapshot.getId());
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error!=null){
                                    Log.d("Exception Failed", "onEvent: 0  " + error);

                                }
                                else{
                                    trendingCardModelList.add(new TrendingCardModel(value.getString("name"),
                                            value.getString("priceperkg"),
                                            value.getString("imageurl")));
                                    pageAdapterTrendingCard.notifyDataSetChanged();
                                }

                            }
                        });

                    }
                }
            }
        });

        pageAdapterTrendingCard = new PageAdapterTrendingCard(getContext(),trendingCardModelList);
        viewPager.setAdapter(pageAdapterTrendingCard);
        viewPager.setPadding(150,0,150,0);


    }

    public void autoScroll(){
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {

                if (currentPage == trendingCardModelList.size()-1) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }
}
