package miituo.com.miituo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.InfoClient;

/**
 * Created by miituo on 26/01/2017.
 */
@SuppressWarnings("serial")
public class VehicleModelAdapter extends BaseAdapter {

    private Context mContext;
    private List<InfoClient> mInfoClientList;
    public Typeface tipo;

    public VehicleModelAdapter(Context mContext, List<InfoClient> mInfoClientList, Typeface t) {
        this.mContext = mContext;
        this.mInfoClientList = mInfoClientList;

        tipo = t;
    }

    @Override
    public int getCount() {
        return mInfoClientList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInfoClientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=View.inflate(mContext,R.layout.infoclient_item,null);
        TextView TxtNoPolicy=(TextView)v.findViewById(R.id.Vehicle);
        TextView TxtVehicleDesc=(TextView)v.findViewById(R.id.NoPolicy);
        TextView TxtMensajeLimite=(TextView)v.findViewById(R.id.mensajelimite);

        ImageView imagen = (ImageView)v.findViewById(R.id.profile_image);
        if(imagen != null){
            //ruta del archivo...
            String filePathString = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+"frontal_"+mInfoClientList.get(position).getPolicies().getNoPolicy()+".png";
            File f = new File(filePathString);
            //File image = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+username+"_"+polizaFolio+tag+".png");
            if(f.exists()) {
                Glide.with(mContext)
                        .load(filePathString)
                        .into(imagen);
            }else{
                Glide.with(mContext)
                        .load(R.drawable.foto)
                        .into(imagen);
            }

            //new ImageDownloaderTask(imagen).execute(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+"frontal_"+mInfoClientList.get(position).getPolicies().getNoPolicy()+".png");
            /*try {
                String url = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+"frontal_"+mInfoClientList.get(position).getPolicies().getNoPolicy()+".png";
                //File image = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+username+"_"+polizaFolio+tag+".png");
                File image = new File(url);
                if(image.exists()){

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;

                    Bitmap myBitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),options);

                    Bitmap resized = Bitmap.createScaledBitmap(myBitmap, myBitmap.getWidth()/40, myBitmap.getHeight()/40, false);
                    imagen.setImageBitmap(resized);
                    //return resized;
                }
            }
            catch(Exception e) {
            }*/
        }

        TxtNoPolicy.setTypeface(tipo);
        TxtVehicleDesc.setTypeface(tipo);
        TxtMensajeLimite.setTypeface(tipo);
        //CardView card = (CardView)v.findViewById(R.id.view2);
        //ImageView circulo = (ImageView)v.findViewById(R.id.imageView4);
        //set values
        try {
            //int num = mVehicleModelList.get(position).getId();
            //circulo.setImageResource(R.drawable.odometer);
            //card.setBackgroundResource(R.drawable.frontal);

            /*MessageFormat Description= new MessageFormat("{0} {1} {2} {3} {4}");
            String[] values=new String[]{
                    mInfoClientList.get(position).getPolicies().getVehicle().getType().getDescription(),
                    String.valueOf(mInfoClientList.get(position).getPolicies().getVehicle().getModel().getModel()),
                    mInfoClientList.get(position).getPolicies().getVehicle().getBrand().getDescription(),
                    mInfoClientList.get(position).getPolicies().getVehicle().getSubtype().getDescription(),
                    mInfoClientList.get(position).getPolicies().getVehicle().getDescription().getDescription()

            };*/
            TxtNoPolicy.setText("Póliza: "+String.valueOf(mInfoClientList.get(position).getPolicies().getNoPolicy()));
            /*String DescriptionVehicle=String.format("{0} {1} {2} {3} {4}",
                    mInfoClientList.get(position).getPolicies().getVehicle().getType().getDescription(),
                    mInfoClientList.get(position).getPolicies().getVehicle().getModel().getModel(),
                    mInfoClientList.get(position).getPolicies().getVehicle().getBrand().getDescription(),
                    mInfoClientList.get(position).getPolicies().getVehicle().getSubtype().getDescription(),
                    mInfoClientList.get(position).getPolicies().getVehicle().getDescription().getDescription()
                    );
            */
            //Set description vehicle
            //TxtVehicleDesc.setText("Vehículo: "+Description.format(values));
            //update tag for get position in another place
            v.setTag(mInfoClientList.get(position));
            //update image upon statu pictures
            ImageView img=(ImageView)v.findViewById(R.id.StateImage);
            TxtVehicleDesc.setText("Ver información "+mInfoClientList.get(position).getPolicies().getVehicle().getSubtype().getDescription());
            TxtMensajeLimite.setVisibility(View.GONE);

            //TxtVehicleDesc.setTextColor(R.drawable.blumiituo);

            if(mInfoClientList.get(position).getPolicies().getReportState() == 15){
                //verde -- reportar odometro
                //img.setImageResource(R.drawable.greenmiituo);
                //TxtVehicleDesc.setTextColor(Color.rgb(62,253,202));
                img.setImageResource(R.drawable.reedmiituo);
                TxtVehicleDesc.setTextColor(Color.RED);
                TxtVehicleDesc.setText("Solicitud de ajuste odómetro "+mInfoClientList.get(position).getPolicies().getVehicle().getSubtype().getDescription());
                TxtMensajeLimite.setVisibility(View.GONE);

            }
            if(mInfoClientList.get(position).getPolicies().getReportState() == 14){
                //verde -- reportar odometro
                //img.setImageResource(R.drawable.greenmiituo);
                //TxtVehicleDesc.setTextColor(Color.rgb(62,253,202));
                img.setImageResource(R.drawable.reedmiituo);
                TxtVehicleDesc.setTextColor(Color.RED);
                TxtVehicleDesc.setText("Solicitud de cancelación voluntaria "+mInfoClientList.get(position).getPolicies().getVehicle().getSubtype().getDescription());
                TxtMensajeLimite.setVisibility(View.GONE);
            }
            if(mInfoClientList.get(position).getPolicies().getReportState() == 13){
                //verde -- reportar odometro
                //img.setImageResource(R.drawable.greenmiituo);
                //TxtVehicleDesc.setTextColor(Color.rgb(62,253,202));
                img.setImageResource(R.drawable.reedmiituo);
                TxtVehicleDesc.setTextColor(Color.RED);
                TxtVehicleDesc.setText("Es hora de reportar tu odómetro "+mInfoClientList.get(position).getPolicies().getVehicle().getSubtype().getDescription());
                TxtMensajeLimite.setVisibility(View.VISIBLE);
                TxtMensajeLimite.setTextColor(Color.RED);
                Calendar c = Calendar.getInstance();
                Date fechacadena = mInfoClientList.get(position).getPolicies().getLimitReportDate();
                c.setTime(fechacadena);
                fechacadena = c.getTime();

                SimpleDateFormat month_date = new SimpleDateFormat("MMM");
                String month_name = month_date.format(c.getTime());
                String real = getName(month_name);
                SimpleDateFormat year_data = new SimpleDateFormat("yyyy");
                String year = year_data.format(c.getTime());
                SimpleDateFormat hour = new SimpleDateFormat("HH");
                String hora = hour.format(c.getTime());
                SimpleDateFormat minuts = new SimpleDateFormat("mm");
                String minuto = minuts.format(c.getTime());

                TxtMensajeLimite.setText("Tienes hasta el:\n"+new SimpleDateFormat("dd").format(fechacadena)+" del "+real+" a las "+hora+":"+minuto);

            }
            if(!mInfoClientList.get(position).getPolicies().isHasVehiclePictures())
            {
                //rojo
                img.setImageResource(R.drawable.reedmiituo);
                TxtVehicleDesc.setTextColor(Color.RED);
                TxtVehicleDesc.setText("No has enviado fotos de tu auto "+mInfoClientList.get(position).getPolicies().getVehicle().getSubtype().getDescription());
                TxtMensajeLimite.setVisibility(View.VISIBLE);
                TxtMensajeLimite.setTextColor(Color.RED);

                TxtMensajeLimite.setTextColor(Color.RED);
                Calendar c = Calendar.getInstance();
                Date fechacadena = mInfoClientList.get(position).getPolicies().getLimitReportDate();
                c.setTime(fechacadena);
                fechacadena = c.getTime();

                SimpleDateFormat month_date = new SimpleDateFormat("MMM");
                String month_name = month_date.format(c.getTime());
                String real = getName(month_name);
                SimpleDateFormat year_data = new SimpleDateFormat("yyyy");
                String year = year_data.format(c.getTime());
                SimpleDateFormat hour = new SimpleDateFormat("HH");
                String hora = hour.format(c.getTime());
                SimpleDateFormat minuts = new SimpleDateFormat("mm");
                String minuto = minuts.format(c.getTime());

                TxtMensajeLimite.setText("Tienes hasta el:\n"+new SimpleDateFormat("dd").format(fechacadena)+" del "+real+" a las "+hora+":"+minuto);

            }
            if(mInfoClientList.get(position).getPolicies().isHasVehiclePictures() && !mInfoClientList.get(position).getPolicies().isHasOdometerPicture())
            {
                //rojo
                img.setImageResource(R.drawable.reedmiituo);
                TxtVehicleDesc.setTextColor(Color.RED);
                TxtVehicleDesc.setText("No has enviado foto de tu odómetro "+mInfoClientList.get(position).getPolicies().getVehicle().getSubtype().getDescription());
                TxtMensajeLimite.setVisibility(View.VISIBLE);
                TxtMensajeLimite.setTextColor(Color.RED);

                Calendar c = Calendar.getInstance();
                Date fechacadena = mInfoClientList.get(position).getPolicies().getLimitReportDate();
                c.setTime(fechacadena);
                fechacadena = c.getTime();

                SimpleDateFormat month_date = new SimpleDateFormat("MMM");
                String month_name = month_date.format(c.getTime());
                String real = getName(month_name);
                SimpleDateFormat year_data = new SimpleDateFormat("yyyy");
                String year = year_data.format(c.getTime());
                SimpleDateFormat hour = new SimpleDateFormat("HH");
                String hora = hour.format(c.getTime());
                SimpleDateFormat minuts = new SimpleDateFormat("mm");
                String minuto = minuts.format(c.getTime());

                TxtMensajeLimite.setText("Tienes hasta el:\n"+new SimpleDateFormat("dd").format(fechacadena)+" del "+real+" a las "+hora+":"+minuto);

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return v;
    }

    public String getName(String nombre){

        if(nombre.equals("ene") || nombre.equals("Jan") || nombre.equals("jan") || nombre.equals("Ene")){
            return "enero";
        }else if(nombre.equals("feb") || nombre.equals("Feb")){
            return "febrero";
        }else if (nombre.equals("mar") || nombre.equals("Mar")){
            return "marzo";
        }else if (nombre.equals("abr") || nombre.equals("apr") || nombre.equals("Apr")){
            return "abril";
        }else if (nombre.equals("may") || nombre.equals("May")){
            return "mayo";
        }else if (nombre.equals("jun") || nombre.equals("Jun")){
            return "junio";
        }else if (nombre.equals("jul") || nombre.equals("Jul")){
            return "julio";
        }else if (nombre.equals("ago") || nombre.equals("aug") || nombre.equals("Aug") || nombre.equals("Ago")){
            return "agosto";
        }else if (nombre.equals("sep") || nombre.equals("Sep")){
            return "septiembre";
        }else if (nombre.equals("oct") || nombre.equals("Oct")){
            return "octubre";
        }else if (nombre.equals("nov") || nombre.equals("Nov")){
            return "noviembre";
        }else if (nombre.equals("dic") || nombre.equals("dec") || nombre.equals("Dic") || nombre.equals("Dec")){
            return "diciembre";
        }else{
            return nombre;
        }
    }

    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        //Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.placeholder);
                        //imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }

        private Bitmap downloadBitmap(String url) {

            try {
                //File image = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+username+"_"+polizaFolio+tag+".png");
                File image = new File(url);
                if(image.exists()){

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;

                    Bitmap myBitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),options);

                    Bitmap resized = Bitmap.createScaledBitmap(myBitmap, myBitmap.getWidth()/40, myBitmap.getHeight()/40, false);
                    //imagen.setImageBitmap(resized);
                    return resized;
                }
            }
            catch(Exception e) {
            }
            /*HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                //Log.w("ImageDownloader", "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }*/
            return null;
        }
    }


}
