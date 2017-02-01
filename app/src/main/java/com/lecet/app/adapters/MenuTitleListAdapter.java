package com.lecet.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecet.app.R;

/**
 * Created by Josué Rodríguez on 11/11/2016.
 */

public class MenuTitleListAdapter extends BaseAdapter {

    private Context context;
    private String title;
    private String[] options;
    private int size;

    public MenuTitleListAdapter(Context context, String title, String[] options) {
        this.context = context;
        this.title = title;
        this.options = options;
        this.size = options.length + 1;// + 1 title header
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return title;
        } else {
            return options[position - 1];
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        if (position == 0) { //Inflate the custom top item
            convertView = inflater.inflate(R.layout.list_item_mtm_title, parent, false);
        } else {
            convertView = inflater.inflate(R.layout.list_item_mtm_project_or_company, parent, false);
        }

        MenuTitleListAdapter.Holder holder = new MenuTitleListAdapter.Holder(convertView);
        if (position == 0) {
            holder.imageView1.setVisibility(View.GONE);
        }

        Object item = getItem(position);

        holder.text1.setText(item.toString()); //should be String object

        return convertView;
    }

    private class Holder {
        private TextView text1;
        private TextView text2;
        private ImageView imageView1;

        Holder(View view) {
            text1 = (TextView) view.findViewById(R.id.text1);
            text2 = (TextView) view.findViewById(R.id.text2);
            imageView1 = (ImageView) view.findViewById(R.id.imageView1);
        }
    }

}
