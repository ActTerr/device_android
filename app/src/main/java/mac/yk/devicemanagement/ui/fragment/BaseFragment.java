package mac.yk.devicemanagement.ui.fragment;


import android.support.v4.app.Fragment;

import rx.Subscription;

/**
 * Created by mac-yk on 2017/3/18.
 */

public class BaseFragment extends Fragment {
    public BaseFragment() {
    }

    protected Subscription subscription;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
