package cse489.assignment.id2020160139;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UpdateContact extends AppCompatActivity {
  EditText name,email,homephone,officephone;
  ImageView image;
  Button save,cancel;
  String  Name = "", Email = "", HomePhone= "", OfficePhone = "", Image;
  String UniqueID;
  int ID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_contact);
    name= findViewById(R.id.name);
    email= findViewById(R.id.email);
    homephone= findViewById(R.id.phoneHome);
    officephone= findViewById(R.id.phoneOffice);
    image= findViewById(R.id.image);
    save= findViewById(R.id.saveButton);
    cancel =findViewById(R.id.cancelButton);

    Intent intent = getIntent();
    if (intent.hasExtra("UniqueID")) {
      UniqueID = intent.getStringExtra("UniqueID");
      name.setText(intent.getStringExtra("Name"));
      email.setText(intent.getStringExtra("email"));
      homephone.setText(intent.getStringExtra("homePhone"));
      officephone.setText(intent.getStringExtra("officePhone"));



    }
     ID = Integer.parseInt(UniqueID);



    save.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Name = name.getText().toString();
        Email = email.getText().toString();
        HomePhone = homephone.getText().toString();
        OfficePhone = officephone.getText().toString();


        String validationMessage = validateInputs(Name, Email, HomePhone, OfficePhone);

        if (!validationMessage.isEmpty()) {
          Toast.makeText(UpdateContact.this, validationMessage, Toast.LENGTH_SHORT).show();
        } else {
          ContactDB myDb = new ContactDB(UpdateContact.this);


          if (myDb.hasRecord(ID)) {
              myDb.updateContactDB(
                  ID,
                  Name,
                  Email,
                  HomePhone,
                  OfficePhone
              );


          }
          else{
            Toast.makeText(UpdateContact.this, "update error", Toast.LENGTH_SHORT).show();
          }
          finish();
          Intent i = new Intent(UpdateContact.this, ContactListActivity.class);
          startActivity(i);
        }
      }
    });


    cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent contactIntent = new Intent(UpdateContact.this, ContactListActivity.class);
        startActivity(contactIntent);

      }
    });

  }

  private String validateInputs(String name, String email, String homePhone, String officePhone) {
    boolean isNameValid = name.matches("[a-zA-Z ]{4,15}+");
    boolean isEmailValid = email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    boolean ishomePhoneValid = homePhone.matches("\\d{11}");
    boolean isofficePhoneValid = officePhone.matches("\\d{11}");
    boolean empty = homePhone.isEmpty() || officePhone.isEmpty() || name.isEmpty() || email.isEmpty();
    if (empty) {
      return "You have to fill all the inputs correctly.";
    } else if (!isNameValid) {
      return "Invalid name. It should contain 4 to 15 characters.";
    } else if (!isEmailValid) {
      return "Invalid email address.";
    } else if (!ishomePhoneValid) {
      return "Invalid Home phone number.";
    } else if (!isofficePhoneValid) {
      return "Invalid Office phone number.";
    }

    return "";
  }

  }
