package proyecto.com.chefappcom;

/**
 * Created by alfredo on 25/10/16.
 */

import android.os.AsyncTask;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


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
import java.util.concurrent.TimeUnit;




public class FlujoPasos extends AppCompatActivity {

    private List<String> pasosReceta = new ArrayList<String>();
    private List<String> pasos;

    private TextView tv1, vista;
    private ListView lv1;
    private int remove;
    private double time;
    private ArrayAdapter<String> ad;
    private String nombreC = "";
    private long timeMillis;
    private String nombreReceta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flujo_pasos);

        final Button terminar = (Button) findViewById(R.id.Terminar);
        terminar.setVisibility(View.INVISIBLE);
        tv1 = (TextView) findViewById(R.id.Pasos_Receta);
        vista = (TextView) findViewById(R.id.vista);
        vista.setMovementMethod(new ScrollingMovementMethod());
        vista.setVisibility(View.INVISIBLE);
        lv1 = (ListView) findViewById(R.id.Pasos);
        lv1.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nombreC = extras.getString("nombreC");
        }
        try {
            new ObtenerOrdenes().execute(new URL("http://192.168.1.62:9080/Proyecto2/central/chef/orden"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        lv1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                remove = (int) lv1.getItemIdAtPosition(posicion);
                time = System.currentTimeMillis();
                terminar.setVisibility(View.VISIBLE);
                vista.setVisibility(View.VISIBLE);
                vista.setText(pasosReceta.get(remove));
                lv1.setVisibility(View.INVISIBLE);
                tv1.setVisibility(View.INVISIBLE);
            }
        });

        terminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pasosReceta.size() == 0 && pasos.size() == 0) {
                    finish();

                } else {

                    pasosReceta.remove(remove);
                    pasos.remove(remove);
                    ad.notifyDataSetChanged();


                    vista.setVisibility(View.GONE);
                    terminar.setVisibility(View.INVISIBLE);


                    float timeSeconds = TimeUnit.MILLISECONDS.toSeconds((long) (timeMillis - time));
                    timeSeconds = Math.abs(timeSeconds);

                    lv1.setVisibility(View.VISIBLE);
                    tv1.setVisibility(View.VISIBLE);

                    if (pasosReceta.size() == 0 && pasos.size() == 0) {
                        try {
                            new TerminarOrden().execute(new URL("http://192.168.1.62:9080/Proyecto2/central/chef/orden/fin"));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        finish();
                        Toast.makeText(getBaseContext(), "Orden finalizada\nTiempo transcurrido: " + timeSeconds + " segundos", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        timeMillis = System.currentTimeMillis();
    }

    public ArrayList crearArregloPasos(int len) {
        ArrayList<String> arreglo = new ArrayList<String>();
        String text = "Paso no.";

        for (int i = 0; i < pasosReceta.size(); i++) {
            arreglo.add(text + (i + 1));
        }
        return arreglo;
    }

    public class ObtenerOrdenes extends AsyncTask<URL, Void, List<String>> {

        @Override
        protected List<String> doInBackground(URL... urls) {
            // Obtener la conexión
            HttpURLConnection con = null;

            try {

                con = (HttpURLConnection) urls[0].openConnection();

                // Activar método POST
                con.setDoOutput(true);

                String[] normal = nombreC.split(" ");
                nombreC = "";
                for (int e = 0; e < normal.length; e++) {
                    nombreC = nombreC + normal[e];
                }

                con.setFixedLengthStreamingMode(nombreC.getBytes().length);
                con.setRequestProperty("Content-Type", "application/json");

                OutputStream out = new BufferedOutputStream(con.getOutputStream());

                out.write(nombreC.getBytes());
                out.flush();
                out.close();


            } catch (IOException e) {
                e.printStackTrace();
            }

            List<String> msj = null;

            try {
                //con = (HttpURLConnection) urls[0].openConnection();
                try {
                    InputStream in = new BufferedInputStream(con.getInputStream());
                    InputStreamReader j = new InputStreamReader(in, "UTF-8");
                    LectorJSON parser = new LectorJSON();

                    msj = parser.readJsonStream(in);

                } finally {
                    con.disconnect();
                }
            } catch (Exception e) {


            }
            return msj;
        }

        @Override
        protected void onPostExecute(List<String> conjunto) {
            if (conjunto.size() == 1) {
                Toast.makeText(getBaseContext(), "No tiene ordenes pendientes", Toast.LENGTH_LONG).show();
                finish();
            } else {
                String infog = conjunto.get(0);
                String[] InfoG = infog.split("jk");
                conjunto.remove(0);
                String[] nomIng = InfoG[0].split("\n");
                nombreReceta = nomIng[0];
                infog = "Receta a preparar: " + nomIng[0] + "\n\nIngredientes:\n";
                for (int w = 1; w < nomIng.length; w++) {
                    infog = infog + "-->" + nomIng[w] + "\n";
                }
                String todospasos = conjunto.get(0);
                conjunto.remove(0);
                String[] divisiones = todospasos.split("jk");
                for (int u = 0; u < divisiones.length; u++) {
                    String comodin = infog + "\n\n" + "Procedimiento:\n" + "-->" + divisiones[u];
                    conjunto.add(comodin);
                }
                pasosReceta = conjunto;
                pasos = crearArregloPasos(conjunto.size());
                ad = new ArrayAdapter<String>(lv1.getContext(), android.R.layout.simple_list_item_1, pasos);
                lv1.setAdapter(ad);
                lv1.setVisibility(View.VISIBLE);
            }
        }
    }

    public class TerminarOrden extends AsyncTask<URL, Void, Void> {

        @Override
        protected Void doInBackground(URL... urls) {
            // Obtener la conexión
            HttpURLConnection con = null;

            try {

                con = (HttpURLConnection) urls[0].openConnection();

                // Activar método POST
                con.setDoOutput(true);

                con.setFixedLengthStreamingMode(nombreReceta.getBytes().length);
                con.setRequestProperty("Content-Type", "application/json");

                OutputStream out = new BufferedOutputStream(con.getOutputStream());

                out.write(nombreReceta.getBytes());
                out.flush();
                out.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {

        }


    }
}