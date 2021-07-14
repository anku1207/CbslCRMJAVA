package com.cbslprojects.crm.CRM.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cbslprojects.crm.CRM.Activitys.MainActivity;
import com.cbslprojects.crm.CRM.Adapters.RegisterComplaintAdapter;
import com.cbslprojects.crm.CRM.Interfaces.ApiInterface;
import com.cbslprojects.crm.CRM.Interfaces.SearchSoldIdListener;
import com.cbslprojects.crm.CRM.Interfaces.TotalComplaintListener;
import com.cbslprojects.crm.CRM.Model.BranchDetails;
import com.cbslprojects.crm.CRM.Model.RagisterComplaint;
import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.ApiClient;
import com.cbslprojects.crm.CRM.Util.Constaints;
import com.cbslprojects.crm.CRM.Util.MyPrefences;
import com.cbslprojects.crm.CRM.Util.Utility;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterComplaintFragment extends Fragment implements SearchSoldIdListener, TotalComplaintListener {

    private ArrayList<BranchDetails> arrayList;
    private RegisterComplaintAdapter registerComplaintAdapter;
    private ProgressDialog progressDialog;
    private String uid;
    private TextView tv_no_data;
    private MainActivity mainActivity;
    private SwipeRefreshLayout swipeRefreshLayout;

    public RegisterComplaintFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register_complaint, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        arrayList = new ArrayList<>();

        uid = MyPrefences.getInstance(getActivity()).getString(MyPrefences.PREFRENCE_USER_ID, null);

        tv_no_data = v.findViewById(R.id.tv_no_data);
        RecyclerView rcv_register_complaint_list = v.findViewById(R.id.rcv_register_complaint_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcv_register_complaint_list.setLayoutManager(linearLayoutManager);

        Bundle bd = getArguments();
        boolean status = true;
        int fragmentNo = 0;
        if (bd != null) {
            status = bd.getBoolean(Constaints.WHOS_FRAGMENT_LOAD,false);
          //  fragmentNo = bd.getInt(Constaints.WHOS_FRAGMENT_LOAD,0);
        }

        registerComplaintAdapter = new RegisterComplaintAdapter(getActivity(), this, arrayList, status,fragmentNo);
        rcv_register_complaint_list.setAdapter(registerComplaintAdapter);
        rcv_register_complaint_list.setHasFixedSize(true);
        rcv_register_complaint_list.setNestedScrollingEnabled(true);
        rcv_register_complaint_list.addItemDecoration(new DividerItemDecoration((getContext()), DividerItemDecoration.VERTICAL));

        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                callApi("");
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        //get branch details list....................................................................
        //  if (arrayList.size()==0)
        callApi("");
        mainActivity.refreshList();
    }

    private void RagisterComplaintOnServer(String user_id, String sold_id) {
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<RagisterComplaint> listCall = apiInterface.RagisterComplaintList(user_id, sold_id);

        listCall.enqueue(new Callback<RagisterComplaint>() {
            @Override
            public void onResponse(@NonNull Call<RagisterComplaint> call, @NonNull retrofit2.Response<RagisterComplaint> response) {
                try {
                    RagisterComplaint ragisterComplaint = response.body();

                    if (ragisterComplaint == null) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Error :" + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    arrayList.clear();
                    if (ragisterComplaint.getErrorMessage().equals("Sucess") && ragisterComplaint.getErrorCode().equals("000") && ragisterComplaint.getBranchDetails() != null) {

                        arrayList.addAll(ragisterComplaint.getBranchDetails());

                        if (ragisterComplaint.getBranchDetails().size() == 0) {
                            tv_no_data.setText("No data available");
                        } else {
                            tv_no_data.setText("Total : " + arrayList.size());
                        }

                    } else {
                        tv_no_data.setText("No data available");
                    }

                    swipeRefreshLayout.setRefreshing(false);

                    progressDialog.dismiss();
                    registerComplaintAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RagisterComplaint> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(mainActivity, "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void SearchSoldId(String soldId) {
        Log.i("query", "soldId " + soldId);
        registerComplaintAdapter.getFilter().filter(soldId);
    }

    @Override
    public void SearchSoldIdOnline(String soldId) {

        if (soldId.isEmpty()) {
            if (arrayList.size() == 0)
                callApi(soldId);
        } else {
            callApi(soldId);
        }

    }

    private void callApi(String soldId) {
        if (Utility.isNetworkAvailable(getActivity())) {
            RagisterComplaintOnServer(uid, soldId);
        } else {
            Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void showTotalComplaintNumber(int size) {
        tv_no_data.setText("Total : " + size);
    }
}
