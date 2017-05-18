package mac.yk.devicemanagement.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.MyMemory;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Status;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.ui.fragment.fragDeviceDetail;
import mac.yk.devicemanagement.util.ActivityUtils;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class DeviceDetailActivity extends BaseActivity {
    ProgressDialog progressDialog;
    String[] data;
    Activity context;
    boolean isDianchi = false;
    fragDeviceDetail fragD;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawLayout)
    DrawerLayout drawLayout;

    Status mStatus;
    /**
     * progressDialog
     */

    String id;

    boolean isBaofei = false;
    @BindView(R.id.netView)
    TextView mTv;
    User user;

    boolean isFromList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_currency);
        ButterKnife.bind(this);
        context = this;
        progressDialog = new ProgressDialog(context);
        String did = getIntent().getStringExtra("Did");
        isFromList = getIntent().getBooleanExtra("isFromList", false);
        getDevice(did);

        Log.e("main", "setArgument执行");
    }

    private int getMenu() {

        switch (user.getGrade()) {
            case 0:
            case 1:
                if (isDianchi) {
                    return R.menu.menu_battery_detail;
                }
                return R.menu.menu_device_detail;
            case 2:
                return R.menu.menu_service_detail;
        }
        return 0;
    }

    private void initView() {
        MyMemory.getInstance().setData(data);
        mStatus = new Status(data[0], data[11]);
        MyMemory.getInstance().setStatus(mStatus);
        id = String.valueOf(data[0]);
        if (data[2].equals("电池")) {
            isDianchi = true;
            MyMemory.getInstance().setFlag(true);
        }
        if (mStatus.getStatus().equals("报废")) {
            isBaofei = true;
        }
        user = MyMemory.getInstance().getUser();
        setTitle("设备详情");
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        fragD = new fragDeviceDetail();
        Bundle bundle = new Bundle();
        bundle.putStringArray("data", data);
        fragD.setArguments(bundle);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragD, R.id.frame);
        if (navView != null) {
            navView.inflateMenu(getMenu());
            setUpNavView(navView);
            ImageView imageView = (ImageView) navView.getHeaderView(0).findViewById(R.id.avatar);
            TextView textView = (TextView) navView.getHeaderView(0).findViewById(R.id.nav_name);
            textView.setText(MyMemory.getInstance().getUser().getName());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoSetActivity(context);
                }
            });
        }
        if (isDianchi) {

            navView.getMenu().getItem(4).setTitle("充电");
            navView.getMenu().getItem(5).setTitle("充满");
        } else {
            //如果不是电池该项不可见
            navView.getMenu().getItem(3).setVisible(false);
        }
    }

    private void getDevice(String id) {
        progressDialog.show();
        ApiWrapper<ServerAPI> network = new ApiWrapper<>();
        subscription = network.targetClass(ServerAPI.class).
                getAPI().chaxun(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(network.<String[]>applySchedulers())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        if (ExceptionFilter.filter(context, e)) {
                            L.e("main", "gotoSave");
                        }
                    }

                    @Override
                    public void onNext(String[] deviceOld) {
                        data = deviceOld;
                        progressDialog.dismiss();
                        L.e("main", deviceOld.toString());
                        initView();
                    }
                });
    }


    private void setUpNavView(NavigationView navView) {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (isBaofei) {
                    Toast.makeText(context, "设备已报废！不可操作！", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (item.getItemId() != R.id.record && user.getAuthority() == 0) {
                    ToastUtil.showToast(context, "该帐号没有权限操作！");
                    return false;
                }
                if (isFromList) {
                    ToastUtil.showToast(context, "请扫描进入详情后操作！");
                    return false;
                }
                switch (item.getItemId()) {
                    case R.id.beiyong:
                        if (!mStatus.getStatus().equals("运行")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            postControl("备用");
                        }
                        break;
                    case R.id.daiyong:
                        if (!mStatus.getStatus().equals("备用")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            postControl("待用");
                        }
                        break;
                    case R.id.yunxing:
                        if (!mStatus.getStatus().equals("待用")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            postControl("运行");
                        }
                        break;
                    case R.id.xunjian:
                        if (!mStatus.getStatus().equals("备用")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            showXunJianDialog();
                        }
                        break;
                    case R.id.xiujun:
                        if (!mStatus.getStatus().equals("维修")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            showXiujunDialog();
                        }
                        break;
                    case R.id.record:
                        MFGT.gotoRecordActivity(context, mStatus.getDid());
                        MFGT.finish(context);
                        break;
                    case R.id.baofei:
                        postBaofei();
                        break;
                    case R.id.shiyong:
                        if (!mStatus.getStatus().equals("使用")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            postControlD(I.CONTROL_D.SHIYONG);
                        }
                        break;
                    case R.id.Ddaiyong:
                        if (!mStatus.getStatus().equals("待用")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            postControlD(I.CONTROL_D.D_DAIYONG);
                        }
                        break;
                    case R.id.weixiu:
                        if (!mStatus.getStatus().equals("待修")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            postControl("维修");
                        }
                        break;
                    case R.id.chongdian:
                        postControlD(I.CONTROL_D.CHONGDIAN);

                        break;
                }
                item.setChecked(true);
                drawLayout.closeDrawers();
                return false;
            }
        });
    }

    private void postControlD(String s) {
        progressDialog.show();
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().controlD(s, mStatus.getDid())
                .compose(wrapper.<String>applySchedulers())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        if (ExceptionFilter.filter(context, e)) {
                            ToastUtil.showToast(context, "操作失败");
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        progressDialog.dismiss();
                        mStatus.setStatus(s);
                    }
                });
    }


    public class BaofeiHolder {
        View v;
        @BindView(R.id.dia_title)
        TextView diaTitle;
        @BindView(R.id.cb_no)
        CheckBox cbNo;
        @BindView(R.id.cb_yes)
        CheckBox cbYes;
        @BindView(R.id.remark)
        EditText remark;

        public BaofeiHolder() {
            v = View.inflate(context, R.layout.dialog_currency, null);
            ButterKnife.bind(this, v);
            cbNo.setVisibility(View.GONE);
            cbYes.setVisibility(View.GONE);
            diaTitle.setText("报废提交");
            remark.setHint("请说明报废原因");

        }

        public View getV() {
            return v;
        }

        public void setV(View v) {
            this.v = v;
        }

        @OnClick(R.id.btn_commit)
        public void onClick(View view) {
            progressDialog.dismiss();
            progressDialog.show();
            ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
            subscription = wrapper.targetClass(ServerAPI.class).getAPI().baofei(user.getName()
                    , id, remark.getText().toString(), data[16], data[21])
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(wrapper.<String>applySchedulers())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            progressDialog.dismiss();
                            if (ExceptionFilter.filter(context, e)) {
                                ToastUtil.showcannotControl(context);
                            }
                        }

                        @Override
                        public void onNext(String s) {
                            progressDialog.dismiss();
                            mStatus.setStatus(s);
                            ToastUtil.showControlSuccess(context);
                        }
                    });
        }
    }

    private void postBaofei() {
        BaofeiHolder baofei = new BaofeiHolder();
        progressDialog.setContentView(baofei.getV());
        progressDialog.setTitle(null);
        progressDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Observer<String> obControl = new Observer<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            progressDialog.dismiss();
            if (ExceptionFilter.filter(context, e)) {
                ToastUtil.showcannotControl(context);
            }
        }

        @Override
        public void onNext(String s) {
            progressDialog.dismiss();
            mStatus.setStatus(s);
            ToastUtil.showControlSuccess(context);
        }
    };

    private void postControl(String status) {
        progressDialog.show();
        ApiWrapper<ServerAPI> network = new ApiWrapper<>();
        network.targetClass(ServerAPI.class).getAPI().control(status, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(network.<String>applySchedulers())
                .subscribe(obControl);
    }

    private int getControl(int cid) {
        switch (cid) {
            case R.id.beiyong:
                return I.CONTROL.BEIYONG;
            case R.id.daiyong:
                return I.CONTROL.DAIYONG;
            case R.id.yunxing:
                return I.CONTROL.YUNXING;

        }
        return 0;
    }


    public class xiujunHoler {
        @BindView(R.id.cb_no)
        CheckBox cbNo;
        @BindView(R.id.cb_yes)
        CheckBox cbYes;
        @BindView(R.id.remark)
        EditText remark;

        View v;
        @BindView(R.id.type)
        TextView type;
        @BindView(R.id.show_select)
        ImageView showSelect;

        boolean isShow;
        PopupWindow popupWindow;

        public xiujunHoler() {
            v = View.inflate(context, R.layout.dialog_currency, null);
            ButterKnife.bind(this, v);
            initPopuWindow();
        }

        private void initPopuWindow() {
            View view = View.inflate(context, getViewId(), null);
            initpopuHolder(view);
            popupWindow = new PopupWindow(view, ConvertUtils.dp2px(context, 50),
                    ConvertUtils.dp2px(context, view.getHeight()));

            popupWindow.setTouchable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());

        }

        private void initpopuHolder(View v) {
            switch (data[2]) {
                case "手持台":
                    new shouchitaiHolder(v);
                    break;
                case "机控器":
                    new jikongqiHolder(v);
                    break;
                case "区控器":
                    new qukongqiHolder(v);
                    break;
            }
        }

        private int getViewId() {
            switch (data[2]) {
                case "手持台":
                    return R.layout.item_popu_diantai;
                case "机控器":
                    return R.layout.item_popu_jikongqi;
                case "区控器":
                    return R.layout.item_popu_qukongqi;
            }
            return 0;
        }

        public View getV() {
            return v;
        }

        @OnClick({R.id.cb_no, R.id.cb_yes, R.id.btn_commit, R.id.show_select})
        public void onClick(View view) {
            boolean translate = false;
            switch (view.getId()) {
                case R.id.cb_no:
                    cbNo.setChecked(true);
                    cbYes.setChecked(false);
                    translate = false;
                    break;
                case R.id.cb_yes:
                    cbYes.setChecked(true);
                    cbNo.setChecked(false);
                    translate = true;
                    break;
                case R.id.btn_commit:
                    progressDialog.dismiss();
                    progressDialog.show();
                    postXiuJu(translate, type.getText().toString(), remark.getText().toString());
                    break;
                case R.id.show_select:
                    if (isShow) {
                        showSelect.setImageResource(R.mipmap.up);
                        isShow = false;
                        popupWindow.dismiss();
                    } else {
                        showSelect.setImageResource(R.mipmap.down);
                        isShow = true;
                        popupWindow.showAsDropDown(showSelect);
                    }
                    break;
            }

        }

        class shouchitaiHolder {
            View v;

            public shouchitaiHolder(View v) {
                this.v = v;
                ButterKnife.bind(this, v);
            }

            @OnClick({R.id.waike, R.id.zhuban, R.id.yuyin, R.id.xinling, R.id.other})
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.waike:
                    case R.id.zhuban:
                    case R.id.yuyin:
                    case R.id.xinling:
                    case R.id.other:
                        TextView tv = (TextView) view;
                        type.setText(tv.getText());
                        popupWindow.dismiss();
                }
            }
        }

        class jikongqiHolder {
            View v;

            public jikongqiHolder(View v) {
                this.v = v;
                ButterKnife.bind(this, v);
            }


            @OnClick({R.id.waike, R.id.zhuban, R.id.dengxian, R.id.other})
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.waike:
                    case R.id.zhuban:
                    case R.id.dengxian:
                    case R.id.other:
                        TextView tv = (TextView) view;
                        type.setText(tv.getText());
                        popupWindow.dismiss();
                }
            }
        }

        class qukongqiHolder {
            View v;

            public qukongqiHolder(View v) {
                this.v = v;
                ButterKnife.bind(this, v);
            }


            @OnClick({R.id.yuyin, R.id.other})
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.yuyin:
                    case R.id.other:
                        TextView tv = (TextView) view;
                        type.setText(tv.getText());
                        popupWindow.dismiss();
                }
            }
        }


    }

    public class xunjianHolder {

        View v;
        @BindView(R.id.dia_title)
        TextView diaTitle;
        @BindView(R.id.cb_no)
        CheckBox cbNo;
        @BindView(R.id.cb_yes)
        CheckBox cbYes;
        @BindView(R.id.remark)
        EditText remark;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.ll)
        LinearLayout ll;
        @BindView(R.id.btn_commit)
        Button btnCommit;

        public xunjianHolder() {
            v = View.inflate(context, R.layout.dialog_currency, null);
            ButterKnife.bind(this, v);
            title.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);
            diaTitle.setText("设备状态");
            remark.setHint("如有问题请备注");
            cbNo.setText("异常");
            cbYes.setText("良好");
        }

        public View getV() {
            return v;
        }


        @OnClick({R.id.cb_no, R.id.cb_yes, R.id.btn_commit})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cb_no:
                    cbNo.setChecked(true);
                    cbYes.setChecked(false);
                    break;
                case R.id.cb_yes:
                    cbYes.setChecked(true);
                    cbNo.setChecked(false);
                    break;
                case R.id.btn_commit:
                    progressDialog.dismiss();
                    progressDialog.show();
                    String status;
                    if (cbYes.isChecked()) {
                        status = "1";
                    } else {
                        status = "0";
                    }
                    postXunJian(status, remark.getText().toString());
                    break;
            }
        }
    }

    private void showXiujunDialog() {
        if (!isDianchi) {
            xiujunHoler xiujunHoler = new xiujunHoler();
            progressDialog.setContentView(xiujunHoler.getV());
            progressDialog.setTitle(null);
            progressDialog.show();
        }
    }

    private void postXiuJu(boolean translate, String type, String remark) {
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        subscription = wrapper.targetClass(ServerAPI.class).getAPI()
                .xiujun(user.getName(), id, translate, type, remark)
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
                        if (ExceptionFilter.filter(context, e)) {
                            ToastUtil.showcannotControl(context);
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        progressDialog.dismiss();
                        mStatus.setStatus(s);
                        ToastUtil.showControlSuccess(context);
                    }
                });
    }

    private void showXunJianDialog() {
        if (!isDianchi) {
            xunjianHolder xunjianHolder = new xunjianHolder();
            progressDialog.setContentView(xunjianHolder.getV());
            progressDialog.setTitle(null);
            progressDialog.show();
        }
    }

    private void postXunJian(final String status, String remark) {
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        subscription = wrapper.targetClass(ServerAPI.class).getAPI()
                .xunjian(user.getName(), id, status, remark, ConvertUtils.getServiceStation(user.getUnit()))
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
                        if (ExceptionFilter.filter(context, e)) {
                            ToastUtil.showcannotControl(context);
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        progressDialog.dismiss();
                        mStatus.setStatus(s);
                        ToastUtil.showControlSuccess(context);
                    }
                });

    }

    @Override
    public void onBackPressed() {
        MFGT.gotoMainActivity(context);
        MFGT.finish(context);
    }
}
