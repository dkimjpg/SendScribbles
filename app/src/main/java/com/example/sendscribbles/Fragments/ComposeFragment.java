package com.example.sendscribbles.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sendscribbles.Post;
import com.example.sendscribbles.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class ComposeFragment extends Fragment {

    public static final String TAG = "Compose Fragment";
    private ImageView ivDraw;
    private EditText etDescription;
    private Button btnSubmit;

    public ComposeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { //may or may not need to change this
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // objects
        ivDraw = view.findViewById(R.id.ivDraw);
        etDescription = view.findViewById(R.id.etDescription);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        // Draw the image
        // -------------------TO DO---------------------

        // Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view){
                // convert the content of description to a string
                String content = etDescription.getText().toString();

                // to check if the content is empty
                if (content.isEmpty())
                {
                    Toast.makeText(getContext(), "Need to write a description", Toast.LENGTH_SHORT).show();
                }

                // to check if the drawing is empty
                // ------------- TO DO -------------------

                ParseUser currentUser = ParseUser.getCurrentUser();

                // Lack the drawing (TO DO)
                savePost(currentUser, content);
            }
        });
    }

    private void savePost(ParseUser currentUser, String content)
    {
        // ----- TO DO ------ Need to implement things for the drawing

        // Declare post object
        Post post = new Post();

        // setting information
        post.setUser(currentUser);
        post.setKeyDescription(content);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                // error checking
                if (e != null)
                {
                    Log.e(TAG, "Saving posts issue", e);
                    Toast.makeText(getContext(), "Errors with saving posts", Toast.LENGTH_SHORT).show();
                    return;
                }

                // post is successfully saved
                Log.i(TAG, "Post is saved successfully");
                // empty description box
                etDescription.setText("");
            }
        });
    }
}