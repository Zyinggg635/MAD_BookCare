package com.example.bookcare_qy;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder> {

    private final List<Badge> badgeList;
    private int userPoints = 0;

    public BadgeAdapter(List<Badge> badgeList) {
        this.badgeList = badgeList;
    }

    public void updateUserPoints(int points) {
        this.userPoints = points;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BadgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_badge, parent, false);
        return new BadgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeViewHolder holder, int position) {
        Badge badge = badgeList.get(position);
        holder.tvBadgeName.setText(badge.getName());
        holder.ivBadgeIcon.setImageResource(R.drawable.ic_badge);

        // Gray out the badge if the user hasn't earned it yet
        if (userPoints < badge.getPointsRequired()) {
            holder.itemView.setAlpha(0.5f);
            holder.ivBadgeIcon.setColorFilter(Color.GRAY);
        } else {
            holder.itemView.setAlpha(1.0f);
            holder.ivBadgeIcon.clearColorFilter();
        }
    }

    @Override
    public int getItemCount() {
        return badgeList.size();
    }

    public static class BadgeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBadgeIcon;
        TextView tvBadgeName;

        public BadgeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBadgeIcon = itemView.findViewById(R.id.ivBadgeIcon);
            tvBadgeName = itemView.findViewById(R.id.tvBadgeName);
        }
    }
}
