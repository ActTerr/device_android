package mac.yk.devicemanagement.ui.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.OkHttpUtils;
import mac.yk.devicemanagement.util.TestUtil;

import static android.view.View.inflate;
import static mac.yk.devicemanagement.R.id.diantai;

public class SaveActivity extends AppCompatActivity implements View.OnClickListener {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ButterKnife.bind(this);
        model = TestUtil.getData();
        id = getIntent().getStringExtra("id");
        if (id == null) {
            finish();
        }
        v = inflate(this, R.layout.item_name_popu, null);
        v2 = inflate(this, R.layout.item_status_popu, null);

    }

    public void onSave(View view) {
        final Device device = new Device(Integer.parseInt(id), ConvertUtils.getDname(name.getText().toString()),
                ConvertUtils.getStatus(status.getText().toString()),
                ConvertUtils.String2Date(chuchang.getText().toString()),
                ConvertUtils.String2Date(xunjian.getText().toString()));
        L.e("main", "save:" + name.getText().toString());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();

        model.saveDevice(this, MyApplication.getInstance().getUserName(), device, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                progressDialog.dismiss();
                L.e("main",result.toString());
                if (result != null && result.getRetCode() == I.RESULT.SUCCESS) {
                    Toast.makeText(SaveActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    MFGT.gotoDetailActivity(SaveActivity.this, device);
                    L.e("main", device.toString());
                    finish();
                } else {
                    Toast.makeText(SaveActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                Toast.makeText(SaveActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showPopuWindow(View v) {
        pop = new PopupWindow(v, ConvertUtils.dp2px(this, 100), ConvertUtils.dp2px(this, 150));
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
                    name.setText("diantai");
                    break;
                case R.id.jikongqi:
                    name.setText("机控器");
                    break;
                case R.id.qukongqi:
                    name.setText("区控器");
                    break;
                case R.id.dianchi:
                    name.setText("dianchi");
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
                        status.setText("weixiu");
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
        int height=ConvertUtils.dp2px(this, 150);
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
