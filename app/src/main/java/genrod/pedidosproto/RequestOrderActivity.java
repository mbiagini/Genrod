package genrod.pedidosproto;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Martin on 24/08/2017.
 */

public class RequestOrderActivity extends AppCompatActivity {

    private String global_username;
    private String global_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up navigation button.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (global_username == null) {
            global_username = getIntent().getStringExtra("username");
            global_password = getIntent().getStringExtra("password");
        }

        Button search_order_button = (Button) findViewById(R.id.order_search_button);
        search_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText order_number_text = (EditText) findViewById(R.id.order_number_input);
                String number = order_number_text.getText().toString();
                searchOrder(number);
            }
        });

    }

    private Intent setIntent(Context context, Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean searchOrder(String number) {
        if (number.length() != 6) {
            Toast.makeText(getApplicationContext(), getString(R.string.order_number_error_lenght), Toast.LENGTH_LONG).show();
            return false;
        }

        Intent intent = setIntent(getApplicationContext(), OrderActivity.class);

        intent.putExtra("orderNumber", number);
        intent.putExtra("username", global_username);
        intent.putExtra("password", global_password);

        startActivity(intent);
        return true;
    }
}
