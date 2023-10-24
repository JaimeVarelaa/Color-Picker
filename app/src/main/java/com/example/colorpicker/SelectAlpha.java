package com.example.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SelectAlpha extends View {
    private Paint checkerPaint;
    private Paint gradientPaint;
    private Paint indicatorPaint;
    private int alphaLevel = 255;
    private float indicatorPosition;
    private int initialRed;
    private int initialGreen;
    private int initialBlue;
    private OnSelectedColorListener listener;
    private OnSelectedAlphaListener selectedAlphaListener;
    public void setOnSelectedColorListener(OnSelectedColorListener l){
        listener = l;
    }
    public void setOnSelectedAlphaListener(OnSelectedAlphaListener li){
        selectedAlphaListener = li;
    }

    public SelectAlpha(Context context){
        super(context);
        init();
    }
    public SelectAlpha(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.selectColor);
        initialRed = a.getInt(R.styleable.selectColor_red, 255);
        initialGreen = a.getInt(R.styleable.selectColor_green, 0);
        initialBlue = a.getInt(R.styleable.selectColor_blue, 0);
        a.recycle();
        init();
    }

    private void init() {
        checkerPaint = new Paint();
        gradientPaint = new Paint();
        alphaLevel = 255;
        indicatorPaint = new Paint();
        indicatorPaint.setColor(Color.WHITE);
        indicatorPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        int cellSize = width / 10;
        for (int i = 0; i < width; i += cellSize) {
            for (int j = 0; j < height; j += cellSize) {
                if ((i / cellSize + j / cellSize) % 2 == 0) {
                    checkerPaint.setColor(Color.WHITE);
                } else {
                    checkerPaint.setColor(Color.BLACK);
                }
                Rect rect = new Rect(i, j, i + cellSize, j + cellSize);
                canvas.drawRect(rect, checkerPaint);
            }
        }

        int barHeight = height / 4;
        Rect gradientRect = new Rect(0, 0, width, height);
        gradientPaint.setShader(new LinearGradient(0, 0, 0, height, Color.rgb(initialRed, initialGreen, initialBlue), 0x00FF0000, Shader.TileMode.CLAMP));
        gradientPaint.setAlpha(255);
        canvas.drawRect(gradientRect, gradientPaint);
        float indicatorHeight = 10;
        canvas.drawRect(0, indicatorPosition - 5, width, indicatorPosition + 5, indicatorPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            float y = event.getY();
            indicatorPosition = y;
            alphaLevel = (int) (255 * (1 - y / getHeight()));
            if (alphaLevel < 0) {
                alphaLevel = 0;
            } else if (alphaLevel > 255) {
                alphaLevel = 255;
            }
            if(selectedAlphaListener != null) {
                selectedAlphaListener.onselectedAlpha(alphaLevel);
            }
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void getrgb(int red, int green, int blue){
        initialRed = red;
        initialGreen = green;
        initialBlue = blue;
        invalidate();
    }
}