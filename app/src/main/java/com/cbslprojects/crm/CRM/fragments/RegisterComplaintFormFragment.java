package com.cbslprojects.crm.CRM.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import android.widget.Toast;

import com.cbslprojects.crm.CRM.Interfaces.ApiInterface;
import com.cbslprojects.crm.CRM.Model.CommenResponse;

import com.cbslprojects.crm.CRM.Model.MachineInformation;
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
public class RegisterComplaintFormFragment extends Fragment {

    private Spinner spinner_comp_type;
    private Spinner spinner_comp_item;
    private ArrayAdapter<String> adapter_item;
    private ArrayList<String> arrayList = new ArrayList<>();

    private TextInputEditText edittext_city;
    private TextInputEditText edittext_state;
    private TextInputEditText branch_Name_textview;
    private TextInputEditText edittext_bankname;
    private TextInputEditText project_name_textview;
    private TextInputEditText sole_id_testview;
    private TextInputEditText location_edittext;
    private TextInputEditText msg_test_view;
    private TextInputEditText text_view_ref_no;
    private TextInputEditText machine_no_textview;
    private TextInputEditText comp_detils_edittext;
    private TextInputLayout comp_detail;
    private String[] data = {"Choose a Complaint Type", "Hardware", "Software", "Service", "Payment FollowUp","PMR", "Other"};
    private LinearLayout comp_item_layout;

    private ProgressDialog progressDialog;
    private String uid;
    private Dialog dialog;

    private String cityName;
    private String stateName;
    private String branchName;
    private String bankName;
    private String bankId;
    private String stateId;
    private String cityid;
    private String Branchid;
    private String projectID;
    private String projectname;
    private String soleid;
    private String location;
    private String machineid;
    private String Itemid;
    private String ItemName;
    private String machineNo;
    private String complaintType;
    private String Details;

