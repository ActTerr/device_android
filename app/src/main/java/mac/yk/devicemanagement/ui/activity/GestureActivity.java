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
    boolean isOpen;
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
        isOpen = SpUtil.getGesture(this);
        if (isOpen){
            setGestureOpen();
        }else {
            setGestureClose();
        }
    }

    @OnClick({R.id.close, R.id.open, R.id.manual, R.id.auto, R.id.reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close:
                setGestureClose();
                break;
            case R.id.open:
                setGestureOpen();
                break;
            case R.id.manual:
                manualImage.setVisibility(View.VISIBLE);
                autoImage.setVisibility(View.INVISIBLE);
                SpUtil.setGestureType(this, I.GESTURE.MANUAL);
                break;
            case R.id.auto:
                autoImage.setVisibility(View.VISIBLE);
                manualImage.setVisibility(View.INVISIBLE);
                SpUtil.setGestureType(this,I.GESTURE.AUTO);
                break;
            case R.id.reset:
                MFGT.gotoSetGestureActivity(this);
                break;
        }
    }

    private void setGestureClose() {
        SpUtil.setGesture(this,false);
        close.setVisibility(View.INVISIBLE);
        open.setVisibility(View.VISIBLE);
        btnBack.setBackground(getResources().getDrawable(R.drawable.backw));
    }

    private void setGestureOpen() {
        SpUtil.setGesture(this,true);
        open.setVisibility(View.INVISIBLE);
        close.setVisibility(View.VISIBLE);
        btnBack.setBackground(getResources().getDrawable(R.drawable.back));
       String passwd= com.wujay.fund.util.SpUtil.getGesPasswd(this);
        if (passwd.equals("")){
            MFGT.gotoSetGestureActivity(this);
        }
    }
}
