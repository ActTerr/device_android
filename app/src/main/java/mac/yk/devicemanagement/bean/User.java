package mac.yk.devicemanagement.bean;

/**
 * Created by mac-yk on 2017/5/3.
 */

public class User {
    String unit;
    String accounts;
    int authority;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAccounts() {
        return accounts;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "User{" +
                "unit='" + unit + '\'' +
                ", accounts='" + accounts + '\'' +
                ", authority=" + authority +
                ", type='" + type + '\'' +
                '}';
    }
}
