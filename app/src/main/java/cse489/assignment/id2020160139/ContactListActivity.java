package cse489.assignment.id2020160139;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {
  Button btnLogout, btnNew;

  private ArrayList<ContactSummary> classes;
  private ContactSummaryAdapter adapter;
  private ListView lvClasses;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contact_list);
    lvClasses = findViewById(R.id.contactListView);
    classes = new ArrayList<>();


    btnNew = findViewById(R.id.btnNew);
    btnLogout = findViewById(R.id.btnLogout);
    btnNew.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent classLectureIntent = new Intent(ContactListActivity.this, ContactFormActivity.class);
        startActivity(classLectureIntent);

      }
    });
    btnLogout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
        editor.putBoolean("isLogin", false);
        editor.apply();
        Intent contactIntent = new Intent(ContactListActivity.this, LoginActivity.class);
        startActivity(contactIntent);
        finish();

      }
    });


  }
  @Override
  protected void onStart() {
    super.onStart();
    loadData();
  }

  private void loadData() {
    classes.clear();
    ContactDB db = new ContactDB(this);
    Cursor rows = db.selectContactDB("SELECT * FROM ContactTable",null);

    if (rows.getCount() > 0) {
      while (rows.moveToNext()) {

        String UniqueID = rows.getString(0);
        String name = rows.getString(1);
        String email = rows.getString(2);
        String homePhone = rows.getString(3);
        String officePhone = rows.getString(4);
        String image = rows.getString(5);
        ContactSummary cs = new ContactSummary(UniqueID, name, email, homePhone, officePhone, image);
        classes.add(cs);
      }
    }
    db.close();
    adapter = new ContactSummaryAdapter(this, classes);
    lvClasses.setAdapter(adapter);



    lvClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

        ContactSummary clickedContact = classes.get(position);


        Intent i = new Intent(ContactListActivity.this, UpdateContact.class);


        i.putExtra("UniqueID", clickedContact.UniqueID);
        i.putExtra("Name", clickedContact.name);
        i.putExtra("email", clickedContact.email);
        i.putExtra("homePhone", clickedContact.homePhone);
        i.putExtra("officePhone", clickedContact.officePhone);
//        i.putExtra("image", clickedContact.image);


        startActivity(i);
        finish();
      }
    });



    lvClasses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        showCancelDialog(position);
        return true;
      }
    });


  }
  private void showCancelDialog(final int position) {
    final Dialog dialog = new Dialog(ContactListActivity.this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.delete_contact);

    Button btnCancel = dialog.findViewById(R.id.btnYes);
    Button btnNo = dialog.findViewById(R.id.btnNo);
    btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        deleteContact(position);

        dialog.dismiss();
      }
    });
    btnNo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });

    dialog.show();
  }
  private void deleteContact(int position) {
    ContactSummary clickedContact = classes.get(position);
    ContactDB myDb = new ContactDB(ContactListActivity.this);
    myDb.deleteContactDB(clickedContact.UniqueID);

    loadData();
  }
}