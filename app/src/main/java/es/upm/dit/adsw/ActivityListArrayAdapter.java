package es.upm.dit.adsw;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityListArrayAdapter extends AppCompatActivity {
    private static final String TAG = ActivityListArrayAdapter.class.getName();
    private final static int PICKER_RESULT = 888;
    private List<String> listItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_array_adapter);
        listItems = new ArrayList<>();
        rellenaLista();
        ListView miListView = (ListView) findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        miListView.setAdapter(adapter);
        miListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = "Seleccionado " + parent.getAdapter().getItem(position);
                Log.d(TAG, msg);
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0: { llama(); break; }
                    case 1: { listaContactos(); break; }
                    case 2: { verContacto(); break; }
                    case 3: { editarContacto(); break; }
                    case 4: { verWeb(); break; }
                    case 5: { enviarCorreo(); break; }
                    case 6: { seleccionarContacto(); break; }
                    case 7: { compartir(); break; }
                    default: break;
                }
            }
        });

    }


    private void rellenaLista() {
        String[] items = {"Llamar por teléfono", "Listar contactos", "Ver contacto", "Editar contacto", "Ver web",
                "Enviar un correo", "Escoger un contacto", "Compartir"};
        listItems.addAll(Arrays.asList(items));
    }

    private void llama() {

        Uri numero = Uri.parse("tel:915495700");
        Intent miIntencion = new Intent(Intent.ACTION_CALL, numero);
        startActivity(miIntencion);
		/* Alternativa
		Intent miIntencion2 = new Intent(Intent.ACTION_CALL);
		miIntencion2.setData(numero);
		startActivity(miIntencion2);
        */
    }

    private void listaContactos() {
        Uri uri = Uri.parse("content://contacts/people/");
        Intent miIntencion = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(miIntencion);
    }

    private void verContacto() {
        Uri uri = Uri.parse("content://contacts/people/1");
        Intent miIntencion = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(miIntencion); // permiso READ_CONTACTS
    }

    private void editarContacto() {
        Uri uri = Uri.parse("content://contacts/people/1");
        Intent miIntencion = new Intent(Intent.ACTION_EDIT, uri);
        startActivity(miIntencion); // permiso WRITE_CONTACTS
    }

    private void verWeb() {

        Uri webPage = Uri.parse("http://www.dit.upm.es");
        Intent intention = new Intent(Intent.ACTION_VIEW, webPage);  // requiere permiso INTERNET

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intention, 0);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
            startActivity(intention);
        }


    }

    private void enviarCorreo() {
        Uri correo = Uri.parse("mailto:adsw@dit.upm.es");
        Intent miIntencion = new Intent(Intent.ACTION_SENDTO, correo);
        miIntencion.putExtra(Intent.EXTRA_SUBJECT, "práctica");
        miIntencion.putExtra(Intent.EXTRA_TEXT, "Hola");
        startActivity(miIntencion);
    }

    private void seleccionarContacto() {

        //Uri uri = Uri.parse("content://contacts");

        Intent miIntencion = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(miIntencion, PICKER_RESULT);
    }

    private void compartir() {
        String titulo = "Compartir con";
        Intent compartir = new Intent(Intent.ACTION_SEND);
        compartir.setType("text/plain");
        Intent escoger = Intent.createChooser(compartir, titulo);
        startActivity(escoger);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case (PICKER_RESULT): {
                    if (resultCode == Activity.RESULT_OK) {
                        String seleccionado = data.getDataString();
                        Uri uri = Uri.parse(seleccionado);
                        Log.i(TAG, "onActivityResult seleccionado " + uri);
                        Intent miActividadVer = new Intent(Intent.ACTION_VIEW,
                                uri);
                        startActivity(miActividadVer);
                    } else { // Activity.RESULT_CANCELLED
                        Log.i(TAG, "onActivityResult nada seleccionado");
                        Toast.makeText(getBaseContext(),
                                "Cancelado", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        } // catch
    } // onActivityResult
}
