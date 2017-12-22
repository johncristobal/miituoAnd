package miituo.com.miituo;

import java.util.Map;

import miituo.com.miituo.data.imagenClass;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface UploadImageInterface {
    @Multipart
    @POST("ImageSendProcess/Array/")
    Call<UploadObject> uploadFile(
            @Part MultipartBody.Part file,
            @PartMap () Map<String, String> partMap,
            @Header("Authorization") String Authorization,
            @Header("Content-Type") String content
            );
}
/*
public interface UploadImageInterface {
    @Multipart
    @POST("ImageSendProcess/Array")
    Call<UploadObject> uploadFile(
            @Header("Authorization") String Authorization,
            @Header("Content-Type") String content,
            @Part("PolicyId") RequestBody PolicyId,
            @Part MultipartBody.Part image,
            @Part("Type") RequestBody Type,
            @Part("PolicyFolio") RequestBody PolicyFolio
    );
}*/