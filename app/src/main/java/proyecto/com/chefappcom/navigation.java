package proyecto.com.chefappcom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * Clase encargada de mostrar las funcionalidades de la aplicacion mediante un menu de navegacion.
 */
public class navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ImageView foto;
    private TextView tvt2, tvtf, tvtc, tvtl, tvtv, tvtg, tvting, nomPerfil,correo;
    private Spinner spf, spc, spv, spl, spg, spingred;
    private EditText ingred;
    private Button btnagr;
    private FloatingActionButton fab, fmenu;
    int contmenu=1;
    int continv=1;
    private String [] menuspf,menuspc,menuspv,menuspg,menuspl,invspf,invspc,invspv,invspg,invspl;
    private JSONArray paquete=new JSONArray();
    private JSONObject objetoJSON=new JSONObject();
    private String nombreC;

    /**
     * Constructor de la clase.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tvt2 = (TextView) findViewById(R.id.tvt2);
        tvt2.setVisibility(View.GONE);
        View headerView = navigationView.getHeaderView(0);
        nomPerfil=(TextView)headerView.findViewById(R.id.nomPerfil);
        correo=(TextView)headerView.findViewById(R.id.correoPerfil);
        foto=(ImageView)headerView.findViewById(R.id.imagePerfil);
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
                String[]dataG=extras.getStringArray("objeto");
                correo.setText(dataG[0]);
                nomPerfil.setText(dataG[1]);
                nombreC=dataG[1];
                Picasso.with(foto.getContext()).load(dataG[2]).resize(250,250).into(foto);

        }
        tvtf = (TextView) findViewById(R.id.tvtf);
        tvtf.setVisibility(View.GONE);
        tvtc = (TextView) findViewById(R.id.tvtc);
        tvtc.setVisibility(View.GONE);
        tvtg = (TextView) findViewById(R.id.tvtg);
        tvtg.setVisibility(View.GONE);
        tvtv = (TextView) findViewById(R.id.tvtv);
        tvtv.setVisibility(View.GONE);
        tvtl = (TextView) findViewById(R.id.tvtl);
        tvtl.setVisibility(View.GONE);
        tvting = (TextView) findViewById(R.id.tvting);
        tvting.setVisibility(View.GONE);

        spc = (Spinner) findViewById(R.id.spc);
        spc.setVisibility(View.GONE);
        spv = (Spinner) findViewById(R.id.spv);
        spv.setVisibility(View.GONE);
        spg = (Spinner) findViewById(R.id.spg);
        spg.setVisibility(View.GONE);
        spl = (Spinner) findViewById(R.id.spl);
        spl.setVisibility(View.GONE);
        spf = (Spinner) findViewById(R.id.spf);
        spf.setVisibility(View.GONE);
        spingred = (Spinner) findViewById(R.id.spingred);
        spingred.setVisibility(View.GONE);

        ingred = (EditText) findViewById(R.id.ingred);
        ingred.setVisibility(View.GONE);

        btnagr = (Button) findViewById(R.id.btnagr);
        btnagr.setVisibility(View.GONE);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fmenu = (FloatingActionButton) findViewById(R.id.fmenu);
        fab.setVisibility(View.GONE);
        fmenu.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvt2.setVisibility(View.VISIBLE);
                tvting.setVisibility(View.VISIBLE);
                btnagr.setVisibility(View.VISIBLE);
                spingred.setVisibility(View.VISIBLE);
                ingred.setVisibility(View.VISIBLE);
            }
        });
        fmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), LogicaMenu.class);
                intent.putExtra("frutas",invspf);
                intent.putExtra("granos",invspg);
                intent.putExtra("carnes",invspc);
                intent.putExtra("lacteos",invspl);
                intent.putExtra("vegetales",invspv);
                startActivityForResult(intent,808);
            }
        });

        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                new GetMenuInfo().execute(new URL("http://192.168.43.116:9080/Proyecto2/central/chef/menu/ensaladas"));
                new GetMenuInfo().execute(new URL("http://192.168.43.116:9080/Proyecto2/central/chef/menu/sopas"));
                new GetMenuInfo().execute(new URL("http://192.168.43.116:9080/Proyecto2/central/chef/menu/platos"));
                new GetMenuInfo().execute(new URL("http://192.168.43.116:9080/Proyecto2/central/chef/menu/bebidas"));
                new GetMenuInfo().execute(new URL("http://192.168.43.116:9080/Proyecto2/central/chef/menu/postres"));
                new GetInventarioInfo().execute(new URL("http://192.168.43.116:9080/Proyecto2/central/chef/inventario/frutas"));
                new GetInventarioInfo().execute(new URL("http://192.168.43.116:9080/Proyecto2/central/chef/inventario/granos"));
                new GetInventarioInfo().execute(new URL("http://192.168.43.116:9080/Proyecto2/central/chef/inventario/carnes"));
                new GetInventarioInfo().execute(new URL("http://192.168.43.116:9080/Proyecto2/central/chef/inventario/lacteos"));
                new GetInventarioInfo().execute(new URL("http://192.168.43.116:9080/Proyecto2/central/chef/inventario/vegetales"));
            } else {
                Toast.makeText(this, "Error de conexion", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    /**
     * Metodo encargado de enviar al servidor la informacion de un ingrediente que se desea agregar al inventario.
     * @param view vista donde se encuentra el boton.
     */
    public void botonAgregar(View view){
        try {
            objetoJSON.put("nombre",String.valueOf(ingred.getText()));
            objetoJSON.put("categoria",String.valueOf(spingred.getSelectedItem()));
            try {
                new EnviarDatosInv().execute(new URL("http://192.168.43.116:9080/Proyecto2/central/chef/inventario/agregar"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }finally{
                ingred.setText("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Metodo encargado de realizar acciones dependiendo la opcion seleccionada en el menu de navegacion
     * @param item opcion seleccionada
     * @return true si se logra realizar la accion correctamente, de lo contrario false.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {            //INVENTARIO
            spc.setVisibility(View.VISIBLE);
            spv.setVisibility(View.VISIBLE);
            spf.setVisibility(View.VISIBLE);
            spl.setVisibility(View.VISIBLE);
            spg.setVisibility(View.VISIBLE);
            tvtf.setVisibility(View.VISIBLE);
            tvtc.setVisibility(View.VISIBLE);
            tvtg.setVisibility(View.VISIBLE);
            tvtl.setVisibility(View.VISIBLE);
            tvtv.setVisibility(View.VISIBLE);
            fmenu.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);

            tvtf.setText("Frutas");
            tvtg.setText("Granos");
            tvtc.setText("Carnes");
            tvtl.setText("Lacteos");
            tvtv.setText("Vegetales");


            String[] categorias = {"fruta", "grano", "carne", "lacteo", "vegetal"};
            ArrayAdapter<String> adapterC = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias);
            spingred.setAdapter(adapterC);
            spf.setAdapter(adapterC);
            spg.setAdapter(adapterC);
            spl.setAdapter(adapterC);
            spv.setAdapter(adapterC);
            spc.setAdapter(adapterC);

            if(continv>5){
                ArrayAdapter<String> adapterF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, invspf);
                spf.setAdapter(adapterF);
                adapterF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, invspc);
                spc.setAdapter(adapterF);
                adapterF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, invspl);
                spl.setAdapter(adapterF);
                adapterF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, invspv);
                spv.setAdapter(adapterF);
                adapterF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, invspg);
                spg.setAdapter(adapterF);
            }


        } else if (id == R.id.nav_gallery) {  //MENU
            spc.setVisibility(View.VISIBLE);
            spv.setVisibility(View.VISIBLE);
            spf.setVisibility(View.VISIBLE);
            spl.setVisibility(View.VISIBLE);
            spg.setVisibility(View.VISIBLE);
            tvtf.setVisibility(View.VISIBLE);
            tvtc.setVisibility(View.VISIBLE);
            tvtg.setVisibility(View.VISIBLE);
            tvtl.setVisibility(View.VISIBLE);
            tvtv.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
            fmenu.setVisibility(View.VISIBLE);

            tvt2.setVisibility(View.GONE);
            tvting.setVisibility(View.GONE);
            btnagr.setVisibility(View.GONE);
            spingred.setVisibility(View.GONE);
            ingred.setVisibility(View.GONE);




            tvtf.setText("Ensaladas");
            tvtg.setText("Sopas");
            tvtc.setText("Platos Fuertes");
            tvtl.setText("Bebida");
            tvtv.setText("Postres");

            if(contmenu>5){
                ArrayAdapter<String> adapterF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, menuspf);
                spf.setAdapter(adapterF);
                adapterF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, menuspc);
                spc.setAdapter(adapterF);
                adapterF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, menuspl);
                spl.setAdapter(adapterF);
                adapterF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, menuspv);
                spv.setAdapter(adapterF);
                adapterF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, menuspg);
                spg.setAdapter(adapterF);
            }

        } else if (id == R.id.nav_slideshow) {  //ORDENES
            Intent flujopasos = new Intent(this, FlujoPasos.class);
            flujopasos.putExtra("nombreC", nombreC);//nombreC
            startActivity(flujopasos);

        }else if (id == R.id.nav_share) {   //CHAT
            Intent chatNom=new Intent(this,Chat.class);
            chatNom.putExtra("nombre",nombreC);
            startActivity(chatNom);
        } else if (id == R.id.nav_send) {
            try {
                new CerrarSesion().execute(new URL("http://192.168.1.62:9080/Proyecto2/central/chef/eliminar"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Metodo que realiza distintas acciones dependiendo el codigo obtenido de una activity
     * @param requestCode codigo unico de cada activity
     * @param resultCode codigo que representa si los procesos terminaron correctamente
     * @param data informacion enviada por un activity.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (808) : {
                if (resultCode == Activity.RESULT_OK) {
                    String newText = data.getStringExtra("parametro");
                    try {
                        paquete.put(new JSONObject(newText));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        new EnviarDatosMenu().execute(new URL("http://192.168.1.62:9080/Proyecto2/central/chef/menu/agregar"));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }


    /**
     * Clase encargada de obtener la informacion del menu almacenada en el servidor.
     */
    public class GetMenuInfo extends AsyncTask<URL, Void, List<String>> {



        @Override
        protected List<String> doInBackground(URL... urls) {

            List<String> msj = null;

            try {
                HttpURLConnection urlConnection = (HttpURLConnection) urls[0].openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader j=new InputStreamReader(in,"UTF-8");
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
        protected void onPostExecute(List<String> coleccion){

            String[] algo=new String[coleccion.size()];
            for(int h=0;h<coleccion.size();h++){
                String[] solonombre=coleccion.get(h).split("jk");
                algo[h]=solonombre[0];
            }

            if(contmenu==1){
            ArrayAdapter<String> adapterC = new ArrayAdapter<String>(spf.getContext(), android.R.layout.simple_spinner_item, algo);
            spf.setAdapter(adapterC);
            menuspf=algo;}
            else if(contmenu==2){
                ArrayAdapter<String> adapterC = new ArrayAdapter<String>(spf.getContext(), android.R.layout.simple_spinner_item, algo);
                spg.setAdapter(adapterC);
                menuspg=algo;
            }else if(contmenu==3){
                ArrayAdapter<String> adapterC = new ArrayAdapter<String>(spf.getContext(), android.R.layout.simple_spinner_item, algo);
                spc.setAdapter(adapterC);
                menuspc=algo;
            }else if(contmenu==4){
                ArrayAdapter<String> adapterC = new ArrayAdapter<String>(spf.getContext(), android.R.layout.simple_spinner_item, algo);
                spl.setAdapter(adapterC);
                menuspl=algo;
            }else if(contmenu==5){
                ArrayAdapter<String> adapterC = new ArrayAdapter<String>(spf.getContext(), android.R.layout.simple_spinner_item, algo);
                spv.setAdapter(adapterC);
                menuspv=algo;
            }
            contmenu++;
    }
    }

    /**
     * Clase encargada de obtener la informacion del inventario de la base de datos del servidor.
     */
    public class GetInventarioInfo extends AsyncTask<URL, Void, List<String>> {

        @Override
        protected List<String> doInBackground(URL... urls) {

            List<String> msj = null;

            try {
                HttpURLConnection urlConnection = (HttpURLConnection) urls[0].openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader j=new InputStreamReader(in,"UTF-8");
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
        protected void onPostExecute(List<String> coleccion){

            String[] algo=new String[coleccion.size()];
            for(int h=0;h<coleccion.size();h++){
                algo[h]=coleccion.get(h);
            }

            if(continv==1){
                ArrayAdapter<String> adapterC = new ArrayAdapter<String>(spf.getContext(), android.R.layout.simple_spinner_item, algo);
                spf.setAdapter(adapterC);
                invspf=algo;}
            else if(continv==2){
                ArrayAdapter<String> adapterC = new ArrayAdapter<String>(spf.getContext(), android.R.layout.simple_spinner_item, algo);
                spg.setAdapter(adapterC);
                invspg=algo;
            }else if(continv==3){
                ArrayAdapter<String> adapterC = new ArrayAdapter<String>(spf.getContext(), android.R.layout.simple_spinner_item, algo);
                spc.setAdapter(adapterC);
                invspc=algo;
            }else if(continv==4){
                ArrayAdapter<String> adapterC = new ArrayAdapter<String>(spf.getContext(), android.R.layout.simple_spinner_item, algo);
                spl.setAdapter(adapterC);
                invspl=algo;
            }else if(continv==5){
                ArrayAdapter<String> adapterC = new ArrayAdapter<String>(spf.getContext(), android.R.layout.simple_spinner_item, algo);
                spv.setAdapter(adapterC);
                invspv=algo;
            }
            continv++;
        }


    }


    /**
     * Clase encargada de enviar al servidor los datos necesarios para almacenar una receta en la base de datos.
     */
    public class EnviarDatosMenu extends AsyncTask<URL, Void, Void> {

        @Override
        protected Void doInBackground(URL... urls) {
            // Obtener la conexión
            HttpURLConnection con = null;

            try {

                con = (HttpURLConnection)urls[0].openConnection();

                // Activar método POST
                con.setDoOutput(true);

                con.setFixedLengthStreamingMode(paquete.toString().getBytes().length);
                con.setRequestProperty("Content-Type","application/json");

                OutputStream out = new BufferedOutputStream(con.getOutputStream());

                out.write(paquete.toString().getBytes());
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
            Toast.makeText(getBaseContext(), "Receta Registrada", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Clase encargada de enviar los datos necesarios al servidor para almacenar un nuevo ingrediente la base de datos.
     */
    public class EnviarDatosInv extends AsyncTask<URL, Void, Void> {

        @Override
        protected Void doInBackground(URL... urls) {
            // Obtener la conexión
            HttpURLConnection con = null;

            try {

                con = (HttpURLConnection)urls[0].openConnection();

                // Activar método POST
                con.setDoOutput(true);

                con.setFixedLengthStreamingMode(objetoJSON.toString().getBytes().length);
                con.setRequestProperty("Content-Type","application/json");

                OutputStream out = new BufferedOutputStream(con.getOutputStream());

                out.write(objetoJSON.toString().getBytes());
                out.flush();
                out.close();
                objetoJSON=new JSONObject();

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
            Toast.makeText(getBaseContext(), "Ingrediente Registrado", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Clase encargada de enviar al servidor el mensaje necesario para registrar al chef como inactivo.
     */
    public class CerrarSesion extends AsyncTask<URL, Void, Void> {

        @Override
        protected Void doInBackground(URL... urls) {
            // Obtener la conexión
            HttpURLConnection con = null;

            try {

                con = (HttpURLConnection)urls[0].openConnection();

                // Activar método POST
                con.setDoOutput(true);

                String[] normal=nombreC.split(" ");
                nombreC="";
                for(int e=0;e<normal.length;e++){
                    nombreC=nombreC+normal[e];
                }

                con.setFixedLengthStreamingMode(nombreC.getBytes().length);
                con.setRequestProperty("Content-Type","application/json");

                OutputStream out = new BufferedOutputStream(con.getOutputStream());

                out.write(nombreC.getBytes());
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
