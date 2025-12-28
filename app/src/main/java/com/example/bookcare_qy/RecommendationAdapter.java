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

    public RecommendationAdapter(List<Book> recommendations) {
        this.recommendations = recommendations;
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
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.genre.setText(book.getGenre());
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
    }
}
