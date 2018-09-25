package com.example.android.router;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    static final String routerLoginUrl = "http://192.168.0.1/login.htm";
    EditText usnET, pwdET;
    WebView routerWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.router_login);

        usnET = findViewById(R.id.router_login_usn_edit_text);
        pwdET = findViewById(R.id.router_login_pwd_edit_text);

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            routerWebView.evaluateJavascript( "document.getElementById('username').setAttribute('value','" + usn + "');" +
                    "document.getElementById('password').setAttribute('value','" + pwd + "');" +
                    "document.getElementById('loginBtn').click();", null);
        }


        Intent intent = new Intent(this,ConnectedDevicesParser.class);
        startActivity(intent);
        //check for credentials and start intent
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
