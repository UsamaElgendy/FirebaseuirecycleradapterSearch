package com.example.firebaseuirecycleradapter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseuirecycleradapter.adapter.PostsAdapter;
import com.example.firebaseuirecycleradapter.viewHolder.PostsViewHolder;
import com.example.firebaseuirecycleradapter.model.Posts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.search_ET)
    EditText searchET;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private DatabaseReference postRef;
    private FirebaseRecyclerAdapter<Posts, PostsViewHolder> adapter;
    private FirebaseRecyclerOptions<Posts> options;

    private ArrayList<Posts> postsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        postsArrayList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);


        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    searchPosts(s.toString());
                } else {
                    fillRecyclerViewData();
                }
            }
        });


        fillRecyclerViewData();

    }

    private void searchPosts(String searchString) {
        Query query = postRef.orderByChild("name")
                .startAt(searchString)
                .endAt(searchString + "/uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    postsArrayList.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        final Posts posts = data.getValue(Posts.class);
                        postsArrayList.add(posts);
                    }

                    PostsAdapter postsAdapter = new PostsAdapter(getApplicationContext(), postsArrayList);
                    recyclerView.setAdapter(postsAdapter);
                    postsAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fillRecyclerViewData() {

        options = new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(postRef, Posts.class).build();

        adapter = new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostsViewHolder holder, int i, @NonNull Posts model) {
                Picasso.get().load(model.getImage()).into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                holder.textView.setText(model.getName());
            }

            @NonNull
            @Override
            public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_posts_item, parent, false);
                return new PostsViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }
}
