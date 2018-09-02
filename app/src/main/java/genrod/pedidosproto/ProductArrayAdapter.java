package genrod.pedidosproto;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Martin on 24/08/2017.
 */

public class ProductArrayAdapter extends ArrayAdapter<Product> {

    private ArrayList<Product> products;
    private String[] editedAmounts;
    private Integer[] motives;
    private Boolean[] clicked;
    private Boolean[] ready;
    private Context context;
    private ListView listView;

    public ProductArrayAdapter(Activity context, ArrayList<Product> objects, ListView listView) {
        super(context, R.layout.product_list_item, objects);
        this.context = context;
        this.listView = listView;
        products = objects;
        editedAmounts = new String[products.size()];
        motives = new Integer[products.size()];
        clicked = new Boolean[products.size()];
        ready = new Boolean[products.size()];
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(products != null && products.size() != 0){
            return products.size();
        }
        return 0;
    }

    @Override
    public Product getItem(int position) {
        // TODO Auto-generated method stub
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ProductViewHolder holder;
        Product product = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_list_item, parent, false);
            holder = new ProductViewHolder();
            holder.positionTextView = (TextView) convertView.findViewById(R.id.product_item_position);
            holder.codeTextView = (TextView) convertView.findViewById(R.id.product_item_code);
            holder.descriptionTextView = (TextView) convertView.findViewById(R.id.product_item_description);
            holder.amountTextView = (TextView) convertView.findViewById(R.id.product_item_amount);
            holder.emptyTextView = (TextView) convertView.findViewById(R.id.product_empty_text_view);
            holder.alternativeTextView = (TextView) convertView.findViewById(R.id.product_item_alternative);
            holder.locationTextView = (TextView) convertView.findViewById(R.id.product_item_location);
            holder.amountEditText = (EditText) convertView.findViewById(R.id.product_amount_edit_text);
            holder.motiveSpinner = (Spinner) convertView.findViewById(R.id.product_spinner);

            convertView.setTag(holder);
        } else {
            holder = (ProductViewHolder) convertView.getTag();
        }

        holder.ref = position;

        holder.positionTextView.setText((position + 1) + ".");
        holder.codeTextView.setText(product.getCode());
        holder.descriptionTextView.setText(product.getDescription());
        Double amount = Double.parseDouble(product.getAmount());
        amount = Math.floor(amount * 100) / 100;
        holder.amountTextView.setText(amount.toString());
        holder.alternativeTextView.setText(product.getAlternative());
        holder.locationTextView.setText(product.getLocation());
        holder.amountEditText.setText(editedAmounts[position]);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.product_spinner_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        holder.motiveSpinner.setAdapter(adapter);
        holder.motiveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int options_position, long id)
            {
                motives[holder.ref] = options_position;
                Spinner spinner = (Spinner) view.findViewById(R.id.product_spinner);
                if (spinner != null) {
                    String motive = spinner.getSelectedItem().toString();
                    getItem(position).setMotive(motive);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                // vacio
            }
        });
        if (motives[holder.ref] != null) {
            holder.motiveSpinner.setSelection(motives[holder.ref]);
        }
        holder.amountEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                editedAmounts[holder.ref] = arg0.toString();
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout product_detail = (LinearLayout) view.findViewById(R.id.product_detail_layout);
                if (product_detail.getVisibility() != View.VISIBLE) {
                    product_detail.setVisibility(View.VISIBLE);
                    clicked[holder.ref] = true;
                    if (position == (products.size() - 1)) {
                        listView.setSelection(ProductArrayAdapter.this.getCount() - 1);
                    }
                }
                else {
                    product_detail.setVisibility(View.GONE);
                    clicked[holder.ref] = false;
                }
            }
        });

        LinearLayout product_detail = (LinearLayout) convertView.findViewById(R.id.product_detail_layout);
        if (clicked[holder.ref] != null && clicked[holder.ref]) {
            product_detail.setVisibility(View.VISIBLE);
        }
        else {
            product_detail.setVisibility(View.GONE);
        }
        if (product.getPrepared().equals("PREPARADO")) {
            ready[holder.ref] = true;
        }
        if (ready[holder.ref] != null && ready[holder.ref]) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.md_green_100));
            holder.amountEditText.setVisibility(View.GONE);
            holder.motiveSpinner.setVisibility(View.GONE);
            holder.emptyTextView.setVisibility(View.VISIBLE);
        }
        else {
            if ((position % 2) == 0)
                convertView.setBackgroundColor(context.getResources().getColor(R.color.md_amber_50));
            else
                convertView.setBackgroundColor(context.getResources().getColor(R.color.md_brown_50));
            holder.amountEditText.setVisibility(View.VISIBLE);
            holder.motiveSpinner.setVisibility(View.VISIBLE);
            holder.emptyTextView.setVisibility(View.GONE);
        }
        return convertView;
    }

    String[] getEditedAmounts() {
        return editedAmounts;
    }

    Integer[] getMotives() {
        return motives;
    }

}
