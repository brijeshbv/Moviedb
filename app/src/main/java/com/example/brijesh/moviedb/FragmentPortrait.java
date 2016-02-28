package com.example.brijesh.moviedb;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Brijesh on 25-02-2016.
 */
public class FragmentPortrait extends Fragment {


    private ArrayAdapter<String> movieAdapter;
    ArrayList<String> poster_array = new ArrayList<String>();

    String[] poster_array_string;

    public GridView gridViewPass;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


   View rootView= inflater.inflate(R.layout.fragment_main,container,false);
        GridView gridView = (GridView) rootView.findViewById(R.id.usage_example_gridview);

        return inflater.inflate(R.layout.fragment_main,container,false);

    }
public Void passGridView(GridView gridView) {

gridViewPass=gridView;
    return null;
}

    @Override
    public void onStart() {
        super.onStart();
        getJson();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment,menu);
    }

    private void getJson(){
        GetMovieTask movie = new GetMovieTask();
        movie.execute();
    }
    public void update() {
        GridView gridView = (GridView) getView().findViewById(R.id.usage_example_gridview);
      final ImageListAdapter  imageListAdapter= new ImageListAdapter(getActivity(),poster_array_string);
        gridView.setAdapter(imageListAdapter);
     gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View v, int position, long l) {
               Object moviedetail = imageListAdapter.getItem(position);
               String moviedeets = moviedetail.toString();
               Intent intent = new Intent(getActivity(), DetailActivity.class);
               intent.putExtra(Intent.EXTRA_TEXT,moviedeets);
               startActivity(intent);
           }

       });

    }

    public class GetMovieTask extends AsyncTask<Void,Void,String[]> {

        private final String LOG_TAG= GetMovieTask.class.getSimpleName();




        private String[]  updateView(String movieJsonStr)
                throws JSONException {
            final String JSON_poster="poster_path";
            final String JSON_result="results";
            final String URL_poster="http://image.tmdb.org/t/p/";
            final String poster_size="w185/";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieJsonArray = movieJson.getJSONArray(JSON_result);
            //   String[] poster_array=new String[movieJsonArray.length()];
            for(int i=0;i<movieJsonArray.length();i++)
            {
                JSONObject movieEach= movieJsonArray.getJSONObject(i);
                String poster_path = movieEach.getString(JSON_poster);
                poster_path= URL_poster+poster_size+poster_path;

                poster_array.add(poster_path);

            }

//.....
            poster_array_string = poster_array.toArray(new String[poster_array.size()]);



            return null;
        }



        @Override
        protected String[] doInBackground(Void... params) {



            HttpURLConnection urlConnection =null;
            BufferedReader reader=null;
            String movieJsonStr=null;
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";

                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.TMDB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();



            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {

                return updateView(movieJsonStr);
            }catch (JSONException e){
                Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
         update();
        }
    }
}
