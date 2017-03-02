package mac.yk.devicemanagement.controller.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.controller.activity.tongjiActivity;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.model.Model;
import mac.yk.devicemanagement.util.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class frag_main extends Fragment {
    IModel model;
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    ProgressDialog progressDialog;
    Context context;
    @BindView(R.id.saoma)
    Button saoma;
    @BindView(R.id.tongji)
    Button tongji;
    @BindView(R.id.yujing)
    Button yujing;

    public frag_main() {
        context = getContext();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.frag_main, container, false);
        ButterKnife.bind(this, view);
        progressDialog = new ProgressDialog(context);
        model=new Model();
        builder.setTitle("预警信息")
                .setPositiveButton("已读", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return view;
    }



    @OnClick({R.id.saoma, R.id.tongji, R.id.yujing})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saoma:
                scan(I.SCAN.SAVE);
                break;
            case R.id.tongji:
                startActivity(new Intent(context,tongjiActivity.class));
                break;
            case R.id.yujing:
                progressDialog.show();
                getYujing();
                break;
        }
    }

    private void getYujing() {
        model.getYujing(context, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                progressDialog.dismiss();
                if (result!=null&&result.getRetCode()==I.SUCCESS){
                    String yujing= (String) result.getRetData();
                    builder.setMessage(yujing).show();
                }else {
                    Toast.makeText(context, "获取预警信息失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                Toast.makeText(context, "获取预警信息失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void scan(int id) {
        startActivityForResult(new Intent(context, CaptureActivity.class), id);
    }
}
