package com.eventtus.twitterapp.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventtus.twitterapp.R;
import com.eventtus.twitterapp.utilities.ImageLoader;

import java.util.ArrayList;


public class FollowersAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Followers> Items;
    public ImageLoader imageLoader;

    public FollowersAdapter(Context context, ArrayList<Followers> Items){
        this.context = context;
        this.Items = Items;
        imageLoader = new ImageLoader(context.getApplicationContext());
    }

    private class ViewHolder {
        TextView name;
        TextView handle;
        TextView description;
        ImageView profImage;
    }
    @Override
    public int getCount() {
        return Items.size();
    }

    @Override
    public Object getItem(int position) {
        return Items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.follower_single_item, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.handle = (TextView) convertView.findViewById(R.id.handle);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.profImage = (ImageView) convertView.findViewById(R.id.profile_image);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Followers item = (Followers) getItem(position);

        holder.name.setText(item.getName());
        holder.handle.setText(item.getHandle());

        imageLoader.DisplayImage(item.getProfImage(), holder.profImage);

        if(item.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        }else{
            holder.description.setText(item.getDescription());
        }

        return convertView;

    }
}

