package com.example.sendscribbles.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sendscribbles.LoginActivity;
import com.example.sendscribbles.Post;
import com.example.sendscribbles.R;
import com.example.sendscribbles.postsAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends UserFeedFragment {

    // empty constructor
    public ProfileFragment() {

    }


    @Override
    protected void queryPost(View view) {
        adapter.clear();
        // ParseQuery object
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        // set the query
        query.include(Post.KEY_USER);
        query.addDescendingOrder(Post.KEY_CreatedAt);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // error checking
                if (e != null) {
                    if(e.getCode() == ParseException.CONNECTION_FAILED){
                        Snackbar.make(view,"Connection Timed Out. Check your Internet", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                queryPost(view);
                            }
                        }).show();
                    }
                    Log.e(TAG, "Getting posts issue", e);
                    return;
                }

                for (Post post : posts)
                {
                    Log.i(TAG, post.getUser().getUsername() + " : post is success");
                }

                adapter.addAll(posts);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}