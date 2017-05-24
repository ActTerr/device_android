package mac.yk.devicemanagement.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipaulpro.afilechooser.utils.FileUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mac.yk.devicemanagement.R;
import mac.yk.devicemanagement.adapter.AttachmentAdapter;
import mac.yk.devicemanagement.bean.Attachment;
import mac.yk.devicemanagement.net.ApiWrapper;
import mac.yk.devicemanagement.net.ServerAPI;
import mac.yk.devicemanagement.util.L;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mac-yk on 2017/5/9.
 */

public class AttachmentFragment extends BaseFragment {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.add_history)
    TextView addHistory;
    @BindView(R.id.iv_add)
    ImageView ivAdd;

    AttachmentAdapter adapter;
    Context context;

    String TAG="AttachmentFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notice, container, false);
        ButterKnife.bind(this, view);
       Long nid= getArguments().getLong("Nid",0);
        addHistory.setVisibility(View.GONE);
        context=getContext();
        adapter=new AttachmentAdapter(context);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(context));
        if (nid!=0){
            getAttachments(nid);
        }


        return view;
    }

    private void getAttachments(long nid) {

        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        wrapper.targetClass(ServerAPI.class).getAPI().getAttachment(nid)
                .compose(wrapper.<ArrayList<Attachment>>applySchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Attachment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<Attachment> attachments) {
                        adapter.getFileEntries(attachments);
                        L.e(TAG, String.valueOf(attachments.get(0).getNid()));
                        adapter.notifyDataSetChanged();

                    }
                });
    }

    @OnClick({ R.id.iv_add})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_add:
                selectFile();
                break;
        }
    }

    private void selectFile() {
        Intent getContentIntent = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(getContentIntent, "Select a file");
        Activity activity= (Activity) context;
        activity.startActivityForResult(intent, AttachmentAdapter.REQUEST_CHOOSER);
    }


}
