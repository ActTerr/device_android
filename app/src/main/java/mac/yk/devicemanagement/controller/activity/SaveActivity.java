package mac.yk.devicemanagement.controller.activity;

import android.app.ProgressDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.model.Model;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.OkHttpUtils;

import static android.view.View.inflate;

public class SaveActivity extends AppCompatActivity {
    String id;
    IModel model;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.select)
    ImageView select;
    @BindView(R.id.chuchang)
    EditText chuchang;
    @BindView(R.id.xunjian)
    EditText xunjian;
    @BindView(R.id.zhuangtai)
    EditText zhuangtai;
    PopupWindow pop;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        if (id == null) {
            finish();
        }
        v = inflate(this, R.layout.item_popu, null);

    }

    public void onSave(View view) {
        Device device = new Device(id, name.getText().toString(), xunjian.getText().toString(), chuchang.getText().toString(), zhuangtai.getText().toString());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        model = new Model();
        model.saveDevice(this, device, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                progressDialog.dismiss();
                if (result != null && result.getRetCode() == 0) {

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

    @OnClick(R.id.select)
    public void onClick() {
        showPopuWindow(v);
    }

    private void showPopuWindow(View v) {
        pop = new PopupWindow(v, ConvertUtils.dp2px(this, 50), ConvertUtils.px2dp(this, 100));
        pop.setOutsideTouchable(true);
        pop.setTouchable(true);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.showAsDropDown(select);
    }

    @OnClick({R.id.diantai, R.id.jikongqi, R.id.qukongqi, R.id.dianchi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.diantai:
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
                break;
        }
    }
}
