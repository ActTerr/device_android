package mac.yk.devicemanagement.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.util.SpUtil;

public class GestureActivity extends AppCompatActivity {
    boolean isStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isStart= SpUtil.getFlag(this);
        if (isStart){
            setContentView(R.layout.activity_gesture_start);
        }else {
            setContentView(R.layout.activity_gesture_close);
        }
    }
}
