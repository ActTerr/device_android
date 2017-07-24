package mac.yk.devicemanagement.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import mac.yk.customdialog.CustomDialog;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.DeviceResumeAdapter;
import mac.yk.devicemanagement.bean.DeviceResume;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Subscriber;

/**
 * Created by mac-yk on 2017/5/17.
 */

public class DeviceListActivity extends BaseActivity {


    ArrayList<DeviceResume> list;
    DeviceResumeAdapter deviceResumeAdapter;
    boolean isMore=true;
    int page=0;
    Context context;
    CustomDialog pd;
    RecyclerView rv;
    GridLayoutManager gl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        rv= (RecyclerView) findViewById(R.id.rv);
        context=this;
        pd= CustomDialog.create(context,"加载中...",false,null);
        Intent intent=getIntent();
        String unit= String.valueOf(intent.getIntExtra("unit",0));
        String category=intent.getStringExtra("category");
        String status=intent.getStringExtra("status");
        String sType=intent.getStringExtra("sType");
        getData(sType,unit,category,status);
        list=new ArrayList<>();
        deviceResumeAdapter=new DeviceResumeAdapter(list,context);
        rv.setAdapter(deviceResumeAdapter);
        gl=new GridLayoutManager(context,1);
        rv.setLayoutManager(gl);
        setListener(sType,unit,category,status);

    }

    private void setListener(final String sType,final String unit, final String category, final String status) {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int lastposition=gl.findLastVisibleItemPosition();
                if (lastposition==list.size()-1&&newState==RecyclerView.SCROLL_STATE_IDLE&&isMore){
                    page++;
                    getData(sType,unit,category,status);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void getData(String sType,String unit, String category, String status) {
        String type;
        if (category.equals("固定机控器")){
            type="数字固定";
            category="机控器";
        }else if (category.equals("移动机控器")){
            type="数字移动";
            category="机控器";
        }else {
            type=null;
        }
        pd.show();
        
        ApiWrapper<ServerAPI> wrapper=new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().getDeviceResume(sType,unit,category,type,status,page)
        .compose(wrapper.<ArrayList<DeviceResume>>applySchedulers())
        .subscribe(new Subscriber<ArrayList<DeviceResume>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                pd.dismiss();
                if(ExceptionFilter.filter(context,e)){
                    ToastUtil.showException(context);
                }
                finish();
            }

            @Override
            public void onNext(ArrayList<DeviceResume> deviceResumes) {
                pd.dismiss();
                list.addAll(deviceResumes);
                deviceResumeAdapter.notifyDataSetChanged();
                if (deviceResumes.size()<10){
                    isMore=false;
                }
            }
        });
    }


}
