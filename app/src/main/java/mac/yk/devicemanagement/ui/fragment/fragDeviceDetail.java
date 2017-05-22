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
import mac.yk.devicemanagement.MyMemory;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.DetailAdapter;
import mac.yk.devicemanagement.bean.Status;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.widget.loodView;

/**
 * Created by mac-yk on 2017/3/3.
 */

public class fragDeviceDetail extends BaseFragment implements Observer {

    Observer observer;
    @BindView(R.id.loodView)
    loodView lllView;
    @BindView(R.id.rv)
    RecyclerView rv;
    String[] data;
    @BindView(R.id.deviceStatus)
    TextView deviceStatus;

    Status status;

    DetailAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_detail, container, false);
        ButterKnife.bind(this, view);
        observer = this;
        status= MyMemory.getInstance().getStatus();
        data = getArguments().getStringArray("data");
        deviceStatus.setText("设备状态："+status.getStatus());
        status.addObserver(observer);
        adapter=new DetailAdapter(getContext(), data);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(getContext(),1));
        return view;
    }



    @Override
    public void update(Observable o, Object arg) {
        status= (Status) o;
        L.e("deviceDetail","statusupdate:"+status.getStatus());
        deviceStatus.setText("设备状态："+status.getStatus());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lllView.destory();
        status.deleteObserver(this);

    }


    public void refreshStatus(String status) {
        data[11]=status;
        adapter.notifyDataSetChanged();
    }


    public void refreshUsePosition(String status,String local) {
        data[11]=status;
        data[8]=local;
        adapter.notifyDataSetChanged();
    }
}
