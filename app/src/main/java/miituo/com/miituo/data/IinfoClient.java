package miituo.com.miituo.data;

/**
 * Created by Edrei on 30/01/2017.
 */

public class IinfoClient {
    public static InfoClient InfoClientObject=null;


    public static InfoClient getInfoClientObject() {
        return InfoClientObject;
    }

    public static void setInfoClientObject(InfoClient infoClientObject) {
        InfoClientObject = infoClientObject;
    }
}
