package genrod.pedidosproto;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Martin on 24/08/2017.
 */

public class OrderActivity extends AppCompatActivity {

    ArrayList<Product> allProducts;
    ProductArrayAdapter productsAdapter;

    private String username;
    private String password;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up navigation button.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        number = getIntent().getStringExtra("orderNumber");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        setView();
    }

    private void setView() {
        ((TextView) findViewById(R.id.order_number_text_view)).setText(number);
        final ListView listView = (ListView) findViewById(R.id.products_list_view);

        final ScreenUtility screenUtility = new ScreenUtility(this);

        final Button order_detail_button = (Button) findViewById(R.id.order_detail_button);
        final LinearLayout order_detail_layout = (LinearLayout) findViewById(R.id.order_detail_layout);
        order_detail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order_detail_layout.getVisibility() != View.VISIBLE)
                    order_detail_layout.setVisibility(View.VISIBLE);
                else
                    order_detail_layout.setVisibility(View.GONE);
            }
        });

        setProducts();
        setTestProducts();

        ProductArrayAdapter adapter = new ProductArrayAdapter(OrderActivity.this, allProducts, listView);
        productsAdapter = adapter;
        if (listView != null) {
            listView.setAdapter(adapter);
        }
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

    private void setProducts() {
        if (allProducts == null)
            return;
        Product p = allProducts.get(0);
        ((TextView) findViewById(R.id.product_item_client))
                .setText(p.getClient());
        ((TextView) findViewById(R.id.product_item_nameclient))
                .setText(p.getClientName());
        ((TextView) findViewById(R.id.product_item_seller))
                .setText(p.getSeller());
        ((TextView) findViewById(R.id.product_item_auditloguser))
                .setText(p.getAuditLogUser());
        ((TextView) findViewById(R.id.product_item_ordertype))
                .setText(p.getOrderType());
    }

    private void setTestProducts() {
        allProducts = new ArrayList<>();

        Product product = new Product("1364RMX12", "alternativo2434", "Tornillo X10 Radio 5mm", "780",
                "A21", "Juan Lopez", "Empresa SA", "NombreEmpresa", "Urgente", "1234124",
                "QueEsEsto", "-", "Preparado");
        Product preparedProd = new Product("1364RMX12", "alternativo2434", "Tornillo X10 Radio 5mm", "780",
                "A21", "Juan Lopez", "Empresa SA", "NombreEmpresa", "Urgente", "1234124",
                "QueEsEsto", "-", "PREPARADO");
        for (int i = 0; i < 45; i++) {
            if (i % 4 == 0)
                allProducts.add(preparedProd);
            else
                allProducts.add(product);
        }
    }

    private class HttpGetProducts extends AsyncTask<Void, Void, String> {

        private final String number;
        private final String username;
        private final String password;

        HttpGetProducts(String number, String username, String password){
            this.number = number;
            this.username = username;
            this.password = password;
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;

            try {
                //URL url = new URL("http://200.11.112.85/gb.asp?usu=" + username + "&psw=" + password + "&q=" + number);

                URL url = new URL("http://192.168.21.18:85/gb.asp?usu=" + username + "&psw=" + password + "&q=" + number);
                urlConnection = (HttpURLConnection) url.openConnection();

                int status = urlConnection.getResponseCode();

                InputStream in;
                if(status >= 400)
                    in = urlConnection.getErrorStream();
                else
                    in = new BufferedInputStream(urlConnection.getInputStream());
                return readStream(in);
            } catch (Exception exception) {
                Toast.makeText(getApplicationContext(),
                        "Verifique su conexión a internet",
                        Toast.LENGTH_SHORT).show();
                exception.printStackTrace();
                return "Error";
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                String string = null;
                Boolean found = false;
                for (int i = 0; i < result.length() && !found; i++) {
                    if (result.charAt(i) == '[') {
                        string = result.substring(i);
                        found = true;
                    }
                }
                if (string == null) {
                    return;
                }
                string = "{\"productos\":" + string + "}";
                JSONObject obj = new JSONObject(string);

                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Product>>() {
                }.getType();

                String jsonFragment = obj.getString("productos");
                ArrayList<Product> productList = gson.fromJson(jsonFragment, listType);
                allProducts = productList;

                final TextView clientTextView = (TextView) findViewById(R.id.product_item_client);
                final TextView nameClientTextView = (TextView) findViewById(R.id.product_item_nameclient);
                final TextView sellerTextView = (TextView) findViewById(R.id.product_item_seller);
                final TextView auditLogUserTextView = (TextView) findViewById(R.id.product_item_auditloguser);
                final TextView orderTypeTextView = (TextView) findViewById(R.id.product_item_ordertype);

                if (allProducts != null) {
                    clientTextView.setText(allProducts.get(0).getClient());
                    nameClientTextView.setText(allProducts.get(0).getClientName());
                    sellerTextView.setText(allProducts.get(0).getSeller());
                    auditLogUserTextView.setText(allProducts.get(0).getAuditLogUser());
                    orderTypeTextView.setText(allProducts.get(0).getOrderType());
                }

                ListView listView = (ListView) findViewById(R.id.products_list_view);
                ProductArrayAdapter adapter = new ProductArrayAdapter(OrderActivity.this, allProducts, listView);
                productsAdapter = adapter;
                if (listView != null) {
                    listView.setAdapter(adapter);
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Ocurrió un error inesperado.",
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
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
    }

}

