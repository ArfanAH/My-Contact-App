package cse489.assignment.id2020160139;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ContactSummaryAdapter extends ArrayAdapter<ContactSummary> {


  private final Context context;
  private final ArrayList<ContactSummary> values;

  public ContactSummaryAdapter(@NonNull Context context, @NonNull ArrayList<ContactSummary> items) {
    super(context, -1, items);
    this.context = context;
    this.values = items;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    View rowView = inflater.inflate(R.layout.activity_contact_row, parent, false);

    TextView name = rowView.findViewById(R.id.name);
    TextView email = rowView.findViewById(R.id.email);
    TextView phone = rowView.findViewById(R.id.phone);
    ImageView image = rowView.findViewById(R.id.image);

    ContactSummary e = values.get(position);
    name.setText(e.name);
    email.setText(e.email);
    phone.setText(e.homePhone);
    if (e.image != null && !e.image.isEmpty()) {
      Bitmap decodedImage = decodeBase64(e.image);
      image.setImageBitmap(decodedImage);
    }


    return rowView;
  }
  private Bitmap decodeBase64(String base64String) {
    byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
  }
}
