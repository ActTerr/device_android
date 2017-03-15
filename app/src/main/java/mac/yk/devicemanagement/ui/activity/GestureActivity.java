package mac.yk.devicemanagement.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.util.SpUtil;

public class GestureActivity extends AppCompatActivity {
    boolean isStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);
        isStart= SpUtil.getFlag(this);

    }
}
