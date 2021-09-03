package com.example.taskappkb;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskappkb.databinding.FragmentFormBinding;
import com.example.taskappkb.databinding.FragmentHomeBinding;
import com.example.taskappkb.local.AppDatabase;
import com.example.taskappkb.model.TaskModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FormFragment extends Fragment {

    private FragmentFormBinding binding;
    private NavController navController;
    TaskModel taskModel;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private int i = 0;
    private String requestKey = "form";
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFormBinding.inflate(inflater, container, false);
        userPermissions();
        listenerText();
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
            // requestKey = "formRed";
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
        if (taskModel == null) {
            taskModel = new TaskModel(title, description, System.currentTimeMillis());
            App.getInstance(requireContext()).taskDao().insert(taskModel);
        } else {
            taskModel.setTitle(title);
            taskModel.setDescription(description);
            App.getInstance(requireContext()).taskDao().update(taskModel);
        }
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("keyModel", taskModel);
//        getParentFragmentManager().setFragmentResult("task", bundle);
        close();
    }

    private void close() {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigateUp();
    }

    private void listenerText() {
        binding.ivMic.setImageResource(R.drawable.ic_mic_off);
        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireActivity());
        final Intent speehcReconizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        binding.ivMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    speechRecognizer.startListening(speehcReconizerIntent);
                    i = 1;
                    binding.ivMic.setImageResource(R.drawable.ic_mic);
                } else {
                    speechRecognizer.stopListening();
                    i = 0;
                    binding.ivMic.setImageResource(R.drawable.ic_mic_off);
                }
//                Intent intent
//                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
//                        Locale.getDefault());
//                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
//
//                try {
//                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
//                }
//                catch (Exception e) {
//                    Toast
//                            .makeText(requireActivity(), " " + e.getMessage(),
//                                    Toast.LENGTH_SHORT)
//                            .show();
//                }
            }
        });

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {
                binding.etTitle.setHint("Listener");
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                speechRecognizer.stopListening();
            }

            @Override
            public void onError(int error) {
//                binding.ivMic.setImageResource(R.drawable.ic_mic_off);
//                binding.etTitle.setHint("enter title");

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                binding.etTitle.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

    }

    private void userPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO}, 2);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions,
//                                           @NonNull @NotNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 2) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(requireActivity(),
//                        "permission_granted",
//                        Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(requireActivity(),
//                    "permission_dein",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }


}