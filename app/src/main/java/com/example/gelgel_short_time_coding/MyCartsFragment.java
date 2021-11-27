package com.example.gelgel_short_time_coding;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gelgel_short_time_coding.activities.PlacedOrderActivity;
import com.example.gelgel_short_time_coding.adapters.MyCartAdapter;
import com.example.gelgel_short_time_coding.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyCartsFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth auth;

    TextView overTotalAmount;

    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelsList;

    Button buyNow;
    StringBuilder sb = new StringBuilder();
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
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {

                        String documentId = documentSnapshot.getId();

                        MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);
                        sb.append(documentId + "\n" + cartModel.toString());
                        cartModel.setDocumentId(documentId);

                        cartModelsList.add(cartModel);
                        cartAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    calculateTotalAmount(cartModelsList);
                    sb.append("\n"+overTotalAmount.getText().toString());

                }
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").
                child(Objects.requireNonNull(auth.getUid())).
                child(Objects.requireNonNull(auth.getUid())).child("addresses");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(root.getContext(), android.R.layout.select_dialog_singlechoice);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String str = snapshot1.getValue().toString();
                    adapter.add(str);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        buyNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(root.getContext());
                dialog.setContentView(R.layout.address_verification);
                AutoCompleteTextView addressTxt = dialog.findViewById(R.id.address);

                addressTxt.setAdapter(adapter);
                Button sbmAddress = dialog.findViewById(R.id.submitAddress);
                dialog.show();

                dialog.getWindow().setLayout((1000), (1005));
                sbmAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String address = addressTxt.getText().toString();
                        if (address.isEmpty()) {
                            Toast.makeText(root.getContext(), "Empty Address", Toast.LENGTH_SHORT).show();
                        } else {
                            sb.append("\nAddress: " + address);
                            ref.child(address).setValue(address);
                            JavaMailAPI javaMailAPI = new JavaMailAPI(root.getContext(), "pera121101@gmail.com", "GelGel Order", sb.toString());
                            javaMailAPI.execute();
                            Intent intent = new Intent(getContext(), PlacedOrderActivity.class);
                            dialog.dismiss();
                            intent.putExtra("itemList", (Serializable) cartModelsList);
                            startActivity(intent);
                        }
                    }
                });

            }
        });

        return root;
    }

    private void calculateTotalAmount(List<MyCartModel> cartModelsList) {

        double totalAmount = 0.0;

        for (MyCartModel myCartModel : cartModelsList) {

            totalAmount += myCartModel.getTotalPrice();

        }

        overTotalAmount.setText("Total Amount : " + totalAmount + " Manat");

    }

}