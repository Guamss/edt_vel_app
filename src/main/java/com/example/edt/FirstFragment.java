package com.example.edt;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.edt.databinding.FragmentFirstBinding;
import com.example.edt.modele.Jour;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FirstFragment extends Fragment
{

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String dateFormat = "dd/MM/yyyy";
                DateFormat sdf = new SimpleDateFormat(dateFormat); // LOL
                sdf.setLenient(false);

                String federationIdText = binding.federationIdForm.getText().toString();
                String dateText = binding.dateForm.getText().toString();

                if (TextUtils.isEmpty(federationIdText) || TextUtils.isEmpty(dateText))
                {
                    binding.textViewError.setText("Veuillez remplir tous les champs !");
                }
                else
                {
                    try
                    {
                        sdf.parse(dateText);

                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    SimpleDateFormat sdfBase = new SimpleDateFormat("yyyy-MM-dd");
                                    Date dateTemp = sdf.parse(dateText);
                                    String dateOut = sdfBase.format(dateTemp);

                                    Jour jour = new Jour(federationIdText, dateOut, dateText);

                                    getActivity().runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run() {
                                            if (!jour.getCoursList().isEmpty())
                                            {
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("jour", jour); // Passer l'objet Jour

                                                NavHostFragment.findNavController(FirstFragment.this)
                                                        .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
                                            }
                                            else
                                            {
                                                binding.textViewError.setText("Le groupe saisi n'est pas correct ou il n'y a pas cours... (yipee !)");
                                            }
                                        }
                                    });
                                }
                                catch (Exception e)
                                {
                                    getActivity().runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            binding.textViewError.setText("Une erreur est survenue : " + e);
                                        }
                                    });
                                }
                            }
                        }).start();

                    }
                    catch (ParseException e)
                    {
                        binding.textViewError.setText("Le format de la date est incorrect !");
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
