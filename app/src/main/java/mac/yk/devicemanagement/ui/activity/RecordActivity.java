package mac.yk.devicemanagement.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.ui.fragment.fragRecord;
import mac.yk.devicemanagement.util.ActivityUtils;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;

public class RecordActivity extends BaseActivity {

    fragRecord fragment,fragment2;

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawLayout)
    DrawerLayout drawLayout;
    Context context;
    String[] deviceOld;
    boolean isWeixiu=true;
    @BindView(R.id.netView)
    TextView mTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        ButterKnife.bind(this);
        context=this;

       deviceOld = (String[]) getIntent().getSerializableExtra("deviceOld");

        if (deviceOld ==null) {
            MFGT.finish((Activity) context);
        } else {
            init();
            fragment = (fragRecord) getSupportFragmentManager().findFragmentById(R.id.frame);
            Bundle bundle1 = new Bundle();
            bundle1.putString("id", String.valueOf(deviceOld[0]));
            bundle1.putBoolean("flag", true);
            if (fragment==null){
                Log.e("main","fragment从空被赋值");
                fragment=new fragRecord();
            }
            fragment.setArguments(bundle1);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.frame);

            fragment2 = (fragRecord) getSupportFragmentManager().findFragmentById(R.id.frame);
            Bundle bundle2 = new Bundle();
            bundle2.putString("id", String.valueOf(deviceOld[0]));
            bundle2.putBoolean("flag", false);
            if (fragment2==null){
                fragment2=new fragRecord();
            }
            fragment2.setArguments(bundle2);
//            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment2, R.id.frame);
        }

    }

    private void init() {
        setSupportActionBar(toolBar);
        ActionBar ab=getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        if (navView != null) {
            navView.inflateMenu(R.menu.menu_record_frag);
            setUpNavView(navView);
            ImageView imageView= (ImageView) navView.getHeaderView(0).findViewById(R.id.avatar);
            TextView textView= (TextView) navView.getHeaderView(0).findViewById(R.id.nav_name);
            textView.setText(MyApplication.getInstance().getUser().getName());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoSetActivity(context);
                }
            });
        }
    }

    private void setUpNavView(final NavigationView navView) {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.weixiu:
                        if (!isWeixiu){
                            ActivityUtils.changeFragment(getSupportFragmentManager(),fragment,R.id.frame);
                            isWeixiu=true;
                        }
                        L.e("main","执行切换weixiu");
                        break;
                    case R.id.xunjian:
                        if (isWeixiu){
                            ActivityUtils.changeFragment(getSupportFragmentManager(),fragment2,R.id.frame);
                            isWeixiu=false;
                        }
                        break;
                }
                item.setChecked(true);
                drawLayout.closeDrawers();
                return false;
            }
        });
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

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        setOverflowIconVisible(featureId, menu);
        return super.onMenuOpened(featureId, menu);
    }
    private void setOverflowIconVisible(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.d("OverflowIconVisible", e.getMessage());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        MFGT.gotoDetailActivity(context, deviceOld);
        MFGT.finish((Activity) context);
    }
}
