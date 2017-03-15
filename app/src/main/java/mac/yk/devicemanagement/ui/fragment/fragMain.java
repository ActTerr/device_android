package mac.yk.devicemanagement.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.I;
import mac.yk.devicemanagement.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragMain extends Fragment {


    public fragMain() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.frag_main, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    public void scan(int id) {
        getActivity().startActivityForResult(new Intent(getActivity(), CaptureActivity.class), id);
    }

    @OnClick(R.id.saoma)
    public void onClick() {
        scan(I.CONTROL.START);
    }
}