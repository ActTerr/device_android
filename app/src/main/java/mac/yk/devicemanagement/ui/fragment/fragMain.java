package mac.yk.devicemanagement.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragMain extends BaseFragment {


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



}
