package mac.yk.devicemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.bean.Scrap;
import mac.yk.devicemanagement.util.ConvertUtils;

/**
 * Created by mac-yk on 2017/3/13.
 */

public class ScrapAdapter extends RecyclerView.Adapter {
    ArrayList<Scrap> scraps = new ArrayList<>();
    Context context;


    public ScrapAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_scrap, null);
        ScrapHolder scrapHolder=new ScrapHolder(view);
        return scrapHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Scrap scrap=scraps.get(position);
        if (scrap!=null){
            ScrapHolder scrapHolder= (ScrapHolder) holder;
            scrapHolder.Did.setText(String.valueOf(scrap.getDid()));
            scrapHolder.devName.setText(ConvertUtils.getdevName(scrap.getdevName()));
            scrapHolder.user.setText(scrap.getUser());
            scrapHolder.ScrapDate.setText(ConvertUtils.Date2String(scrap.getScrapDate()));
            scrapHolder.remark.setText(scrap.getRemark());
        }
    }

    @Override
    public int getItemCount() {
        return scraps.size();
    }

    public void changeData(ArrayList<Scrap> slist) {
        if (scraps!=null){
            scraps.clear();
        }
        scraps.addAll(slist);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Scrap> slist) {
        scraps.addAll(slist);
        notifyDataSetChanged();
    }

    public void clear() {
        if (scraps!=null){
            scraps.clear();
        }
    }


    class ScrapHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Did)
        TextView Did;
        @BindView(R.id.devName)
        TextView devName;
        @BindView(R.id.user)
        TextView user;
        @BindView(R.id.ScrapDate)
        TextView ScrapDate;
        @BindView(R.id.remark)
        TextView remark;
        public ScrapHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
