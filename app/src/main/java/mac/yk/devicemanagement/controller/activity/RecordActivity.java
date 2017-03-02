package mac.yk.devicemanagement.controller.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.controller.fragment.fragList;
import mac.yk.devicemanagement.util.ActivityUtils;

public class RecordActivity extends AppCompatActivity {

    @BindView(R.id.weixiu)
    Button weixiu;
    @BindView(R.id.xunjian)
    Button xunjian;
    Fragment fragment1;
    Fragment fragment2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        int id = getIntent().getIntExtra("id", 0);
        if (id == 0) {
            finish();
        } else {
            fragment1= new fragList();
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            bundle.putBoolean("flag",true);
            fragment1.setArguments(bundle);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment1, R.id.fl);

            fragment2= new fragList();
            Bundle bundle1 = new Bundle();
            bundle1.putInt("id", id);
            bundle1.putBoolean("flag",true);
            fragment2.setArguments(bundle1);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment2, R.id.fl);
        }

    }


    @OnClick({R.id.weixiu, R.id.xunjian})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.weixiu:
                weixiu.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                xunjian.setBackgroundColor(Color.GRAY);
                ActivityUtils.changeFragment(getSupportFragmentManager(),fragment1,R.id.fl);
                break;
            case R.id.xunjian:
                xunjian.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                weixiu.setBackgroundColor(Color.GRAY);
                ActivityUtils.changeFragment(getSupportFragmentManager(),fragment2,R.id.fl);
                break;
        }
    }
}
