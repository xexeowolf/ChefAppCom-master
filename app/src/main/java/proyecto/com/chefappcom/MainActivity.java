package proyecto.com.chefappcom;

import android.content.Intent;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import android.app.ProgressDialog;
import android.widget.ImageView;


import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Clase encargada de la pantalla de inicio de sesion y recopilar la informacion de la cuenta LinkedIn del usuario.
 */
public class MainActivity extends AppCompatActivity {


    private String nombreUsuario,correo;
    private Intent intento;
    private TextView tvtcorreo;


    /**
     * Constructor de la clase.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvtcorreo=(TextView)findViewById(R.id.tvtcorreo);
        tvtcorreo.setTextColor(Color.parseColor("#000000"));
        tvtcorreo.setBackgroundColor(Color.WHITE);
        TextView tvnom=(TextView)findViewById(R.id.tvtnombre);
        tvnom.setBackgroundColor(Color.WHITE);
        tvnom.setTextColor(Color.parseColor("#000000"));
        TextView tvb=(TextView)findViewById(R.id.linkedtext);
        tvb.setBackgroundColor(Color.WHITE);
        tvb.setTextColor(Color.parseColor("#000000"));
        LISessionManager.getInstance(getApplicationContext())
                .init(this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {

                        /*Toast.makeText(getApplicationContext(), "success" +
                                        LISessionManager.getInstance(getApplicationContext())
                                                .getSession().getAccessToken().toString(),
                                Toast.LENGTH_LONG).show();*/
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        Toast.makeText(getApplicationContext(), "failed "
                                        + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }, true);

       ImageView imageView = (ImageView) findViewById(R.id.imageView);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
        Glide.with(this).load(R.raw.r15).into(imageViewTarget);
    }


    /**
     * Metodo que envia al servidor la informacion del usuario para registrarlo como chef activo.
     * @param view vista donde se encuentra el boton.
     */
    public void login(View view){

        try {
            new RegistrarChef().execute(new URL("http://192.168.43.116:9080/Proyecto2/central/chef/agregar"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        startActivity(intento);
    }


    /**
     * Metodo que realiza distintas acciones dependiendo el codigo obtenido de una activity
     * @param requestCode codigo unico de cada activity
     * @param resultCode codigo que representa si los procesos terminaron correctamente
     * @param data informacion enviada por un activity.
     */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        LISessionManager.getInstance(getApplicationContext())
                .onActivityResult(this,
                        requestCode, resultCode, data);

        connectProfile();
    }



    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE,
                Scope.R_EMAILADDRESS);
    }

    private static final String url = "https://api.linkedin.com/v1/people/~:(id,email-address," +
            "first-name,last-name,formatted-name,picture-url)?format=json";

    private ProgressDialog progress;
    private TextView user_name, user_email;
    private ImageView profile_picture;


    protected void connectProfile() {

        progress= new ProgressDialog(this);
        progress.setMessage("Retrieve data...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        user_email = (TextView) findViewById(R.id.email);
        user_email.setBackgroundColor(Color.WHITE);
        user_email.setTextColor(Color.parseColor("#000000"));
        user_name = (TextView) findViewById(R.id.name);
        user_name.setBackgroundColor(Color.WHITE);
        user_name.setTextColor(Color.parseColor("#000000"));
        profile_picture = (ImageView) findViewById(R.id.profile_picture);

        linkededinApiHelper();

    }

    public void linkededinApiHelper(){
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(MainActivity.this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    showResult(result.getResponseDataAsJson());
                    progress.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {

            }
        });
    }

    public void showResult(JSONObject response){

        try {

            String[]dataJ=new String[3];
            correo=response.get("emailAddress").toString();
            dataJ[0]=correo;
            user_email.setText(correo);
            nombreUsuario=response.get("formattedName").toString();
            dataJ[1]=nombreUsuario;
            user_name.setText(nombreUsuario);

            Picasso.with(this).load(response.getString("pictureUrl"))
                    .into(profile_picture);
            dataJ[2]=response.getString("pictureUrl");
            intento=new Intent(tvtcorreo.getContext(),navigation.class);
            intento.putExtra("objeto",dataJ);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Clase que envia al servidor la informacion personal de usuario para registrarlo como chef activo.
     */
    public class RegistrarChef extends AsyncTask<URL, Void, Void> {

        @Override
        protected Void doInBackground(URL... urls) {
            // Obtener la conexión
            HttpURLConnection con = null;

            try {

                con = (HttpURLConnection)urls[0].openConnection();

                // Activar método POST
                con.setDoOutput(true);

                String[] mod=nombreUsuario.split(" ");
                nombreUsuario="";
                for(int h=0;h<mod.length;h++){
                    nombreUsuario=nombreUsuario+mod[h];
                }
                con.setFixedLengthStreamingMode(nombreUsuario.getBytes().length);
                con.setRequestProperty("Content-Type","application/json");

                OutputStream out = new BufferedOutputStream(con.getOutputStream());

                out.write(nombreUsuario.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(con!=null)
                    con.disconnect();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void s){
        }
    }
}