package com.example.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class selectColor extends View {
    private Paint pointerPaint;
    private int pointerPositionX, pointerPositionY;
    private int selectedColor;
    private Paint colorLinePaint;
    private float angleDeg=0;
    private float maxRadius;
    private OnSelectedColorListener listener;
    private int initialRed;
    private int initialGreen;
    private int initialBlue;
    public void setOnSelectedColorListener(OnSelectedColorListener l){
        listener = l;
    }

    public selectColor(Context context) {
        super(context);
        init();
    }

    public selectColor(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.selectColor);
        initialRed = a.getInt(R.styleable.selectColor_red, 255);
        initialGreen = a.getInt(R.styleable.selectColor_green, 0);
        initialBlue = a.getInt(R.styleable.selectColor_blue, 0);
        a.recycle();
        init();
    }

    private void init() {
        pointerPaint = new Paint();
        pointerPaint.setStyle(Paint.Style.STROKE);
        pointerPaint.setStrokeWidth(2);
        pointerPaint.setColor(Color.BLACK);
        pointerPaint.setAntiAlias(true);

        colorLinePaint = new Paint();
        colorLinePaint.setStyle(Paint.Style.STROKE);
        colorLinePaint.setStrokeWidth(70);
        colorLinePaint.setAntiAlias(true);
        colorLinePaint.setStrokeCap(Paint.Cap.ROUND);

        selectedColor = Color.rgb(initialRed, initialGreen, initialBlue);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float centerX = getWidth() / 2f;
                float centerY = getHeight() / 2f;

                float distance = (float) Math.sqrt(Math.pow(centerX - event.getX(), 2) + Math.pow(centerY - event.getY(), 2));
                if (distance >= maxRadius-70) {
                    pointerPositionX = (int) event.getX();
                    pointerPositionY = (int) event.getY();
                    updateSelectedColor();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void updateSelectedColor() {
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float angleRad = (float) Math.atan2(centerY - pointerPositionY, pointerPositionX - centerX);
        angleDeg = (float) Math.toDegrees(angleRad);
        if (angleDeg < 0) {
            angleDeg += 360;
        }
        float[] hsv = {angleDeg, 1, 1};
        selectedColor = Color.HSVToColor(hsv);
        int red = Color.red(selectedColor);
        int green = Color.green(selectedColor);
        int blue = Color.blue(selectedColor);

        if (listener != null) {
            listener.onSelectedColor(red, green, blue);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        maxRadius = Math.min(centerX, centerY);

        float radius = maxRadius - colorLinePaint.getStrokeWidth() / 2f;
        SweepGradient gradient = new SweepGradient(centerX, centerY, new int[]{Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED}, null);
        colorLinePaint.setShader(gradient);
        canvas.drawCircle(centerX, centerY, radius, colorLinePaint);

        float triangleSize = (maxRadius - colorLinePaint.getStrokeWidth()) * 0.8f;
        float triangleX = getWidth() / 2f;
        float triangleY = (getHeight() * 0.58f) - (triangleSize / 2);
        canvas.save();
        canvas.rotate(-angleDeg + 90, centerX, centerY);

        Path trianglePath = new Path();
        trianglePath.moveTo(triangleX, triangleY - triangleSize);
        trianglePath.lineTo(triangleX + triangleSize, triangleY + triangleSize);
        trianglePath.lineTo(triangleX - triangleSize, triangleY + triangleSize);
        trianglePath.close();

        int[] colors = {Color.WHITE, selectedColor, Color.BLACK};
        float[] positions = {0, 0.5f, 1.0f};
        triangleSize = (maxRadius - colorLinePaint.getStrokeWidth()) * 0.8f;
        triangleX = getWidth() / 2f;
        triangleY = (getHeight() * 0.58f) - (triangleSize / 2);
        int startX = (int) (triangleX - triangleSize);
        int endX = (int) (triangleX + triangleSize);

        LinearGradient linearGradient = new LinearGradient(startX, triangleY, endX, triangleY, colors, positions, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(linearGradient);
        canvas.drawPath(trianglePath, paint);


        Paint whiteLinePaint = new Paint();
        whiteLinePaint.setColor(Color.WHITE);
        whiteLinePaint.setStyle(Paint.Style.STROKE);
        whiteLinePaint.setStrokeWidth(8);
        whiteLinePaint.setAntiAlias(true);

        Path whiteLinePath = new Path();
        whiteLinePath.moveTo(triangleX, triangleY - triangleSize);
        whiteLinePath.lineTo(triangleX, triangleY - triangleSize-61);
        canvas.drawPath(whiteLinePath, whiteLinePaint);
        canvas.restore();
    }

    public void setrgb(int red, int green, int blue){
        selectedColor = Color.rgb(red,green,blue);
    }
}