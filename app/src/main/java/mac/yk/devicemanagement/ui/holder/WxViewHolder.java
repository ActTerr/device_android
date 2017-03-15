package mac.yk.devicemanagement.ui.holder;

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

    @BindView(R.id.name)
            public
    TextView wxUser;

    @BindView(R.id.remark)
    public
    TextView wxRemark;

    @BindView(R.id.translate)
    public
    TextView translate;
    public WxViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

}
