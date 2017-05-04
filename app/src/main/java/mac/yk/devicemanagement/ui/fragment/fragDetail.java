package mac.yk.devicemanagement.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.MyApplication;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.DetailAdapter;
import mac.yk.devicemanagement.bean.Status;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.widget.loodView;

/**
 * Created by mac-yk on 2017/3/3.
 */

public class fragDetail extends BaseFragment implements Observer {

    Observer observer;
    @BindView(R.id.loodView)
    loodView lllView;
    @BindView(R.id.rv)
    RecyclerView rv;
    String[] data;
    @BindView(R.id.deviceStatus)
    TextView deviceStatus;

    Status status;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_detail, container, false);
        ButterKnife.bind(this, view);
        observer = this;
        status=MyApplication.getStatus();
        data = getArguments().getStringArray("data");
        deviceStatus.setText("设备状态："+ConvertUtils.getStatus(false, Integer.parseInt(status.getStatus())));
        status.addObserver(observer);
        rv.setAdapter(new DetailAdapter(getContext(), data));
        rv.setLayoutManager(new GridLayoutManager(getContext(),1));
        return view;
    }



    @Override
    public void update(Observable o, Object arg) {
        status= (Status) o;
        deviceStatus.setText("设备状态："+ConvertUtils.getStatus(false, Integer.parseInt(status.getStatus())));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lllView.destory();
        status.deleteObserver(this);
    }


}
