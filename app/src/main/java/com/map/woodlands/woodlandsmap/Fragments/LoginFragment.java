package com.map.woodlands.woodlandsmap.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.map.woodlands.woodlandsmap.Data.UserInfo;
import com.map.woodlands.woodlandsmap.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 3/9/2015.
 * This is used for Login
 */
public class  LoginFragment extends Fragment implements OnClickListener{

    private EditText emailField;
    private EditText passwordField;
    private Button loginBtn;
    private Button logoutBtn;
    private TextView messageView;
    private ProgressBar progressBar;

    private TextView usernameView;
    private TextView roleView;
    private RelativeLayout loginLayout;
    private RelativeLayout loggedInLayout;

    private String mUsername;
    private String mPassword;

    private Activity mActivity;

    public static LoginFragment newInstance(){
        LoginFragment mLoginFragment = new LoginFragment();

        return mLoginFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        emailField = (EditText) rootView.findViewById(R.id.EmailField);
        passwordField = (EditText) rootView.findViewById(R.id.PasswordField);
        messageView = (TextView) rootView.findViewById(R.id.Message);

        loginBtn = (Button) rootView.findViewById(R.id.Login);
        loginBtn.setOnClickListener(this);

        logoutBtn = (Button) rootView.findViewById(R.id.Logout);
        logoutBtn.setOnClickListener(this);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        usernameView = (TextView) rootView.findViewById(R.id.username);
        roleView = (TextView) rootView.findViewById(R.id.role);

        loginLayout = (RelativeLayout) rootView.findViewById(R.id.LoginLayout);
        loggedInLayout = (RelativeLayout) rootView.findViewById(R.id.LoggedInLayout);

        emailField.setText("whr0002@gmail.com");
        passwordField.setText("`Nmhwj0002");

        if(hasUserData()){
            showLoggedInView();
            setLoggedInView();
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        // Login button clicked, do sign in
        switch(v.getId()){
            case R.id.Login:
                disableViews();
                messageView.setText("");
                this.mUsername = this.emailField.getText().toString();
                this.mPassword = this.passwordField.getText().toString();
//                Log.i("debug", this.mUsername);
//                Log.i("debug", this.mPassword);

                progressBar.setVisibility(View.VISIBLE);

                new LoginAsyncTask(this.mUsername, this.mPassword).execute();
                break;

            case R.id.Logout:
                deleteUserData();
                showLoginView();
                break;

            default:

                break;
        }
    }

    /*
    * Disable field and buttons when login
    * */
    public void disableViews(){
        this.emailField.setEnabled(false);
        this.passwordField.setEnabled(false);
        this.logoutBtn.setEnabled(false);
    }

    public void enableViews(){
        this.emailField.setEnabled(true);
        this.passwordField.setEnabled(true);
        this.logoutBtn.setEnabled(true);
    }

    public void showLoginView(){
        loginLayout.setVisibility(View.VISIBLE);
        loggedInLayout.setVisibility(View.GONE);
    }

    public void showLoggedInView(){
        loginLayout.setVisibility(View.GONE);
        loggedInLayout.setVisibility(View.VISIBLE);
    }

    public String signIn(String email, String password){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://woodlandstest.azurewebsites.net/Android/Login");

        try{
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("Email", email));
            nameValuePairs.add(new BasicNameValuePair("Password", password));
            nameValuePairs.add(new BasicNameValuePair("RememberMe", "false"));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP POST Request
            HttpResponse response = httpClient.execute(httpPost);
            String responseContent = null;

            if(response.getStatusLine().toString().contains("200")){
                // Successful Connection
                responseContent = getStringFromInputStream(response.getEntity().getContent());
            }

            return responseContent;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void setLoggedInView(){
        SharedPreferences sp = this.getActivity().getSharedPreferences("UserInfo", 0);
        String json = sp.getString("json","");

        if(!json.equals("")){
            Gson gson = new Gson();
            UserInfo user;
            user = gson.fromJson(json, UserInfo.class);


            this.usernameView.setText("Hello " + user.getUsername());
            this.roleView.setText("Your role: " + user.getRole());
        }
    }

    /*
    * Check to see whether a user is logged in
    * */
    public boolean hasUserData(){
        SharedPreferences sp = this.getActivity().getSharedPreferences("UserInfo", 0);
        String json = sp.getString("json","");

        if(!json.equals("")) {
            return true;
        }

        return false;
    }


    /*
    * Save user information to local preference
    * */
    public void saveUserData(String... data){
        SharedPreferences userPrefs = this.getActivity().getSharedPreferences("UserInfo",0);
        SharedPreferences.Editor userEditor = userPrefs.edit();

        String json = userPrefs.getString("json","");

        if(json.equals("")){
            // No User information yet, add now
            Gson gson = new Gson();
            UserInfo user = new UserInfo();
            if(data.length>2) {
                user.setUsername(data[0]);
                user.setPassword(data[1]);
                user.setRole(data[2]);
            }

            json = gson.toJson(user);
            userEditor.putString("json", json);
            userEditor.commit();
        }
    }


    /*
    * Delete user information when logout
    * */
    public void deleteUserData(){
        SharedPreferences sp = this.getActivity().getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor spEditor = sp.edit();

        String json = sp.getString("json", "");

        if(!json.equals("")){
            spEditor.putString("json", "");
            spEditor.commit();
        }

    }

    /*
    * Sign in to the server
    * */
    public class LoginAsyncTask extends AsyncTask<String,String, String>{

        private String mEmail;
        private String mPassword;

        public LoginAsyncTask(String email, String password){
            this.mEmail = email;
            this.mPassword = password;
        }


        @Override
        protected String doInBackground(String... params) {

            return signIn(this.mEmail, this.mPassword);
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);

            if(s != null){
                Log.i("debug", s);
                try{
                    JSONObject reader = new JSONObject(s);
                    if(reader.getString("Status").equals("success")){
                        // Login Success
                        usernameView.setText("Hello " + reader.getString("Email"));
                        roleView.setText("Your role: " + reader.getString("Role"));
                        showLoggedInView();
                        saveUserData(this.mEmail, this.mPassword, reader.getString("Role"));
                    }else{
                        // Login failed, show message
                        messageView.setText(reader.getString("Message"));

                    }
                    enableViews();
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else{
                Log.i("debug", "Response is null");
            }

        }
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

}
