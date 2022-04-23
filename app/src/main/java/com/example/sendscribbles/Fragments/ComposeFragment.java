package com.example.sendscribbles.Fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sendscribbles.FingerPath;
import com.example.sendscribbles.Post;
import com.example.sendscribbles.R;
import com.example.sendscribbles.SimpleDrawingView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import androidx.annotation.Nullable;


public class ComposeFragment extends Fragment {

    public static final String TAG = "Compose Fragment";
    private Button undoBtn;
    private Button redoBtn;
    private EditText etDescription;
    private Button btnSubmit;
    private SimpleDrawingView simpleDrawingView;
    private File photoFile;
    private String drawingFileName = "drawing.png";

    public ComposeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //CanvasLayout layout = new CanvasLayout(getActivity());
        //layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

        return inflater.inflate(R.layout.fragment_compose, container, false); //Might need to edit this code <---------
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleDrawingView = view.findViewById(R.id.simpleDrawingView);
        etDescription = view.findViewById(R.id.etDescription);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        undoBtn = view.findViewById(R.id.undoBtn);
        redoBtn = view.findViewById(R.id.redoBtn);

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

                photoFile = getPhotoFileUri(drawingFileName);
                FileOutputStream fout = null;
                try {
                    fout = new FileOutputStream(photoFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e(TAG,"Something went wrong!",e);
                }
                Bitmap drawn = simpleDrawingView.getDrawing();
                drawn.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, fout);

                if(photoFile == null){
                    Toast.makeText(getContext(), "There is no image", Toast.LENGTH_SHORT).show();
                }

                ParseUser currentUser = ParseUser.getCurrentUser();

                // Lack the drawing (TO DO)
                savePost(currentUser, content, photoFile);
            }
        });

        // undo button
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleDrawingView.undo();
            }
        });

        // redo button
        redoBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                simpleDrawingView.redo();
            }
        });
    }

    private void savePost(ParseUser currentUser, String content, File photoFile)
    {
        // ----- TO DO ------ Need to implement things for the drawing


        // Declare post object
        Post post = new Post();

        // setting information
        post.setUser(currentUser);
        post.setKeyDescription(content);
        post.setImage(new ParseFile(photoFile));

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
                simpleDrawingView.clear();
            }
        });
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);


    }

}