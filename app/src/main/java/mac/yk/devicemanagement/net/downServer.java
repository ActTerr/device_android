package mac.yk.devicemanagement.net;

import java.util.ArrayList;

import mac.yk.devicemanagement.bean.Attachment;
import mac.yk.devicemanagement.bean.FileEntry;
import rx.Observable;

/**
 * Created by mac-yk on 2017/5/25.
 */

public class downServer implements IDown {

    @Override
    public Observable<ArrayList<Attachment>> getAttachments(long nid) {
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        return     wrapper.targetClass(ServerAPI.class).getAPI().getAttachment(nid)
                .compose(wrapper.<ArrayList<Attachment>>applySchedulers());
    }

    @Override
    public Observable<String> deleteAttachment(FileEntry entry) {
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();

        return wrapper.targetClass(ServerAPI.class).getAPI().deleteAttachment(entry.getFileName())
                .compose(wrapper.<String>applySchedulers());
    }

    @Override
    public  Observable<String>  updateAttachment(FileEntry entry, String text, String type) {
        ApiWrapper<ServerAPI> wrapper = new ApiWrapper<>();
        return wrapper.targetClass(ServerAPI.class).getAPI()
                .updateAttachment(entry.getAid(),entry.getFileName(),text,type)
                .compose(wrapper.<String>applySchedulers());
    }


}
