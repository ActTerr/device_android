package mac.yk.devicemanagement.controller.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.controller.fragment.fragDetail;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.model.Model;
import mac.yk.devicemanagement.util.ActivityUtils;
import mac.yk.devicemanagement.util.MFGT;

import static android.R.attr.id;


public class DetailActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    IModel model;
    Device device;
    Context context;
    boolean isDianchi = false;
    fragDetail fragD;

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawLayout)
    DrawerLayout activityDetail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        device = (Device) getIntent().getSerializableExtra("device");
        if (device == null) {
            finish();
        } else {
            if (device.getName().equals("电池")) {
                isDianchi = true;
            }
        }
        setSupportActionBar(toolBar);
        ActionBar ab=getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        fragD=new fragDetail();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragD, R.id.frame);
        model = Model.getInstance();
        if (navView != null) {
            setUpNavView(navView);
            navView.inflateMenu(R.menu.menu_detail);
        }
       setArguments();
    }

    private void setArguments() {
        setArguments();
        Bundle bundle=new Bundle();
        bundle.putSerializable("device",device);
        fragD.setArguments(bundle);
    }

    private void setUpNavView(NavigationView navView) {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.daiyong:
                    case R.id.yunxing:
                    case R.id.xiujun:
                    case R.id.xunjian:
                        if (isDianchi){
                            item.setTitle("用后");
                        }
                        break;
                    case R.id.weixiu:
                        if (isDianchi){
                            item.setTitle("充电");
                        }
                        break;
                    case R.id.baofei:
                        break;
                    case R.id.record:
                        MFGT.gotoRecordActivity(context,id);
                        break;
                }
                item.setChecked(true);
                activityDetail.closeDrawers();
                return true;
            }
        });
    }


}
