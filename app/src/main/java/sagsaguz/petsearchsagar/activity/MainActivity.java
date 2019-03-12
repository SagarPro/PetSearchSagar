package sagsaguz.petsearchsagar.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sagsaguz.petsearchsagar.R;
import sagsaguz.petsearchsagar.adapter.MoviesAdapter;
import sagsaguz.petsearchsagar.api.MovieApiService;
import sagsaguz.petsearchsagar.model.Movie;
import sagsaguz.petsearchsagar.model.MovieResponse;
import sagsaguz.petsearchsagar.utils.Config;
import sagsaguz.petsearchsagar.utils.Network;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout rlMainActivity;

    private ListView listView;
    private List<Movie> movies;

    private Snackbar snackbar;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlMainActivity = findViewById(R.id.rlMainActivity);
        listView = findViewById(R.id.lvMovies);

        setTitle("Popular Movies");

        progressDialog = new ProgressDialog(this, R.style.AlertDialogStyle);
        progressDialog.setMessage("Loading, please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        MovieApiService service = retrofit.create(MovieApiService.class);
        Call<MovieResponse> call = service.getPopularMovies();

        movies = new ArrayList<>();

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null) {
                    movies.clear();
                    movies = response.body().getResults();
                    listView.setAdapter(new MoviesAdapter(MainActivity.this, movies));
                }else {
                    showSnackBarMessage("Network Error! Please try again", true);
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                if (!Network.isInternetAvaialble(getBaseContext())) {
                    showSnackBarMessage("Internet not available", true);
                } else {
                    showSnackBarMessage("Network Error! Please try again", true);
                }
                progressDialog.dismiss();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), MovieDetailsActivity.class);
                intent.putExtra("MOVIENAME", movies.get(position).getOriginalTitle());
                intent.putExtra("MOVIEID", movies.get(position).getId());
                startActivity(intent);
            }
        });

    }

    private void showSnackBarMessage(String message, boolean retry){
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
        int snackBarLength = retry ? Snackbar.LENGTH_INDEFINITE : Snackbar.LENGTH_SHORT;
        snackbar = Snackbar.make(rlMainActivity, message, snackBarLength);
        if (retry) {
            snackbar.setAction("Try Again", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                    recreate();
                }
            });
        }
        snackbar.show();
    }

}
