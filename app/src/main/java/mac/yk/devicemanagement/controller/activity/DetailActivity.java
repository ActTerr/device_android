package mac.yk.devicemanagement.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import mac.yk.devicemanagement.R;

public class DetailActivity extends Activity {
    Button weixiu,xunjian;
    int device;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        weixiu= (Button) findViewById(R.id.weixiu);
        xunjian= (Button) findViewById(R.id.xunjian);
       device=getIntent().getIntExtra("id",0);
        if(device==R.id.dianchi){
            weixiu.setText("充电");
            xunjian.setText("用后");
        }
    }
}
