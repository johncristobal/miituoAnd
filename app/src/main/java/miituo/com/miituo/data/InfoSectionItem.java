package miituo.com.miituo.data;

/**
 * Created by Edrei on 02/02/2017.
 */

public class InfoSectionItem {
    private String Label;
    private String Text;

    public InfoSectionItem(String label, String text) {
        Label = label;
        Text = text;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
