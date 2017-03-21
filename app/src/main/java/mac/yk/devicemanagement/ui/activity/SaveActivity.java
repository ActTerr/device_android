package mac.yk.devicemanagement.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.TestUtil;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.inflate;
import static mac.yk.devicemanagement.R.id.diantai;

public class SaveActivity extends BaseActivity implements View.OnClickListener {
    String id;
    IModel model;

    PopupWindow pop;
    View v;
    View v2;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.nameSelect)
    ImageView nameSelect;
    @BindView(R.id.chuchang)
    TextView chuchang;
    @BindView(R.id.chuchangBtn)
    ImageView chuchangBtn;
    @BindView(R.id.xunjian)
    TextView xunjian;
    @BindView(R.id.xunjian_btn)
    ImageView xunjianBtn;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.statusSelect)
    ImageView statusSelect;
    boolean isDianchi = false;
    boolean isSelected = false;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ButterKnife.bind(this);
        context=this;
        model = TestUtil.getData();
        id = getIntent().getStringExtra("id");
        if (id == null) {
            MFGT.finish((Activity) context);
        }
        v = inflate(this, R.layout.item_name_popu, null);
        v2 = inflate(this, R.layout.item_status_popu, null);

    }

    public void onSave(View view) {
        final Device device = new Device(Integer.parseInt(id),
                ConvertUtils.getDname(name.getText().toString()),
                ConvertUtils.getStatus(status.getText().toString()),
                ConvertUtils.String2Date(chuchang.getText().toString()),
                ConvertUtils.String2Date(xunjian.getText().toString()));
        L.e("main", "save:" + name.getText().toString());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();

        ApiWrapper<ServerAPI> wrapper=new ApiWrapper<>();
        subscription=wrapper.targetClass(ServerAPI.class).getAPI().
                saveDevice(MyApplication.getInstance().getUserName(),ConvertUtils.getjson(device))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(wrapper.<String>applySchedulers())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        if ( ExceptionFilter.filter(context,e)){
                            ToastUtil.showToast(context,"请求失败!");
                        }

                    }

                    @Override
                    public void onNext(String s) {
                        progressDialog.dismiss();
                        Toast.makeText(SaveActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        MFGT.gotoDetailActivity(SaveActivity.this, device);
                        L.e("main", device.toString());
                        MFGT.finish((Activity) context);
                    }
                });
    }


    private void showPopuWindow(View v) {
        pop = new PopupWindow(v, ConvertUtils.dp2px(this, 100), ConvertUtils.dp2px(this, 140));
        pop.setOutsideTouchable(true);
        pop.setTouchable(true);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.showAsDropDown(nameSelect);
    }


    class nameHolder{
        public nameHolder() {
            ButterKnife.bind(this,v);

        }
        @OnClick({R.id.diantai, R.id.jikongqi, R.id.qukongqi, R.id.dianchi})
        public void onClick(View view) {
            switch (view.getId()) {
                case diantai:
                    name.setText("电台");
                    break;
                case R.id.jikongqi:
                    name.setText("机控器");
                    break;
                case R.id.qukongqi:
                    name.setText("区控器");
                    break;
                case R.id.dianchi:
                    name.setText("电池");
                    isDianchi = true;
                    break;
            }
            isSelected = true;
            pop.dismiss();
            statusHolder holder = new statusHolder();
        }
    }

    class statusHolder {
        @BindView(R.id.weixiu)
        TextView weixiu;

        public statusHolder() {
            ButterKnife.bind(this, v2);
            if (isDianchi){
                weixiu.setText("充电");
            }
        }

        @OnClick({R.id.beiyong, R.id.daiyong, R.id.yunxing, R.id.weixiu, R.id.yonghou})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.beiyong:
                    status.setText("备用");
                    break;
                case R.id.daiyong:
                    status.setText("待用");
                    break;
                case R.id.yunxing:
                    status.setText("运行");
                    break;
                case R.id.weixiu:
                    if (isDianchi) {
                        status.setText("充电");
                    } else {
                        status.setText("维修");
                    }
                    break;
                case R.id.yonghou:
                    status.setText("用后");
                    break;
            }
            pop.dismiss();
        }
    }

    @OnClick({R.id.nameSelect, R.id.chuchangBtn, R.id.xunjian_btn, R.id.btn_save, R.id.statusSelect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nameSelect:
                nameHolder n=new nameHolder();
                showPopuWindow(v);
                break;
            case R.id.chuchangBtn:
                pickDate(chuchang);
                break;
            case R.id.xunjian_btn:
                pickDate(xunjian);
                break;
            case R.id.btn_save:
                onSave(view);
                btnSave.setEnabled(false);
                break;
            case R.id.statusSelect:
                if (isSelected) {
                    showStatusWindow(v2);
                } else {
                    Toast.makeText(this, "请先选择设备名称", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void showStatusWindow(View v2) {
        int width= ConvertUtils.dp2px(this, 100);
        int height=ConvertUtils.dp2px(this, 140);
        L.e("main","w:"+width+" h:"+height);
        pop = new PopupWindow(v2, width,height);
        pop.setOutsideTouchable(true);
        pop.setTouchable(true);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.showAsDropDown(statusSelect);
    }

    private void pickDate(final TextView tv) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv.setText(String.format("%d-%d-%d", year, month+1, dayOfMonth));
                //月份从0开始的，所以正常显示要加1
            }
        }, 2017, 1, 12).show();
    }
}
