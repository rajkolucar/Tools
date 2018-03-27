package lucar.rajko.rmi.tools.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import lucar.rajko.rmi.tools.R;
import lucar.rajko.rmi.tools.utils.CopyToClipboardUtils;
import lucar.rajko.rmi.tools.utils.PasswordGeneratorUtils;

public class PassGenFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    FrameLayout generate;
    FrameLayout copyToClipboard;

    TextView generatedPassword;
    TextView passwordLength;

    Button plus;
    Button minus;

    int valueOfPassLength;
    String pass = "";
    PasswordGeneratorUtils passwordGeneratorUtils = null;
    CopyToClipboardUtils copyToClipboardUtils = null;
    public PassGenFragment() {

    }

    public static PassGenFragment newInstance(String param1, String param2) {
        PassGenFragment fragment = new PassGenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pass_gen, container, false);
        passwordGeneratorUtils = PasswordGeneratorUtils.getInstance();
        passwordLength = root.findViewById(R.id.pass_length);
        generate = root.findViewById(R.id.pass_gen);
        generatedPassword = root.findViewById(R.id.generated_pass);
        plus = root.findViewById(R.id.pass_length_plus);
        minus = root.findViewById(R.id.pass_length_minus);
        valueOfPassLength = Integer.parseInt(passwordLength.getText().toString());
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valueOfPassLength < 20) {
                    valueOfPassLength = Integer.parseInt(passwordLength.getText().toString());
                    valueOfPassLength++;
                    passwordLength.setText(Integer.toString(valueOfPassLength));
                } else {
                    Toast.makeText(getContext(), "Max length is 20", Toast.LENGTH_SHORT).show();
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valueOfPassLength <= 1) {
                    Toast.makeText(getContext(), "Value must be at least 1!", Toast.LENGTH_SHORT).show();
                } else {
                    valueOfPassLength = Integer.parseInt(passwordLength.getText().toString());
                    valueOfPassLength--;
                    passwordLength.setText(Integer.toString(valueOfPassLength));

                }
            }
        });


        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pl = Integer.parseInt(passwordLength.getText().toString());
//                if (pl <= 20) {
                    pass = passwordGeneratorUtils.generatePassword(pl);
                    generatedPassword.setText(pass);

//                } else {
//                    Toast.makeText(getContext(), "Max length is 20", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        copyToClipboard = root.findViewById(R.id.copy_to_clipboard);
        copyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyToClipboardUtils = CopyToClipboardUtils.getInstance();
                copyToClipboardUtils.setClipboard(getContext(), pass);
                Toast.makeText(getContext(), "Password is copied to clipboard.", Toast.LENGTH_SHORT).show();
            }
        });



        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
