package com.example.mrschmitz.jobs.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.database.Users;
import com.example.mrschmitz.jobs.misc.Utils;
import com.example.mrschmitz.jobs.pojos.Job;

import java.text.NumberFormat;
import java.util.ArrayList;

import agency.tango.android.avatarview.views.AvatarView;

/**
 * Created by Noah on 11/8/2017.
 */

public class JobAdapter extends ArrayAdapter<Job> {

    private static class ViewHolder {
        AvatarView avatarView;
        TextView titleTextView;
        TextView amountTextView;
    }

    public JobAdapter(Context context, ArrayList<Job> jobs) {
        super(context, R.layout.list_item_job, jobs);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();

        } else {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_job, parent, false);
            convertView.setTag(viewHolder);

            viewHolder.avatarView = convertView.findViewById(R.id.avatar);
            viewHolder.titleTextView = convertView.findViewById(R.id.title);
            viewHolder.amountTextView = convertView.findViewById(R.id.amount);
        }

        final Job job = getItem(position);
        Users.loadUser(job.getPosterUid(), user -> {
            Utils.loadProfileImage(getContext(), user, viewHolder.avatarView);
        });
        viewHolder.titleTextView.setText(job.getTitle());
        String paymentAmount = NumberFormat.getCurrencyInstance().format(job.getPaymentAmount());
        viewHolder.amountTextView.setText(paymentAmount);

        return convertView;
    }


}
