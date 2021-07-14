package com.cbslprojects.crm.CRM.dialog;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbslprojects.crm.CRM.Adapters.UserDetailAdapter;
import com.cbslprojects.crm.CRM.Model.User;
import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.Constaints;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SaveUserDetailDialogFragment extends DialogFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dialog_fragment_save_user_detail, container, false);
        ImageView iv_dialog_close = v.findViewById(R.id.iv_dialog_close);
        TextView tv_data_no = v.findViewById(R.id.tv_data_no);

        iv_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ArrayList<User> users = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constaints.USER_CREDENTIAL_DATA, 0);

        String s = sharedPreferences.getString(Constaints.USER_CREDENTIAL_DATA, null);

        Gson gson = new Gson();

        List<User> list1 = gson.fromJson(s, new TypeToken<List<User>>() {
        }.getType());

        if (list1 != null && list1.size() != 0) {
            users.addAll(list1);
            tv_data_no.setVisibility(View.GONE);
        } else {
            tv_data_no.setVisibility(View.VISIBLE);
        }

        RecyclerView rcv_save_user_list = v.findViewById(R.id.rcv_save_user_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcv_save_user_list.setLayoutManager(linearLayoutManager);
        UserDetailAdapter userDetailAdapter = new UserDetailAdapter(getActivity(), users);
        rcv_save_user_list.setAdapter(userDetailAdapter);
        rcv_save_user_list.setHasFixedSize(true);
        //rcv_save_user_list.setNestedScrollingEnabled(true);
        rcv_save_user_list.addItemDecoration(new DividerItemDecoration((getActivity()), DividerItemDecoration.VERTICAL));


        return v;
    }
}
