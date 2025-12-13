package com.example.bookcare_qy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeBookAdapter extends RecyclerView.Adapter<HomeBookAdapter.BookHolder> {

    private final List<Book> books = new ArrayList<>();
    private final Fragment fragment;

    public HomeBookAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void submitList(List<Book> newBooks) {
        books.clear();
        books.addAll(newBooks);
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

        // The getUploadedBy() method no longer exists in the new Book model.
        // We will hide the username field for now.
        holder.username.setVisibility(View.GONE);

        holder.statusChip.setText(book.getType());
        boolean isDonate = "Donate".equalsIgnoreCase(book.getType());
        int color = ContextCompat.getColor(holder.statusChip.getContext(),
                isDonate ? android.R.color.holo_red_dark : android.R.color.holo_green_dark);
        holder.statusChip.setTextColor(color);

        // Set click listener to navigate to detail view
        holder.card.setOnClickListener(v -> {
            if (fragment != null && fragment.getParentFragmentManager() != null) {
                fragment.getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, ViewBookDetailFragment.newInstance(book))
                        .addToBackStack(null)
                        .commit();
            }
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
