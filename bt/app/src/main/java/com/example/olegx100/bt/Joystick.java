package com.example.olegx100.bt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class Joystick extends FrameLayout {

    public interface IJoystickEvents {
        void OnMove (double x, double y);
    }

    Point jLocation;
    Point jSize;
    Rect jCover;
    Bitmap bmpJoystick;
    Bitmap bmpBackground;
    IJoystickEvents eventsCallback;

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.joystick, this);

        Init();
    }

    public Joystick(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.joystick, this);

        Init();
    }

    public void setEventsListener (IJoystickEvents listener) {
        eventsCallback = listener;
    }

    private void Init () {
        //setBackgroundColor(Color.parseColor("#808080"));

        bmpJoystick = (Bitmap) BitmapFactory.decodeResource(getResources(), R.drawable.joystick);
        bmpBackground = (Bitmap)BitmapFactory.decodeResource(getResources(), R.drawable.joystick_bg);
        jLocation = new Point(0, 0);
    }

    protected void onSizeChanged (int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        this.jSize = new Point(w, h);
        jLocation.x = w/2;
        jLocation.y = h/2;
        jCover = new Rect(
            jLocation.x - bmpBackground.getWidth()/2,
            jLocation.y - bmpBackground.getHeight()/2,
            jLocation.x + bmpBackground.getWidth()/2,
            jLocation.y + bmpBackground.getHeight()/2
        );
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bmpBackground, (this.jSize.x - bmpBackground.getWidth()) / 2, (this.jSize.y - bmpBackground.getHeight()) / 2, null);

        //draw the joystick background
        canvas.drawBitmap(bmpJoystick, jLocation.x - bmpJoystick.getWidth()/2 ,jLocation.y - bmpJoystick.getHeight() / 2, null);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event)
    {
        this.jLocation.x = (int)event.getX();
        this.jLocation.y = (int)event.getY();
        if (this.jLocation.x > jCover.right)
            this.jLocation.x = jCover.right;

        if (this.jLocation.x < jCover.left)
            this.jLocation.x = jCover.left;
        if (this.jLocation.y < jCover.top)
            this.jLocation.y = jCover.top;
        if (this.jLocation.y > jCover.bottom)
            this.jLocation.y = jCover.bottom;

        invalidate();

        IJoystickEvents l = eventsCallback;
        if (l != null)
            l.OnMove(2.0*(this.jLocation.x - jCover.centerX())/jCover.width(), -2.0*(this.jLocation.y - jCover.centerY())/jCover.height());
        return true;
    }

}
