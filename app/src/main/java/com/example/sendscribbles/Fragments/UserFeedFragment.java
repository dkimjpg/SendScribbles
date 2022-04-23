package com.example.sendscribbles.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sendscribbles.EndlessRecyclerViewScrollListener;
import com.example.sendscribbles.LoginActivity;
import com.example.sendscribbles.MainActivity;
import com.example.sendscribbles.Post;
import com.example.sendscribbles.R;
import com.example.sendscribbles.postsAdapter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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
    private MenuItem logOut;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
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
        setHasOptionsMenu(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());

        // objects
        rvPost = view.findViewById(R.id.rvPost);
        logOut = view.findViewById(R.id.logout);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        // declaring post object and adapter
        Posts = new ArrayList<>();
        adapter = new postsAdapter(getContext(), Posts);

        rvPost.setAdapter(adapter);
        rvPost.setLayoutManager(linearLayoutManager);

        // Set SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPost(view);
            }
        });

        // Set Infinite Scroll
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMore(totalItemsCount, view);
            }
        };
        rvPost.addOnScrollListener(endlessRecyclerViewScrollListener);

        queryPost(view);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                goLoginActivity();
                Toast.makeText(getContext(), "Log Out Success!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadMore(int offset, View view) {
        // ParseQuery object
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        // set the query
        query.include(Post.KEY_USER);
        query.addDescendingOrder(Post.KEY_CreatedAt);
        query.setSkip(offset);
        query.setLimit(3);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // error checking
                if (e != null) {
                    Log.e(TAG, "Getting More Posts Issue", e);
                    return;
                }

                Log.i(TAG,"Getting More Posts is Successful");
                /*for (Post post : posts)
                {
                    Log.i(TAG, post.getUser().getUsername() + " : post is success");
                }*/

                adapter.addAll(posts);
            }
        });
    }

    protected void queryPost(View view) {
        adapter.clear();
        // ParseQuery object
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        // set the query
        query.include(Post.KEY_USER);
        query.addDescendingOrder(Post.KEY_CreatedAt);
        query.setLimit(3);

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
                        }).setBehavior(new NoSwipeBehavior()).show();
                    }
                    Log.e(TAG, "Getting posts issue", e);
                    return;
                }

                /*for (Post post : posts)
                {
                    Log.i(TAG, post.getUser().getUsername() + " : post is success");
                }*/

                adapter.addAll(posts);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    class NoSwipeBehavior extends BaseTransientBottomBar.Behavior {

        @Override
        public boolean canSwipeDismissView(View child) {
            return false;
        }
    }

    private void goLoginActivity() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }
}