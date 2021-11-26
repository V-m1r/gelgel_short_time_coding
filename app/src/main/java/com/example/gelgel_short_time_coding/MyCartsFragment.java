package com.example.gelgel_short_time_coding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gelgel_short_time_coding.activities.PlacedOrderActivity;
import com.example.gelgel_short_time_coding.adapters.MyCartAdapter;
import com.example.gelgel_short_time_coding.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyCartsFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth auth;

    TextView overTotalAmount;

    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelsList;

    Button buyNow;

    ProgressBar progressBar;

    public MyCartsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_carts, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        progressBar = root.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        buyNow = root.findViewById(R.id.buy_now);

        overTotalAmount = root.findViewById(R.id.textView7);


        cartModelsList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(), cartModelsList);
        recyclerView.setAdapter(cartAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                        String documentId = documentSnapshot.getId();

                        MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);

                        cartModel.setDocumentId(documentId);

                        cartModelsList.add(cartModel);
                        cartAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    calculateTotalAmount(cartModelsList);

                }
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PlacedOrderActivity.class);
                intent.putExtra("itemList", (Serializable) cartModelsList);
                startActivity(intent);
            }
        });

        return root;
    }

    private void calculateTotalAmount(List<MyCartModel> cartModelsList) {

        double totalAmount = 0.0;

        for (MyCartModel myCartModel : cartModelsList){

            totalAmount += myCartModel.getTotalPrice();

        }

        overTotalAmount.setText("Total Amount : " + totalAmount + " Manat");

    }

}