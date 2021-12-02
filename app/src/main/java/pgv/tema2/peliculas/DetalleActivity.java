package pgv.tema2.peliculas;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class DetalleActivity extends AppCompatActivity {
    private String titulo, imagen, sinopsis, id;
    private Button btnactores;
    private static final String MOVIE_BASE_URL="https://image.tmdb.org/t/p/w185";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        btnactores = findViewById(R.id.btnactores);
        // Leer el parametro de la pelicula.
        Intent intent = getIntent();
        this.titulo = intent.getStringExtra("titulo");
        this.imagen = intent.getStringExtra("imagen");
        this.sinopsis = intent.getStringExtra("sinopsis");
        this.id = intent.getStringExtra("id");

        Log.d("test", "Creditos: Leyendo id:" + id);
        Log.d("test", "Creditos: Leyendo titulo:" + titulo);
        TextView txtTitle = findViewById(R.id.txtTitulo);
        TextView txtText = findViewById(R.id.txtDescripcion);
        ImageView imgView = findViewById(R.id.ivPortada);
        txtTitle.setText(titulo);
        txtText.setText(sinopsis);
        Picasso.get().load(MOVIE_BASE_URL + imagen).into(imgView);
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        btnactores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent p = new Intent(DetalleActivity.this, ListaActoresActivity.class);
                Bundle b = new Bundle();
                b.putString("idPeliculas", id);
                p.putExtras(b);
                startActivity(p);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
