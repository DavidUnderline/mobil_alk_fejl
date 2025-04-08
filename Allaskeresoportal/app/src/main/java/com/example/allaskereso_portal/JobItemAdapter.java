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

//recycleview --> dynamic lists

public class JobItemAdapter extends RecyclerView.Adapter<JobItemAdapter.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Job> job;
    private ArrayList<Job> alljob;
    private int lastpos = -1;

    JobItemAdapter(Context context, ArrayList<Job> jobsdata){
        this.context = context;
        this.job = jobsdata;
        this.alljob = jobsdata;
    }

//bind layout to adapter
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_jobs, parent, false);

        return new ViewHolder(view);
//        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_jobs, parent, false));
    }

    @Override
    public void onBindViewHolder(JobItemAdapter.ViewHolder holder, int position) {
        Job current = job.get(position);
        holder.bind(current);
    }

    @Override
    public int getItemCount() {
        return job.size();
    }

    @Override
    public Filter getFilter() {
        return jobfilter;
    }

    Filter jobfilter = new Filter() {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        ArrayList<Job> list = new ArrayList<>();
        FilterResults res = new FilterResults();

        if(constraint.length() != 0 || constraint != null){
            String category = constraint.toString().toLowerCase().trim();
            for(Job i : alljob){
                if(i.getCategory().toLowerCase().contains(category)){
                    list.add(i);
                }
            }
        }

        res.count = list.size();
        res.values = list;
        return res;
    }

//        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults res) {
            job = (ArrayList<Job>) res.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView category;
        private TextView salary;
        private TextView description;


        public ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.job_title);
            category = view.findViewById(R.id.job_category);
            salary = view.findViewById(R.id.job_salary);
            description = view.findViewById(R.id.job_description);
        }

        public void bind(Job job) {
            title.setText(job.getTitle());
            category.setText(job.getCategory());
            salary.setText(job.getSalary());
            description.setText(job.getDescription());
        }
    }

}
