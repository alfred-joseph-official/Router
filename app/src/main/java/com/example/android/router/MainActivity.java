package com.example.android.router;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    static final String routerLoginUrl = "http://192.168.0.1/login.htm";
    boolean usnChange,pwdChange;
    EditText usnET, pwdET;
    WebView routerWebView;
    CheckBox saveCredentialsCheckbox;
    private SharedPreferences credentials;
    SharedPreferences.Editor sharedPreferencesEditor;
    android.support.design.widget.TextInputLayout pwdTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.router_login);

        credentials = getSharedPreferences("Credentials",MODE_PRIVATE);

        usnET = findViewById(R.id.router_login_usn_edit_text);
        pwdET = findViewById(R.id.router_login_pwd_edit_text);
        pwdTextInputLayout = findViewById(R.id.router_login_pwd_text_input_layout);
        saveCredentialsCheckbox = findViewById(R.id.router_login_save_credentials_checkbox);
        loadCredentials();

        usnET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                usnChange = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pwdET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pwdChange = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        routerWebView = new WebView(getApplicationContext());
        routerWebView.setWebViewClient(new myBrowser());
        routerWebView.getSettings().setJavaScriptEnabled(true);
        routerWebView.loadUrl(routerLoginUrl);

    }

    public class myBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view,String url) {
            super.onPageFinished(view,url);
            // do task here
        }
    }

    public void loginButton(View view) {
        String usn = usnET.getText().toString();
        String pwd = pwdET.getText().toString();
        if((usnChange || pwdChange) && saveCredentialsCheckbox.isChecked()) {
            saveCredentials(usn,pwd);
        } else if(!saveCredentialsCheckbox.isChecked()) {
            saveCredentials("","");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            routerWebView.evaluateJavascript( "document.getElementById('username').setAttribute('value','" + usn + "');" +
                    "document.getElementById('password').setAttribute('value','" + pwd + "');" +
                    "document.getElementById('loginBtn').click();", null);
        }


        Intent intent = new Intent(this,ConnectedDevicesParser.class);
        startActivity(intent);
        //check for credentials and start intent
    }

    private void saveCredentials(String usn, String pwd) {
            sharedPreferencesEditor = credentials.edit();
            sharedPreferencesEditor.putString("usn",usn);
            sharedPreferencesEditor.putString("pwd",pwd);
            sharedPreferencesEditor.commit();
    }

    private void loadCredentials() {
        String usn = credentials.getString("usn","");
        String pwd = credentials.getString("pwd","");
        if(!usn.equals("")){
            pwdTextInputLayout.setPasswordVisibilityToggleEnabled(false); //disables the show password option.
            usnET.setText(usn);
            pwdET.setText(pwd);
        } else {
            usnET.setText("");
            pwdET.setText("");
            pwdTextInputLayout.setPasswordVisibilityToggleEnabled(true);
        }
        usnChange = false;
        pwdChange = false;
    }

    public void routerLoginUsnImgClick (View view) {
        usnET.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.showSoftInput(usnET,InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void routerLoginPwdImgClick (View view) {
        pwdET.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(pwdET,InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void routerLoginConstraintLayoutClicked(View view) {
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
            view.clearFocus();
        }
    }
}
