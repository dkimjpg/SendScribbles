package com.example.sendscribbles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class SimpleDrawingView extends View {
    // setup initial color
    private final int paintColor = Color.BLACK;
    // defines paint and canvas
    private Paint drawPaint;
    // stores next circle
    private Path path = new Path();
    private Canvas c;
    public static Bitmap drawing;
    private ArrayList<Path> mPath = new ArrayList<Path>();
    private ArrayList<Paint> mPaint = new ArrayList<Paint>();
    private ArrayList<Path> undoPath = new ArrayList<Path>();
    private ArrayList<Paint> undoPaint = new ArrayList<Paint>();

    public SimpleDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawing = Bitmap.createBitmap(1080,1100, Bitmap.Config.ARGB_8888);
        c = new Canvas(drawing);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    // Setup paint with color and stroke styles
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas c) {
        c.drawBitmap(drawing,0,0,new Paint(Paint.ANTI_ALIAS_FLAG));

        int i = 0;
        for (Path path : mPath){
            c.drawPath(path, mPaint.get(i));
            i++;
        }

        c.drawPath(path,drawPaint);

    }

    private void touchup(float x, float y){
        path.lineTo(x,y);
        //c.drawPath(path,drawPaint);
        mPath.add(path);
        mPaint.add(drawPaint);
        path = new Path();
        setupPaint();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();
        // Checks for the event that occurs
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                // draw while move on the cursor
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY);
                break;
            case MotionEvent.ACTION_UP:
                touchup(pointX, pointY);
                break;
            default:
                return false;
        }
        // Force a view to draw again
        postInvalidate();
        return true;
    }

    public Bitmap getDrawing(){
        int i = 0;
        for (Path path : mPath){
            c.drawPath(path, mPaint.get(i));
            i++;
        }
        return drawing;
    }

    public void clear(){
        mPath.clear();
        mPaint.clear();
        c.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void undo(){
        if (mPath.size() > 0){
            undoPath.add(mPath.remove(mPath.size() - 1));
            undoPaint.add(mPaint.remove(mPaint.size() - 1));
        }
        invalidate();
    }

    public void redo(){
        if (undoPath.size() > 0){
            mPath.add(undoPath.remove(undoPath.size() - 1));
            mPaint.add(undoPaint.remove(undoPaint.size() - 1));
        }
        invalidate();
    }

}
