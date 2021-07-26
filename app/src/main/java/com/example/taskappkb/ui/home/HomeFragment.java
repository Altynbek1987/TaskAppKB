package com.example.taskappkb.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    private HomeAdapter homeAdapter;
    private FragmentHomeBinding binding;
    private List<TaskModel> list = new ArrayList<>();



    @Override
    public void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeAdapter = new HomeAdapter();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Log.e("TAG", "onCreateView: " + App.getInstance(requireContext()).taskDao().getAllLive() );
        setAdapter();
        databasee();
        pushData();
        setResultListener();
        return root;
    }
    public void setAdapter() {
        binding.recyclerVieww.setAdapter(homeAdapter);
        //binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerVieww.setLayoutManager(new LinearLayoutManager(getContext()));
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull  RecyclerView recyclerView, @NonNull  RecyclerView.ViewHolder viewHolder, @NonNull  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull  RecyclerView.ViewHolder viewHolder, int direction) {
                App.getInstance(requireContext()).taskDao().delete(list.get(viewHolder.getAdapterPosition()));
               // list.remove(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(binding.recyclerVieww);

    }
    public void databasee() {
        App.getInstance(requireContext()).taskDao().getAllLive().
                observe(getViewLifecycleOwner(), new Observer<List<TaskModel>>() {
                    @Override
                    public void onChanged(List<TaskModel> tasks) {
                        list = tasks;
                        homeAdapter.setList(tasks);
                    }
                });
//        if (App.getInstance(requireContext()).taskDao().getAllLive() != null){
//            homeAdapter.setList(App.getInstance(requireContext()).taskDao().getAllLive());
//        }

    }

    public void pushData() {
        homeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, TaskModel taskModel) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putSerializable("task", taskModel);
                navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_nav_home_to_formFragment, bundle);
            }

            @Override
            public void onLongItemClick(final int position , TaskModel taskModel) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Вы хотите удалить?" + taskModel.getTitle() + taskModel.getDescription())
                            .setNegativeButton("Нет", null)
                            .setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    App.getInstance(requireContext()).taskDao().delete(taskModel);
                                }
                            });
                    builder.show();

            }

        });
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
}