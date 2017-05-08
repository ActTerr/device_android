package mac.yk.devicemanagement.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;

/**
 * Created by mac-yk on 2017/5/8.
 */

public class fragStatusCount extends BaseFragment {
    ProgressDialog dialog;
    String yaer="all";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_count, container, false);
        ButterKnife.bind(this, view);
        dialog=new ProgressDialog(getContext());
        initView();
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.all){
            yaer="all";
        }else {
            yaer=String.valueOf(item.getItemId()).substring(1);
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_year,menu);
    }

    private void initView() {
    }
}
