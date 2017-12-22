package miituo.com.miituo.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import miituo.com.miituo.AndroidMultiPartEntity;
import miituo.com.miituo.UploadImageInterface;
import miituo.com.miituo.UploadObject;
import miituo.com.miituo.data.ClientMovil;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.InfoClient;
import miituo.com.miituo.data.imagenClass;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

    /**
 * Created by Edrei on 24/01/2017.
 * /*lVehicle=new ArrayList<>();

 for(int i=0;i<result.length();i++)
 {
 lVehicle.add(new VehicleModel(result.getJSONObject(i).getInt("Id"),result.getJSONObject(i).getString("Model")));
 }
 vList=(ListView)findViewById(R.id.listmodel);
 vadapter=new VehicleModelAdapter(getApplicationContext(),lVehicle);
 vList.setAdapter(vadapter);
 */

public class ApiClient {

    //final String UrlApi; //DEV
    //public static String UrlApi = "http://10.58.68.140:8080/api/"; //EDREI_LOCAL

    //final String UrlApi="http://miituodev.sytes.net:1001/api/"; //DEV
    final String UrlApi="http://miituodev.sytes.net:1003/api/"; //QAS
    //final String UrlApi="https://miituo.com/api/api/"; //PROD

    public ApiClient(Context context){  //--1
        /*SharedPreferences app_preferences;
        app_preferences= PreferenceManager.getDefaultSharedPreferences(context);

        String UrlApitemp = app_preferences.getString("api","");
        if(UrlApitemp.equals("")){
            UrlApi = "http://miituodev.sytes.net:1001/api/";
        }else{
            UrlApi = UrlApitemp;
        }*/
    }

