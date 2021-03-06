package proyecto.com.chefappcom;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by alfredo on 22/10/16.
 * Clase encargada de extraer la informacion del flujo de datos entrante en formato JSON.
 */

public class LectorJSON {

    /*
   Método encargado de coordinar la conversión final de un flujo
   con formato JSON
    */
    public List<String> readJsonStream(InputStream in) throws IOException {

        // Nueva instancia de un lector JSON
        JsonReader reader = new JsonReader(
                new InputStreamReader(in, "UTF-8"));

        try {
            return readCommentsArray(reader);
        }
        finally{
            reader.close();
        }
    }

    /*
    Este método lee cada elemento al interior de un array JSON
     */
    public List<String> readCommentsArray(JsonReader reader) throws IOException {
        List<String> comments = new ArrayList<>();

        // Se dirige al corchete de apertura del arreglo
        reader.beginArray();
        while (reader.hasNext()) {
            comments.add(readMessage(reader,"atributo"));
        }

        // Se dirige al corchete de cierre
        reader.endArray();
        return comments;
    }

    /*
    Metodo que lee los atributos de cada objeto
     */
    public String readMessage(JsonReader reader,String llave) throws IOException {

        // Cuerpo del comentario
        String body = null;

        // Se dirige a la llave de apertura del objeto
        reader.beginObject();

        while (reader.hasNext()) {

            // Se obtiene el nombre del atributo
            String name = reader.nextName();

            if (name.equals(llave)) {
                body= reader.nextString();
            }else {
                reader.skipValue();
            }
        }

        // Se dirige a la llave de cierre del objeto
        reader.endObject();
        return body;
    }
}

