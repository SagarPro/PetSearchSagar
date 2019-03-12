package sagsaguz.petsearchsagar.activity;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sagsaguz.petsearchsagar.R;
import sagsaguz.petsearchsagar.adapter.MoviesAdapter;
import sagsaguz.petsearchsagar.api.MovieApiService;
import sagsaguz.petsearchsagar.model.Genre;
import sagsaguz.petsearchsagar.model.MovieDetail;
import sagsaguz.petsearchsagar.model.MovieResponse;
import sagsaguz.petsearchsagar.utils.Config;
import sagsaguz.petsearchsagar.utils.Network;

public class MovieDetailsActivity extends AppCompatActivity {

    private RelativeLayout rlMovieDetails;
    private Snackbar snackbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rlMovieDetails = findViewById(R.id.rlMovieDetails);
        rlMovieDetails.setVisibility(View.GONE);

        setTitle(getIntent().getStringExtra("MOVIENAME"));

        progressDialog = new ProgressDialog(this, R.style.AlertDialogStyle);
        progressDialog.setMessage("Loading, please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        MovieApiService service = retrofit.create(MovieApiService.class);
        Call<MovieDetail> call = service.getMovieDetails(getIntent().getIntExtra("MOVIEID", 0));

        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.body() != null) {
                    setMovieDetails(response.body());
                } else {
                    showSnackBarMessage("Network Error! Please try again", true);
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                if (!Network.isInternetAvaialble(getBaseContext())) {
                    showSnackBarMessage("Internet not available", true);
                } else {
                    showSnackBarMessage("Network Error! Please try again", true);
                }
                progressDialog.dismiss();
            }
        });

    }

    private void setMovieDetails(MovieDetail movieDetails){
        //Toast.makeText(this, ""+movieDetails.getBudget(), Toast.LENGTH_SHORT).show();
        ImageView coverImage = findViewById(R.id.coverImage);
        String imageUrl = Config.IMAGE_BASE_URL.concat(movieDetails.getBackdropPath());
        Glide.with(getBaseContext())
                .load(imageUrl)
                .into(coverImage);
        TextView overview = findViewById(R.id.overview);
        overview.setText(movieDetails.getOverview());
        TextView duration = findViewById(R.id.duration);
        duration.setText(movieDetails.getRuntime().toString());
        TextView releaseDate = findViewById(R.id.releaseDate);
        String date = movieDetails.getReleaseDate();
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd");
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("MMM dd, yyyy");
        date = spf.format(newDate);
        releaseDate.setText(date);
        TextView rating = findViewById(R.id.rating);
        rating.setText(movieDetails.getVoteAverage().toString());
        TextView genre = findViewById(R.id.genre);
        genre.setText(getGenreNames(movieDetails.genres));
        TextView language = findViewById(R.id.language);
        language.setText(movieDetails.getOriginalLanguage().toUpperCase());
        TextView budget = findViewById(R.id.budget);
        budget.setText(String.valueOf("$").concat(truncateNumber(movieDetails.getBudget())));
        TextView revenue = findViewById(R.id.revenue);
        revenue.setText(String.valueOf("$").concat(truncateNumber(movieDetails.getRevenue())));

        rlMovieDetails.setVisibility(View.VISIBLE);
    }

    private String getGenreNames(List<Genre> genreList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Genre genre : genreList) {
            stringBuilder.append(genre.getName().concat(","));
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public String truncateNumber(float floatNumber) {
        long million = 1000000L;
        long billion = 1000000000L;
        long number = Math.round(floatNumber);
        if ((number >= million) && (number < billion)) {
            float fraction = calculateFraction(number, million);
            return Float.toString(fraction) + "Million";
        } else if (number >= billion) {
            float fraction = calculateFraction(number, billion);
            return Float.toString(fraction) + "Billion";
        }
        return Long.toString(number);
    }

    public float calculateFraction(long number, long divisor) {
        long truncate = (number * 10L + (divisor / 2L)) / divisor;
        return (float) truncate * 0.10F;
    }

    private void showSnackBarMessage(String message, boolean retry){
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
        int snackBarLength = retry ? Snackbar.LENGTH_INDEFINITE : Snackbar.LENGTH_SHORT;
        snackbar = Snackbar.make(rlMovieDetails, message, snackBarLength);
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

}
