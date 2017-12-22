package miituo.com.miituo.data;

/**
 * Created by john.cristobal on 12/04/17.
 */

public class Coverage
{
    private int Id;
    private String Description;

    public Coverage(String description, int id) {
        Description = description;
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
