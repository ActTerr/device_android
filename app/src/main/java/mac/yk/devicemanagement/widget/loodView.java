package mac.yk.devicemanagement.widget;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;


/**
 * Created by mac-yk on 2017/3/24.
 * 出现bug的原因：
 * 1.butterknife忘记bind，导致空指针
 * 2.指示器init的时机不对，导致空指针
 * 3.viewPager直接通过资源id实例化的,又new了一个，导致界面上的控件并未被操作，而是放在了又new的控件上
 */

public class loodView extends FrameLayout {
    final String TAG="loodView";
    int currentItem = 0;
    Context context;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }
    };
    List<ImageView> imageViews = new ArrayList<>();
    List<View> indicator = new ArrayList<>();
    boolean isfromCache = true;
    private final static boolean isAutoPlay = true;
    int count=0;
    Subscription subscription;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.v_dot1)
    View vDot1;
    @BindView(R.id.v_dot2)
    View vDot2;
    @BindView(R.id.v_dot3)
    View vDot3;
    @BindView(R.id.v_dot4)
    View vDot4;
    @BindView(R.id.v_dot5)
    View vDot5;
    @BindView(R.id.v_dot6)
    View vDot6;
    @BindView(R.id.v_dot7)
    View vDot7;


    int Dname;
    private ScheduledExecutorService scheduledExecutorService;

    public loodView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        Dname= ConvertUtils.getDname(MyApplication.getInstance().getData()[2]);
        initCount();
    }
    public loodView(Context context) {
        super(context);
        this.context = context;
    }

    public loodView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
        this.context = context;
    }
    private void initCount() {
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        subscription = wrapper.targetClass(ServerAPI.class).getAPI().getPicCount(Dname, I.PIC.DEVICE)
                .compose(wrapper.<Integer>applySchedulers())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (ExceptionFilter.filter(context, e)) {
                            ToastUtil.showToast(context, "服务器没有图片");
                        }
                        if (subscription != null && !subscription.isUnsubscribed()) {
                            subscription.unsubscribe();
                        }
                    }

                    @Override
                    public void onNext(Integer integer) {
                        count = integer;
                        L.e(TAG,"count:"+count);
                        initUi(context);
                        if(isAutoPlay){
                            startPlay();
                        }
                        if (subscription != null && !subscription.isUnsubscribed()) {
                            subscription.unsubscribe();
                        }
                    }
                });
    }

    /**
     * 命名时最后一个使用数字，然后for循环里通过反射取得控件并操作
     */
    private void initIndicator() {
        try {
            Class c=Class.forName("mac.yk.devicemanagement.widget.loodView");
            for (int i=1;i<8;i++){
                Field field=c.getDeclaredField("vDot"+i);
                field.setAccessible(true);
                View view= (View) field.get(this);
                indicator.add(view);
                if (i>count+1){
                    view.setVisibility(GONE);
                }
            }
        L.e(TAG,"size:"+indicator.size());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initUi(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.load_view, this, true);
        ButterKnife.bind(this,view);
        initIndicator();
        for (int i = 0; i < count; i++) {

            String imagesUrl = I.REQUEST.SERVER_ROOT + I.REQUEST.PATH + "?request=" + I.REQUEST.DOWNPIC + "&" + I.DEVICE.DNAME + "=" +
                    Dname + "&" + I.PIC.PID + "=" + i + "&" + I.PIC.TYPE + "=" + I.PIC.DEVICE;
            L.e("TAG", imagesUrl);
            Uri uri = Uri.parse(imagesUrl);
            //facebook的View控件
            final SimpleDraweeView mAlbumArt = new SimpleDraweeView(context);
            //facebook的控制监听
            ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                    if (imageInfo == null) {
                        return;
                    }
                    QualityInfo qualityInfo = imageInfo.getQualityInfo();
                    FLog.d("Final image received! " +
                                    "Size %d x %d",
                            "Quality level %d, good enough: %s, full quality: %s",
                            imageInfo.getWidth(),
                            imageInfo.getHeight(),
                            qualityInfo.getQuality(),
                            qualityInfo.isOfGoodEnoughQuality(),
                            qualityInfo.isOfFullQuality());
                }

                @Override
                public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                    //FLog.d("Intermediate image received");
                }

                @Override
                public void onFailure(String id, Throwable throwable) {
                    mAlbumArt.setImageURI(Uri.parse("res:/" + R.drawable.nopic));
                }
            };
            if (uri!=null){
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).build();

                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setOldController(mAlbumArt.getController())
                        .setAutoPlayAnimations(true)
                        .setImageRequest(request)
                        .setControllerListener(controllerListener)
                        .build();

                mAlbumArt.setController(controller);
            }else {
                mAlbumArt.setImageDrawable(getResources().getDrawable(R.drawable.nopic));
            }
            mAlbumArt.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(mAlbumArt);
        }
        viewPager.setFocusable(true);
        viewPager.setAdapter(new LooperViewAdapter());
        viewPager.addOnPageChangeListener(new myPagerListener());
    }

    class LooperViewAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            return imageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    class myPagerListener implements ViewPager.OnPageChangeListener {
        boolean isAutoPlay = true;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            for (int i = 0; i < indicator.size(); i++) {
                if (i == position) {
                    indicator.get(i).setBackgroundResource(R.mipmap.red_point);
                } else {
                        indicator.get(i).setBackgroundResource(R.mipmap.grey_point);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case 1:
                    isAutoPlay = false;
//                    startPlay();
                    break;
                case 2:
                    isAutoPlay = true;
//                    stopPlay();
                    break;
                case 0:
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    }
                    //当前为第一张，从左向右滑
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

    }

    /**
     * 在这重写的方法是viewGroup的，触摸事件在它下一层view的onTouchEvent中，
     * 所以只能通过分发事件得到触摸事件类型
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
            case MotionEvent.ACTION_MOVE:
                stopPlay();
                break;
            case MotionEvent.ACTION_UP:
                startPlay();
                break;

        }
        return super.dispatchTouchEvent(ev);
    }




    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new LoopTask(), 1, 3, TimeUnit.SECONDS);

    }

    private void stopPlay() {
        scheduledExecutorService.shutdown();
    }

    public void destory() {
        scheduledExecutorService.shutdown();
        scheduledExecutorService = null;
    }

    class LoopTask implements Runnable {

        @Override
        public void run() {
            currentItem = (currentItem + 1) % count;
            handler.obtainMessage().sendToTarget();
        }
    }

    /**
     * 销毁ImageView回收资源
     */
    private void destoryBitmaps() {
        for (int i = 0; i < count; i++) {
            ImageView imageView = imageViews.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null)
                //解除drawable对view的引用
                drawable.setCallback(null);
        }
    }
}
