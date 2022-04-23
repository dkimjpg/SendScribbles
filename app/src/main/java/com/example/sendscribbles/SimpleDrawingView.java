package com.example.sendscribbles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SimpleDrawingView extends View {
    // setup initial color
    private final int paintColor = Color.BLACK;
    // defines paint and canvas
    private Paint drawPaint;
    // stores next circle
    private Path path = new Path();
    private Canvas c;
    public static Bitmap drawing;

    public SimpleDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawing = Bitmap.createBitmap(1080, 1080, Bitmap.Config.ARGB_8888);
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
        c.drawPath(path,drawPaint);

    }

    private void touchup(float x, float y){
        path.lineTo(x,y);
        c.drawPath(path,drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();
        // Checks for the event that occurs
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY);
                break;
            case MotionEvent.ACTION_UP:
                touchup(event.getX(), event.getY());
            default:
                return false;
        }
        // Force a view to draw again
        postInvalidate();
        return true;
    }

    public Bitmap getDrawing(){
        return drawing;
    }


}
