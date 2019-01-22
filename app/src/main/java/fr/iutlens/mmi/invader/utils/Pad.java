package fr.iutlens.mmi.fight.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * TODO: document your custom view class.
 */
public class Pad extends View {


    private Paint paint, ray;
    private double angle;
    private float r;
    private double length;

    public Pad(Context context) {
        super(context);
        init(null, 0);
    }

    public Pad(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Pad(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(0xCC666666);

        ray = new Paint();
        ray.setStyle(Paint.Style.STROKE);
        ray.setStrokeWidth(10);
        ray.setStrokeCap(Paint.Cap.ROUND);
        ray.setColor(0x44BBBBBB);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float xc;
        float yc;
        xc = getWidth()/2;
        yc = getHeight()/2;

        r = xc > yc ? yc : xc;

        canvas.drawCircle(xc,yc, r,paint);

        if(length >0){
            float xd = (float) (xc + Math.cos(angle) * length*r);
            float yd = (float) (yc+ Math.sin(angle)* length*r);

            canvas.drawLine(xc,yc,xd,yd,ray);
        }
    }

    public double getAngle() {
        return angle;
    }

    public double getLength() {
        return length;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if ((event.getAction() & (MotionEvent.ACTION_UP))!=0){
            length = 0;
            invalidate();
            return true;
        }

        float x = event.getX() - getWidth()/2;
        float y = event.getY() - getHeight()/2;

        this.angle = Math.atan2(y,x);
        this.length = Math.sqrt(x*x+y*y)/r;

        if (length >1) length = 1;

        invalidate();

        return true;
    }


}
