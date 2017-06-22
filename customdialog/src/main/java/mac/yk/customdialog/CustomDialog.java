package mac.yk.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


/**
 * Created by mac-yk on 2017/6/20.
 */

public class CustomDialog extends Dialog {
    Context context;
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dialog_pg.refresh();
        }
    };
    dialog_pg dialog_pg;
    public CustomDialog(Context context) {
        super(context);
        this.context=context;
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 当窗口焦点改变时调用
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        dialog_pg= (dialog_pg) findViewById(R.id.dialog_pg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    handler.sendEmptyMessage(1);
                    try {
                        Thread.sleep(246);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) findViewById(R.id.message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context
     *            上下文
     * @param message
     *            提示
     * @param cancelable
     *            是否按返回键取消
     * @param cancelListener
     *            按下返回键监听
     * @return
     */
    public  static CustomDialog create(Context context, CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        CustomDialog dialog= new CustomDialog(context, R.style.Custom_Progress);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_custom);
        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.message);
            txt.setText(message);
        }
        // 按返回键是否取消
        dialog.setCancelable(cancelable);
        // 监听返回键处理
        dialog.setOnCancelListener(cancelListener);
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        return dialog;
    }




}
