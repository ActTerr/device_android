package mac.yk.devicemanagement.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.application.MyMemory;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.User;
import mac.yk.devicemanagement.db.dbUser;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.MFGT;
import mac.yk.devicemanagement.util.SpUtil;

public class Splash extends AppCompatActivity {
    Context context;
    @BindView(R.id.activity_splash)
    RelativeLayout activitySplash;
    String TAG="Splash";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        context = this;
        Animation animation= AnimationUtils.loadAnimation(this, R.anim.fade_in);
        if (animation!=null&&activitySplash!=null){

            activitySplash.setAnimation(animation);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String name = SpUtil.getLoginUser(context);
                if (name.equals("")) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    MFGT.finish((Activity) context);
                } else {
                    User user= dbUser.getInstance(context).select2(name);
                    MyMemory.getInstance().setUser(user);
                    L.e(TAG,user.toString());
                    boolean gesture = SpUtil.getGesture(context);
                    if (gesture) {
                        MFGT.gotoValidateGestureActivity((Activity) context);
                    } else {
                        gotoMainActivity();
                    }
                }
            }
        }).start();

    }

    private void gotoMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        MFGT.finish((Activity) context);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            gotoMainActivity();
        }
    }
}
