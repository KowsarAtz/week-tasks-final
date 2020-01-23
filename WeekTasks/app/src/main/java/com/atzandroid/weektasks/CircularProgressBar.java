package com.atzandroid.weektasks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class CircularProgressBar extends View {
    Paint mPaintProgress, mPaintBackground, mPaintProgress2;
    Context context;
    float strokeWidth, fillWidth;
    float sweepAngle;
    boolean finished = false;

    public CircularProgressBar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircularProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        this.strokeWidth = 10;
        this.fillWidth = 8*this.strokeWidth;
        this.sweepAngle = 0;

        mPaintProgress = new Paint();
        mPaintProgress.setAntiAlias(true);
        mPaintProgress.setDither(true);
        mPaintProgress.setStyle(Paint.Style.STROKE);
        mPaintProgress.setStrokeWidth(fillWidth);
        mPaintProgress.setStrokeCap(Paint.Cap.ROUND);
        mPaintProgress.setColor(context.getResources().getColor(R.color.green_approved));

        mPaintProgress2 = new Paint();
        mPaintProgress2.setAntiAlias(true);
        mPaintProgress2.setDither(true);
        mPaintProgress2.setStyle(Paint.Style.STROKE);
        mPaintProgress2.setStrokeWidth((float) (fillWidth*0.7));
        mPaintProgress2.setStrokeCap(Paint.Cap.ROUND);
        mPaintProgress2.setColor(context.getResources().getColor(R.color.green_approved));


        mPaintBackground = new Paint();
        mPaintBackground.setAntiAlias(true);
        mPaintBackground.setDither(true);
        mPaintBackground.setStyle(Paint.Style.STROKE);
        mPaintBackground.setStrokeWidth(strokeWidth);
        mPaintBackground.setStrokeCap(Paint.Cap.ROUND);
        mPaintBackground.setColor(context.getResources().getColor(R.color.white));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawArc(strokeWidth, strokeWidth, getWidth()-strokeWidth, getHeight()-strokeWidth
//                , 0, 360, false, mPaintBackground);
        float temp = (strokeWidth+fillWidth)/2;
        mPaintProgress.setColor(context.getResources().getColor(R.color.tasky_blue));
        canvas.drawArc(strokeWidth+temp, strokeWidth+temp, getWidth()-strokeWidth-temp, getHeight()-strokeWidth-temp
                , 0, 360, false, mPaintProgress);
        mPaintProgress2.setColor(context.getResources().getColor(R.color.tasky_dark_blue));
        canvas.drawArc(strokeWidth+temp, strokeWidth+temp, getWidth()-strokeWidth-temp, getHeight()-strokeWidth-temp
                , 0, sweepAngle, false, mPaintProgress2);
        if (finished) {
            mPaintProgress.setColor(context.getResources().getColor(R.color.green_approved));
            canvas.drawArc(strokeWidth + temp, strokeWidth + temp, getWidth() - strokeWidth - temp, getHeight() - strokeWidth - temp
                    , 0, 360, false, mPaintProgress);
        }
        //        canvas.drawArc(strokeWidth+2*temp, strokeWidth+2*temp, getWidth()-strokeWidth-2*temp, getHeight()-strokeWidth-2*temp
//                , 0, 360, false, mPaintBackground);

//        canvas.drawArc(padding,padding,getWidth()-padding,getHeight()-padding,0, 270, false, mPaintBackground);

    }

    public void update(float stepAngle) {
        sweepAngle += stepAngle;
    }

    public void setFinished(){
        finished = true;
    }
}
