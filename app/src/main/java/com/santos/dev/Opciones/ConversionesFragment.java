package com.santos.dev.Opciones;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.santos.dev.Operaciones.Fraccion;
import com.santos.dev.R;

public class ConversionesFragment extends Fragment {

    private TextView mTextViewRespuesta;
    private EditText mEditTextInput;
    private Button mButtonOperar;
    int posicion1;
    int posicion2;

    String[] lenguajes = {"Grados", "Radianes", "Rev", "Gon"};

    public ConversionesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversiones, container, false);
        final Fraccion fraccion = new Fraccion();
        mTextViewRespuesta = view.findViewById(R.id.tv_resultado);
        mButtonOperar = view.findViewById(R.id.btn_operar);
        mEditTextInput = view.findViewById(R.id.et_ingreso);

        final Spinner Slenguajes = view.findViewById(R.id.spinner1);
        Spinner Slenguajes1 = view.findViewById(R.id.spinner2);

        Slenguajes.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, lenguajes));
        Slenguajes1.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, lenguajes));


        Slenguajes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posicion1 = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                //subject_input.setText("");
            }
        });

        Slenguajes1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posicion2 = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        mButtonOperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posicion1 == 0 && posicion2 == 0) { //TODO COMIENZA GRADOS
                    mTextViewRespuesta.setText(mEditTextInput.getText().toString());
                } else if (posicion1 == 0 && posicion2 == 1) {
                    mTextViewRespuesta.setText("Grados a Radianes");
                    if (mEditTextInput.getText().toString().equals("360")) {
                        mTextViewRespuesta.setText("2 π radianes");
                    } else if (mEditTextInput.getText().toString().equals("180")) {
                        mTextViewRespuesta.setText("π radianes");
                    } else
                        mTextViewRespuesta.setText(String.valueOf(fraccion.Grados_a_Radianes(Integer.parseInt(mEditTextInput.getText().toString())).toString(0)));
                } else if (posicion1 == 0 && posicion2 == 2) {
                    mTextViewRespuesta.setText("Grados a Rev");
                    if (mEditTextInput.getText().toString().equals("360")) {
                        mTextViewRespuesta.setText("1 rev");
                    } else if (mEditTextInput.getText().toString().equals("180")) {
                        mTextViewRespuesta.setText("1/2 rev.");
                    } else
                        mTextViewRespuesta.setText(String.valueOf(fraccion.Grados_a_revoluciones(Integer.parseInt(mEditTextInput.getText().toString())).toString(1)));
                } else if (posicion1 == 0 && posicion2 == 3) {
                    mTextViewRespuesta.setText("Grados a Gon");
                    if (mEditTextInput.getText().toString().equals("360")) {
                        mTextViewRespuesta.setText("400 gons");
                    } else if (mEditTextInput.getText().toString().equals("180")) {
                        mTextViewRespuesta.setText("200 gons.");
                    } else
                        mTextViewRespuesta.setText(String.valueOf(fraccion.Grados_a_gon(Integer.parseInt(mEditTextInput.getText().toString())).toString(2)));
                } else if (posicion1 == 1 && posicion2 == 0) {//TODO COMIENZA RADIANES
                    mTextViewRespuesta.setText("Radianes a Grados");
                    String currentString = mEditTextInput.getText().toString();
                    String[] separated = currentString.split("/");
                    if (mEditTextInput.getText().toString().equals("2") && mEditTextInput.getText().toString().equals("2/1")) {
                        mTextViewRespuesta.setText("360");
                    } else if (mEditTextInput.getText().toString().equals("1") && mEditTextInput.getText().toString().equals("1/1")) {
                        mTextViewRespuesta.setText("180.");
                    } else {
                        if (separated.length >= 2) {
                            mTextViewRespuesta.setText(String.valueOf(fraccion.radianes_a_Grados(Integer.parseInt(separated[0]), Integer.parseInt(separated[1])).toString(3)));
                        } else {
                            mTextViewRespuesta.setText(String.valueOf(fraccion.radianes_a_Grados(Integer.parseInt(separated[0]), 1).toString(3)));
                        }
                    }
                } else if (posicion1 == 1 && posicion2 == 1) {
                    mTextViewRespuesta.setText(mEditTextInput.getText().toString());
                } else if (posicion1 == 1 && posicion2 == 2) {
                    mTextViewRespuesta.setText("Radianes a Rev");
                    if (mEditTextInput.getText().toString().equals("2") || mEditTextInput.getText().toString().equals("2/1")) {
                        mTextViewRespuesta.setText("1 rev");
                    } else if (mEditTextInput.getText().toString().equals("1") || mEditTextInput.getText().toString().equals("1/1")) {
                        mTextViewRespuesta.setText("1/2 rev.");
                    } else {
                        //Codigo de la nueva operacion
                    }
                } else if (posicion1 == 1 && posicion2 == 3) {
                    mTextViewRespuesta.setText("Radianes a Gon");
                    if (mEditTextInput.getText().toString().equals("2") || mEditTextInput.getText().toString().equals("2/1")) {
                        mTextViewRespuesta.setText("400 gons.");
                    } else if (mEditTextInput.getText().toString().equals("1") || mEditTextInput.getText().toString().equals("1/1")) {
                        mTextViewRespuesta.setText("200 gons.");
                    } else {
                        //Codigo de la nueva operacion
                    }
                } else if (posicion1 == 2 && posicion2 == 0) {//TODO COMIENZA REV
                    mTextViewRespuesta.setText("Rev a Grados");
                } else if (posicion1 == 2 && posicion2 == 1) {
                    mTextViewRespuesta.setText("Rev a Radianes");
                } else if (posicion1 == 2 && posicion2 == 2) {
                    mTextViewRespuesta.setText(mEditTextInput.getText().toString());
                } else if (posicion1 == 2 && posicion2 == 3) {//TODO COMIENZA GON
                    mTextViewRespuesta.setText("Radianes a Gon");
                } else if (posicion1 == 3 && posicion2 == 0) {
                    mTextViewRespuesta.setText("Gon a Grados");
                } else if (posicion1 == 3 && posicion2 == 1) {
                    mTextViewRespuesta.setText("Gon a Radianes");
                } else if (posicion1 == 3 && posicion2 == 2) {
                    mTextViewRespuesta.setText("Gon a Rev");
                } else if (posicion1 == 3 && posicion2 == 3) {
                    mTextViewRespuesta.setText(mEditTextInput.getText().toString());
                }
            }
        });
        return view;
    }


}
