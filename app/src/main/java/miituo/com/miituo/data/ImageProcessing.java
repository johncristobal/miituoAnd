package miituo.com.miituo.data;

import android.graphics.Bitmap;

/**
 * Created by Edrei on 01/02/2017.
 */

public class ImageProcessing {
    private int ImageType;
    private Bitmap Image;

    public ImageProcessing(Bitmap image, int imageType) {
        Image = image;
        ImageType = imageType;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }

    public int getImageType() {
        return ImageType;
    }

    public void setImageType(int imageType) {
        ImageType = imageType;
    }
}
