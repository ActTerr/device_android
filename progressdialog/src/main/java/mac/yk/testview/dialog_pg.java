package mac.yk.testview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import org.jetbrains.annotations.Nullable;

import java.util.Timer;

public class dialog_pg extends View {
    int mWidth,mHeight,radius;
    Paint mArcPaint,mLinePaint;
    int count=12;
    int start=0;
    Bitmap bitmap ;
    Timer timer=new Timer();
    public dialog_pg(Context context) {
        super(context);

    }

    public dialog_pg(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public dialog_pg(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mArcPaint=new Paint();
        mArcPaint.setColor(getResources().getColor(R.color.alpha));
        mLinePaint=new Paint();

    }

    private void getMyBitmap(int type) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        View view=null;
        if (type==1){
             view= factory.inflate(R.layout.item_white, null);
        }else {
            view=factory.inflate(R.layout.item_gray, null);
        }


        //启用绘图缓存
        view.setDrawingCacheEnabled(true);
//        ImageView pic = (ImageView) view.findViewById(R.id.cao);
//        ViewGroup.LayoutParams layoutParams=pic.getLayoutParams();
//        layoutParams.height=radius;
//        layoutParams.width= (int) (radius/0.382);
        //调用下面这个方法非常重要，如果没有调用这个方法，得到的bitmap为null
        view.measure(MeasureSpec.makeMeasureSpec(256, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(256, MeasureSpec.EXACTLY));
        //这个方法也非常重要，设置布局的尺寸和位置
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        bitmap = view.getDrawingCache();
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
            radius=(mWidth-getPaddingLeft()-getPaddingRight())/10;//半径
            canvas.translate(mWidth/2,mHeight/2);
            drawArcView(canvas);
            drawLine(canvas);
    }



    private void addMark() {
        start+=4;
        if (start==12){
            start=0;
        }
    }


    private void drawArcView(Canvas canvas) {
        RectF mRect=new RectF(-radius,-radius,radius,radius);
        //canvas.drawRect(mRect,mArcPaint);
        canvas.drawArc(mRect,0,360,false,mArcPaint);
    }

    private void drawLine(Canvas canvas) {
        canvas.save();
        float angle = -360+(float)360/count;//刻度间隔
        for(int i=0;i<count;i++){
////            if(i==start){
            if (i>=start&&i<start+4){
                getMyBitmap(1);
            }else {
                getMyBitmap(2);
            }

            mLinePaint.setStyle(Paint.Style.FILL);//充满
            mLinePaint.setAntiAlias(true);// 设置画笔的锯齿效果
            canvas.drawBitmap(bitmap, ConvertUtils.dp2px(getContext(),-2),radius,mLinePaint);
//                canvas.drawLine(0,radius,0,radius+radius,mLinePaint);
////            }else if (i==end){
////                mLinePaint.setStrokeWidth(20);
////                mLinePaint.setColor(Color.WHITE);
////                canvas.drawLine(0,-radius,0,-radius+40,mLinePaint);
////            }
            canvas.rotate(angle);//逆时针旋转
        }
        canvas.restore();
    }

//    private void drawLine(Canvas canvas) {
//        canvas.save();
//        float angle = (float)sweepAngle/count;//刻度间隔
//        canvas.rotate(-270+startAngle);//将起始刻度点旋转到正上方
//        for(int i=0;i<=count;i++){
//            if(i==0 || i==count){
//                mLinePaint.setStrokeWidth(1);
//                mLinePaint.setColor(Color.WHITE);
//                canvas.drawLine(0,-radius,0,-radius+40,mLinePaint);
//
//            }else if(i>=getStartLineIndex(minTemp,maxTemp) && i<=getEndLineIndex(minTemp,maxTemp)){
//                mLinePaint.setStrokeWidth(3);
//                mLinePaint.setColor(getRealColor(minTemp,maxTemp));
//                canvas.drawLine(0,-radius,0,-radius+30,mLinePaint);
//
//            }else {
//                mLinePaint.setStrokeWidth(2);
//                mLinePaint.setColor(Color.WHITE);
//                canvas.drawLine(0,-radius,0,-radius+30,mLinePaint);
//            }
//
//            canvas.rotate(angle);//逆时针旋转
//        }
//        canvas.restore();
//    }

    public void stop(){
        timer.cancel();
    }


    public void refresh() {
        addMark();
        invalidate();
    }
}
