package mac.yk.devicemanagement.controller.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Device;

/**
 * Created by mac-yk on 2017/3/3.
 */

public class fragDetail extends Fragment {

    @BindView(R.id.deviceName)
    TextView deviceName;
//    @BindView(R.id.lunbo)
//    lunboView lunbo;
//    @BindView(R.id.zhishi)
//    zhishiqiView zhishi;
    @BindView(R.id.detail)
    TextView detail;

    Device device;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_detail, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
//        device= (Device) getArguments().getSerializable("device");
        device= MyApplication.getDevice();
        deviceName.setText(device.getName());
        detail.setText(device.toString());
//        lunbo.startPlay(zhishi);
        super.onResume();
    }
}
