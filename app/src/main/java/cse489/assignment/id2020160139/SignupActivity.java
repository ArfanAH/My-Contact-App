package cse489.assignment.id2020160139;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
  EditText userName,email,password,confpassword;
  Button signupBtn;
  TextView loginNow;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);
    userName = findViewById(R.id.name);
    email = findViewById(R.id.email);
    password = findViewById(R.id.password);
    confpassword = findViewById(R.id.confirm_password);
    signupBtn = findViewById(R.id.signupBtn);
    loginNow = findViewById(R.id.loginNow);

    SharedPreferences localPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    if (localPref.getBoolean("isLogin", false)) {
      Intent loginIntent = new Intent(SignupActivity.this, ContactListActivity.class);
      startActivity(loginIntent);
      finish();
    }

    signupBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String name = userName.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String rePassword = confpassword.getText().toString();

        String validationMessage = validateInputs(name, Email,Password,rePassword);
        if (!validationMessage.isEmpty()) {
          Toast.makeText(SignupActivity.this, validationMessage, Toast.LENGTH_SHORT).show();
        }
        else {

          SharedPreferences localPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
          SharedPreferences.Editor editor = localPref.edit();

          editor.putString("name", name);
          editor.putString("email", Email);
          editor.putString("password", Password);
          editor.putBoolean("isLogin", true);
          editor.apply();

          Intent loginIntent = new Intent(SignupActivity.this, ContactListActivity.class);
          startActivity(loginIntent);
          finish();
        }
      }
    });
    loginNow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

      }
    });
  }
  private String validateInputs(String name, String email,String password, String rePassword) {
    boolean isNameValid = name.matches("[a-zA-Z ]{4,15}+");
    boolean isEmailValid = email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    boolean isPasswordValid = password.matches("^(?=.*[a-zA-Z])(?=.*\\d).{6,}$");
    boolean passwordsMatch = password.equals(rePassword);
    boolean empty = password.isEmpty() || name.isEmpty() || rePassword.isEmpty() || email.isEmpty();
    if (empty) {
      return "You have to fill all the inputs correctly.";
    } else if (!isNameValid) {
      return "Invalid name. It should contain 4 to 15 characters.";
    } else if (!isEmailValid) {
      return "Invalid email address.";
    } else if (!isPasswordValid) {
      return "Invalid password.";
    } else if (!passwordsMatch) {
      return "Passwords do not match.";
    }

    return "";
  }
}