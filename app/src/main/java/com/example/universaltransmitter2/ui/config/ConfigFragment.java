package com.example.universaltransmitter2.ui.config;

import android.content.Intent;
import android.widget.Button;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.universaltransmitter2.*;

public class ConfigFragment extends Fragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_conf, container, false);
        Button button_red = v.findViewById(R.id.button_red);
        button_red.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Config.class);
            startActivity(intent);
        });
        Button button_imp = v.findViewById(R.id.button_imp);
        button_imp.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ImportConfig.class);
            startActivity(intent);
        });
        Button button_exp = v.findViewById(R.id.button_eks);
        button_exp.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ExportConfig.class);
            startActivity(intent);
        });
        Button button_his = v.findViewById(R.id.button_his);
        button_his.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HistoryConfig.class);
            startActivity(intent);
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}