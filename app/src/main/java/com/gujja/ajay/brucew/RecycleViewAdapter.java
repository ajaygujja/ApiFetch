package com.gujja.ajay.brucew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gujja.ajay.brucew.model.Repo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    Context context;
    List<Repo> arrayList;

    public RecycleViewAdapter( Context context, List<Repo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.ViewHolder holder, int position) {
        Repo repo = arrayList.get(position);

        holder.loginName.setText(repo.getLogin());
        holder.type.setText(repo.getType());
        Glide.with(context).load(repo.getAvatar()).into(holder.avatarImageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView loginName, type ;
        public ImageView avatarImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            loginName = itemView.findViewById(R.id.login_name);
            type = itemView.findViewById(R.id.type_);
            avatarImageView = itemView.findViewById(R.id.image_view);
        }
    }
}