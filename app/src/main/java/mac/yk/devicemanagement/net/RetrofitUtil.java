package mac.yk.devicemanagement.net;

import java.util.concurrent.TimeUnit;

import mac.yk.devicemanagement.I;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    public static Retrofit retrofit;
    private static OkHttpClient okHttpClient = new OkHttpClient();

    static {
        okHttpClient.newBuilder().connectTimeout(5000, TimeUnit.MILLISECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public static <T> T createService(Class<T> clazz) {
        if (retrofit == null) {
            synchronized (RetrofitUtil.class) {

                Retrofit.Builder builder = new Retrofit.Builder();
                retrofit = builder.baseUrl(I.REQUEST.SERVER_ROOT)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
            }
        }
        return retrofit.create(clazz);
    }


    public static <T> T initDown(Class<T> clazz,ProgressListener listener){
        if (retrofit==null){
            DownloadProgressInterceptor interceptor=new DownloadProgressInterceptor(listener);
          OkHttpClient  okHttpClient=new OkHttpClient.Builder().addInterceptor(interceptor).build();
            retrofit=new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(I.REQUEST.SERVER_ROOT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit.create(clazz);
    }

}