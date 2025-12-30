package com.example.bookcare_qy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PointTransactionAdapter extends RecyclerView.Adapter<PointTransactionAdapter.TransactionViewHolder> {

    private final List<PointTransaction> transactionList;

    public PointTransactionAdapter(List<PointTransaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_point_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        PointTransaction transaction = transactionList.get(position);
        holder.tvTransactionType.setText(transaction.getType());
        holder.tvTransactionPoints.setText(String.format(Locale.getDefault(), "+%d", transaction.getPoints()));
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        holder.tvTransactionDate.setText(sdf.format(transaction.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTransactionType;
        TextView tvTransactionDate;
        TextView tvTransactionPoints;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTransactionType = itemView.findViewById(R.id.tvTransactionType);
            tvTransactionDate = itemView.findViewById(R.id.tvTransactionDate);
            tvTransactionPoints = itemView.findViewById(R.id.tvTransactionPoints);
        }
    }
}
