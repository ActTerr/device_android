package mac.yk.devicemanagement.net;

import mac.yk.devicemanagement.I;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mac-yk on 2017/3/18.
 */

public class netWork<T> {
    public netWork() {
    }

    OkHttpClient okHttpClient=new OkHttpClient();
    //反射注解生成代码的
    private  Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    //反射传参数的
    private CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    Class<T> mClazz;
    public netWork<T> targetClass(Class<T> clazz){
        mClazz=clazz;
        return this;
    }
    public T getAPI(){
        return initRetrofit().create(mClazz);
    }

    private  Retrofit initRetrofit() {
        Retrofit retrofit=new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(I.REQUEST.SERVER_ROOT)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
        return  retrofit;
    }
    
}
