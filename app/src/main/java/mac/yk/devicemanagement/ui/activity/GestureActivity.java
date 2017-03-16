package mac.yk.devicemanagement.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.SpUtil;

public class GestureActivity extends AppCompatActivity {
    boolean isStart;
    @BindView(R.id.manual_image)
    ImageView manualImage;
    @BindView(R.id.auto_image)
    ImageView autoImage;
    @BindView(R.id.close)
    Button close;
    @BindView(R.id.open)
    Button open;
    @BindView(R.id.btn_back)
    RelativeLayout btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);
        ButterKnife.bind(this);
        isStart = SpUtil.getGesture(this);

    }

    @OnClick({R.id.close, R.id.open, R.id.manual_image, R.id.auto_image, R.id.reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close:
                SpUtil.setGesture(this,false);
                close.setVisibility(View.INVISIBLE);
                open.setVisibility(View.VISIBLE);
                btnBack.setBackground(getResources().getDrawable(R.drawable.backw));
                break;
            case R.id.open:
                SpUtil.setGesture(this,true);
                open.setVisibility(View.INVISIBLE);
                close.setVisibility(View.VISIBLE);
                btnBack.setBackground(getResources().getDrawable(R.drawable.back));
                MFGT.gotoSetGestureActivity(this);
                break;
            case R.id.manual_image:
                manualImage.setVisibility(View.VISIBLE);
                autoImage.setVisibility(View.INVISIBLE);
                SpUtil.setGestureType(this, I.GESTURE.MANUAL);
                break;
            case R.id.auto_image:
                autoImage.setVisibility(View.VISIBLE);
                manualImage.setVisibility(View.INVISIBLE);
                SpUtil.setGestureType(this,I.GESTURE.AUTO);
                break;
            case R.id.reset:
                MFGT.gotoSetGestureActivity(this);
                break;
        }
    }
}
