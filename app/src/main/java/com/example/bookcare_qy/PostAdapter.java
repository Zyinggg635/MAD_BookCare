package com.example.bookcare_qy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    public interface OnPostInteractionListener {
        void onLikeClicked(ForumPost post, int position);
        void onCommentClicked(ForumPost post);
    }

    private final List<ForumPost> postsList;
    private final OnPostInteractionListener listener;
    private final FirebaseUser currentUser;

    public PostAdapter(List<ForumPost> postsList, OnPostInteractionListener listener) {
        this.postsList = postsList;
        this.listener = listener;
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        ForumPost post = postsList.get(position);
        Context context = holder.itemView.getContext();

        holder.ivProfilePic.setImageResource(R.drawable.ic_account_circle);
        holder.tvPosterName.setText(post.getPosterName());
        holder.tvPostType.setText(post.getPostType());
        holder.tvTitle.setText(post.getTitle());
        holder.tvMessage.setText(post.getMessage());
        holder.tvUpvoteCount.setText(String.valueOf(post.getLikeCount()));
        holder.tvCommentCount.setText(String.valueOf(post.getCommentCount()));

        if (post.getTimestamp() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            holder.tvTimestamp.setText(sdf.format(post.getTimestamp()));
        } else {
            holder.tvTimestamp.setText("");
        }

        // Definitive check for the like state
        if (currentUser != null && post.getLikes().containsKey(currentUser.getUid())) {
            holder.ivLike.setImageResource(R.drawable.ic_like_filled);
            holder.ivLike.setColorFilter(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        } else {
            holder.ivLike.setImageResource(R.drawable.ic_like_outline);
            holder.ivLike.setColorFilter(ContextCompat.getColor(context, android.R.color.darker_gray));
        }

        holder.likeLayout.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLikeClicked(post, position);
            }
        });

        holder.commentLayout.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCommentClicked(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public void updateLikeStatus(int position, boolean isLiked) {
        ForumPost post = postsList.get(position);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        if (isLiked) {
            post.getLikes().put(user.getUid(), true);
        } else {
            post.getLikes().remove(user.getUid());
        }
        notifyItemChanged(position);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPosterName, tvPostType, tvTitle, tvMessage, tvTimestamp, tvUpvoteCount, tvCommentCount;
        ImageView ivLike, ivProfilePic;
        LinearLayout likeLayout, commentLayout;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvPosterName = itemView.findViewById(R.id.tvPosterName);
            tvPostType = itemView.findViewById(R.id.tvPostType);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvUpvoteCount = itemView.findViewById(R.id.tvUpvoteCount);
            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
            ivLike = itemView.findViewById(R.id.ivLike);
            likeLayout = itemView.findViewById(R.id.likeLayout);
            commentLayout = itemView.findViewById(R.id.commentLayout);
        }
    }
}
