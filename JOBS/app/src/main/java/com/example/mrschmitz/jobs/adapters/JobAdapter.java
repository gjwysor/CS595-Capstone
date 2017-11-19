package com.example.mrschmitz.jobs.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mrschmitz.jobs.pojos.Job;

import java.util.ArrayList;

/**
 * Created by Noah on 11/8/2017.
 */

public class JobAdapter extends ArrayAdapter<Job> {

    private static class ViewHolder {
        TextView textView;
    }

    public JobAdapter(Context context, ArrayList<Job> jobs) {
        super(context, android.R.layout.simple_list_item_1, jobs);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();

        } else {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            convertView.setTag(viewHolder);

            viewHolder.textView = convertView.findViewById(android.R.id.text1);
        }

        Job job = getItem(position);
        viewHolder.textView.setText(job.getTitle());

        return convertView;
    }


}
