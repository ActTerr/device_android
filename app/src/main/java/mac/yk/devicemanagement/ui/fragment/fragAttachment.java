package mac.yk.devicemanagement.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.AttachmentAdapter;
import mac.yk.devicemanagement.bean.Attachment;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import rx.Subscriber;

/**
 * Created by mac-yk on 2017/5/9.
 */

public class fragAttachment extends BaseFragment {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.add_history)
    TextView addHistory;
    @BindView(R.id.iv_add)
    ImageView ivAdd;

    AttachmentAdapter adapter;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notice, container, false);
        ButterKnife.bind(this, view);
       Long nid= getArguments().getLong("Nid",0);
        addHistory.setVisibility(View.GONE);
        context=getContext();
        if (nid!=0){
            getAttachments(nid);
        }


        return view;
    }

    private void getAttachments(long nid) {

        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().getAttachment(nid)
                .compose(wrapper.<ArrayList<Attachment>>applySchedulers())
                .subscribe(new Subscriber<ArrayList<Attachment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<Attachment> attachments) {
                        adapter=new AttachmentAdapter(attachments,context);
                        rv.setAdapter(adapter);
                        rv.setLayoutManager(new LinearLayoutManager(context));
                    }
                });
    }

    @OnClick({ R.id.iv_add})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_add:

                break;
        }
    }
}
