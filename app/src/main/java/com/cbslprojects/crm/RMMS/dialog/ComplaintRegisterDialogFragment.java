package com.cbslprojects.crm.RMMS.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cbslprojects.crm.R;
import com.cbslprojects.crm.RMMS.Util.ApiClient;
import com.cbslprojects.crm.RMMS.Util.Constraints;
import com.cbslprojects.crm.RMMS.Util.Utility;
import com.cbslprojects.crm.RMMS.model.RagisterComplaint;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ComplaintRegisterDialogFragment extends DialogFragment {

    private final String[] data = {"Choose a Complaint Type", "Hardware", "Software", "Service", "Payment FollowUp", "Other"};
    private Spinner spinner_comp_type;
    private String complaintType;
    private TextInputEditText tiet_complaint_detail;
    private TextInputLayout til_complaint_detail;
    private ProgressDialog progressDialog;
    private String branch_code;
    private String machine_no;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

        Bundle bd = getArguments();
        if (bd != null) {
            branch_code = bd.getString(Constraints.BRANCH_CODE, null);
            machine_no = bd.getString(Constraints.MACHINE_NO, null);
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = 1000;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dialog_fragment_complaint_register, container, false);
        ImageView iv_dialog_close = v.findViewById(R.id.iv_dialog_close);
        Button btn_save = v.findViewById(R.id.btn_save);
        Button btn_close = v.findViewById(R.id.btn_close);
        tiet_complaint_detail = v.findViewById(R.id.tiet_complaint_detail);
        til_complaint_detail = v.findViewById(R.id.til_complaint_detail);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        iv_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isNetworkAvailable(getContext())) {
                    registerComplaint();
                } else {
                    Toast.makeText(getContext(),
                            "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        spinner_comp_type = v.findViewById(R.id.complaint_type_spinner);
        ArrayAdapter<String> adapter_comp_type = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, data);
        spinner_comp_type.setAdapter(adapter_comp_type);
        spinner_comp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                complaintType = String.valueOf(spinner_comp_type.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return v;
    }

    private void registerComplaint() {
        if (complaintType.equalsIgnoreCase("Choose a Complaint Type")) {
            Toast.makeText(getActivity(), "Choose a Complaint Type", Toast.LENGTH_SHORT).show();
        } else {
            String complaintDetail = tiet_complaint_detail.getText().toString();
            if (complaintDetail.trim().equals("")) {
                til_complaint_detail.setError("Fill Complaint Details");
            } else {
                til_complaint_detail.setErrorEnabled(false);
                if (machine_no == null) {
                    Toast.makeText(getActivity(), "Machine Number Invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (branch_code == null) {
                    Toast.makeText(getActivity(), "Branch Code Invalid", Toast.LENGTH_SHORT).show();
                    return;
                }

                registerComplaintOnServer(complaintDetail);

            }
        }
    }


    private void registerComplaintOnServer(String complaintdetail) {
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("BranchCode", branch_code);
        hashMap.put("ComplaintType", complaintType);
        hashMap.put("ComplaintDetails", complaintdetail);
        hashMap.put("MachineNo", machine_no);
        hashMap.put("UserId", "0");

//        hashMap.put("BranchCode", "260600");
//        hashMap.put("ComplaintType", "Software");
//        hashMap.put("ComplaintDetails", "This is Software COMPLAINT test");
//        hashMap.put("MachineNo", "EPB 300 CB/101400040");
//        hashMap.put("UserId", "0");

        final RequestBody requestBody = ApiClient.  javaObjectToXml(Constraints.API_REGISTERCOMPLAINT, hashMap);

        Call<RagisterComplaint> mainResponseCall = ApiClient.callJsonRetrofit().RegisterComplaint(requestBody);

        mainResponseCall.enqueue(new Callback<RagisterComplaint>() {
            @Override
            public void onResponse(@NonNull Call<RagisterComplaint> call, @NonNull Response<RagisterComplaint> response) {

                try {
                    RagisterComplaint ragisterComplaint = response.body();
                    if (ragisterComplaint == null) {
                        Toast.makeText(getContext(), "Error : null", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (ragisterComplaint.getStatus().equals("Success")) {
                        if (ragisterComplaint.getData().getErrorMessage().equals("Success")
                                && ragisterComplaint.getData().getErrorCode().equals("000")) {
                            Utility.ShowDialog(getContext(), "Complaint Status!", "Success, Complaint No.- " + ragisterComplaint.getData().getReferenceNo());
                        } else {
                            Toast.makeText(getContext(), "" + ragisterComplaint.getData().getErrorMessage(), Toast.LENGTH_LONG).show();
                        }
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "" + ragisterComplaint.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(@NonNull Call<RagisterComplaint> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
