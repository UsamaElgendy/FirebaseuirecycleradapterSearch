package com.example.firebaseuirecycleradapter.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseuirecycleradapter.R;
import com.example.firebaseuirecycleradapter.model.Posts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyAdapterViewHolder> {

    public Context context;
    public ArrayList<Posts> postsArrayList;

    public PostsAdapter(Context context, ArrayList<Posts> postsArrayList) {
        this.context = context;
        this.postsArrayList = postsArrayList;
    }

    @NonNull
    @Override
    public MyAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_posts_item, parent, false);

        return new MyAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterViewHolder holder, int position) {
        Posts posts = postsArrayList.get(position);
        holder.textView.setText(posts.getName());
        Picasso.get().load(posts.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;

        public MyAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.custom_post_textView);
            imageView = itemView.findViewById(R.id.custom_post_imageView);

        }
    }
}
