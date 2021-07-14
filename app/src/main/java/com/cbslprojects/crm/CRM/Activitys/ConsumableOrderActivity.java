package com.cbslprojects.crm.CRM.Activitys;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cbslprojects.crm.CRM.Interfaces.ApiInterface;
import com.cbslprojects.crm.CRM.Model.BranchDetails;
import com.cbslprojects.crm.CRM.Model.CompanyDetail;
import com.cbslprojects.crm.CRM.Model.ItemDetail;
import com.cbslprojects.crm.CRM.Model.QuantityDetail;
import com.cbslprojects.crm.CRM.Model.RagisterComplaint;
import com.cbslprojects.crm.CRM.Model.Result;
import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.ApiClient;
import com.cbslprojects.crm.CRM.Util.Constaints;
import com.cbslprojects.crm.CRM.Util.MyPrefences;
import com.cbslprojects.crm.CRM.Util.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class ConsumableOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private LinearLayout toolbar;
    private TextView tbTitle;
    private ImageView ivBack;
    private TextView tvBank;
    private TextView tvBranchcode;
    private TextView tvBranch;
    private TextView tvCity;
    private TextView tvState;
    private TextView tvpostalAddress;
    private TextView tvItem;
    private TextView tvQuantity;
    private TextView tvMachine;
    private TextView tvProject;
    private TextView tvCompany;
    private TextView tvOrderdate;
    private TextView tvRequiredDate;
    private TextView tvContactPerson;
    private TextView tvClientEmail;
    private TextView tvAddress;

    private Spinner spinnerItem;
    private EditText etOrderNum;
    private EditText etMachine;
    private Spinner spinnerProject;
    //  private Spinner spinnerCompany;
    private EditText etOrderdate;
    private EditText etRequiredDate;

    private Button btnDelete;
    private Button btnCancel;
    private Button btnEdit;
    private Button btnPlaceOrder;


    private String userId = "";
    ArrayList<CompanyDetail> companyMappedInformationList = new ArrayList<>();

    private BranchDetails branchDetail;
    private Spinner spinnerQuantity;
    private List<QuantityDetail> quantityDetails = new ArrayList<>();

    private List<BranchDetails> branchDetailList = new ArrayList<>();
    List<ItemDetail> itemDetailList = new ArrayList<ItemDetail>();


    int selectedBankId = 0;
    int selectedProjectId = 0;

    private TextView tvProjectName;
    private TextView tvCompanyName;
    private LinearLayout llSpinnerProject;

    private int selectedCompanyId = 0;


    // private final String DATE_FORMAT="dd-MM-yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumable_order);
        context = this;
        intiToolbar();
        findView();
        getIntentData();

        getDataFromApi();

    }

    private void intiToolbar() {
        userId = MyPrefences.getInstance(context).getString(MyPrefences.PREFRENCE_USER_ID, "");

    }

    private void getIntentData() {
        Intent intent = getIntent();
        branchDetail = (BranchDetails) intent.getSerializableExtra(Constaints.BRANCHDETAILS_OBJECT);

        tvBank.setText(branchDetail.getBankName().trim());
        tvBranch.setText(branchDetail.getBranchName().trim());

        tvState.setText(branchDetail.getStateName().trim());
        tvCity.setText(branchDetail.getCityName().trim());
        tvAddress.setText(branchDetail.getLocationName().trim());
        tvpostalAddress.setText(branchDetail.getPostalCode() + "");
        if (branchDetail.getContactNo() != null)
            tvContactPerson.setText(branchDetail.getContactNo().trim());
        if (branchDetail.getEmailId() != null)
            tvClientEmail.setText(branchDetail.getEmailId().trim());

    }


    private void findView() {
        tvProjectName = (TextView) findViewById(R.id.tv_projct_name);
        tvCompanyName = (TextView) findViewById(R.id.tv_company_name);
        llSpinnerProject = (LinearLayout) findViewById(R.id.ll_spinner_project);

        tvBank = (TextView) findViewById(R.id.tv_bank);
        tvBranchcode = (TextView) findViewById(R.id.tv_brach_code);
        tvBranch = (TextView) findViewById(R.id.tv_branch);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvState = (TextView) findViewById(R.id.tv_state);
        tvpostalAddress = (TextView) findViewById(R.id.tv_postal_code);
        tvItem = (TextView) findViewById(R.id.tv_item);
        tvQuantity = (TextView) findViewById(R.id.tv_quantity);
        tvMachine = (TextView) findViewById(R.id.tv_machine);
        //tvProject = (TextView) findViewById(R.id.tv_project);
        tvCompany = (TextView) findViewById(R.id.tv_company);
        tvOrderdate = (TextView) findViewById(R.id.tv_order_date);
        tvRequiredDate = (TextView) findViewById(R.id.tv_required_date);
        tvContactPerson = (TextView) findViewById(R.id.tv_contact_person);
        tvClientEmail = (TextView) findViewById(R.id.tv_client_email);
        tvAddress = (TextView) findViewById(R.id.tv_address);


        //spinnerBank = (Spinner) findViewById(R.id.spinner_bank);
        spinnerItem = (Spinner) findViewById(R.id.spinner_item);
        spinnerProject = (Spinner) findViewById(R.id.spinner_project);
        //spinnerCompany = (Spinner) findViewById(R.id.spinner_company);

        //  etBranchcode = (EditText) findViewById(R.id.et_branch_code);

        etOrderNum = (EditText) findViewById(R.id.et_order_num);
        // etQuantity = (EditText) findViewById(R.id.et_quantity);
        etMachine = (EditText) findViewById(R.id.et_machine);
        etOrderdate = (EditText) findViewById(R.id.et_order_date);
        etRequiredDate = (EditText) findViewById(R.id.et_required_date);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnEdit = (Button) findViewById(R.id.btn_edit);
        btnPlaceOrder = (Button) findViewById(R.id.btn_place_order);

        spinnerQuantity = (Spinner) findViewById(R.id.spinner_quantity);

        setQuantitySpinner();

        btnCancel.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnPlaceOrder.setOnClickListener(this);

        etMachine.setFocusable(false);
        //etBranchcode.setOnClickListener(this);

        //etBranchcode.setFocusable(false);

        etOrderdate.setFocusable(false);
        etRequiredDate.setFocusable(false);
        etRequiredDate.setOnClickListener(this);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        etOrderdate.setText(dateFormat.format(new Date()));


        spinnerProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    branchDetailsData(position - 1);

                } else {
                    etMachine.setText("");
                    selectedProjectId = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {

                    for (int i = 0; i < companyMappedInformationList.size(); i++) {
                        if (itemDetailList.get(position - 1).getCompanyId().equals(companyMappedInformationList.get(i).getCompanyId())) {
                            tvCompanyName.setText(companyMappedInformationList.get(i).getCompanyName().trim());
                            selectedCompanyId = Integer.parseInt(companyMappedInformationList.get(i).getCompanyId());
                            break;
                        }
                    }

                    //  getQuantityListOfSelectedItem(itemMappedInformationList.get(position - 1).getItemId());
                    getQuantityListOfSelectedItem(selectedBankId, selectedProjectId,
                            itemDetailList.get(position - 1).getConsumableItemId());


                } else {
                    tvCompanyName.setText("");
                    selectedCompanyId = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_delete:
                //  hideKeyboard();

                break;
            case R.id.btn_cancel:
                //  hideKeyboard();
                finish();
                break;
            case R.id.btn_edit:
                //  hideKeyboard();
                break;
            case R.id.btn_place_order:
                //  a();
                submitConsumableOrders();

                break;
            case R.id.et_required_date:
                int mYear = Calendar.YEAR;
                int mMonth = Calendar.MONTH;
                int mDay = Calendar.DAY_OF_MONTH;
                DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String monthStr = "";

                        if (month < 10) {
                            monthStr = "0" + (month + 1);
                        } else {
                            monthStr = "" + (month + 1);
                        }
                        String dayStr = "";

                        if (dayOfMonth < 10) {
                            dayStr = "0" + dayOfMonth;
                        } else {
                            dayStr = "" + dayOfMonth;
                        }
                        //  etRequiredDate.setText(dayStr + "-" + monthStr + "-" + year);
                        etRequiredDate.setText(monthStr + "/" + dayStr + "/" + year);


                    }
                }, mYear, mMonth, mDay);

                dpd.getDatePicker().setMinDate(new Date().getTime());
                dpd.show();

                break;
        }
    }

    private void getDataFromApi() {

        if (Utility.isNetworkAvailable(this)) {
            getCompanyDetails();

            getConsumableOrderListData();
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_network_available), Toast.LENGTH_LONG).show();
        }

    }


    private void getCompanyDetails() {
        final ProgressDialog dialog = Utility.showProgressDialog(context);

        ApiInterface apiInterface = ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<CompanyDetail> listCall = apiInterface.getCompanyDetail();

        listCall.enqueue(new Callback<CompanyDetail>() {
            @Override
            public void onResponse(@NonNull Call<CompanyDetail> call, @NonNull retrofit2.Response<CompanyDetail> response) {
                try {
                    CompanyDetail companyDetail = response.body();

                    if (companyDetail == null) {
                        dialog.dismiss();
                        Toast.makeText(ConsumableOrderActivity.this, "Error :" + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (companyDetail.getErrorCode().equals("000")) {
                        if (companyDetail.getMappedInformation() != null) {
                            companyMappedInformationList = companyDetail.getMappedInformation();
                        }
                    } else {
                        Toast.makeText(context, companyDetail.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ConsumableOrderActivity.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompanyDetail> call, @NonNull Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
                Toast.makeText(ConsumableOrderActivity.this, "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void submitConsumableOrders() {

                String postalcode = tvpostalAddress.getText().toString();
                int selectedProjectPos = 0;
                if (llSpinnerProject.getVisibility() == View.VISIBLE) {
                    selectedProjectPos = spinnerProject.getSelectedItemPosition();
                    if (selectedProjectPos == 0) {
                        Utility.showToast(context, "Please select project");
                        return;
                    } else {
                        selectedProjectId = branchDetailList.get(selectedProjectPos - 1).getProjectId();
                    }
                }

                int itemSelectedPos = spinnerItem.getSelectedItemPosition();
                if (itemSelectedPos != 0) {
                    int selectedQuantityPos = spinnerQuantity.getSelectedItemPosition();

                    if (selectedQuantityPos > 0) {
                        String machine = etMachine.getText().toString();
                        if (machine.length() > 0) {

                            final String requiredDate = etRequiredDate.getText().toString();
                            if (requiredDate.length() > 0) {
                                String orderDate = etOrderdate.getText().toString();
                                if (getDiffernceBetweenDate(orderDate, requiredDate) > 6) {

                                    String contactperson = tvContactPerson.getText().toString();
                                    String clientEmail = tvClientEmail.getText().toString();
                                    final ProgressDialog dialog = Utility.showProgressDialog(context);

                                    ApiInterface apiInterface = ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

                                    Call<Result> listCall = apiInterface.SubmitConsumableOrders(branchDetail.getBankId(),
                                            branchDetail.getSoleId(),
                                            itemDetailList.get(itemSelectedPos - 1).getConsumableItemId(),
                                            selectedCompanyId,
                                            selectedProjectId,
                                            machine,
                                            userId,
                                            quantityDetails.get(selectedQuantityPos - 1).getQuantity() + "",
                                            requiredDate,
                                            branchDetail.getBankName(),
                                            itemDetailList.get(itemSelectedPos - 1).getConsumableItemName(),
                                            contactperson,
                                            clientEmail,
                                            postalcode);

                                    listCall.enqueue(new Callback<Result>() {
                                        @Override
                                        public void onResponse(@NonNull Call<Result> call, @NonNull retrofit2.Response<Result> response) {
                                            try {
                                                Result result = response.body();

                                                if (result == null) {
                                                    dialog.dismiss();
                                                    Toast.makeText(context, "Error :" + response.message(), Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                if (result.getErrorMessage().equals("Success") &&
                                                        result.getErrorCode().equals("000") ) {
                                                    Utility.showToast(context, result.getErrorMessage());
                                                    finish();
                                                } else {
                                                    Toast.makeText(context, "Error :" + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                                }

                                                dialog.dismiss();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(context, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                                            t.printStackTrace();
                                            dialog.dismiss();
                                            Toast.makeText(context, "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    Utility.showToast(context, "Minimum difference of Required date and order date be seven days");
                                }

                            } else {
                                Utility.showToast(context, "Please select required date");
                                // Toast.makeText(context, "Please select required date", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Utility.showToast(context, "Please enter machine name");
                            // Toast.makeText(context, "Please enter machine name", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Utility.showToast(context, "Please select quantity");
                        // Toast.makeText(context, "Please enter quantity", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Utility.showToast(context, "Please select item");
                    // Toast.makeText(context, "Please select item", Toast.LENGTH_SHORT).show();
                }


            }



    private long getDiffernceBetweenDate(String orderDate, String requiredDate) {
        try {

            //Dates to compare
            String CurrentDate = orderDate;
            String FinalDate = requiredDate;

            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");

            //Setting dates
            date1 = dates.parse(CurrentDate);
            date2 = dates.parse(FinalDate);

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);

            return differenceDates;
            //Convert long to String
            // String dayDifference = Long.toString(differenceDates);
            // Toast.makeText(context,differenceDates+"",Toast.LENGTH_SHORT).show();
            //Log.e("HERE","HERE: " + dayDifference);

        } catch (Exception exception) {
            //Log.e("DIDN'T WORK", "exception " + exception);
        }

        return 0;
    }

    private void setQuantitySpinner() {

        List<String> list = new ArrayList<String>();
        list.add("Select Quantity");
        for (int i = 0; i < quantityDetails.size(); i++) {
            list.add(quantityDetails.get(i).getQuantity() + "");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuantity.setAdapter(dataAdapter);
    }

    private void getQuantityListOfSelectedItem(int bankId, int projectId, int itemId) {

        final ProgressDialog dialog = Utility.showProgressDialog(context);

        ApiInterface apiInterface = ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<QuantityDetail> listCall = apiInterface.getItemQuantityInformation(itemId, bankId, projectId);

        listCall.enqueue(new Callback<QuantityDetail>() {
            @Override
            public void onResponse(@NonNull Call<QuantityDetail> call, @NonNull retrofit2.Response<QuantityDetail> response) {
                try {
                    QuantityDetail quantityDetail = response.body();

                    if (quantityDetail == null) {
                        dialog.dismiss();
                        Toast.makeText(context, "Error :" + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (quantityDetail.getErrorMessage().equals("Success") &&
                            quantityDetail.getErrorCode().equals("000") &&
                            quantityDetail.getQuantityDetails() != null) {
                        quantityDetails = quantityDetail.getQuantityDetails();
                        setQuantitySpinner();
                    } else {
                        quantityDetails.clear();
                        setQuantitySpinner();
                        Toast.makeText(context, "Error :" + quantityDetail.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuantityDetail> call, @NonNull Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
                Toast.makeText(context, "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getConsumableOrderListData() {

        final ProgressDialog dialog = Utility.showProgressDialog(context);

        ApiInterface apiInterface = ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<RagisterComplaint> listCall = apiInterface.RagisterComplaintList(userId, branchDetail.getSoleId());

        listCall.enqueue(new Callback<RagisterComplaint>() {
            @Override
            public void onResponse(@NonNull Call<RagisterComplaint> call, @NonNull retrofit2.Response<RagisterComplaint> response) {
                try {
                    RagisterComplaint ragisterComplaint = response.body();

                    if (ragisterComplaint == null) {
                        dialog.dismiss();
                        Toast.makeText(context, "Error :" + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (ragisterComplaint.getErrorMessage().equals("Sucess") &&
                            ragisterComplaint.getErrorCode().equals("000") && ragisterComplaint.getBranchDetails() != null) {

                        branchDetailList = ragisterComplaint.getBranchDetails();
                        if (branchDetailList.size() > 1) {
                            tvProjectName.setVisibility(View.GONE);
                            llSpinnerProject.setVisibility(View.VISIBLE);
                            List<String> list = new ArrayList<String>();
                            list.add("Select Project");
                            for (int i = 0; i < branchDetailList.size(); i++) {
                                list.add(branchDetailList.get(i).getProjectName());
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_spinner_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerProject.setAdapter(dataAdapter);
                        } else {
                            branchDetailsData(0);
                            tvProjectName.setVisibility(View.VISIBLE);
                            tvProjectName.setText(branchDetailList.get(0).getProjectName());
                            llSpinnerProject.setVisibility(View.GONE);
                        }
                        dialog.dismiss();
                    } else {
                        Toast.makeText(context, "Error :" + ragisterComplaint.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RagisterComplaint> call, @NonNull Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
                Toast.makeText(context, "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void consumableItemProjectWise(int bankId, int projectId) {

        final ProgressDialog dialog = Utility.showProgressDialog(context);

        ApiInterface apiInterface = ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<ItemDetail> listCall = apiInterface.getConsumableItemProjectwise(bankId, projectId);

        listCall.enqueue(new Callback<ItemDetail>() {
            @Override
            public void onResponse(@NonNull Call<ItemDetail> call, @NonNull retrofit2.Response<ItemDetail> response) {
                try {
                    ItemDetail itemDetail = response.body();

                    if (itemDetail == null) {
                        dialog.dismiss();
                        Toast.makeText(ConsumableOrderActivity.this, "Error :" + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (itemDetail.getErrorCode().equals("000") && itemDetail.getErrorMessage().equals("Sucess")) {
                        List<String> list = new ArrayList<String>();
                        list.clear();
                        itemDetailList.clear();

                        itemDetailList = itemDetail.getItemDetails();

                        list.add("Select Item");
                        for (int i = 0; i < itemDetailList.size(); i++) {
                            list.add(itemDetailList.get(i).getConsumableItemName());
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                                android.R.layout.simple_spinner_item, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerItem.setAdapter(dataAdapter);
                    } else {
                        Toast.makeText(context, itemDetail.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ConsumableOrderActivity.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ItemDetail> call, @NonNull Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
                Toast.makeText(ConsumableOrderActivity.this, "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void branchDetailsData(int position) {
        selectedBankId = branchDetailList.get(position).getBankId();
        selectedProjectId = branchDetailList.get(position).getProjectId();

        etMachine.setText(branchDetailList.get(position).getMachineNo());
        int projectId = branchDetailList.get(position).getProjectId();
        int bankId = branchDetailList.get(position).getBankId();

        consumableItemProjectWise(bankId, projectId);
    }

}
