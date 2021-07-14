package com.cbslprojects.crm.RMMS.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.Utility;


/**
 * A simple {@link Fragment} subclass.
 */
public class NetworkCheckFragment extends Fragment {


    public NetworkCheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.error_layout, container, false);
        Button btn_try_again=v.findViewById(R.id.btn_try_again);

        btn_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0 ){
                    if (Utility.isNetworkAvailable(getContext())) {
                    getFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getContext(),
                                "Please Check Internet Connection.Try Again!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return v;
    }

}
