package sagsaguz.petsearchsagar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sagsaguz.petsearchsagar.R;
import sagsaguz.petsearchsagar.model.Movie;
import sagsaguz.petsearchsagar.utils.Config;

public class MoviesAdapter extends ArrayAdapter<Movie>{

    private Context context;
    private List<Movie> movies;

    public MoviesAdapter(@NonNull Context context, List<Movie> movieNames) {
        super(context, R.layout.list_items, movieNames);

        this.context = context;
        this.movies = movieNames;
    }

    @SuppressLint("SimpleDateFormat")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;

        if (row == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            row = layoutInflater.inflate(R.layout.list_items, parent, false);
        }

        ImageView movieImage = row.findViewById(R.id.movieImage);
        String imageUrl = Config.IMAGE_BASE_URL.concat(movies.get(position).getPosterPath());
        Glide.with(context)
                .load(imageUrl)
                .into(movieImage);
        TextView movieName = row.findViewById(R.id.movieName);
        movieName.setText(movies.get(position).getOriginalTitle());
        TextView movieDesc = row.findViewById(R.id.movieDesc);
        movieDesc.setText(movies.get(position).getOverview());
        TextView movieRating = row.findViewById(R.id.movieRating);
        movieRating.setText(""+movies.get(position).getVoteAverage());
        TextView movieReleaseDate = row.findViewById(R.id.movieReleaseDate);
        String date = movies.get(position).getReleaseDate();
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd");
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("MMM dd, yyyy");
        date = spf.format(newDate);
        movieReleaseDate.setText(date);
        TextView movieLang = row.findViewById(R.id.movieLang);
        movieLang.setText(movies.get(position).getOriginalLanguage().toUpperCase());

        return row;
    }
}
