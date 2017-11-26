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
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.ui.fragment.RecordFragment;
import mac.yk.devicemanagement.util.ActivityUtils;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;

public class RecordActivity extends BaseActivity {

    RecordFragment serviceFragment,checkFragment;

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawLayout)
    DrawerLayout drawLayout;
    Context context;
    String Did;
    boolean isService=true;
    @BindView(R.id.netView)
    TextView mTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        ButterKnife.bind(this);

        context=this;

        Did=getIntent().getStringExtra("Did");

        if (Did ==null) {
            MFGT.finish((Activity) context);
        } else {
            init();
            serviceFragment = (RecordFragment) getSupportFragmentManager().findFragmentById(R.id.frame);
            Bundle bundle1 = new Bundle();
            bundle1.putString("id", Did); 
            bundle1.putBoolean("flag", true);
            if (serviceFragment==null){
                Log.e("main","fragment从空被赋值");
                serviceFragment=new RecordFragment();
            }
            serviceFragment.setArguments(bundle1);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),serviceFragment,R.id.frame);



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
            textView.setText(MyMemory.getInstance().getUser().getAccounts());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoSetActivity(context);
                }
            });
        }
    }

    /**
     * 懒加载fragment
     * @param navView
     */
    private void setUpNavView(final NavigationView navView) {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.service:
                        if (!isService){
                            ActivityUtils.changeFragment(getSupportFragmentManager(),serviceFragment,checkFragment);
                            isService=true;
                            toolBar.setTitle("维修记录");

                        }
                        break;
                    case R.id.check:
//                        checkFragment= (RecordFragment) getSupportFragmentManager().findFragmentById(R.id.frame);
                        if(!getSupportFragmentManager().getFragments().contains(checkFragment)){
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("id", String.valueOf(Did));
                            bundle2.putBoolean("flag", false);
                            if (checkFragment==null){
                                checkFragment=new RecordFragment();
                            }
                            checkFragment.setArguments(bundle2);
                            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), checkFragment, R.id.frame);
                        }
                        if (isService){
                            ActivityUtils.changeFragment(getSupportFragmentManager(),checkFragment,serviceFragment);
                            isService=false;
                            toolBar.setTitle("巡检记录");
                        }
                        break;
                }
                item.setChecked(true);
                drawLayout.closeDrawers();
                return false;
            }
        });
    }


    /**
     * 重写了Activity的该方法,会在点击menu的时候回调，返回true则不往下传递了，false则传递给fragment
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawLayout.openDrawer(GravityCompat.START);
                return true;
        }
        L.e("test",super.onOptionsItemSelected(item)+"");
        return false;
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
        MFGT.gotoDetailActivity(context, true,true,Did);
        MFGT.finish((Activity) context);
    }
}
