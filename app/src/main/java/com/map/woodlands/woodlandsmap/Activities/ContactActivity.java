package com.map.woodlands.woodlandsmap.Activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.map.woodlands.woodlandsmap.R;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ContactActivity extends ActionBarActivity implements View.OnClickListener{

    private Button sendBtn;
    private EditText fullNameEdit, emailEdit, subjectEdit, phoneEdit, messageEdit;
    private TextView websiteView;
    private Session session = null;
    private ProgressDialog progressDialog = null;
    Context context = null;
    String fullName, email, subject, phoneNumber, textMessage, fullBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        context = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sendBtn = (Button)findViewById(R.id.send);
        fullNameEdit = (EditText)findViewById(R.id.full_name);
        emailEdit = (EditText)findViewById(R.id.email);
        subjectEdit = (EditText)findViewById(R.id.subject);
        phoneEdit = (EditText)findViewById(R.id.phone);
        messageEdit = (EditText)findViewById(R.id.body);

        websiteView = (TextView)findViewById(R.id.website);
        websiteView.setText(Html
                .fromHtml("<a href=\"http://www.woodlandsnorth.co/\">www.woodlandsnorth.co</a>"));
        websiteView.setMovementMethod(LinkMovementMethod.getInstance());

        sendBtn.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                return true;

            case android.R.id.home:
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void startEmailIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL,
                new String[]{this.getResources().getString(R.string.contact_email)});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Interested in SCARI Application");
        intent.putExtra(Intent.EXTRA_TEXT, "Hello");
        try{
            startActivity(Intent.createChooser(intent, "Send Email ..."));

        }catch (ActivityNotFoundException e){
            Toast.makeText(this,
                    "There is no Email applications installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send:
                // Send Email
                fullName = fullNameEdit.getText().toString().trim();
                email = emailEdit.getText().toString().trim();

                if(fullName == null || email == null
                        || fullName.length() == 0 || email.length() == 0){
                    Toast.makeText(context,
                            "Please fill in your Name and Email.",
                            Toast.LENGTH_LONG).show();
                    return;

                }
                fullName = "Name: " + fullNameEdit.getText().toString() + "<br />";
                email = "Email: " + emailEdit.getText().toString() + "<br />";
                subject = subjectEdit.getText().toString();
                phoneNumber = "Phone Number: " + phoneEdit.getText().toString() + "<br />";
                textMessage = "Message: " + messageEdit.getText().toString();
                fullBody = fullName + email + phoneNumber + textMessage;

                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                session = Session.getDefaultInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("woodlands.public@gmail.com", "Woodlands-public");
                    }
                });

                progressDialog = ProgressDialog.show(this,"","Sending Mail...", true);

                RetreiveFeedTask task = new RetreiveFeedTask();
                task.execute();

                break;
        }
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            try{

                Message message = new MimeMessage(session);

                message.setFrom(new InternetAddress("woodlands.public@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("info@woodlandsnorth.co"));
                message.setSubject(subject);
                message.setContent(fullBody, "text/html; charset=utf-8");

                Transport.send(message);

            }catch (MessagingException e){
//                e.printStackTrace();
            }catch (Exception e){
//                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            fullNameEdit.setText("");
            emailEdit.setText("");
            subjectEdit.setText("");
            phoneEdit.setText("");
            messageEdit.setText("");
            Toast.makeText(context, "Message sent", Toast.LENGTH_LONG).show();

        }
    }
}
