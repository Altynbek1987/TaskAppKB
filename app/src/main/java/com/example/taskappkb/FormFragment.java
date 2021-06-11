package com.example.taskappkb;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.taskappkb.databinding.FragmentFormBinding;
import com.example.taskappkb.databinding.FragmentHomeBinding;
import com.example.taskappkb.local.AppDatabase;
import com.example.taskappkb.model.TaskModel;

import org.jetbrains.annotations.NotNull;

public class FormFragment extends Fragment {

    private FragmentFormBinding binding;
    private NavController navController;
    TaskModel taskModel;
    private String requestKey = "form";
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            taskModel = (TaskModel) getArguments().getSerializable("task");
            if (taskModel != null) {
                binding.etTitle.setText(taskModel.getTitle());
                binding.etDescription.setText(taskModel.getDescription());
            }
        }
        if (getArguments() != null) {
            TaskModel taskModel = (TaskModel) getArguments().getSerializable("task");
            position = getArguments().getInt("position");
            assert taskModel != null;
            binding.etTitle.setText(taskModel.getTitle());
            binding.etDescription.setText(taskModel.getDescription());
            requestKey = "formRed";
        }
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });


    }

    private void save() {
        String title = binding.etTitle.getText().toString();
        String description = binding.etDescription.getText().toString();
        TaskModel taskModel = new TaskModel(title, description, System.currentTimeMillis());
        Bundle bundle = new Bundle();
        bundle.putSerializable("keyModel", taskModel);
        getParentFragmentManager().setFragmentResult("task", bundle);
        close();
    }

    private void close() {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigateUp();
    }

}