package com.example.edt;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.edt.databinding.FragmentSecondBinding;
import com.example.edt.modele.Cours;
import com.example.edt.modele.Jour;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            Jour jour = (Jour) getArguments().getSerializable("jour");

            if (jour != null) {
                String str = "";
                SimpleDateFormat sdfBase = new SimpleDateFormat("yyyy-MM-dd"); // LOL 2
                SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy"); // LOL 3
                try
                {
                    Date dateTemp = sdfBase.parse(jour.getStartDate());
                    String dateOut = sdfOut.format(dateTemp);
                    binding.textViewTitle.setText(dateOut);
                }
                catch (ParseException e)
                {
                    throw new RuntimeException(e);
                }

                for (Cours cours : jour.getCoursList()) {
                    str += cours.toString() + "\n";
                }
                binding.textviewSecond.setText(str + "------------------------------------------------------------------\n");

            } else {
                binding.textviewSecond.setText("KO - Jour est null");
            }
        } else {
            binding.textviewSecond.setText("KO - getArguments est null");
        }

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}