package mac.yk.devicemanagement.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mac-yk on 2017/5/19.
 */

public class Battery implements Parcelable{
    String did;
    long start_time;
    long used_duration;
    long theory_duration;
    String status;
    String unit_id;
    String type;

    public Battery(String did, long start_time, long used_duration, long theory_duration, String status, String unit_id,String type) {
        this.did = did;
        this.start_time = start_time;
        this.used_duration = used_duration;
        this.theory_duration = theory_duration;
        this.status = status;
        this.unit_id = unit_id;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Battery() {
    }

    protected Battery(Parcel in) {
        did = in.readString();
        start_time = in.readLong();
        used_duration = in.readLong();
        theory_duration = in.readLong();
        status = in.readString();
        unit_id = in.readString();
        type=in.readString();
    }

    public static final Creator<Battery> CREATOR = new Creator<Battery>() {
        @Override
        public Battery createFromParcel(Parcel in) {
            return new Battery(in);
        }

        @Override
        public Battery[] newArray(int size) {
            return new Battery[size];
        }
    };

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getUsed_duration() {
        return used_duration;
    }

    public void setUsed_duration(long used_duration) {
        this.used_duration = used_duration;
    }

    public long getTheory_duration() {
        return theory_duration;
    }

    public void setTheory_duration(long theory_duration) {
        this.theory_duration = theory_duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(did);
        dest.writeLong(start_time);
        dest.writeLong(used_duration);
        dest.writeLong(theory_duration);
        dest.writeString(status);
        dest.writeString(unit_id);
        dest.writeString(type);
    }
}
