package sagsaguz.petsearchsagar.utils;

public class Config {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_KEY = "4fde9572f70186fdd74e71961e52f1db";
    public static final String MOVIE_LIST_URL = "discover/movie?sort_by=popularity.desc&api_key=" + API_KEY;
    public static final String MOVIE_DETAIL_URL = "movie/{movieId}?api_key=" + API_KEY + "&language=en-US";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w300_and_h450_bestv2";

}
