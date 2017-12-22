package miituo.com.miituo.data;

/**
 * Created by john.cristobal on 05/12/17.
 */

public class imagenClass {

    int type;
    String policyid;
    String policyfolio;
    byte[] image;

    public imagenClass(int type, String policyid, String policyfolio, byte[] image) {
        this.type = type;
        this.policyid = policyid;
        this.policyfolio = policyfolio;
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPolicyid() {
        return policyid;
    }

    public void setPolicyid(String policyid) {
        this.policyid = policyid;
    }

    public String getPolicyfolio() {
        return policyfolio;
    }

    public void setPolicyfolio(String policyfolio) {
        this.policyfolio = policyfolio;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
