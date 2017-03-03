package mac.yk.devicemanagement.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yao on 2016/12/2.
 */

public class OkImageLoader {
    private static final int DOWNLOAD_SUCCESS=0;
    private static final int DOWNLOAD_ERROR = 1;

    private static final String UTF_8 = "utf-8";

    private static OkHttpClient mOkHttpClient;

    private static Handler mHandler;

    private ImageBean mBean;

    private static Context mContext;

    /** 内存缓存图片的集合 sdk3.0 level=11推出的
     * 自动管理集合中的图片，
     * */
    private static LruCache<String,Bitmap> mCaches;


    private ViewGroup mParentLayout;
    /**
     * 图片下载完成时，处理的接口
     */
    public interface OnImageLoadListener {
        void onSuccess(String url, Bitmap bitmap);

        void onError(String error);
    }

    /**
     * 封装了每个图片的相关信息
     */
    private class ImageBean{
        private StringBuilder url;//图片的下载网址
        private Bitmap bitmap;
        private int width;
        private int height;
        private OnImageLoadListener listener;
        private String error;
        private String saveFileName;
        private ImageView imageView;
        private int defaultPicId;
    }

    public static OkImageLoader build(Context context) {
        return new OkImageLoader(context);
    }
    private OkImageLoader(Context context) {
        mBean=new ImageBean();
        if (mOkHttpClient == null) {
            synchronized (OkImageLoader.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient=new OkHttpClient();
                    //初始化一级缓存的集合
                    initCaches(context);
                    mContext=context;
                }
            }
        }
        initHandler(context);
    }

    private void initCaches(Context context) {
        long maxMemory = Runtime.getRuntime().maxMemory();
        mCaches=new LruCache<String, Bitmap>((int)maxMemory/4){
            @Override
            protected int sizeOf(String url, Bitmap bitmap) {
                //计算集合中每个图片所占字节数，为LruCache的动态管理提供信息
                return bitmap.getRowBytes()*bitmap.getHeight();
            }
        };
    }

    private void initHandler(Context context) {
        mHandler = new Handler(context.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                ImageBean bean= (ImageBean) msg.obj;
                switch (msg.what) {
                    case DOWNLOAD_ERROR:
                        bean.listener.onError(mBean.error);
                        break;
                    case DOWNLOAD_SUCCESS:
                        bean.listener.onSuccess(bean.url.toString(),bean.bitmap);
                        break;
                }
            }
        };
    }

    public OkImageLoader url(String url) {
        mBean.url = new StringBuilder(url);
        return this;
    }

    public OkImageLoader addParam(String key, String value) {
        if (mBean.url.indexOf("?") == -1) {
            mBean.url.append("?");
        }else {
            mBean.url.append("&");
        }
        try {
            mBean.url.append(key).append("=").append(URLEncoder.encode(value, UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public OkImageLoader width(int width) {
        mBean.width=width;
        return this;
    }

    public OkImageLoader height(int height) {
        mBean.height=height;
        return this;
    }

    public OkImageLoader listener(OnImageLoadListener listener) {
        mBean.listener=listener;
        return this;
    }

    public Bitmap loadImage() {
        if (mBean.url == null || mBean.url.length() == 0) {
            mBean.error = "没有url地址";
            Message msg = Message.obtain();
            msg.what=DOWNLOAD_ERROR;
            msg.obj=mBean;
            mHandler.sendMessage(msg);
            return null;
        }
        //从一级缓存中获取Bitmap
        if (mCaches.get(mBean.url.toString()) != null) {
            return mCaches.get(mBean.url.toString());
        }
        if (mBean.saveFileName != null) {
            //从二级缓存中读取Bitmap
            String path = FileUtils.getDir(mContext, mBean.saveFileName);
            Bitmap bitmap = BitmapUtils.getBitmap(path);
            if (bitmap != null) {
                return bitmap;
            }
        }

        Request request = new Request.Builder().url(mBean.url.toString()).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(mCallback);
        return null;
    }

    Callback mCallback=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Message msg = Message.obtain();
            msg.what=DOWNLOAD_ERROR;
            mBean.error=e.getMessage();
            msg.obj=mBean;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            byte[] data = response.body().bytes();
            Bitmap bitmap = BitmapUtils.getBitmap(data, mBean.width, mBean.height);
            if (bitmap != null) {
                //一级缓存
                mCaches.put(mBean.url.toString(), bitmap);
                if (mBean.saveFileName != null) {
                    //二级缓存
                    String path = FileUtils.getDir(mContext, mBean.saveFileName);
                    BitmapUtils.saveBitmap(bitmap,path);
                }
                Message msg = Message.obtain();
                mBean.bitmap=bitmap;
                msg.what=DOWNLOAD_SUCCESS;
                msg.obj=mBean;
                mHandler.sendMessage(msg);
            }
        }
    };

    public OkImageLoader saveFileName(String saveFileName) {
        mBean.saveFileName=saveFileName;
        return this;
    }

    public OkImageLoader listener(final ViewGroup parent) {
        mParentLayout=parent;
        mBean.listener=new OnImageLoadListener() {
            @Override
            public void onSuccess(String url, Bitmap bitmap) {
                //根据url查找ImageView
                ImageView iv = (ImageView) parent.findViewWithTag(url);
                if (iv != null) {//若下载之前的url相关的holder的ImageView还存在
                    //下载的图片错位显示
                    iv.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        };
        return this;
    }

    public void showImage() {
        listener(mParentLayout);
        Bitmap bitmap = loadImage();
        if (bitmap != null) {
            mBean.imageView.setImageBitmap(bitmap);
        } else {
            mBean.imageView.setImageResource(mBean.defaultPicId);
        }
    }

    public OkImageLoader imageView(ImageView imageView) {
        mBean.imageView=imageView;
        imageView.setTag(mBean.url.toString());
        mParentLayout= (ViewGroup) imageView.getParent();
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        mBean.width=params.width;
        mBean.height=params.height;
        return this;
    }

    public OkImageLoader defaultPicId(int defaultPicId) {
        mBean.defaultPicId=defaultPicId;
        return this;
    }

    public static void release() {
        if (mOkHttpClient != null) {
            //取消所有的请求
            mOkHttpClient.dispatcher().cancelAll();
            mOkHttpClient=null;
            mContext=null;
            mCaches=null;
        }
    }
}
