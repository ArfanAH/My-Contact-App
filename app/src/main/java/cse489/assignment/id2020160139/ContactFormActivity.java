package cse489.assignment.id2020160139;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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

public class ContactFormActivity extends AppCompatActivity {
  EditText name,email,homephone,officephone;
  ImageView image;
  Button save,cancel;
  String  Name = "", Email = "", HomePhone= "", OfficePhone = "", Image = "";
  Integer UniqueID = (int) System.currentTimeMillis();
  private static final int PICK_IMAGE_REQUEST = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contact_form);
    name= findViewById(R.id.name);
    email= findViewById(R.id.email);
    homephone= findViewById(R.id.phoneHome);
    officephone= findViewById(R.id.phoneOffice);
    image= findViewById(R.id.image);
    save= findViewById(R.id.saveButton);
    cancel =findViewById(R.id.cancelButton);


    image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        if (ContextCompat.checkSelfPermission(ContactFormActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//          ActivityCompat.requestPermissions(ContactFormActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PICK_IMAGE_REQUEST);
//        }
//        else{
          openImagePicker();
//        }

      }
    });

    save.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Name = name.getText().toString();
        Email = email.getText().toString();
        HomePhone = homephone.getText().toString();
        OfficePhone = officephone.getText().toString();


        String validationMessage = validateInputs(Name, Email, HomePhone, OfficePhone);

        if (!validationMessage.isEmpty()) {
          Toast.makeText(ContactFormActivity.this, validationMessage, Toast.LENGTH_SHORT).show();
        } else {
          ContactDB myDb = new ContactDB(ContactFormActivity.this);


            myDb.insertContactDB(UniqueID, Name, Email, HomePhone, OfficePhone, Image);


          finish();
          Intent i = new Intent(ContactFormActivity.this, ContactListActivity.class);
          startActivity(i);
        }
      }
    });


    cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent contactIntent = new Intent(ContactFormActivity.this, ContactListActivity.class);
        startActivity(contactIntent);

      }
    });

  }
  private void openImagePicker() {
    image.setImageBitmap(null);
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    if(requestCode ==PICK_IMAGE_REQUEST || grantResults[0]== PackageManager.PERMISSION_GRANTED){
      openImagePicker();
//    }
//    else{
//      Toast.makeText(this, "permission Denied", Toast.LENGTH_SHORT).show();
//    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    try {
      if (resultCode == RESULT_OK) {
        if (requestCode == PICK_IMAGE_REQUEST) {

          if (data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
              InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
              Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
              image.setImageBitmap(bitmap);



              Image = encodeImage(bitmap);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }catch (Exception e){
      Toast.makeText(ContactFormActivity.this, "image error", Toast.LENGTH_SHORT).show();
    }
  }

  private String encodeImage(Bitmap bitmap) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] imageBytes = baos.toByteArray();
    return Base64.encodeToString(imageBytes, Base64.DEFAULT);
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