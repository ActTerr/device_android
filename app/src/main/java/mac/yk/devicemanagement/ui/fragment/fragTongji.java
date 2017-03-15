package mac.yk.devicemanagement.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.DeviceAdapter;
import mac.yk.devicemanagement.bean.Device;
import mac.yk.devicemanagement.model.IModel;
import mac.yk.devicemanagement.util.ConvertUtils;
import mac.yk.devicemanagement.util.L;
import mac.yk.devicemanagement.util.OkHttpUtils;
import mac.yk.devicemanagement.util.TestUtil;

/**
 * Created by mac-yk on 2017/3/3.
 */

public class fragTongji extends Fragment {

    IModel model;
    Context context;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.rv)
    RecyclerView rv;

    int page = 1;
    int selected=0;
    ArrayList<Device> devices = new ArrayList<>();
    DeviceAdapter deviceAdapter;
    ArrayList<Device> currentDevices=new ArrayList<>();
    LinearLayoutManager llm;
    boolean isMore;
    ProgressDialog pd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_tongji, container, false);
        ButterKnife.bind(this, view);
        model = TestUtil.getData();
        context = getContext();
        deviceAdapter = new DeviceAdapter(context);
        llm=new LinearLayoutManager(context);
        pd=new ProgressDialog(context);
        rv.setAdapter(deviceAdapter);
        rv.setLayoutManager(llm);
        downData();
        setListener();
        setHasOptionsMenu(true);
        return view;
    }

    private void setListener() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int lastPosition=llm.findLastVisibleItemPosition();
                if (newState==RecyclerView.SCROLL_STATE_IDLE
                        &&lastPosition==deviceAdapter.getItemCount()&&isMore){
                    downData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });

    }

    private void downData() {
        pd.show();
        model.downDevice(context, page, 10, new OkHttpUtils.OnCompleteListener<Device[]>() {
            @Override
            public void onSuccess(Device[] result) {
                pd.dismiss();
                L.e("main","id:"+result[1].getDid());
                if (result != null) {
                    ArrayList<Device> list = ConvertUtils.array2List(result);
                    L.e("main","list"+list.size());
                    devices.addAll(list);
                    if (selected==0){
                        deviceAdapter.addData(list);
                    }else {
                        SetSelectedList(selected,false,list);
                    }
                    isMore=true;
                } else {
                    isMore=false;
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, "请检查网络状况!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void scan(int id) {
        getActivity().startActivityForResult(new Intent(getActivity(), CaptureActivity.class), id);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_name, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dianchi:
                selected = I.DNAME.DIANCHI;
                break;
            case R.id.diantai:
                selected = I.DNAME.DIANTAI;
                break;
            case R.id.qukongqi:
                selected = I.DNAME.QUKONGQI;
                break;
            case R.id.jikongqi:
                selected = I.DNAME.JIKONGQI;
                break;
            case R.id.action_capture:
                scan(I.CONTROL.START);
                break;
        }
        SetSelectedList(selected,true,null);
        return true;
    }

    /**
     * 减少for循环次数
     * @param selected
     * @param ischange
     * @param list
     */
    private void SetSelectedList(int selected,boolean ischange,ArrayList<Device> list) {
        ArrayList<Device> slist = new ArrayList<>();
        if (ischange){
            for (Device d : devices) {
                if (d.getDname() == selected) {
                    slist.add(d);
                }
            }
            deviceAdapter.changeData(slist);
        }else {
            for (Device d : list) {
                if (d.getDname() == selected) {
                    slist.add(d);
                }
            }
            deviceAdapter.addData(slist);
        }
    }



    @OnClick(R.id.btn_top)
    public void onClick() {
        rv.setScrollingTouchSlop(0);
    }
}
