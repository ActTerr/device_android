package mac.yk.devicemanagement.net;

import java.util.ArrayList;

import mac.yk.devicemanagement.bean.Attachment;
import mac.yk.devicemanagement.bean.FileEntry;
import rx.Observable;

/**
 * Created by mac-yk on 2017/5/25.
 */

public interface IDown {
    Observable<ArrayList<Attachment>> getAttachments(long nid);
    Observable<String> deleteAttachment(FileEntry entry);
    Observable<Long>  updateAttachment(FileEntry entry, String text);

}
