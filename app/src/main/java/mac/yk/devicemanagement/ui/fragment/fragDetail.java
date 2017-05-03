package mac.yk.devicemanagement.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.DetailAdapter;
import mac.yk.devicemanagement.bean.DeviceOld;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.widget.loodView;

/**
 * Created by mac-yk on 2017/3/3.
 */

public class fragDetail extends BaseFragment implements Observer {

    @BindView(R.id.deviceName)
    TextView deviceName;
    @BindView(R.id.detail)
    TextView detail;
    Observer observer;
    DeviceOld deviceOld;
    @BindView(R.id.loodView)
    loodView lllView;
    @BindView(R.id.rv)
    RecyclerView rv;
    ArrayList<String> data;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_detail, container, false);
        ButterKnife.bind(this, view);
        observer = this;
        deviceOld = MyApplication.getDeviceOld();
        L.e("main", "fragdetail:" + deviceOld.toString());
        deviceName.setText(ConvertUtils.getDname(deviceOld.getDname()));
        detail.setText(deviceOld.toString());
        deviceOld.addObserver(observer);
        data=new ArrayList<>();
        rv.setAdapter(new DetailAdapter(getContext(),data));
        return view;
    }


    @Override
    public void update(Observable o, Object arg) {
        deviceOld = (DeviceOld) o;
        detail.setText(deviceOld.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lllView.destory();
        deviceOld.deleteObserver(this);
    }
}
