package mac.yk.testserver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.testserver.bean.Device;
import mac.yk.testserver.bean.Result;
import mac.yk.testserver.bean.Weixiu;
import mac.yk.testserver.bean.Xunjian;
import mac.yk.testserver.model.IModel;
import mac.yk.testserver.model.Model;

public class MainActivity extends AppCompatActivity {
    IModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        model= Model.getInstance();
    }

    @OnClick({R.id.tongji, R.id.chaxun, R.id.control, R.id.save, R.id.down, R.id.xunjian, R.id.xiujun})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tongji:
                model.getTongji(this, new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result!=null){
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
                        if (result!=null){
                            Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                break;
            case R.id.control:
                model.control(this, "1", "1111", new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result!=null){
                            Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                break;
            case R.id.save:
                SimpleDateFormat sm=new SimpleDateFormat("YYYY-MM-dd");
                String s="2017-03-02";
                Date date= Date.valueOf(s);
                Device device=new Device(2222,1,1,date,date);
                model.saveDevice(this, "yk", device, new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result!=null){
                            Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                break;
            case R.id.down:
                model.downloadWeixiu(this, "1111", 1, new OkHttpUtils.OnCompleteListener<Weixiu[]>() {
                    @Override
                    public void onSuccess(Weixiu[] result) {
                        if (result!=null){
                            Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                model.downloadXunjian(this, "1111", 1, new OkHttpUtils.OnCompleteListener<Xunjian[]>() {
                    @Override
                    public void onSuccess(Xunjian[] result) {  if (result!=null){
                        Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                    }

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                break;
            case R.id.xunjian:
                model.xunjian(this, "yk", "1111", "1", "afafaf", new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result!=null){
                            Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                break;
            case R.id.xiujun:
                model.xiujun(this, "yk", "1111", true, "aaaa", new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result!=null){
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
}
