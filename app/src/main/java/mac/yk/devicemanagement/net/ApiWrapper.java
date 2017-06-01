package mac.yk.devicemanagement.net;

import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.bean.Result;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by mac-yk on 2017/3/18.
 */

public class ApiWrapper<T> {
    public ApiWrapper() {
    }
    Retrofit retrofit;
    OkHttpClient okHttpClient;
    //反射注解生成代码的
    private  Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    //反射传参数的
    private CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    Class<T> mClazz;
    public ApiWrapper<T> targetClass(Class<T> clazz){
        mClazz=clazz;
        return this;
    }
    public T getAPI(){
        return initRetrofit().create(mClazz);
    }


    private  Retrofit initRetrofit() {
        if (retrofit==null){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient=new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
            retrofit=new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(I.REQUEST.SERVER_ROOT)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
        }
        return  retrofit;
    }

    public <T> Observable<T> flatResponse(final Result<T> result) {
        return Observable.create(new Observable.OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> subscriber) {
                if (result.isSuccess()) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(result.retData);
                    }
                } else {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(new APIException(result.retCode, result.retMsg));
                    }
                    return;
                }

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }

            }
        });
    }


    public  <T> Observable.Transformer<Result<T>, T> applySchedulers() {
        return (Observable.Transformer<Result<T>, T>) transformer;
    }

    final Observable.Transformer transformer = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Func1() {
                        @Override
                        public Object call(Object result) {
                            return flatResponse((Result<Object>) result);
                        }
                    })
                    ;
        }
    };

}
