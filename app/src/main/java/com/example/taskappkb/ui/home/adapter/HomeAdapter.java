package com.example.taskappkb.ui.home.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskappkb.App;
import com.example.taskappkb.R;
import com.example.taskappkb.interf.OnItemClickListener;
import com.example.taskappkb.model.TaskModel;
import com.example.taskappkb.ui.home.HomeFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<TaskModel> list = new ArrayList<>();
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public HomeAdapter.HomeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_holder,parent,false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeAdapter.HomeViewHolder holder, int position) {
        holder.bind(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void addInfo(TaskModel taskModel){
        list.add(taskModel);
        notifyDataSetChanged();
    }
    public void setList(List<TaskModel> listM) {
        list.clear();
        this.list.addAll(listM);
        notifyDataSetChanged();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtDescription;
        public HomeViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtDescription = itemView.findViewById(R.id.txt_description);
        }

        public void bind(TaskModel taskModel) {
            txtTitle.setText(taskModel.getTitle());
            txtDescription.setText(taskModel.getDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(),taskModel);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongItemClick(getAdapterPosition(),taskModel);
                    return false;
                }
            });

        }
    }
}
