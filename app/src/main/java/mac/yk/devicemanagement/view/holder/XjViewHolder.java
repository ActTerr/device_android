package mac.yk.devicemanagement.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;

/**
 * Created by mac-yk on 2017/3/2.
 */

public class XjViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.xJianDate)
    public
    TextView xJianDate;

    public XjViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
