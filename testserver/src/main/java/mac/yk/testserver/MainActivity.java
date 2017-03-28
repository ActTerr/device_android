package mac.yk.testserver;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.testserver.bean.Result;
import mac.yk.testserver.model.IModel;
import mac.yk.testserver.net.api.yujingAPI;
import mac.yk.testserver.net.netWork;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    IModel model;
    protected Subscription subscription;
    Handler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Looper.prepare();
        Looper.loop();
        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        });
    }

    @OnClick({R.id.tongji, R.id.chaxun, R.id.control, R.id.save, R.id.down, R.id.xunjian, R.id.xiujun, R.id.baofei})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tongji:
                netWork<yujingAPI> netWork= new netWork<>();
                subscription= netWork.targetClass(yujingAPI.class).getYujingAPI().getyujing().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<Result, String>() {
                            @Override
                            public String call(Result result) {
                                return result.getRetData().toString();
                            }
                        }).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.chaxun:
                break;
            case R.id.control:
                break;
            case R.id.save:
                break;
            case R.id.down:
                break;
            case R.id.xunjian:
                break;
            case R.id.xiujun:
                break;
            case R.id.baofei:
                break;
        }
    }

    private void test(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                 handler.sendEmptyMessage(1);
                handler.sendMessage(new Message());
                Message.obtain(handler, new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription!=null&&!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

}
