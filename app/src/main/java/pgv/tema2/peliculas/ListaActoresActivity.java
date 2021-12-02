package pgv.tema2.peliculas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListaActoresActivity extends AppCompatActivity {

        private ArrayList<Actores> listaActores = new ArrayList<Actores>();
        private ListView list;
        private String id = null;
        public static String endPointActores = null;
        public static final String MOVIE_BASE_URL="https://image.tmdb.org/t/p/w185";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_list_actores);
            Bundle b = getIntent().getExtras();
            id = b.getString("idPeliculas");
            list = (ListView) findViewById(R.id.listViewActores);
            endPointActores = "https://api.themoviedb.org/3/movie/"+id+"/casts?api_key=1865f43a0549ca50d341dd9ab8b29f49";
            new pgv.tema2.peliculas.ListaActoresActivity.ObtenerPeliculasAsync().execute(endPointActores);

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            Intent intent;

            // Handle item selection
            switch (item.getItemId()) {
                case R.id.mnuConfiguracion:
                    intent = new Intent(getApplicationContext(), ConfigurationActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.mnuAcerca:
                    intent = new Intent(getApplicationContext(), AcercaActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.mnuSalir:
                    finish();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }


        class ObtenerPeliculasAsync extends AsyncTask<String, Integer, String> {
            ProgressDialog progreso;

            protected void onPreExecute () {
                super.onPreExecute();

                // Mostrar progress bar.
                progreso = new ProgressDialog(ListaActoresActivity.this);
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Obteniendo actores ...");
                progreso.setCancelable(false);
                progreso.setMax(100);
                progreso.setProgress(0);
                progreso.show();
            }

            protected String doInBackground(String... params) {
                StringBuilder result = new StringBuilder();
                Log.d("test", "entrando");
                try {
                    URL urlObj = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;

                    while ((line = reader.readLine()) != null) result.append(line);

                    Log.d("test", "respuesta: " + result.toString());

                } catch (Exception e) {
                    Log.d("test", "error2: " + e.toString());
                }

                return result.toString();
            }

            protected void onProgressUpdate(Integer...a){
                super.onProgressUpdate(a);
            }

            protected void onPostExecute(String result) {
                Log.d("test", "Result from server: " + result);

                try {
                    JSONObject resp = new JSONObject(result);
                    JSONArray actors= resp.getJSONArray("cast");

                    for (int i = 0; i < actors.length(); i++) {
                        JSONObject movie = actors.getJSONObject(i);
                        listaActores.add(new Actores(
                                movie.getString("name"),
                                movie.getString("character"),
                                movie.getString("profile_path")));
                    }
                    Thread.sleep(2000);
                } catch (InterruptedException | JSONException i) {
                    i.printStackTrace();
                }
                progreso.dismiss();
                AdaptadorActores adapter = new AdaptadorActores(getApplicationContext(), listaActores);
                list.setAdapter(adapter);
            }
        }

        class AdaptadorActores extends BaseAdapter {
            Context context;
            ArrayList<Actores> arrayList;

            public AdaptadorActores(Context context, ArrayList<Actores> arrayList) {
                this.context = context;
                this.arrayList = arrayList;
            }

            public int getCount() {
                return arrayList.size();
            }

            public Actores getItem(int position) {
                return arrayList.get(position);
            }

            public long getItemId(int i) {
                return i;
            }

            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView ==  null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.actores, parent, false);
                }

                TextView name = (TextView) convertView.findViewById(R.id.tvTitlee);
                name.setText(arrayList.get(position).getNombre());

                TextView description = convertView.findViewById(R.id.tvDescripcionn);
                description.setText(arrayList.get(position).getApellidos());

                ImageView imagen = (ImageView) convertView.findViewById(R.id.list_imagee);
                Picasso.get().load(MOVIE_BASE_URL + arrayList.get(position).getImagen()).into(imagen);
                imagen.setScaleType(ImageView.ScaleType.FIT_XY);

                return convertView;
            }
        }

        @Override
        public boolean onSupportNavigateUp() {
            onBackPressed();
            return false;
        }

    }
