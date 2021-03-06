package innovates.com.pucho;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import innovates.com.pucho.utils.CheckNetwork;
import innovates.com.pucho.utils.Utility;

/**
 * Created by Raghunandan on 16-06-2015.
 */
public class ComposeAnswer extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    int id,statusCode;
    private Toolbar toolbar;
    private EditText editText;
    private Button post;
    private int question_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_question);
        question_id = getIntent().getIntExtra("question_id",-1);

        sharedPreferences = getSharedPreferences("User_ID", MODE_PRIVATE);
        id = sharedPreferences.getInt("user_id", -1);

           /* Use toolbar as actionbar */
        toolbar = (Toolbar) this.findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("Compose a Answer");


        this.setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_up);

        editText = (EditText) this.findViewById(R.id.editText);
        post = (Button) this.findViewById(R.id.button);
        post.setText("POST A ANSWER");
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(editText.getText().toString()))
                {
                    // rest call to pot a question.
                    // for now i use asynctask.
                    // later retrofit coupled with rxjava
                    if(CheckNetwork.isInternetAvailable(ComposeAnswer.this)) {
                        if(!TextUtils.isEmpty(editText.getText().toString()))
                        new PostTask().execute(editText.getText().toString());

                    } else
                    {
                        Toast.makeText(getApplicationContext(),"Check your network connection",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class PostTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!TextUtils.isEmpty(s))
            {
                Toast.makeText(getApplicationContext(), "Answer Posted Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection conn = null;
            DataOutputStream dos= null;
            InputStream is=null;
            String response_json=null;

            try
            {
                URL url = new URL("http://128.199.236.130:8080/questions/"+question_id+"/answers");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

                dos = new DataOutputStream(conn.getOutputStream());


                JSONObject jsonObject = new JSONObject();
                if(id!= -1)

                jsonObject.put("userId",id);
                jsonObject.put("questionId",question_id);
                jsonObject.put("content",params[0]);
                jsonObject.put("upvote",0);
                jsonObject.put("downvote",0);
                jsonObject.put("answerdOn", Utility.Datetime());

                String message = jsonObject.toString();
                dos.writeBytes(message);
                dos.flush();
                dos.close();

                conn.connect();
                statusCode = conn.getResponseCode();

                if (statusCode >= conn.HTTP_BAD_REQUEST) {
                    is = conn.getErrorStream();
                    Log.i("Response is", Utility.getStringFromInputStream(is));
                }
                else {
                    is = conn.getInputStream();
                    response_json = Utility.getStringFromInputStream(is);
                    Log.i("Response for Answer is",response_json);
                }


            }catch (Exception e)
            {
                e.printStackTrace();
            } finally {
                //clean up
                try {
                    if(dos!=null && is!=null && conn!=null) {
                        dos.close();
                        is.close();
                        conn.disconnect();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }



            return response_json;
        }
    }
}


