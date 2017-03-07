package mac.yk.devicemanagement.controller.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.controller.fragment.fragList;
import mac.yk.devicemanagement.util.ActivityUtils;
import mac.yk.devicemanagement.util.MFGT;

public class RecordActivity extends AppCompatActivity {

    Fragment fragment1;
    Fragment fragment2;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawLayout)
    DrawerLayout drawLayout;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        ButterKnife.bind(this);
        context=this;

        int id = getIntent().getIntExtra("id", 0);
        if (id == 0) {
            finish();
        } else {
            init();
            fragment1 = new fragList();
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            bundle.putBoolean("flag", true);
            fragment1.setArguments(bundle);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment1, R.id.frame);

            fragment2 = new fragList();
            Bundle bundle1 = new Bundle();
            bundle1.putInt("id", id);
            bundle1.putBoolean("flag", true);
            fragment2.setArguments(bundle1);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment2, R.id.frame);
        }

    }

    private void init() {
        setSupportActionBar(toolBar);
        ActionBar ab=getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        if (navView != null) {
            setUpNavView(navView);
            navView.inflateMenu(R.menu.menu_record);
            ImageView imageView= (ImageView) navView.getHeaderView(0).findViewById(R.id.avatar);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoSetActivity(context);
                    finish();
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
                        ActivityUtils.changeFragment(getSupportFragmentManager(), fragment1, R.id.frame);
                        break;
                    case R.id.xunjian:
                        ActivityUtils.changeFragment(getSupportFragmentManager(), fragment2, R.id.frame);
                        break;
                }
                item.setChecked(true);
                drawLayout.closeDrawers();
                return true;
            }
        });
    }



}
