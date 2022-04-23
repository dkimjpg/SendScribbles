package com.example.sendscribbles.Fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import androidx.annotation.Nullable;


public class ComposeFragment extends Fragment {

    public static final String TAG = "Compose Fragment";
    private ImageView ivDraw;
    private EditText etDescription;
    private Button btnSubmit;
    private View simpleDrawingView;

    public int BRUSH_SIZE = 10;
    public static final int COLOR_PEN = Color.RED;
    public static final int COLOR_ERASER = Color.WHITE;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUUCH_TOLERANCE = 4;

    private float mX,mY;
    private Path mPath;
    private Paint mPaint;
    private int currentColor;
    private ArrayList<FingerPath> paths = new ArrayList<>();

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    public ComposeFragment() {
        // Required empty public constructor
    }

    /*
    public class CanvasLayout extends LinearLayout
    {
        public CanvasLayout(Context context) {
            super(context);
        }

        public class PaintView extends View
        {
            public PaintView(Context context) {
                super(context);
            }

            public PaintView(Context context, @Nullable AttributeSet attrs) {
                super(context, attrs);

                mPaint = new Paint();
                mPaint.setAntiAlias(true);
                mPaint.setDither(true);
                mPaint.setColor(COLOR_PEN);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeJoin(Paint.Join.ROUND);
                mPaint.setStrokeCap(Paint.Cap.ROUND);
                mPaint.setXfermode(null);
                mPaint.setAlpha(0xff);
            }
            public void init(DisplayMetrics metrics){
                int height = metrics.heightPixels;
                int width = metrics.widthPixels;

                mBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
                mCanvas = new Canvas(mBitmap);

                currentColor = COLOR_PEN;

            }
        }

        //May have to implement the following code differently
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(COLOR_PEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);
        //May have to implement the code above differently



        public void pen(){
            currentColor = COLOR_PEN;
        }
        public void eraser(){
            currentColor = COLOR_ERASER;
        }
        public void clear(){
            paths.clear();
            pen();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.save();
            mCanvas.drawColor(DEFAULT_BG_COLOR);

            for(FingerPath fp: paths){
                mPaint.setColor(fp.getColor());
                mPaint.setStrokeWidth(fp.getStrokeWidth());
                mPaint.setMaskFilter(null);

                mCanvas.drawPath(fp.getPath(),mPaint);

            }
            canvas.drawBitmap(mBitmap,0,0,mBitmapPaint);
            canvas.restore();
        }

        private void touchStart(float x,float y){
            mPath = new Path();
            FingerPath fp = new FingerPath(currentColor,BRUSH_SIZE,mPath);
            paths.add(fp);

            mPath.reset();
            mPath.moveTo(x,y);
            mX = x;
            mY = y;
        }
        private void touchMove(float x,float y){
            float dx = Math.abs(x-mX);
            float dy = Math.abs(y-mY);

            if (dx>=TOUUCH_TOLERANCE || dy>=TOUUCH_TOLERANCE){
                mPath.quadTo(mX,mY,(x+mX)/2,(y+mY)/2);
                mX = x;
                mY = y;

            }
        }

        private void touchUp(){
            mPath.lineTo(mX,mY);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    touchStart(x,y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchMove(x,y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touchUp();
                    invalidate();
                    break;
            }
            return true;
        }

    } */

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

        // objects
        //ivDraw = view.findViewById(R.id.ivDraw);
        simpleDrawingView = view.findViewById(R.id.simpleDrawingView);
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