package com.cbslprojects.crm.RMMS.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbslprojects.crm.R;
import com.cbslprojects.crm.RMMS.Util.Constraints;
import com.cbslprojects.crm.RMMS.model.Machine;

public class MachineDetailActivity extends AppCompatActivity {

    private Machine machine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_detail);

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            machine = (Machine) bd.getSerializable(Constraints.MACHINE_OBJ);
        }


        LinearLayout ll_show_machine_detail = findViewById(R.id.ll_show_machine_detail);

        for (int i = 0; i <= 16; i++) {
            TextView textView = new TextView(this);
            TextView textView1 = new TextView(this);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            layoutParams.leftMargin = 30;
            textView.setTextSize(18);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.RIGHT);
            textView.setLayoutParams(layoutParams);
            textView1.setGravity(Gravity.LEFT);
            textView1.setLayoutParams(layoutParams1);
            String heading = null;
            String value = null;
            switch (i) {
                case 1:
                    heading = "Bank : ";
                    value = machine.getBankName();
                    break;
                case 2:
                    heading = "Branch Name : ";
                    value = machine.getBranch_name();
                    break;
                case 3:
                    heading = "IFSC Code : ";
                    value = machine.getIFSC_Code();
                    break;
                case 4:
                    heading = "Branch Code : ";
                    value = machine.getBranchCode();
                    break;
                case 5:
                    heading = "Branch Address : ";
                    value = machine.getLocation_name();
                    break;
                case 6:
                    heading = "Kisok Id : ";
                    value = machine.getKioskId();
                    break;
                case 7:
                    heading = "Machine Number : ";
                    value = machine.getMachineNo();
                    break;
                case 8:
                    heading = "City Name : ";
                    value = machine.getCityName();
                    break;
                case 9:
                    heading = "State Name : ";
                    value = machine.getStateName();
                    break;
                case 10:
                    heading = "Message : ";
                    value = machine.getConnectionStatus();
                    textView1.setText(showStatus(value),TextView.BufferType.SPANNABLE);
                    break;

            }
            textView.setText(heading);
            if (i != 10)
                textView1.setText(value);
            linearLayout.addView(textView);
            linearLayout.addView(textView1);
            ll_show_machine_detail.addView(linearLayout);
        }


    }


    public SpannableStringBuilder showStatus(String value) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString redSpannable = new SpannableString(value);

        if (machine.getConnectionStatus() != null) {
            if (machine.getConnectionStatus().trim().equals("Online")) {
                redSpannable.setSpan(new ForegroundColorSpan(Color.argb(255, 37, 128, 43)), 0, value.length(), 0);
            } else {
                redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, value.length(), 0);
            }
        } else {
            redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, value.length(), 0);

        }
        builder.append(redSpannable);

        return builder;
    }
}
