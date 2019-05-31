package com.helwan.bis.review.screens.loginscreen.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.helwan.bis.review.R;
import com.helwan.bis.review.model.dao.User;
import com.helwan.bis.review.screens.loginscreen.LoginCommunicator;
import com.helwan.bis.review.utilities.CheckInternetConnectionHelper;
import com.helwan.bis.review.utilities.DatePickerFragmentHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.regex.Pattern;

public class SignupFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private EditText edtEmailSignup, edtFirstNameSignup, edtLastNameSignup, edtPasswordSignup, edtConfirmPasswordSignup, edtPhoneSignup, edtIdSignup, edtAreaSignup;
    private TextView txtBirthdateSignup;
    private CheckBox checkboxMale, checkboxFemale;
    private LoginCommunicator communicator;
    private Button btnCreateAccount, btnLoginSignupPage, btnTerms;
    private User user;

    /**
     * Fragment constructor must be empty
     */
    public SignupFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        initViews(view);
        addListeners();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (LoginCommunicator) context;
        user = new User();
    }

    /**
     * Initialize views
     *
     * @param view Created view after inflater
     */
    private void initViews(View view) {
        edtFirstNameSignup = view.findViewById(R.id.edtFirstNameSignup);
        edtLastNameSignup = view.findViewById(R.id.edtLastNameSignup);
        edtEmailSignup = view.findViewById(R.id.edtEmailSignup);
        edtPasswordSignup = view.findViewById(R.id.edtPasswordSignup);
        edtConfirmPasswordSignup = view.findViewById(R.id.edtConfirmPasswordSignup);
        edtPhoneSignup = view.findViewById(R.id.edtPhoneSignup);
        edtIdSignup = view.findViewById(R.id.edtIdSignup);
        edtAreaSignup = view.findViewById(R.id.edtAreaSignup);
        txtBirthdateSignup = view.findViewById(R.id.txtBirthdateSignup);
        checkboxMale = view.findViewById(R.id.checkboxMale);
        checkboxFemale = view.findViewById(R.id.checkboxFemale);
        btnCreateAccount = view.findViewById(R.id.btnCreateAccount);
        btnLoginSignupPage = view.findViewById(R.id.btnLoginSignupPage);
        btnTerms = view.findViewById(R.id.btnTerms);
    }

    /**
     * Add listeners
     */
    private void addListeners() {
        txtBirthdateSignup.setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                DialogFragment datePicker = new DatePickerFragmentHelper();
                datePicker.setTargetFragment(SignupFragment.this, 0);
                datePicker.show(getFragmentManager(), "date picker");
            }
        });

        checkboxMale.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkboxFemale.setChecked(false);
                user.setGender("Male");
            } else {
                user.setGender("");
            }
        });

        checkboxFemale.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkboxMale.setChecked(false);
                user.setGender("Female");
            } else {
                user.setGender("");
            }
        });

        btnCreateAccount.setOnClickListener(v -> {
            if (isDataValid()) {
                if (CheckInternetConnectionHelper.getInstance(getActivity()).checkInternet()) {
                    communicator.onButtonRegisterClick(user);
                } else {
                    communicator.toast("No Internet Connection", false);
                }
            }
        });

        btnLoginSignupPage.setOnClickListener(v -> communicator.replaceFragment("loginFragment"));

        btnTerms.setOnClickListener(v -> {
            try {
                InputStream inputStream = getActivity().getAssets().open("terms.txt");

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader BR = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder msg = new StringBuilder();
                while ((line = BR.readLine()) != null) {
                    msg.append(line + "\n");
                }

                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.terms))
                        .setMessage(Html.fromHtml(msg + ""))
                        .setNegativeButton(getString(R.string.ok), (arg0, arg1) -> {

                        })
                        .show();
            } catch (IOException e) {
                Log.e("Error", "addListeners: " + e.getLocalizedMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if (calendar.before(Calendar.getInstance())) {
            txtBirthdateSignup.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            txtBirthdateSignup.setError(null);
        } else {
            txtBirthdateSignup.setError("Please Enter Right Birthday");
            user.setBirthday("");
        }
    }

    /**
     * Validate input data
     *
     * @return True if data is valid and False if it isn't valid
     */
    private boolean isDataValid() {
        boolean isValid = false;
        user.setFirstName(edtFirstNameSignup.getText().toString().trim());
        user.setLastName(edtLastNameSignup.getText().toString().trim());
        user.setEmail(edtEmailSignup.getText().toString().trim());
        user.setPassword(edtPasswordSignup.getText().toString().trim());
        user.setPhone(edtPhoneSignup.getText().toString().trim());
        user.setId(edtIdSignup.getText().toString().trim());
        user.setArea(edtAreaSignup.getText().toString().trim());
        user.setBirthday(txtBirthdateSignup.getText().toString().trim());

        String confirmPassword = edtConfirmPasswordSignup.getText().toString().trim();
        String emailRegex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String phoneRegex = "(01)[0-9]{9}";
        String idRegex = "(2|3)[0-9][1-9][0-1][1-9][0-3][1-9](01|02|03|04|11|12|13|14|15|16|17|18|19|21|22|23|24|25|26|27|28|29|31|32|33|34|35|88)\\d\\d\\d\\d\\d";

        if (user.getFirstName().equals("")) {
            edtFirstNameSignup.setError("Please Enter Your First Name");
        } else if (user.getLastName().equals("")) {
            edtLastNameSignup.setError("Please Enter Your Last Name");
        } else if (user.getEmail().equals("")) {
            edtEmailSignup.setError("Please Enter Your Email");
        } else if (isRegexNotValid(emailRegex, user.getEmail())) {
            edtEmailSignup.setError("Please Enter Right Email");
        } else if (user.getPassword().equals("")) {
            edtPasswordSignup.setError("Please Enter Your Password");
        } else if (confirmPassword.equals("") || !confirmPassword.equals(user.getPassword())) {
            edtConfirmPasswordSignup.setError("Password Not Match");
        } else if (user.getPhone().equals("")) {
            edtPhoneSignup.setError("Please Enter Your Phone");
        } else if (isRegexNotValid(phoneRegex, user.getPhone())) {
            edtPhoneSignup.setError("Please Enter Right Phone");
        } else if (user.getId().equals("")) {
            edtIdSignup.setError("Please Enter Your Id");
        } else if (isRegexNotValid(idRegex, user.getId())) {
            edtIdSignup.setError("Please Enter Right Id");
        } else if (user.getArea().equals("")) {
            edtAreaSignup.setError("Please Enter Your Area");
        } else if (user.getBirthday().equals("")) {
            txtBirthdateSignup.setError("Please Enter Your Birthday");
        } else if (user.getGender() == null || user.getGender().equals("")) {
            communicator.toast("Please Select Your Gender", false);
        } else {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Validate input data with specific regex
     *
     * @param regex Will use to validate data
     * @param data  Which want to validate
     * @return False if data is valid and True if not valid
     */
    private boolean isRegexNotValid(String regex, String data) {
        Pattern pattern = Pattern.compile(regex);
        return !pattern.matcher(data).find();
    }

}
