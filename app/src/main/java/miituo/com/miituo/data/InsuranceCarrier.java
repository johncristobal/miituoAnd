package miituo.com.miituo.data;

/**
 * Created by john.cristobal on 12/04/17.
 */

public class InsuranceCarrier
{
    private int Id;
    private String Name;

    public InsuranceCarrier(int id, String name) {
        Id = id;
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
