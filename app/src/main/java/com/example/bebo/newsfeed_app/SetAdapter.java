package com.example.bebo.newsfeed_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
 class SetAdapter extends ArrayAdapter<MainList> {

    SetAdapter(Context context, ArrayList<MainList> mainList) {
        super(context, 0, mainList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MainList item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView nameText = convertView.findViewById(R.id.info);
        TextView Name = convertView.findViewById(R.id.name);
        TextView author = convertView.findViewById(R.id.author);
        TextView webPublicationDate = convertView.findViewById(R.id.webPublicationDate);
        if (item != null) {
            nameText.setText("description : " + item.getLocation());
            Name.setText("section name : " + item.getName());
            author.setText("author name : " + item.getAuthorName());
            webPublicationDate.setText("date : " + item.getWebPublicationDate());
        }
        return convertView;
    }
}
