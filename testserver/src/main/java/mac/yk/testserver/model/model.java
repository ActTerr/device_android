package mac.yk.testserver.model;

import mac.yk.testserver.I;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mac-yk on 2017/3/18.
 */

public class model {
    static yujingAPI yujingapi;
    static OkHttpClient okHttpClient=new OkHttpClient();
    //反射注解生成代码的
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    //反射传参数的
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static yujingAPI getYujingAPI(){
        if (yujingapi==null){
            Retrofit retrofit=new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(I.REQUEST.SERVER_ROOT)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            yujingapi=retrofit.create(yujingAPI.class);
        }
        return yujingapi;

    }
}