    //Metodo para recuperar datos del cliente a partir del celular----------------------------------------------------------------
    public List<InfoClient> getInfoClient(String Url,Context context) throws IOException
    {
        final String TAG = "JsonParser.java";
        List<InfoClient> InfoList=null;
        InputStream in=null;
            try {
                URL url = new URL(UrlApi+Url);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != 200) {
                    String jsonstring="";
                    //Log.d(TAG, "The response is: " + statusCode);
                    in=urlConnection.getErrorStream();
                    jsonstring=getStringFromInputStream(in);
                    try {
                        JSONObject error = new JSONObject(jsonstring);
                        throw new IOException(error.getString(("Message")));
                    }
                    catch (JSONException ex)
                    {
                        throw new IOException("Error al Convertir Respuesta de Error");
                    }
                }
                else
                    in = urlConnection.getInputStream();
                    JSONArray resultJson=null;
                    String jsonstring="";
                try {
                    jsonstring=getStringFromInputStream(in);
                    //Log.w("Data",jsonstring);
                    if(jsonstring!=null || jsonstring!="") {
                        Gson parseJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                        InfoList = parseJson.fromJson(jsonstring, new TypeToken<List<InfoClient>>() {
                        }.getType());

                        String test = "";
                        try {
                            JSONArray json= new JSONArray(jsonstring);
                            JSONObject object = (JSONObject) new JSONTokener(json.getString(0)).nextValue();
                            JSONObject json2 = object.getJSONObject("Policies");
                            String report = json2.getString("ReportState");

                            JSONArray objeto = json2.getJSONArray("Tickets");
                            JSONObject lastobj = objeto.getJSONObject(0);

                            //Log.w("id",lastobj.getString("Id"));

                            if(report.equals("15") || report.equals("14")) {
                                SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(context);
                                pre.edit().putString("idticket", lastobj.getString("Id")).apply();
                            }
                            //idTicket = lastobj.getString("Id");

                        } catch (JSONException e) {
                            test = "";
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return InfoList;

                //InputStream in=new BufferedInputStream(urlConnection.getInputStream());
            }
            finally {
                if(in!=null)
                {
                    in.close();
                }
            }
    }

    public static String getStringFromInputStream(InputStream stream) throws IOException
    {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, "UTF8");
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }

    //Subir fotografia----------------------------------------------------------------
    public void UploadPhoto(int Type, Bitmap img,String Url,String tok) throws IOException
    {
        InputStream in=null;
        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";
        DataOutputStream outputStream = null;

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        try {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            //urlConnection.setReadTimeout(0);
            //urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            //urlConnection.setRequestProperty("Content-Type", "application/json");
            //urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",tok);

            outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"file"+Type+".jpg\"" + lineEnd);
            outputStream.writeBytes("Content-Type: image/jpg" + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            outputStream.writeBytes(lineEnd);
            outputStream.write(byteArray);
            outputStream.writeBytes(lineEnd);

            String filename = Type+"";
            String filename2 = IinfoClient.getInfoClientObject().getPolicies().getId()+"";
            String filename3 = IinfoClient.getInfoClientObject().getPolicies().getNoPolicy()+"";
            HashMap<String, String> parmas = new HashMap<>();
            parmas.put("Type", (filename));
            parmas.put("PolicyId", (filename2));
            parmas.put("PolicyFolio", (filename3));
            // Upload POST Data
            Iterator<String> keys = parmas.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = parmas.get(key);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring = "";
                jsonstring = getStringFromInputStream(in);

                int entero = Integer.parseInt(jsonstring);
                if(entero > 4){
                    throw new IOException("Error al subir imagen. Intente más tarde.");
                }
                //IinfoClient.InfoClientObject.getPolicies().setRegOdometer(Integer.parseInt(jsonstring));
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }

        /*InputStream in=null;
        try {
            ByteArrayOutputStream ArrayBytes=new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG,80,ArrayBytes);
            byte[] mapByte=ArrayBytes.toByteArray();
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            String jsonString;//= new String(mapByte,"UTF-8");
            jsonString=Base64.encodeToString(mapByte, Base64.DEFAULT);
            try {
                sendObject.put("Type", Type);
                sendObject.put("Data",jsonString);
                sendObject.put("PolicyId", IinfoClient.getInfoClientObject().getPolicies().getId());
                sendObject.put("PolicyFolio",IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            String objetojson = sendObject.toString();
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring = "";
                jsonstring = getStringFromInputStream(in);

                int entero = Integer.parseInt(jsonstring);
                if(entero > 4){
                    throw new IOException("Error al subir imagen. Intente más tarde.");
                }

                //IinfoClient.InfoClientObject.getPolicies().setRegOdometer(Integer.parseInt(jsonstring));
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }*/
    }

    public RequestBody toRequestBody(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    //Subir fotografia----------------------------------------------------------------
    public Bitmap DownloadPhoto(String Url,Context context,String to) throws IOException
    {
        InputStream in=null;
        try {
            /*ByteArrayOutputStream ArrayBytes=new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG,50,ArrayBytes);
            byte[] mapByte=ArrayBytes.toByteArray();*/

            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            //urlConnection.setReadTimeout(0);
            //urlConnection.setConnectTimeout(0);
            //urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Authorization",to);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();

            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    //throw new IOException(error.getString(("Message")));
                    return null;
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            } else {
                try {
                    in = urlConnection.getInputStream();
                    String jsonstring = "";
                    jsonstring = getStringFromInputStream(in);

                    byte[] decodedString = Base64.decode(jsonstring, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    return decodedByte;
                }
                catch(Exception e){
                    throw new IOException("Error al recuperar imagen del servidor.");
                }
                //ahora convetir string to imagen base64
            /*try {
                JSONObject success = new JSONObject(jsonstring);
            } catch (JSONException ex) {
                throw new IOException("Error al Convertir Respuesta de Error");
            }
            */
                //IinfoClient.InfoClientObject.getPolicies().setRegOdometer(Integer.parseInt(jsonstring));
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Enviamos odometro por pirmera vez-----------------------------------------------------------------
    public Boolean AjusteOdometerCasification(int Type,String Url,Context context,String tt) throws IOException
    {
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",tt);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            try {
                SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(context);
                String idtick = pre.getString("idticket","null");

                sendObject.put("Id", idtick);
                sendObject.put("sTiket",7);
                sendObject.put("GodinName","App");
                sendObject.put("GodynSolution","");
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                Boolean jsonstring;
                jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
                return  jsonstring;
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Enviamos odometro por pirmera vez-----------------------------------------------------------------
    public Boolean AjusteOdometerTicket(int Type,String Url,Context context,String t) throws IOException
    {
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",t);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            try {
                SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(context);
                String idtick = pre.getString("idticket","null");

                sendObject.put("OdomCorrect", 0);
                sendObject.put("OdomMoment",IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
                sendObject.put("idTicket",idtick);
                sendObject.put("idPolicy",IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                Boolean jsonstring;
                jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
                return  jsonstring;
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Enviamos odometro por pirmera vez-----------------------------------------------------------------
    public Boolean AjusteOdometerLast(int Type,String Url,int id,String t) throws IOException
    {
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url+id+"/"+12);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",t);
            urlConnection.connect();
            /*JSONObject sendObject=new JSONObject();
            try {
                sendObject.put("Id", 0);
                sendObject.put("sTiket",7);
                sendObject.put("GodinName","App");
                sendObject.put("GodynSolution","");
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();*/

            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                Boolean jsonstring;
                jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
                return  jsonstring;
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Enviamos odometro por pirmera vez-----------------------------------------------------------------
    public Boolean ConfirmOdometer(int Type,String Url,String tik) throws IOException
    {
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",tik);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            try {
                sendObject.put("Type", Type);
                sendObject.put("PolicyId",IinfoClient.getInfoClientObject().getPolicies().getId());
                sendObject.put("PolicyFolio",IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());
                sendObject.put("Odometer",IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                Boolean jsonstring;
                jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
                    return  jsonstring;
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //cONFIRMREPORT to closde and frupdate table ReportOdometer/Confirmreport-----------------------------
    public String ConfirmReportLast(String Url,String tok) throws IOException
    {
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",tok);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            //JSONObject subItem=new JSONObject();
            try {
                //subItem.put("PolicyId",IinfoClient.getInfoClientObject().getPolicies().getId());
                //subItem.put("PolicyFolio",IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());
                //subItem.put("Odometer",IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
                //subItem.put("ClientId",IinfoClient.getInfoClientObject().getClient().getId());
                sendObject.put("Type",1);
                sendObject.put("PolicyId",IinfoClient.getInfoClientObject().getPolicies().getId());
                sendObject.put("PolicyFolio",IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());
                sendObject.put("Odometer",IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
                sendObject.put("ClientId",IinfoClient.getInfoClientObject().getClient().getId());
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            String cad = sendObject.toString();
            writter.write(sendObject.toString());

            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    //throw new IOException(error.getString(("Message")));
                    return jsonstring;
                }
                catch (JSONException ex)
                {
                    //throw new IOException("Error al Convertir Respuesta de Error");
                    return jsonstring;
                }
            }
            else {
                in = urlConnection.getInputStream();
                //Boolean jsonstring;
                //jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
                String jsonstring=getStringFromInputStream(in);
                return jsonstring;
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Recuperamos recibo con datos y formularas----------------------------------------------------------------
    public String ConfirmReport(String Url,String tok) throws IOException
    {
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",tok);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            JSONObject subItem=new JSONObject();
            try {
                subItem.put("PolicyId",IinfoClient.getInfoClientObject().getPolicies().getId());
                subItem.put("PolicyFolio",IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());
                subItem.put("Odometer",IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
                subItem.put("ClientId",IinfoClient.getInfoClientObject().getClient().getId());
                sendObject.put("Type",1);   //Tipo 1 = pago mensual...
                sendObject.put("ImageItem",subItem);
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            String cad = sendObject.toString();
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                //Boolean jsonstring;
                //jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
                String jsonstring=getStringFromInputStream(in);
                return jsonstring;
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Actualiza token ----------------------------------------------------------------
    public Boolean updateToken(String Url,ClientMovil client,String tok) throws IOException
    {
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",tok);
            urlConnection.connect();

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            Gson parset=new Gson();
            String result=parset.toJson(client);
            writter.write(result);
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                Boolean jsonstring;
                jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
                return  jsonstring;

            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Metodo para recuperar datos del cliente----------------------------------------------------------------
    public String getToken(String Url) throws IOException
    {
        final String TAG = "JsonParser.java";
        List<InfoClient> InfoList=null;
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            //urlConnection.setReadTimeout(10000);
            //urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else
                in = urlConnection.getInputStream();

            JSONArray resultJson=null;
            String jsonstring="";
            try {
                jsonstring=getStringFromInputStream(in);
                if(jsonstring!=null || jsonstring!="") {
                    return jsonstring;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonstring;

            //InputStream in=new BufferedInputStream(urlConnection.getInputStream());
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }
}

