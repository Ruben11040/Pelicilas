package pgv.tema2.peliculas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfigurationActivity extends AppCompatActivity {
    private SharedPreferences preferencias;
    private EditText etApiKey;
    private EditText editTextEndPointF;
    private EditText editTextEndPointCredits;
    private  Button btGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EditText etApiKey = (EditText) findViewById(R.id.etApiKey);
        EditText editTextEndPointF = (EditText) findViewById(R.id.editTextEndPointF);
        EditText editTextEndPointCredits = (EditText) findViewById(R.id.editTextEndPointCredits);
        Button btGuardar = (Button) findViewById(R.id.btGuardar);

        // Preferencias.
        preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);

        // Mostrar en la pantalla.
        etApiKey.setText( preferencias.getString("api_key","") );
        editTextEndPointF.setText( preferencias.getString("api","") );
        editTextEndPointCredits.setText( preferencias.getString("name","") );


        btGuardar.setOnClickListener(e->{
            // Guardar preferencias y salir.
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putString("api", editTextEndPointF.getText().toString());
            editor.putString("api_key", etApiKey.getText().toString());
            editor.putString("url_images", editTextEndPointCredits.getText().toString());
            editor.commit();

            Toast t = Toast.makeText(getApplicationContext(), "Configuración guardada correctamente.", Toast.LENGTH_SHORT);
            t.show();

            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
