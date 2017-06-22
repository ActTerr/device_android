package mac.yk.devicemanagement.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import mac.yk.devicemanagement.R;

/**
 * Created by mac-yk on 2017/6/16.
 */

public class dialog_pg extends View {
    int mWidth,mHeight,radius;
    Paint mArcPaint,mLinePaint;
    int count=12;
    int start,end;
    Timer timer=new Timer();
    public dialog_pg(Context context) {
        super(context);
        mArcPaint=new Paint();
        mLinePaint=new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wrap_Len = 600;
        int width = measureDimension(wrap_Len, widthMeasureSpec);
        int height = measureDimension(wrap_Len, heightMeasureSpec);
        int len=Math.min(width,height);
        //保证是一个正方形
        setMeasuredDimension(len,len);
    }

    public int measureDimension(int defaultSize, int measureSpec){
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            result = defaultSize;   //UNSPECIFIED
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        mWidth=getWidth();
        mHeight=getHeight();
        radius=(mWidth-getPaddingLeft()-getPaddingRight())/2;//半径
        canvas.translate(mWidth/2,mHeight/2);
        drawArcView(canvas);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                drawLine(canvas);
                addMark();
            }
        },50,50);

    }

    private void addMark() {
        start++;
        end++;
        if (start>12){
            start=1;
        }
        if (end>12){
            end=1;
        }
    }

    private void drawArcView(Canvas canvas) {
        RectF mRect=new RectF(-radius,-radius,radius,radius);
        //canvas.drawRect(mRect,mArcPaint);
        canvas.drawArc(mRect,0,360,false,mArcPaint);
    }

    private void drawLine(Canvas canvas) {
        canvas.save();
        float angle = (float)360/count;//刻度间隔
        for(int i=0;i<count;i++){
            if(i==start){
                mLinePaint.setStrokeWidth(20);
                mLinePaint.setColor(getResources().getColor(R.color.gray2));
                canvas.drawLine(0,-radius,0,-radius+40,mLinePaint);
            }else if (i==end){
                mLinePaint.setStrokeWidth(20);
                mLinePaint.setColor(getResources().getColor(R.color.white));
                canvas.drawLine(0,-radius,0,-radius+40,mLinePaint);
            }
            canvas.rotate(angle);//逆时针旋转
        }
        canvas.restore();
    }

    public void stop(){
        timer.cancel();
    }
}
