package com.example.taskappkb.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskappkb.App;
import com.example.taskappkb.R;
import com.example.taskappkb.databinding.FragmentHomeBinding;
import com.example.taskappkb.interf.OnItemClickListener;
import com.example.taskappkb.model.TaskModel;
import com.example.taskappkb.ui.home.adapter.HomeAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    NavController navController;
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter= new HomeAdapter();
    private FragmentHomeBinding binding;
    private ArrayList<TaskModel> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter();
        setResultListener();
        //databasee();
        homeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putSerializable("task", list.get(position));
                navController =  Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_nav_home_to_formFragment, bundle);
            }
        });

    }


    public void setAdapter() {
        binding.recyclerView.setAdapter(homeAdapter);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

    }

    private void setResultListener() {
        getParentFragmentManager().setFragmentResultListener("task",
                getViewLifecycleOwner(),
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        TaskModel taskModel = (TaskModel) result.getSerializable("keyModel");
                        if (taskModel != null) {
                            homeAdapter.addInfo(taskModel);
                            homeAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    public void databasee(){
        App.getInstance().getDatabase().taskDao().getAllLive().
                observe(getViewLifecycleOwner(), new Observer<List<TaskModel>>() {
                    @Override
                    public void onChanged(List<TaskModel> tasks) {
                        homeAdapter.setList(tasks);
                    }
                });
    }


}