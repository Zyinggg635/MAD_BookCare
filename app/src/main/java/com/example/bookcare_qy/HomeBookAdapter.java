package com.example.bookcare_qy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeBookAdapter extends RecyclerView.Adapter<HomeBookAdapter.BookHolder> {

    private List<Book> books;

    public HomeBookAdapter(List<Book> books) {
        this.books = new ArrayList<>(books);
    }

    public void updateBooks(List<Book> newBooks) {
        this.books.clear();
        this.books.addAll(newBooks);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_book, parent, false);
        return new BookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        Book book = books.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());

        holder.username.setVisibility(View.GONE);

        holder.statusChip.setText(book.getListingType());
        boolean isDonate = "Donation".equalsIgnoreCase(book.getListingType());
        int color = ContextCompat.getColor(holder.statusChip.getContext(),
                isDonate ? android.R.color.holo_red_dark : android.R.color.holo_green_dark);
        holder.statusChip.setTextColor(color);

        holder.card.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("book", book);
            Navigation.findNavController(v).navigate(R.id.action_navigation_explore_to_viewBookDetailFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView author;
        final TextView username;
        final TextView statusChip;
        final CardView card;

        BookHolder(@NonNull View itemView) {
            super(itemView);
            card = (CardView) itemView;
            title = itemView.findViewById(R.id.tvHomeBookTitle);
            author = itemView.findViewById(R.id.tvHomeBookAuthor);
            username = itemView.findViewById(R.id.tvHomeBookUsername);
            statusChip = itemView.findViewById(R.id.tvHomeBookStatus);
        }
    }
}
