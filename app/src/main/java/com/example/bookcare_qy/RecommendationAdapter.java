package com.example.bookcare_qy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {

    private List<Book> recommendations;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public RecommendationAdapter(List<Book> recommendations, OnItemClickListener listener) {
        this.recommendations = recommendations;
        this.listener = listener;
    }

    public void submitList(List<Book> newRecommendations) {
        this.recommendations.clear();
        this.recommendations.addAll(newRecommendations);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendation_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = recommendations.get(position);
        holder.bind(book, listener);
    }

    @Override
    public int getItemCount() {
        return recommendations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView author;
        TextView genre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewBookTitle);
            author = itemView.findViewById(R.id.textViewBookAuthor);
            genre = itemView.findViewById(R.id.textViewBookGenre);
        }

        public void bind(final Book book, final OnItemClickListener listener) {
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
            genre.setText(book.getGenre());
            itemView.setOnClickListener(v -> listener.onItemClick(book));
        }
    }
}