    public RegisterComplaintFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register_complaint_form, container, false);

        uid = MyPrefences.getInstance(getActivity()).getString(MyPrefences.PREFRENCE_USER_ID, null);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        edittext_bankname = v.findViewById(R.id.bankname_edittext);
        edittext_city = v.findViewById(R.id.city_edittext);
        edittext_state = v.findViewById(R.id.state_edittext);
        branch_Name_textview = v.findViewById(R.id.branch_name_textview);
        location_edittext = v.findViewById(R.id.location_edittext);
        project_name_textview = v.findViewById(R.id.project_name_edittext);
        sole_id_testview = v.findViewById(R.id.solid_id_edittext);
        machine_no_textview = v.findViewById(R.id.machine_no_edittext1);

        spinner_comp_type = v.findViewById(R.id.complaint_type_spinner);
        spinner_comp_item = v.findViewById(R.id.complaint_item_spinner);

        comp_item_layout = v.findViewById(R.id.complaint_item_layout);
        comp_detail = v.findViewById(R.id.complaint_details_layout);
        LinearLayout comp_type = v.findViewById(R.id.complaint_type_layout);
        Button submitbtn = v.findViewById(R.id.submit_btn_register);
        comp_detils_edittext = v.findViewById(R.id.comp_detils);

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spinner_comp_type.getSelectedItem().toString().equals("Hardware")) {
                    if (spinner_comp_type.getSelectedItem().toString().equalsIgnoreCase("Choose a Complaint Type")) {
                        Toast.makeText(getActivity(), "Choose a Complaint Type", Toast.LENGTH_SHORT).show();
                    } else {
                        if (spinner_comp_item.getSelectedItem().toString().equalsIgnoreCase("Choose Your Item")) {
                            Toast.makeText(getActivity(), "Choose Your Item", Toast.LENGTH_SHORT).show();
                        } else {

                            if (comp_detils_edittext.getText().toString().trim().equals("")) {

                                Toast.makeText(getActivity(), "Fill Complaint Details", Toast.LENGTH_SHORT).show();
                            } else {

                                showDialog();

                                if (Utility.isNetworkAvailable(getActivity())) {
                                    ragisterComplaintOnServer(Itemid);
                                } else {
                                    Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }
                    }
                } else {
                    if (spinner_comp_type.getSelectedItem().toString().equalsIgnoreCase("Choose a Complaint Type")) {
                        Toast.makeText(getActivity(), "Choose a Complaint Type", Toast.LENGTH_SHORT).show();
                    } else {
                        if (comp_detils_edittext.getText().toString().trim().equals("")) {

                            Toast.makeText(getActivity(), "Fill Complaint Details", Toast.LENGTH_SHORT).show();
                        } else {
                            showDialog();

                            if (Utility.isNetworkAvailable(getActivity())) {
                                ragisterComplaintOnServer("0");
                            } else {
                                Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });


        ArrayAdapter<String> adapter_comp_type = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, data);
        spinner_comp_type.setAdapter(adapter_comp_type);
        spinner_comp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    comp_item_layout.setVisibility(View.GONE);
                    comp_detail.setVisibility(View.GONE);
                }
                if (i == 1) {
                    complaintType = String.valueOf(spinner_comp_type.getItemAtPosition(i));
                    comp_item_layout.setVisibility(View.VISIBLE);
                    comp_detail.setVisibility(View.VISIBLE);
                }
                if (i == 2) {
                    complaintType = String.valueOf(spinner_comp_type.getItemAtPosition(i));
                    comp_item_layout.setVisibility(View.GONE);
                    comp_detail.setVisibility(View.VISIBLE);
                }
                if (i == 3) {
                    complaintType = String.valueOf(spinner_comp_type.getItemAtPosition(i));
                    comp_item_layout.setVisibility(View.GONE);
                    comp_detail.setVisibility(View.VISIBLE);
                }
                if (i == 4) {
                    complaintType = String.valueOf(spinner_comp_type.getItemAtPosition(i));
                    comp_item_layout.setVisibility(View.GONE);
                    comp_detail.setVisibility(View.VISIBLE);
                }
                if (i == 5) {
                    complaintType = String.valueOf(spinner_comp_type.getItemAtPosition(i));
                    comp_item_layout.setVisibility(View.GONE);
                    comp_detail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapter_item = new ArrayAdapter(getActivity(), R.layout.spinner_item, arrayList);
        spinner_comp_item.setAdapter(adapter_item);
        spinner_comp_item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Details = String.valueOf(spinner_comp_item.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        //****** get a value of machine
        machineNo = getArguments().getString(Constaints.MACHINE_ID);

        if (Utility.isNetworkAvailable(getActivity())) {
            getMachineInformation(machineNo);
        } else {
            Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }

        return v;
    }


    private void ragisterComplaintOnServer(String itemid) {
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<CommenResponse> listCall = apiInterface.RegisterComplaint(cityName, stateName, branchName, location, bankName, machineid, bankId, stateId, cityid, Branchid, projectID, projectname, soleid, itemid, complaintType, comp_detils_edittext.getText().toString(), machineNo, uid);

        listCall.enqueue(new Callback<CommenResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommenResponse> call, @NonNull retrofit2.Response<CommenResponse> response) {
                try {
                    CommenResponse commenResponse = response.body();

                    if (commenResponse == null) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Error : ragisterComplaint=" + null, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (commenResponse.getErrorMessage().equals("Success") && commenResponse.getErrorCode().equals("000")) {
                        dialog.show();
                        msg_test_view = dialog.findViewById(R.id.text_view_msg);
                        text_view_ref_no = dialog.findViewById(R.id.ref_no_text_view);
                        msg_test_view.setText(commenResponse.getErrorMessage());

                        text_view_ref_no.setText("Complaint No -" + commenResponse.getReferenceNo());

                    } else {
                        Toast.makeText(getActivity(), "Complaint already register", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommenResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getMachineInformation(String MachineNo) {
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<MachineInformation> listCall = apiInterface.MachineInfoDetails(MachineNo);

        listCall.enqueue(new Callback<MachineInformation>() {
            @Override
            public void onResponse(@NonNull Call<MachineInformation> call, @NonNull retrofit2.Response<MachineInformation> response) {
                try {
                    MachineInformation machineInformation = response.body();

                    if (machineInformation == null) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Error : ragisterComplaint=" + null, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (machineInformation.getErrorMessage().equals("Successs") && machineInformation.getErrorCode().equals("000") && machineInformation.getHarwareInformation() != null) {

                        machineid = String.valueOf(machineInformation.getMachineId());
                        cityName = machineInformation.getCityName();
                        stateName = machineInformation.getStateName();
                        branchName = machineInformation.getBranchName();
                        bankName = machineInformation.getBankName();
                        bankId = String.valueOf(machineInformation.getBankId());
                        machineid = String.valueOf(machineInformation.getMachineId());
                        stateId = String.valueOf(machineInformation.getStateId());
                        cityid = String.valueOf(machineInformation.getCityId());
                        Branchid = String.valueOf(machineInformation.getBranchId());
                        projectID = String.valueOf(machineInformation.getProjectId());
                        projectname = machineInformation.getProjectName();
                        location = machineInformation.getBranchName();
                        soleid = machineInformation.getSoleId();
                        machineNo = machineInformation.getMachineNo();

                        if (machineInformation.getHarwareInformation().size() == 0) {
                            arrayList.add("No Data Available");
                        } else {
                            arrayList.add("Choose Your Item");
                            for (int i = 0; i < machineInformation.getHarwareInformation().size(); i++) {
                                ItemName = machineInformation.getHarwareInformation().get(i).getItemName();
                                Itemid = String.valueOf(machineInformation.getHarwareInformation().get(i).getItemId());
                                arrayList.add(ItemName);
                            }
                        }

                        edittext_bankname.setText(bankName);
                        branch_Name_textview.setText(branchName);
                        location_edittext.setText(location);
                        edittext_city.setText(cityName);
                        edittext_state.setText(stateName);
                        machine_no_textview.setText(machineNo);
                        project_name_textview.setText(projectname);
                        sole_id_testview.setText(soleid);
                        machine_no_textview.setText(machineNo);

                        adapter_item.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), "Data is not available for this solid Id ", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MachineInformation> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showDialog() {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);

        ImageView check_alert = dialog.findViewById(R.id.check_image);

        check_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(Register_Form_Activity.this, RegisterComplaintActivity.class));
                // Register_Form_Activity.this. finish();
                dialog.cancel();
                getActivity().finish();
            }
        });

    }

}
