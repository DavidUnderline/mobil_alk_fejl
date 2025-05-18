package com.example.allaskereso_portal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

//recycleview --> dynamic lists

public class JobItemAdapter extends RecyclerView.Adapter<JobItemAdapter.JobViewHolder> {
    private List<Job> jobList;
    private Context context;

    public JobItemAdapter(Context context, List<Job> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_jobs, parent, false);
        return new JobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.bind(job);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView jobcategoryTextView;
        public TextView descriptionTextView;
        public TextView salaryTextView;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);;
            titleTextView = itemView.findViewById(R.id.job_title);
            jobcategoryTextView = itemView.findViewById(R.id.job_category);
            descriptionTextView = itemView.findViewById(R.id.job_description);
            salaryTextView = itemView.findViewById(R.id.job_salary);
        }

        public void bind(Job job) {
            titleTextView.setText(job.getTitle());
            descriptionTextView.setText(job.getDescription());
            salaryTextView.setText(String.valueOf(job.getSalary()));
            jobcategoryTextView.setText(job.getCategory());
        }
    }

}
