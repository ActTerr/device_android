package mac.yk.devicemanagement.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Battery;

/**
 * Created by mac-yk on 2017/5/19.
 */

public class BatteryListActivity extends BaseActivity{
    RecyclerView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        rv= (RecyclerView) findViewById(R.id.rv);
        ArrayList<Battery> list=  getIntent().getParcelableArrayListExtra("data");

    }

}
