package com.cbslprojects.crm.CRM.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cbslprojects.crm.CRM.Activitys.AboutActivity;
import com.cbslprojects.crm.CRM.Activitys.MainActivity;
import com.cbslprojects.crm.CRM.Interfaces.ApiInterface;
import com.cbslprojects.crm.CRM.Model.RagisterComplaint;
import com.cbslprojects.crm.CRM.Model.ReloveComplaint;
import com.cbslprojects.crm.CRM.Model.ScheduleMachine;
import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.ApiClient;
import com.cbslprojects.crm.CRM.Util.Constaints;
import com.cbslprojects.crm.CRM.Util.MyPrefences;
import com.cbslprojects.crm.CRM.Util.Utility;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private String uid;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv_total_ragister_complaint,tv_resolve_complaint,tv_file_upload, tv_schedule_machine;

    public HomeFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        tv_resolve_complaint = v.findViewById(R.id.tv_resolve_complaint);
        tv_schedule_machine = v.findViewById(R.id.tv_schedule_machine);
        tv_total_ragister_complaint=v.findViewById(R.id.tv_total_ragister_complaint);
        tv_file_upload=v.findViewById(R.id.tv_file_upload);

        CardView cv_ragister_complaint = v.findViewById(R.id.cv_ragister_complaint);
        CardView cv_resolve_complaint = v.findViewById(R.id.cv_resolve_complaint);
        CardView cv_schedule_machine = v.findViewById(R.id.cv_schedule_machine);
        CardView cv_about = v.findViewById(R.id.cv_about);
        CardView cv_file_upload = v.findViewById(R.id.cv_file_upload);
        CardView cv_logout = v.findViewById(R.id.cv_logout);

        uid = MyPrefences.getInstance(getActivity()).getString(MyPrefences.PREFRENCE_USER_ID, null);


        getComplaintTotal();

        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                getComplaintTotal();
            }
        });

        cv_ragister_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putBoolean(Constaints.WHOS_FRAGMENT_LOAD, true);
              //  b.putInt(Constaints.WHOS_FRAGMENT_LOAD, 1);
                RegisterComplaintFragment registerComplaintFragment = new RegisterComplaintFragment();
                registerComplaintFragment.setArguments(b);
                callFragment(registerComplaintFragment);
                ((MainActivity) getActivity()).showSearchView(R.id.nav_register_complaint,registerComplaintFragment);
            }
        });
        cv_file_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putBoolean(Constaints.WHOS_FRAGMENT_LOAD, false);
                // b.putInt(Constaints.WHOS_FRAGMENT_LOAD, 1);
                RegisterComplaintFragment registerComplaintFragment = new RegisterComplaintFragment();
                registerComplaintFragment.setArguments(b);
                callFragment(registerComplaintFragment);

                ((MainActivity) getActivity()).showSearchView(R.id.nav_file_upload,registerComplaintFragment);
            }
        });

        cv_resolve_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResolveComplaintFragment resolveComplaintFragment=   new ResolveComplaintFragment();
                callFragment(resolveComplaintFragment);
                ((MainActivity) getActivity()).showSearchView(R.id.nav_resolve_complaint,resolveComplaintFragment);
            }
        });

        cv_schedule_machine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleMachineFragment scheduleMachineFragment=new ScheduleMachineFragment();
                callFragment(scheduleMachineFragment);
                ((MainActivity) getActivity()).showSearchView(R.id.nav_schedule_machine_list,scheduleMachineFragment);
            }
        });

        cv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getContext(), AboutActivity.class));
            }
        });


        cv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity()).logout();
            }
        });


        return v;
    }

    private void getComplaintTotal() {
        if (Utility.isNetworkAvailable(getActivity())) {
             RagisterComplaintOnServer(uid);
            ReloveComplaintOnServer(uid);
            getScheduleMachineListFromServer(uid);
        } else {
            Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            // swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void callFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_contain, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



    private void RagisterComplaintOnServer(String user_id) {
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<RagisterComplaint> listCall = apiInterface.RagisterComplaintList(user_id, "");

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

                    if (ragisterComplaint.getErrorMessage().equals("Sucess") && ragisterComplaint.getErrorCode().equals("000")
                            && ragisterComplaint.getBranchDetails() != null) {

                        tv_total_ragister_complaint.setText("Total : " + ragisterComplaint.getBranchDetails().size());
                        tv_file_upload.setText("Total : " + ragisterComplaint.getBranchDetails().size());

                    } else {
                        tv_total_ragister_complaint.setText("Total : 0");
                        tv_file_upload.setText("Total : 0");
                    }

                    swipeRefreshLayout.setRefreshing(false);

                    progressDialog.dismiss();

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
                Toast.makeText(getActivity(), "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ReloveComplaintOnServer(String user_id) {
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<ReloveComplaint> listCall = apiInterface.ResolveComplaintList(user_id, "");

        listCall.enqueue(new Callback<ReloveComplaint>() {
            @Override
            public void onResponse(@NonNull Call<ReloveComplaint> call, @NonNull retrofit2.Response<ReloveComplaint> response) {
                try {
                    ReloveComplaint reloveComplaint = response.body();
                    if (reloveComplaint == null) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Error : " + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (reloveComplaint.getErrorMessage().equals("Success") && reloveComplaint.getErrorCode().equals("000") && reloveComplaint.getList() != null) {

                        tv_resolve_complaint.setText("Total : " + reloveComplaint.getList().size());
                    } else {
                        tv_resolve_complaint.setText("Total : 0");
                    }

                    swipeRefreshLayout.setRefreshing(false);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReloveComplaint> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getScheduleMachineListFromServer(String user_id) {
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<ScheduleMachine> listCall = apiInterface.ScheduleMachineList(user_id, "");

        listCall.enqueue(new Callback<ScheduleMachine>() {
            @Override
            public void onResponse(@NonNull Call<ScheduleMachine> call, @NonNull retrofit2.Response<ScheduleMachine> response) {
                try {
                    ScheduleMachine scheduleMachine = response.body();

                    if (scheduleMachine == null) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Error =" + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (scheduleMachine.getErrorMessage().equals("Success") && scheduleMachine.getErrorCode().equals("000") && scheduleMachine.getScheduleMachineList() != null) {
                        tv_schedule_machine.setText("Total : " + scheduleMachine.getScheduleMachineList().size());
                    } else {
                        tv_schedule_machine.setText("Total : 0");
                    }

                    swipeRefreshLayout.setRefreshing(false);
                    progressDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ScheduleMachine> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
