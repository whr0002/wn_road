package com.map.woodlands.woodlandsmap.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.map.woodlands.woodlandsmap.R;

public class ContactActivity extends ActionBarActivity {

    private Button sendBtn;
    private EditText phoneEdit;
    private EditText messageEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sendBtn = (Button)findViewById(R.id.send);
        phoneEdit = (EditText)findViewById(R.id.phone);
        messageEdit = (EditText)findViewById(R.id.body);
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

            case R.id.home:
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
}
