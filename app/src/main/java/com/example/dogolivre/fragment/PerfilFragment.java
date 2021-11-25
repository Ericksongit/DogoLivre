package com.example.dogolivre.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.dogolivre.R;
import com.example.dogolivre.activity.EditarPerfilActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class PerfilFragment extends Fragment {


    private ProgressBar progressBar;
    private CircleImageView imagePerfil;
    public GridView gridViewPerfil;
    private TextView textPublicações, textAdotados, textDoados;
    private Button buttomEditarPerfil;

    public PerfilFragment() {
        // Required empty public constructor
    }








    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //config componentes
        gridViewPerfil = view.findViewById(R.id.gridViewPerfil);
        progressBar = view.findViewById(R.id.progressBarPerfil);
        imagePerfil = view.findViewById(R.id.imagePerfil);
        textPublicações = view.findViewById(R.id.textPublicacoes);
        textAdotados = view.findViewById(R.id.textAdotados);
        textDoados = view.findViewById(R.id.textDoados);
        buttomEditarPerfil = view.findViewById(R.id.buttonAcaoPerfil);

        buttomEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(i);
            }
        });



        return view;
    }
}