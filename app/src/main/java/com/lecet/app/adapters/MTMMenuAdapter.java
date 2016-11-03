package com.lecet.app.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.data.models.User;
import com.squareup.picasso.Picasso;

/**
 * Created by Josué Rodríguez on 3/11/2016.
 */

public class MTMMenuAdapter extends BaseAdapter {


    private Context context;
    private User user;
    private String[] options;
    private int size;

    public MTMMenuAdapter(Context context, User user, String[] options) {
        this.context = context;
        this.user = user;
        this.options = options;
        this.size = options.length + 1;// + 1 is user header
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return user;
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

        MTMMenuAdapter.Holder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            if (position == 0) { //Inflate the custom top item
                convertView = inflater.inflate(R.layout.list_item_overflow_user, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.list_item_overflow_option, parent, false);
            }
            holder = new MTMMenuAdapter.Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MTMMenuAdapter.Holder) convertView.getTag();
        }

        Object item = getItem(position);
        if (item instanceof User) {
            User user = (User) item;
            holder.text1.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
            holder.text2.setText(user.getEmail());
            if (TextUtils.isEmpty(user.getAvatar())) {
                Picasso.with(context).load(R.drawable.ic_user_placeholder).into(holder.imageView1);
            } else {
                Picasso.with(context).load(user.getAvatar()).into(holder.imageView1);
            }
        } else {
            holder.text1.setText(item.toString()); //should be String object
        }

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
