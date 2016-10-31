package proyecto.com.chefappcom;

/**
 * Created by alfredo on 29/10/16.
 */

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Clase encargada de mostrar los mensajes entre chefs en tiempo real.
 */
public class Chat extends AppCompatActivity {

    private ListView chat;
    private ArrayAdapter<String> adapter;
    private String mensajeEnviar="";
    private String nombreUsuario="";
    private Handler handler = new Handler();
    private Runnable runnable;


    List<String> mensajes;

    /**
     * Constructor de la clase.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        final Button end = (Button) findViewById(R.id.button2);
        final EditText entry = (EditText) findViewById(R.id.editText);
        chat = (ListView) findViewById(R.id.listView);
        mensajes = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mensajes);
        chat.setAdapter(adapter);

        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            nombreUsuario=extras.getString("nombre");
        }

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensajeEnviar=String.valueOf(entry.getText());
                entry.setText("");
               try {
                    new EnviarMensaje().execute(new URL("http://192.168.43.116:9080/Proyecto2/central/chef/chat/agregar"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        });
        runnable= new Runnable(){
            public void run() {
                try {
                    new GetChat().execute(new URL("http://192.168.43.116:9080/Proyecto2/central/chef/chat/obtener"));
                    //Toast.makeText(getBaseContext(), String.valueOf(contDS)+" segundos", Toast.LENGTH_LONG).show();
                    handler.postDelayed(runnable,800);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.post(runnable);
    }


    /**
     * Clase encargada de obtener los mensajes guardados en el servidor y mostrarlos en la activity.
     */
    public class GetChat extends AsyncTask<URL, Void, List<String>> {

        @Override
        protected List<String> doInBackground(URL... urls) {

            List<String> msj = null;

            try {
                HttpURLConnection urlConnection = (HttpURLConnection) urls[0].openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader j = new InputStreamReader(in, "UTF-8");
                    LectorJSON parser = new LectorJSON();

                    msj = parser.readJsonStream(in);

                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {


            }
            return msj;

        }

        @Override
        protected void onPostExecute(List<String> coleccion) {
            String[] datosM=coleccion.get(0).split("jk");
            int normalizar=mensajes.size();
            mensajes.clear();
            for(int o=0;o<datosM.length;o++){
                mensajes.add(datosM[o]);
            }
            adapter.notifyDataSetChanged();
            if(normalizar!=datosM.length){
                chat.setSelection(chat.getAdapter().getCount() - 1);
            }

        }


    }

    /**
     * Clase encargada de enviar una cadena de texto al servidor para ser almacenada.
     */
    public class EnviarMensaje extends AsyncTask<URL, Void, Void> {

        @Override
        protected Void doInBackground(URL... urls) {
            // Obtener la conexión
            HttpURLConnection con = null;

            try {

                con = (HttpURLConnection)urls[0].openConnection();

                // Activar método POST
                con.setDoOutput(true);

                mensajeEnviar=nombreUsuario+" dice: "+mensajeEnviar;
                con.setFixedLengthStreamingMode(mensajeEnviar.getBytes().length);
                con.setRequestProperty("Content-Type","application/json");

                OutputStream out = new BufferedOutputStream(con.getOutputStream());

                out.write(mensajeEnviar.getBytes());
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
        protected void onPostExecute(Void s) {

        }
    }
}