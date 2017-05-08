package mac.yk.devicemanagement.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Status;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.ui.fragment.fragDetail;
import mac.yk.devicemanagement.util.ActivityUtils;
import mac.yk.devicemanagement.util.ExceptionFilter;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.ToastUtil;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class DetailActivity extends BaseActivity {
    ProgressDialog progressDialog;
    String[] data;
    Activity context;
    boolean isDianchi = false;
    fragDetail fragD;
    Dialog dialog;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawLayout)
    DrawerLayout drawLayout;

    Status mStatus;
    /**
     * dialog
     */

    String id;

    boolean isBaofei = false;
    @BindView(R.id.netView)
    TextView mTv;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_currency);
        ButterKnife.bind(this);
        context = this;
        progressDialog = new ProgressDialog(context);
        dialog = new Dialog(context);
        data= (String[]) getIntent().getSerializableExtra("deviceOld");
        MyApplication.getInstance().setData(data);
        mStatus=new Status(data[0],data[11]);
        MyApplication.setStatus(mStatus);
//        L.e("main", "detail:" + deviceOld.toString());
        if (data == null) {
            MFGT.finish(context);
        } else {
            id = String.valueOf(data[0]);
            if (data[2].equals("电池")) {
                isDianchi = true;
                MyApplication.setFlag(true);
            }
            if (mStatus.getStatus().equals("报废")) {
                isBaofei = true;
            }
        }
        user=MyApplication.getInstance().getUser();
        setTitle("设备详情");
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        fragD = new fragDetail();
        Bundle bundle=new Bundle();
        bundle.putStringArray("data",data);
        fragD.setArguments(bundle);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragD, R.id.frame);
        if (navView != null) {
            navView.inflateMenu(getMenu());
            setUpNavView(navView);
            ImageView imageView = (ImageView) navView.getHeaderView(0).findViewById(R.id.avatar);
            TextView textView = (TextView) navView.getHeaderView(0).findViewById(R.id.nav_name);
            textView.setText(MyApplication.getInstance().getUser().getName());
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
        Log.e("main", "setArgument执行");
    }

    private int getMenu() {

        switch (user.getGrade()){
            case 0:
            case 1:
                if (isDianchi){
                    return R.menu.menu_battery_detail;
                }
                return R.menu.menu_device_detail;
            case 2:
                return R.menu.menu_service_detail;
        }
        return 0;
    }


    private void setUpNavView(NavigationView navView) {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (isBaofei) {
                    Toast.makeText(context, "设备已报废！不可操作！", Toast.LENGTH_SHORT).show();
                }
                if (item.getItemId()!=R.id.record&&user.getAuthority()==0){
                    ToastUtil.showToast(context,"该帐号没有权限操作！");
                }
                switch (item.getItemId()) {
                    case R.id.beiyong:
                        if (!mStatus.getStatus().equals("运行")){
                            ToastUtil.showcannotControl(context);
                        }else {
                            postControl("备用");
                        }
                        break;
                    case R.id.daiyong:
                        if (!mStatus.getStatus().equals("备用")){
                            ToastUtil.showcannotControl(context);
                        }else {
                            postControl("待用");
                        }
                        break;
                    case R.id.yunxing:
                        if (!mStatus.getStatus().equals("待用")){
                            ToastUtil.showcannotControl(context);
                        }else {
                            postControl("运行");
                        }
                        break;
                    case R.id.xunjian:
                        if (!mStatus.getStatus().equals("备用")){
                            ToastUtil.showcannotControl(context);
                        }else {
                            showXunJianDialog();
                        }
                        break;
                    case R.id.xiujun:
                        if (!mStatus.getStatus().equals("维修")){
                            ToastUtil.showcannotControl(context);
                        }else {
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
                        if (!mStatus.getStatus().equals("使用")){
                         ToastUtil.showcannotControl(context);
                        }else {
                            postControlD(I.CONTROL_D.SHIYONG);
                        }
                        break;
                    case R.id.Ddaiyong:
                        if (!mStatus.getStatus().equals("待用")){
                            ToastUtil.showcannotControl(context);
                        }else {
                            postControlD(I.CONTROL_D.D_DAIYONG);
                        }
                        break;
                    case R.id.weixiu:
                        if (!mStatus.getStatus().equals("待修")){
                            ToastUtil.showcannotControl(context);
                        }else {
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
        dialog.show();
        ApiWrapper<ServerAPI> wrapper=new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().controlD(s,mStatus.getDid())
                .compose(wrapper.<String>applySchedulers())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        if(ExceptionFilter.filter(context,e)){
                            ToastUtil.showToast(context,"操作失败");
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        dialog.dismiss();
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
            dialog.dismiss();
            progressDialog.show();
            ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
            subscription = wrapper.targetClass(ServerAPI.class).getAPI().baofei(user.getName()
                    ,  id, remark.getText().toString())
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
        dialog.setContentView(baofei.getV());
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

        public xiujunHoler() {
            v = View.inflate(context, R.layout.dialog_currency, null);
            ButterKnife.bind(this, v);
        }

        public View getV() {
            return v;
        }

        @OnClick({R.id.cb_no, R.id.cb_yes, R.id.btn_commit})
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
                    dialog.dismiss();
                    progressDialog.show();
                    postXiuJu(translate, remark.getText().toString());
                    break;
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

        public xunjianHolder() {
            v = View.inflate(context, R.layout.dialog_currency, null);
            ButterKnife.bind(this, v);
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
            dialog.setContentView(xiujunHoler.getV());
            dialog.setTitle(null);
            dialog.show();
        }
    }

    private void postXiuJu(boolean translate, String remark) {
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        subscription = wrapper.targetClass(ServerAPI.class).getAPI()
                .xiujun("", id, translate, remark)
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
            dialog.setContentView(xunjianHolder.getV());
            dialog.setTitle(null);
            dialog.show();
        }
    }

    private void postXunJian(final String status, String remark) {
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        subscription = wrapper.targetClass(ServerAPI.class).getAPI()
                .xunjian("", id, status, remark)
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
