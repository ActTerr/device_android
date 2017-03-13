package mac.yk.testserver;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.testserver.bean.Device;
import mac.yk.testserver.bean.Result;
import mac.yk.testserver.bean.Scrap;
import mac.yk.testserver.model.IModel;
import mac.yk.testserver.model.Model;

public class MainActivity extends AppCompatActivity {
    IModel model;
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Result r = (Result) msg.obj;
            Toast.makeText(MainActivity.this, r.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        model = Model.getInstance();

    }

    @OnClick({R.id.tongji, R.id.chaxun, R.id.control, R.id.save, R.id.down, R.id.xunjian, R.id.xiujun})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tongji:
                model.getTongji(this, new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result != null) {
                            Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                break;
            case R.id.chaxun:
                model.chaxun(this, "1111", new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        Log.e("main", result.toString());
                        Message msg = Message.obtain();
                        msg.obj = result;
                        mhandler.sendMessage(msg);
                        if (result != null && result.getRetCode() == 0) {
                            String s = result.getRetData().toString();
                            Gson gson = new Gson();
                            Device d = gson.fromJson(s, Device.class);
                            Log.e("main", "d" + d.toString());

                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                break;
            case R.id.control:
                model.control(this, true,String.valueOf(I.CONTROL.BEIYONG), "1111", new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result != null) {
                            Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                break;
            case R.id.save:
                String s = "2017-03-02";
                Date date = ConvertUtils.String2Date(s);
                Device device = new Device(2222, 1, 1, date, date);
                model.saveDevice(this, "yk", device, new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result != null) {
                            Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                break;
            case R.id.down:
                model.downDevice(this, 1, 5, new OkHttpUtils.OnCompleteListener<Device[]>() {
                    @Override
                    public void onSuccess(Device[] result) {
                        Device d=result[0];
                        if (result!=null){
                            Toast.makeText(MainActivity.this, d.getName()+"", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });

                model.downScrap(this, 1, 5, new OkHttpUtils.OnCompleteListener<Scrap[]>() {
                    @Override
                    public void onSuccess(Scrap[] result) {
                        Scrap s=result[0];
                        if (result!=null){
                            Toast.makeText(MainActivity.this, result[1].getRemark(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
//                model.downloadWeixiu(this, "1111", 1, new OkHttpUtils.OnCompleteListener<Weixiu[]>() {
//                    @Override
//                    public void onSuccess(Weixiu[] result) {
//                        if (result != null) {
//                            Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onError(String error) {
//
//                    }
//                });
//                model.downloadXunjian(this, "1111", 1, new OkHttpUtils.OnCompleteListener<Xunjian[]>() {
//                    @Override
//                    public void onSuccess(Xunjian[] result) {  if (result!=null){
//                        Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    }
//
//                    @Override
//                    public void onError(String error) {
//
//                    }
//                });
                break;
            case R.id.xunjian:
                model.xunjian(this, true,"yk", "1111", "1", "afafaf", new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result != null) {
                            Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                break;
            case R.id.xiujun:
                model.xiujun(this, true,"yk", "1111", true, "aaaa", new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result != null) {
                            Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                break;

        }
    }

    @OnClick(R.id.baofei)
    public void onClick() {
        model.baofei(this, "yk", "2", "1111", "remark", new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                    String s = String.valueOf(result.getRetData());
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
