package com.example.brijesh.moviedb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.brijesh.moviedb.FragmentPortrait;
import com.example.brijesh.moviedb.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Brijesh on 22-02-2016.
 */
public class ImageListAdapter extends ArrayAdapter {

    private Context context;
    private LayoutInflater inflater;
    final String LOG_TAG= FragmentPortrait.GetMovieTask.class.getSimpleName();

    private String[] imageUrls;

    public ImageListAdapter(Context context, String[] imageUrls) {
        super(context, R.layout.list_item_movies, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.list_item_movies, parent, false);
        }
        Log.d(LOG_TAG, imageUrls[position]);
        Picasso.with(context)
                .load(imageUrls[position])
                .resize(400,500)
            // .fit() // will explain later
                .into((ImageView) convertView);

        return convertView;
    }

}
