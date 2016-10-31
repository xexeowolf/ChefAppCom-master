package proyecto.com.chefappcom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;



/**
 * Created by alfredo on 22/10/16.
 * Clase encargada de obtener la informacion de una receta para posteriormente enviarla al servidor.
 */

public class LogicaMenu extends AppCompatActivity {

    private ListView l1;
    private EditText txtcom,txtprecio,txtiempo,txtnombre;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
    private Button btnain,btnacom,btnext1,btnext3,btnext2;
    private Spinner spci,spi,spcat;
    private List<String> opciones= new ArrayList<>();
    private ArrayAdapter<String> ad;
    private JSONObject medio=new JSONObject();
    private String[]fruta=null,grano=null,carne=null,lacteo=null,vegetal=null;


    /**
     * Constructor de la clase
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opciones_menu);
        l1=(ListView)findViewById(R.id.l1);
        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);
        tv4=(TextView)findViewById(R.id.tv4);
        tv5=(TextView)findViewById(R.id.tv5);
        tv6=(TextView)findViewById(R.id.tv6);
        tv7=(TextView)findViewById(R.id.tv7);
        tv7.setVisibility(View.GONE);
        tv4.setVisibility(View.GONE);
        tv5.setVisibility(View.GONE);
        tv6.setVisibility(View.GONE);
        txtcom=(EditText)findViewById(R.id.txtcom);
        txtiempo=(EditText)findViewById(R.id.numt);
        txtprecio=(EditText)findViewById(R.id.numc);
        txtnombre=(EditText)findViewById(R.id.nom);
        txtnombre.setVisibility(View.GONE);
        txtcom.setVisibility(View.GONE);
        txtprecio.setVisibility(View.GONE);
        txtiempo.setVisibility(View.GONE);
        btnain=(Button)findViewById(R.id.btnain);
        btnacom=(Button)findViewById(R.id.btnacom);
        btnacom.setVisibility(View.GONE);
        btnext1=(Button)findViewById(R.id.btnext1);
        btnext2=(Button)findViewById(R.id.btnext2);
        btnext3=(Button)findViewById(R.id.btnext3);
        btnext2.setVisibility(View.GONE);
        btnext3.setVisibility(View.GONE);
        spi=(Spinner)findViewById(R.id.spi);
        spci=(Spinner)findViewById(R.id.spci);
        spcat=(Spinner)findViewById(R.id.spcat);
        String[] opcionesS={"Frutas","Granos","Carnes","Lacteos","Vegetal"};
        String[] opcionesD={"ensalada","sopa","platofuerte","bebida","postre"};
        ArrayAdapter<String> adapterS= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,opcionesS);
        ArrayAdapter<String> adapterD= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,opcionesD);
        spci.setAdapter(adapterS);
        spcat.setAdapter(adapterD);
        spi.setAdapter(adapterS);
        spcat.setVisibility(View.GONE);
        ad=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,opciones);
        l1.setAdapter(ad);
        l1.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fruta=(String[])extras.get("frutas");
            grano=(String[])extras.get("granos");
            carne=(String[])extras.get("carnes");
            lacteo=(String[])extras.get("lacteos");
            vegetal=(String[])extras.get("vegetales");
            adapterD=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,fruta);
            spi.setAdapter(adapterD);
        }

        spci.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adapterT;
                switch(position){
                    case 0: adapterT=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item,fruta);spi.setAdapter(adapterT);break;
                    case 1: adapterT=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item,grano);spi.setAdapter(adapterT);break;
                    case 2: adapterT=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item,carne);spi.setAdapter(adapterT);break;
                    case 3: adapterT=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item,lacteo);spi.setAdapter(adapterT);break;
                    case 4: adapterT=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item,vegetal);spi.setAdapter(adapterT);break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }


    /**
     * Metodo encargado de almacenar los datos de la primera pantalla.
     * @param view vista en la cual se encuentran los botones.
     */
    public void BotonNext1(View view){
        try {
            medio.put("cantidad",opciones.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        l1.setVisibility(View.GONE);
        opciones.clear();
        ad.notifyDataSetChanged();
        spi.setVisibility(View.INVISIBLE);
        spci.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);
        tv3.setVisibility(View.INVISIBLE);
        txtcom.setVisibility(View.VISIBLE);
        btnacom.setVisibility(View.VISIBLE);
        btnext1.setVisibility(View.INVISIBLE);
        btnext2.setVisibility(View.VISIBLE);
        btnain.setVisibility(View.INVISIBLE);
        btnacom.setVisibility(View.VISIBLE);
        tv1.setText("Ingrese los pasos para preparar la receta");
    }

    /**
     * Metodo encargado de almacenar los datos de la segunda pantalla.
     * @param view vista donde se encuentran los botones.
     */
    public void BotonNext2(View view){

        try {
            medio.put("pasos",opciones.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv1.setText("Ingrese la informacion nutricional:");
        l1.setVisibility(View.GONE);
        btnext2.setVisibility(View.GONE);
        btnext3.setVisibility(View.VISIBLE);
        btnacom.setVisibility(View.GONE);
        tv4.setVisibility(View.VISIBLE);
        tv5.setVisibility(View.VISIBLE);
        tv6.setVisibility(View.VISIBLE);
        tv7.setVisibility(View.VISIBLE);
        txtprecio.setVisibility(View.VISIBLE);
        txtiempo.setVisibility(View.VISIBLE);
        txtnombre.setVisibility(View.VISIBLE);
        spcat.setVisibility(View.VISIBLE);
        tv4.setText("Ingrese el tiempo de preparacion:");


    }

    /**
     * Metodo que recopila toda la informacion obtenida y la envia al servidor
     * @param view vista donde se encuentran los botones.
     */
    public void BotonNext3(View view){
        try {

            medio.put("informacion",String.valueOf(txtcom.getText()));
            medio.put("nombre",String.valueOf(txtnombre.getText()));
            medio.put("precio",Integer.parseInt(String.valueOf(txtprecio.getText())));
            medio.put("tiempo",Integer.parseInt(String.valueOf(txtiempo.getText())));
            switch (String.valueOf(spcat.getSelectedItem())){
                case "ensalada":medio.put("categoria",1);break;
                case "sopa":medio.put("categoria",2);break;
                case "platofuerte":medio.put("categoria",3);break;
                case "bebida":medio.put("categoria",4);break;
                case "postre":medio.put("categoria",5);break;
            }

            opciones.add(spi.getSelectedItem()+" : "+spci.getSelectedItem());
            ad.notifyDataSetChanged();
            l1.setVisibility(View.VISIBLE);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("parametro",medio.toString());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * Boton que agrega un ingrediente a una lista enlazada.
     * @param view vista donde se encuenta el boton.
     */
    public void BotonIngrediente(View view){

        try {
            String nm=String.valueOf(opciones.size()+1);
            medio.put("ingrediente"+nm,(String)spi.getSelectedItem());
            opciones.add(spi.getSelectedItem()+" : "+spci.getSelectedItem());
            ad.notifyDataSetChanged();
            l1.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Metodo que agrega una cadena de texto a una lista enlazada
     * @param view vista donde se encuentra el boton.
     */
    public void BotonPaso(View view){
        try {
            String nm=String.valueOf(opciones.size()+1);
            medio.put("paso"+nm,String.valueOf(txtcom.getText()));
            opciones.add("Paso"+nm+" registrado:"+txtcom.getText());
            txtcom.setText("");
            ad.notifyDataSetChanged();
            l1.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
