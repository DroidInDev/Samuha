package com.stgobain.samuha.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stgobain.samuha.Model.ContextData;
import com.stgobain.samuha.R;

import java.util.List;

/**
 * Created by vignesh on 26-06-2017.
 */

public class PopulateListAdapter extends ArrayAdapter<ContextData> {

    private final List<ContextData> list;
    private final Activity context;

    static class ViewHolder {
        protected TextView name;
       // protected ImageView flag;
    }

    public PopulateListAdapter(Activity context, List<ContextData> list) {
        super(context, R.layout.contest_dialog, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.contest_dialog, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getName());
       // holder.flag.setImageDrawable(list.get(position).getFlag());
        return view;
    }
}
