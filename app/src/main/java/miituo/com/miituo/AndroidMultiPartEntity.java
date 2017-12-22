package miituo.com.miituo;

import android.os.RecoverySystem;

import com.android.internal.http.multipart.MultipartEntity;
import com.android.internal.http.multipart.Part;

import org.apache.http.params.HttpParams;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by john.cristobal on 06/12/17.
 */

public class AndroidMultiPartEntity extends MultipartEntity {

    public ProgressListener listener;

    public AndroidMultiPartEntity(Part[] parts, HttpParams params,final ProgressListener listener) {
        super(parts, params);
        this.listener = listener;
    }

    public AndroidMultiPartEntity(Part[] parts,final ProgressListener listener) {
        super(parts);
        this.listener = listener;
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        super.writeTo(new CountingOutputStream(outstream, this.listener));
    }

    public static interface ProgressListener {
        void transferred(long num);
    }

    public static class CountingOutputStream extends FilterOutputStream {

        private final ProgressListener listener;
        private long transferred;

        public CountingOutputStream(final OutputStream out,
                                    final ProgressListener listener) {
            super(out);
            this.listener = listener;
            this.transferred = 0;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            this.transferred += len;
            this.listener.transferred(this.transferred);
        }

        public void write(int b) throws IOException {
            out.write(b);
            this.transferred++;
            this.listener.transferred(this.transferred);
        }
    }

}
