package com.example.bookcare_qy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EcoPointsHistoryFragment extends Fragment {

    private PointTransactionAdapter transactionAdapter;
    private List<PointTransaction> transactionList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eco_points_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvHistory = view.findViewById(R.id.rvHistory);
        transactionAdapter = new PointTransactionAdapter(transactionList);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistory.setAdapter(transactionAdapter);

        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // Show dummy history instead of fetching from Firebase
        showDummyHistory();
    }

    private void showDummyHistory() {
        transactionList.clear();
        // Add a mix of exchange and donation transactions
        transactionList.add(new PointTransaction("user123", 10, "Donation", new Date()));
        transactionList.add(new PointTransaction("user123", 5, "Exchange", new Date()));
        transactionList.add(new PointTransaction("user123", 10, "Donation", new Date()));
        transactionList.add(new PointTransaction("user123", 10, "Donation", new Date()));
        transactionList.add(new PointTransaction("user123", 5, "Exchange", new Date()));
        transactionList.add(new PointTransaction("user123", 5, "Exchange", new Date()));
        transactionList.add(new PointTransaction("user123", 10, "Donation", new Date()));
        transactionList.add(new PointTransaction("user123", 5, "Exchange", new Date()));
        transactionList.add(new PointTransaction("user123", 10, "Donation", new Date()));
        transactionList.add(new PointTransaction("user12_qy3", 5, "Exchange", new Date()));
        transactionAdapter.notifyDataSetChanged();
    }
}
