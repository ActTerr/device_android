package mac.yk.devicemanagement.bean;

/**
 * Created by mac-yk on 2017/5/3.
 */

public class User {
    String name;
    int authority;
    int grade;
    int unit;
    String accounts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getAccounts() {
        return accounts;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }
}
