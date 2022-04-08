package com.example.sendscribbles.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sendscribbles.Post;
import com.example.sendscribbles.R;
import com.example.sendscribbles.postsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UserFeedFragment extends Fragment {

    public static final String TAG = "UserFeed Fragment";
    private RecyclerView rvPost;
    protected List<Post> Posts;
    protected postsAdapter adapter;

    // empty constructor
    public UserFeedFragment() {

    }

    // Inflate the layout of this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // objects
        rvPost = view.findViewById(R.id.rvPost);

        // declaring post object and adapter
        Posts = new ArrayList<>();
        adapter = new postsAdapter(getContext(), Posts);

        rvPost.setAdapter(adapter);
        rvPost.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPost();
    }

    protected void queryPost() {
        // ParseQuery object
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        // set the query
        query.include(Post.KEY_USER);
        query.addDescendingOrder(Post.KEY_CreatedAt);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // error checking
                if (e != null) {
                    Log.e(TAG, "Getting posts issue", e);
                    return;
                }

                /*for (Post post : posts)
                {
                    Log.i(TAG, post.getUser().getUsername() + " : post is success");
                }*/

                Posts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}