package mac.yk.devicemanagement.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Result;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.util.OkHttpUtils;
import mac.yk.devicemanagement.util.TestUtil;

/**
 * Created by mac-yk on 2017/3/3.
 */

public class fragMain2 extends Fragment {

    IModel model;
    Context context;
    @BindView(R.id.tongji)
    TextView tongji;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_tongji, container, false);
        ButterKnife.bind(this, view);
        model = TestUtil.getData();
        context = getContext();
        getMessage();
        return view;
    }
    private void getMessage() {
        model.getTongji(context,new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if (result!=null&&result.getRetCode()==0){
                    tongji.setText((CharSequence) result.getRetData());
                }else {
                    tongji.setText("请求失败，请刷新重试");
                }
            }

            @Override
            public void onError(String error) {
                tongji.setText("请求失败，请刷新重试");
            }
        });
    }

}
