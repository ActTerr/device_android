package mac.yk.devicemanagement.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import mac.yk.devicemanagement.util.OkImageLoader;

/**
 * Created by mac-yk on 2017/3/3.
 */

public class lunboView extends ViewPager{
    zhishiqiView zhishiView;
    boolean notchange;
    Handler handler;
    String [] images;
    Timer mTimer;
    public lunboView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);

    }



    class AutoLoopPlayAdapter extends PagerAdapter{

        String[] ImagesUrl=new String[]{};
        Context context;
        public AutoLoopPlayAdapter(Context context,String[] imagesUrl) {
            ImagesUrl = imagesUrl;
            this.context=context;
            setListener();
            init();
        }
        private void init() {
            handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (!notchange){
                        setCurrentItem(getCurrentItem()+1);
                    }
                }
            };
        }
        private void setListener() {
            setOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    zhishiView.setFocus(position%ImagesUrl.length);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        @Override
        public int getCount() {
            return ImagesUrl.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view=new ImageView(context);
            addView(view);
            OkImageLoader.build(context)
                    .url("")
                    .height(200)
                    .width(400)
                    .imageView(view)
                    .showImage();
//            int memory;
//            if (position>=images.length-1){
//                memory=(position+1)%images.length;
//            }else {
//                memory=position;
//            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public void startPlay(zhishiqiView z){
        zhishiView=z;
        zhishiView.setCount(images.length);
        AutoLoopPlayAdapter adapter=new AutoLoopPlayAdapter(getContext(),images);
        this.setAdapter(adapter);
        MySrcoller scroller=new MySrcoller(getContext(),new LinearInterpolator());
        scroller.setDuration(2000);
        Field field = null;
        try {
            field =ViewPager.class.getField("MyScroller");
            field.setAccessible(true);
            field.set(this, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mTimer=new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        },1000,2000);

    }

    class MySrcoller extends Scroller{
        int duration;

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public MySrcoller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy,this.duration);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                notchange=true;
                break;
            case MotionEvent.ACTION_UP:
                notchange=false;
                break;
        }
        return super.onTouchEvent(ev);
    }

}
