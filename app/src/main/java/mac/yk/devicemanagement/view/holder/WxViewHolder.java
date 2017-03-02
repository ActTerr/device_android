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

public class WxViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.xjDate)
    public
    TextView xjDate;

    @BindView(R.id.wxDate)
    public
    TextView wxDate;

    public WxViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

}
