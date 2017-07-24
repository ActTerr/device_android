package mac.yk.devicemanagement.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.customdialog.CustomDialog;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.SpUtil;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserActivity extends BaseActivity {

    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.lock)
    ImageView lock;
    @BindView(R.id.shoushiText)
    TextView shoushiText;
    @BindView(R.id.right_btn)
    ImageView rightBtn;

    Context context;
    CustomDialog pd;
    @BindView(R.id.netView)
    TextView netView;
    @BindView(R.id.open)
    Button open;
    @BindView(R.id.close)
    Button close;
    @BindView(R.id.rlPasswd)
    RelativeLayout rlPasswd;
    @BindView(R.id.logOut)
    Button logOut;
    @BindView(R.id.activity_set)
    LinearLayout activitySet;
    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.unit)
    TextView unit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        context = this;
        pd = CustomDialog.create(context,"加载中...",false,null);
        User u=MyMemory.getInstance().getUser();
        user.setText(u.getName());
        String station;
        if (u.getGrade()==1){
            station= ConvertUtils.getUnitName(u.getUnit());
        }else if(u.getGrade()==0){
            station="哈尔滨铁路局";
        }else {
            station=ConvertUtils.getServiceStation(u.getUnit());
        }
        unit.setText(station);
        initNightModeButton();
    }

    private void initNightModeButton() {
        boolean nightMode=SpUtil.getNightMode(context);
        if (nightMode){
            open.setVisibility(View.INVISIBLE);
            close.setVisibility(View.VISIBLE);
            btnBack.setBackground(getResources().getDrawable(R.drawable.back));
        }else {
            close.setVisibility(View.INVISIBLE);
            open.setVisibility(View.VISIBLE);
            btnBack.setBackground(getResources().getDrawable(R.drawable.backw));
        }
    }

    @OnClick({R.id.rlUser, R.id.rlPasswd, R.id.logOut, R.id.auxiliary, R.id.open, R.id.close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlUser:
                Toast.makeText(this, "账号不可修改", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rlPasswd:
                MFGT.gotoGestureActivity(context);
                break;
            case R.id.auxiliary:
                Intent intent = new Intent(this, AuxiliaryActivity.class);
                startActivity(intent);
            case R.id.logOut:
                pd.show();
                ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
                subscription = wrapper.targetClass(ServerAPI.class).getAPI().logOut(MyMemory.getInstance().getUser().getName())
                        .compose(wrapper.<String>applySchedulers())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                pd.dismiss();
                                if (ExceptionFilter.filter(context, e)) {
                                    ToastUtil.showToast(context, "请求失败");
                                }

                            }

                            @Override
                            public void onNext(String s) {
                                pd.dismiss();
                                MyMemory.getInstance().setUser(null);
                                SpUtil.saveLoginUser(context, null);
                                MFGT.gotoLoginActivity(context);
                                MFGT.finish((Activity) context);
                            }
                        });
                break;
            case R.id.open:
                setCheckOpen();
                break;
            case R.id.close:
                setCheckClose();
                break;
        }
    }

    private void setCheckClose() {
        SpUtil.setNightMode(context, false);
        close.setVisibility(View.INVISIBLE);
        open.setVisibility(View.VISIBLE);
        btnBack.setBackground(getResources().getDrawable(R.drawable.backw));
    }

    private void setCheckOpen() {
        SpUtil.setNightMode(context, true);
        open.setVisibility(View.INVISIBLE);
        close.setVisibility(View.VISIBLE);
        btnBack.setBackground(getResources().getDrawable(R.drawable.back));

    }


}
