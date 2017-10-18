package mac.yk.devicemanagement.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.customdialog.CustomDialog;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.DetailAdapter;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.ToastUtil;
import mac.yk.devicemanagement.widget.loodView;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class DeviceDetailActivity extends BaseActivity {
    CustomDialog progressDialog;
    boolean isBattery = false;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawLayout)
    DrawerLayout drawLayout;

    @BindView(R.id.loodView)
    loodView lllView;
    @BindView(R.id.rv)
    RecyclerView rv;
    String[] data;


    DetailAdapter adapter;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    Context context;
    Dialog dialog;
    /**
     * progressDialog
     */

    String id;

    boolean isScrap = false;
    @BindView(R.id.netView)
    TextView mTv;
    User user;

    boolean isFromList;
    boolean backToRecord;
    String status;
    @BindView(R.id.testbar)
    Toolbar toolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_device_detail);
        ButterKnife.bind(this);
        init();


        Log.e("main", "setArgument执行");
    }

    private void initView() {
        setTitle("设备详情");
        status = data[11];
        id = String.valueOf(data[0]);
        if (data[2].contains("电池")) {
            isBattery = true;
            MyMemory.getInstance().setFlag(true);
        }
        if (status.equals("报废")) {
            isScrap = true;
        }
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        initNav();
        adapter = new DetailAdapter(context, data);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(context, 1));
        collapsingToolbar.setTitle("设备状态："+status);

    }


    private void init() {
        user = MyMemory.getInstance().getUser();
        context = this;
        dialog = new Dialog(context);
        progressDialog = CustomDialog.create(context,"加载中...",false,null);
        String did = getIntent().getStringExtra("Did");
        isFromList = getIntent().getBooleanExtra("isFromList", false);
        backToRecord = getIntent().getBooleanExtra("isBack", false);
        if (backToRecord) {
            data = MyMemory.getInstance().getData();
            if (data != null) {
                initView();
            }
        } else {
            getDevice(did);
        }
    }

    private int getMenu() {

        switch (user.getGrade()) {
            case 0:
            case 1:
                if (isBattery) {
                    return R.menu.menu_battery_detail;
                }
                return R.menu.menu_device_detail;
            case 2:
                return R.menu.menu_service_detail;
        }
        return 0;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        lllView.destory();
    }

    private void initNav() {

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
    }


    private void getDevice(String id) {
        progressDialog.show();
        ApiWrapper<ServerAPI> network = new ApiWrapper<>();
        subscription = network.targetClass(ServerAPI.class).
                getAPI().check(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS)
                .compose(network.<String[]>applySchedulers())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        if (ExceptionFilter.filter(context, e)) {
                            L.e(TAG, "gotoSave");
                        }
                    }

                    @Override
                    public void onNext(String[] devices) {
                        progressDialog.dismiss();
                        data = devices;
                        lllView.initCount(data[2]);
                        L.e(TAG, "devName:" + data[2]);

                        MyMemory.getInstance().setData(devices);
                        initView();
                    }
                });
    }


    private void setUpNavView(NavigationView navView) {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (isScrap) {
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
                    case R.id.spare:
                        if (!status.equals("运行")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            postControl("备用");
                        }
                        break;
                    case R.id.inactive:
                        if (!status.equals("备用")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            showDaiyong();
                        }
                        break;
                    case R.id.function:
                        if (!status.equals("待用")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            postControl("运行");
                        }
                        break;
                    case R.id.check:
                        if (!status.equals("备用")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            showCheckDialog();
                        }
                        break;
                    case R.id.repair:
                        if (!status.equals("维修")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            showXiujunDialog();
                        }
                        break;
                    case R.id.record:
                        MFGT.gotoRecordActivity(context, data[0]);
                        MFGT.finish((Activity) context);
                        break;
                    case R.id.scrap:
                        postScrap();
                        break;
                    case R.id.using:
                        if (status.equals("使用")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            postControlD(I.CONTROL_BAT.USING);
                        }
                        break;
                    case R.id.bat_inactive:
                        if (status.equals("待用")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            postControlD(I.CONTROL_BAT.BAT_INACTIVE);
                        }
                        break;
                    case R.id.service:
                        if (!status.equals("待修")) {
                            ToastUtil.showcannotControl(context);
                        } else {
                            postControl("维修");
                        }
                        break;
                    case R.id.charging:
                        postControlD(I.CONTROL_BAT.CHARGE);

                        break;
                }
                item.setChecked(true);
                drawLayout.closeDrawers();
                return false;
            }
        });
    }

    private void showDaiyong() {
        DaiYongHolder inactive = new DaiYongHolder();
        dialog.setContentView(inactive.v);
        dialog.setTitle(null);
        dialog.show();
    }

    class DaiYongHolder {
        View v;
        @BindView(R.id.et_use_local)
        EditText etUseLocal;

        public DaiYongHolder() {
            v = View.inflate(context, R.layout.dialog_inactive, null);
            ButterKnife.bind(this, v);
        }

        @OnClick(R.id.btn_commit)
        public void onClick() {
            dialog.dismiss();
            postDaiyong(etUseLocal.getText().toString());
        }
    }

    private void postDaiyong(final String local) {
        progressDialog.show();
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().inactive(id, local)
                .compose(wrapper.<String>applySchedulers())
                .subscribeOn(Schedulers.io())
                .timeout(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
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
                        ToastUtil.showControlSuccess(context);
                        progressDialog.dismiss();
                        refreshStatus(s);

                    }
                });
    }

    public void refreshStatus(String status) {
        this.status = status;
        data[11]=status;
        adapter.notifyDataSetChanged();
        collapsingToolbar.setTitle("设备状态："+status);
    }


    public void refreshUsePosition(String status, String local) {
        data[11] = status;
        data[8] = local;
        adapter.notifyDataSetChanged();
    }


    private void postControlD(String s) {
        progressDialog.show();
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().controlD(s, data[0])
                .compose(wrapper.<String>applySchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                        ToastUtil.showControlSuccess(context);
                        progressDialog.dismiss();
                        refreshStatus(s);
                    }
                });
    }


    public class ScrapHolder {
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

        public ScrapHolder() {
            v = View.inflate(context, R.layout.dialog_currency, null);
            ButterKnife.bind(this, v);

            title.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);
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
            dialog.dismiss();
            progressDialog.show();
            ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
            subscription = wrapper.targetClass(ServerAPI.class).getAPI().scrap(user.getName(), data[2]
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
                            ToastUtil.showControlSuccess(context);
                            refreshStatus(s);
                        }
                    });
        }
    }

    private void postScrap() {
        ScrapHolder scrap = new ScrapHolder();

        dialog.setContentView(scrap.getV());
        dialog.setTitle(null);
        dialog.show();
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
            ToastUtil.showControlSuccess(context);
            refreshStatus(s);

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


    public class repairHolder {
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
        boolean translate = false;

        public repairHolder() {
            v = View.inflate(context, R.layout.dialog_currency, null);
            ButterKnife.bind(this, v);
            initPopupWindow();
        }

        private void initPopupWindow() {
            View view = View.inflate(context, getViewId(), null);
            initPopupHolder(view, showSelect);
            popupWindow = new PopupWindow(view, ConvertUtils.dp2px(context, 50),
                    ConvertUtils.dp2px(context, ConvertUtils.dp2px(context, 100)));

            popupWindow.setTouchable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());

        }

        private void initPopupHolder(View v, ImageView iv) {
            switch (data[2]) {
                case "手持台":
                    new shouchitaiHolder(v, iv);
                    break;
                case "机控器":
                    new jikongqiHolder(v, iv);
                    break;
                case "区控器":
                    new qukongqiHolder(v, iv);
                    break;
            }
        }

        private int getViewId() {
            switch (data[2]) {
                case "手持台":
                    return R.layout.popu_diantai;
                case "机控器":
                    return R.layout.popu_machine_controller;
                case "区控器":
                    return R.layout.popu_zone_controller;
            }
            return 0;
        }

        public View getV() {
            return v;
        }

        @OnClick({R.id.cb_no, R.id.cb_yes, R.id.btn_commit, R.id.show_select})
        public void onClick(View view) {

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
                    dialog.dismiss();
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
            ImageView iv;

            public shouchitaiHolder(View v, ImageView iv) {
                this.v = v;
                this.iv = iv;
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
                        iv.setImageResource(R.mipmap.up);
                }
            }
        }

        class jikongqiHolder {
            View v;
            ImageView iv;

            public jikongqiHolder(View v, ImageView iv) {
                this.v = v;
                this.iv = iv;
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
                        iv.setImageResource(R.mipmap.up);
                }
            }
        }

        class qukongqiHolder {
            View v;
            ImageView iv;

            public qukongqiHolder(View v, ImageView iv) {
                this.v = v;
                this.iv = iv;
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
                        iv.setImageResource(R.mipmap.up);
                }
            }
        }


    }

    public class checkHolder {

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

        public checkHolder() {
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
                    dialog.dismiss();
                    String status;
                    if (cbYes.isChecked()) {
                        status = "良好";
                    } else {
                        status = "异常";
                    }
                    postCheck(status, remark.getText().toString());
                    break;
            }
        }
    }

    private void showXiujunDialog() {
        if (!isBattery) {
            repairHolder repairHoler = new repairHolder();
            dialog.setContentView(repairHoler.getV());
            dialog.setTitle(null);
            dialog.show();
        }
    }

    private void postXiuJu(boolean translate, String type, String remark) {
        progressDialog.show();
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        subscription = wrapper.targetClass(ServerAPI.class).getAPI()
                .repair(user.getName(), id, translate, type, remark)
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
                        ToastUtil.showControlSuccess(context);
                        refreshStatus(s);
                    }
                });
    }

    private void showCheckDialog() {
        if (!isBattery) {
            checkHolder checkHolder = new checkHolder();
            dialog.setContentView(checkHolder.getV());
//            dialog.setTitle();
            dialog.show();
        }
    }

    private void postCheck(final String status, String remark) {
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        subscription = wrapper.targetClass(ServerAPI.class).getAPI()
                .check(user.getName(), id, status, remark, ConvertUtils.getServiceStation(user.getUnit()))
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
                        ToastUtil.showControlSuccess(context);
                        refreshStatus(s);
                    }
                });

    }

    @Override
    public void onBackPressed() {
        MFGT.gotoMainActivity(context);
        MFGT.finish((Activity) context);
    }
}
