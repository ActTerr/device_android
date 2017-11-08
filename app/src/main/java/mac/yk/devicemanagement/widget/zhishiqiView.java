package mac.yk.devicemanagement.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.util.L;


/**
 * Created by mac-yk on 2017/3/3.
 */

public class zhishiqiView extends View {
    String TAG=getClass().getName();
    Paint paint;
    int space;
    int count;
    int focus;
   int  length;
    int NormalColor;
    int FocusColor;

    boolean CountIsChange=true;
    public zhishiqiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.zhishiqiView);
        space=typedArray.getDimensionPixelOffset(R.styleable.zhishiqiView_space,6);
        count=typedArray.getInt(R.styleable.zhishiqiView_count,0);
        length=typedArray.getDimensionPixelOffset(R.styleable.zhishiqiView_space,10);
        NormalColor=typedArray.getColor(R.styleable.zhishiqiView_normalColor,0xafa);
        FocusColor=typedArray.getColor(R.styleable.zhishiqiView_focusColor,0xafa);
        typedArray.recycle();//一定要回收,否则占用很大资源
        paint=new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!CountIsChange){
            return;
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!CountIsChange){
            return;
        }
        setMeasuredDimension(configWidth(widthMeasureSpec),configHeight(heightMeasureSpec));
    }

    private int configHeight(int heightMeasureSpec) {
       int mode=MeasureSpec.getMode(heightMeasureSpec);
        int size=MeasureSpec.getSize(heightMeasureSpec);
        int result=size;
        if(mode!=MeasureSpec.EXACTLY){
            size=getPaddingTop()+length+getPaddingBottom();
            result=Math.min(size,result);
        }
        return result;
    }

    private int configWidth(int widthMeasureSpec) {
        int mode=MeasureSpec.getMode(widthMeasureSpec);
        int size=MeasureSpec.getSize(widthMeasureSpec);
        int result=size;
        if (mode!=MeasureSpec.EXACTLY){
            size= length*count+space*(count-1)+getPaddingRight()+getPaddingRight();
            result=Math.min(size,result);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       if (count==0){
           return;
       }

        int left=(getWidth()-length*count+space*(count-1))/2;
        for (int i=0;i<count;i++){
            int color=i==focus?FocusColor:NormalColor;
            paint.setColor(color);
            int width=left+(length+space)*(count-1);
            int height=(getHeight()-length)/2;
            L.e(TAG,"开始绘制");
            canvas.drawRect(width,height,width+length,height+length,paint);
        }

    }



    public void setFocus(int focus) {
        CountIsChange=false;
        this.focus = focus;
        invalidate();
    }

    public void setCount(int count) {
        this.count = count;
        CountIsChange=true;
        requestLayout();
    }

}
