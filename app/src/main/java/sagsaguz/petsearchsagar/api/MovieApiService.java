package sagsaguz.petsearchsagar.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import sagsaguz.petsearchsagar.model.MovieDetail;
import sagsaguz.petsearchsagar.model.MovieResponse;
import sagsaguz.petsearchsagar.utils.Config;

public interface MovieApiService {

    @GET(Config.MOVIE_LIST_URL)
    Call<MovieResponse> getPopularMovies();

    @GET(Config.MOVIE_DETAIL_URL)
    Call<MovieDetail> getMovieDetails(@Path("movieId") int id);

}
