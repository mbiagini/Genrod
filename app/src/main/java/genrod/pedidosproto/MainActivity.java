package genrod.pedidosproto;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText username_edit_text = (EditText) findViewById(R.id.login_username_edit_text);
        final EditText password_edit_text = (EditText) findViewById(R.id.login_password_edit_text);

        Button register_button = (Button) findViewById(R.id.login_register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = username_edit_text.getText().toString();
                String password = password_edit_text.getText().toString();
                if (username.equals("")) {
                    Toast.makeText(getContext(),
                            getResources().getString(R.string.login_error_user),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (password.equals("")) {
                    Toast.makeText(getContext(),
                            getResources().getString(R.string.login_error_pass),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = setIntent(getContext(), RequestOrderActivity.class);

                intent.putExtra("username", username);
                intent.putExtra("password", password);

                startActivity(intent);

                /*
                HttpLogin httpLogin = new HttpLogin(username, password);
                httpLogin.execute();
                */
            }
        });
    }

    private Context getContext() {
        return this.getApplicationContext();
    }

    private Intent setIntent(Context context, Class<?> cls) {
        Intent intent = new Intent(getContext(), cls);

        // Use TaskStackBuilder to build the back stack and get the PendingIntent
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                // add all of the next activity's parents to the stack,
                // followed by this activity itself
                .addNextIntentWithParentStack(intent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(pendingIntent);
        return intent;
    }

    private class HttpLogin extends AsyncTask<Void, Void, String> {

        private final String username;
        private final String password;

        public HttpLogin(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://181.46.172.29:33385/gb.asp?usu=" + username + "&psw=" + password);
                //URL url = new URL("http://192.168.21.18:85/gb.asp?usu=" + username + "&psw=" + password);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return readStream(in);
            } catch (Exception exception) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                getResources().getString(R.string.error_no_internet),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                exception.printStackTrace();
                return "Error";
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (checkResult(result)) {
                Intent intent = setIntent(getContext(), RequestOrderActivity.class);

                intent.putExtra("username", username);
                intent.putExtra("password", password);

                startActivity(intent);
            }
        }

        private String readStream(InputStream inputStream) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int i = inputStream.read();
                while (i != -1) {
                    outputStream.write(i);
                    i = inputStream.read();
                }
                return outputStream.toString();
            } catch (IOException e) {
                return "";
            }
        }

        private boolean checkResult(String result) {
            if (result.equals("Error")) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                getResources().getString(R.string.error_conection_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
            if (result.equals("nopasa")) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                getResources().getString(R.string.login_error_pass_user),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
            return true;
        }
    }

}
