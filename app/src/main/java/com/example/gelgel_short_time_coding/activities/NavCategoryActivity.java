package com.example.gelgel_short_time_coding.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gelgel_short_time_coding.R;
import com.example.gelgel_short_time_coding.adapters.NavCategoryDetailedAdapter;
import com.example.gelgel_short_time_coding.models.HomeCategory;
import com.example.gelgel_short_time_coding.models.NavCategoryDetailedModels;
import com.example.gelgel_short_time_coding.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NavCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<NavCategoryDetailedModels> list;
    NavCategoryDetailedAdapter adapter;
    FirebaseFirestore db;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_category);

        db = FirebaseFirestore.getInstance();

        String type = getIntent().getStringExtra("type");

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.nav_cat_det_rec);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new NavCategoryDetailedAdapter(this, list);
        recyclerView.setAdapter(adapter);

        //////Getting drink
        if (type != null && type.equalsIgnoreCase("drink")){
            db.collection("NavCategoryDetailed").whereEqualTo("type", "drink").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        NavCategoryDetailedModels navCategoryDetailedModels = documentSnapshot.toObject(NavCategoryDetailedModels.class);
                        list.add(navCategoryDetailedModels);
                        adapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                }
            });
        }

        //////Getting meat
        if (type != null && type.equalsIgnoreCase("meat")){
            db.collection("NavCategoryDetailed").whereEqualTo("type", "meat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        NavCategoryDetailedModels navCategoryDetailedModels = documentSnapshot.toObject(NavCategoryDetailedModels.class);
                        list.add(navCategoryDetailedModels);
                        adapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                }
            });
        }

        //////Getting breakfast
        if (type != null && type.equalsIgnoreCase("breakfast")){
            db.collection("NavCategoryDetailed").whereEqualTo("type", "breakfast").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        NavCategoryDetailedModels navCategoryDetailedModels = documentSnapshot.toObject(NavCategoryDetailedModels.class);
                        list.add(navCategoryDetailedModels);
                        adapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                }
            });
        }

        //////Getting fruit
        if (type != null && type.equalsIgnoreCase("fruit")){
            db.collection("NavCategoryDetailed").whereEqualTo("type", "fruit").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        NavCategoryDetailedModels navCategoryDetailedModels = documentSnapshot.toObject(NavCategoryDetailedModels.class);
                        list.add(navCategoryDetailedModels);
                        adapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                }
            });
        }

    }
}