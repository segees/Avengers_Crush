package com.example.avengerscrush;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<UserInfo> {

    public ListViewAdapter(@NonNull  Context context, int resource, ArrayList<UserInfo> users) {
        super(context, resource, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.user_score_layout, parent, false);
        }

        UserInfo currentItem = getItem(position);
        TextView name = listItem.findViewById(R.id.name_tv);
        TextView score = listItem.findViewById(R.id.score_tv);

        name.setText(currentItem.getName());
        score.setText("" + currentItem.getScore());

        return listItem;
    }
}