package com.example.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;

public class SelectColorVar extends View {
    private int selectedColor;
    private Paint rectanglePaint;
    private Paint textPaint;
    private float selectedPosition = 320f;
    private Paint indicatorPaint;
    private OnSelectedColorListener listener;

    private OnColorUpdateListener colorUpdateListener;
    private int initialRed;
    private int initialGreen;
    private int initialBlue;
    private float y;
    private int height;
    public void setOnSelectedColorListener(OnSelectedColorListener l){
        listener = l;
    }

    public void setOnColorUpdateListener(OnColorUpdateListener li) {
        colorUpdateListener = li;
    }

    public SelectColorVar(Context context){
        super(context);
        init();
    }
    public SelectColorVar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.selectColor);
        initialRed = a.getInt(R.styleable.selectColor_red, 255);
        initialGreen = a.getInt(R.styleable.selectColor_green, 0);
        initialBlue = a.getInt(R.styleable.selectColor_blue, 0);
        a.recycle();
        init();
    }

    private void init(){
        selectedColor = 0;
        rectanglePaint = new Paint();
        textPaint = new Paint();
        indicatorPaint = new Paint();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                y = event.getY();
                height = getHeight();
                selectedPosition = y;
                selectedColor = getColorFromY(y, height);
                invalidate();
                return true;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        int[] colors = {Color.WHITE, Color.rgb(initialRed,initialGreen,initialBlue), Color.BLACK};
        Shader shader = new LinearGradient(0, 0, 0, height, colors, null, Shader.TileMode.CLAMP);
        rectanglePaint.setShader(shader);

        canvas.drawRect(0, 0, width, height, rectanglePaint);

        indicatorPaint.setColor(Color.WHITE);
        indicatorPaint.setStyle(Paint.Style.FILL);

        canvas.drawRect(0, selectedPosition - 5, width, selectedPosition + 5, indicatorPaint);

        indicatorPaint.setColor(Color.BLACK);
        indicatorPaint.setStyle(Paint.Style.STROKE);
        indicatorPaint.setStrokeWidth(2);

        canvas.drawRect(0, selectedPosition - 5, width, selectedPosition + 5, indicatorPaint);


        /*textPaint.setColor(selectedColor);
        textPaint.setTextSize(50);

        String text = "Texto";
        float textWidth = textPaint.measureText(text);
        float x = (width - textWidth) / 2;
        float y = (height + textPaint.getTextSize()) / 2;
        canvas.drawText(text, x, y, textPaint);*/
    }

    private int getColorFromY(float y, int height) {
        float position = y / height;
        int[] colors = {Color.WHITE, Color.rgb(initialRed, initialGreen, initialBlue), Color.BLACK};
        int color = getColorFromGradient(position, colors);
        return color;
    }

    private int getColorFromGradient(float position, int[] colors) {
        if (position <= 0) return colors[0];
        if (position >= 1) return colors[colors.length - 1];

        float unit = 1.0f / (colors.length - 1);
        int i = (int) (position / unit);
        position = (position - i * unit) / unit;
        int startColor = colors[i];
        int endColor = colors[i + 1];

        int red = (int) (Color.red(startColor) * (1 - position) + Color.red(endColor) * position);
        int green = (int) (Color.green(startColor) * (1 - position) + Color.green(endColor) * position);
        int blue = (int) (Color.blue(startColor) * (1 - position) + Color.blue(endColor) * position);
        if (colorUpdateListener != null) {
            colorUpdateListener.onColorUpdated(red, green, blue);
        }
        return Color.rgb(red, green, blue);
    }

    public void getrgb(int red, int green, int blue){
        initialRed = red;
        initialGreen = green;
        initialBlue = blue;
        selectedColor = getColorFromY(y, height);
        invalidate();
    }
}
