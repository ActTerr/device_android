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

public class CheckViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.checkDate)
    public
    TextView checkDate;

    @BindView(R.id.user)
            public
    TextView user;

    @BindView(R.id.cause)
            public
    TextView cause;

    @BindView(R.id.status)
            public
    TextView status;
    public CheckViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
