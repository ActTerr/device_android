package mac.yk.devicemanagement.bean;

import java.util.Date;

public class Scrap {
      int did,dname;
      String user,remark;
		Date scrapDate;
      public Scrap(){

	  }
	public Scrap(int did, int dname, String user, String remark) {
		super();
		this.did = did;
		this.dname = dname;
		this.user = user;
		this.remark = remark;
	}
	public int getDid() {
		return did;
	}
	public void setDid(int did) {
		this.did = did;
	}
	public int getdevName() {
		return dname;
	}
	public void setdevName(int dname) {
		this.dname = dname;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getScrapDate() {
		return scrapDate;
	}

	public void setScrapDate(Date scrapDate) {
		this.scrapDate = scrapDate;
	}

	@Override
	public String toString() {
		return "Scrap{" +
				"did=" + did +
				", dname=" + dname +
				", user='" + user + '\'' +
				", remark='" + remark + '\'' +
				", scrapDate=" + scrapDate +
				'}';
	}
}