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

public class LoginActivity extends AppCompatActivity {
  EditText email, password;
  TextView signupNow;
  Button loginBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    SharedPreferences localPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    if (localPref.getBoolean("isLogin", false)) {
      Intent loginIntent = new Intent(LoginActivity.this, ContactListActivity.class);
      startActivity(loginIntent);
      finish();
    }


    email = findViewById(R.id.email);
    password = findViewById(R.id.password);
    signupNow = findViewById(R.id.signupNow);

    loginBtn = findViewById(R.id.loginBtn);

    loginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String Email = email.getText().toString();
        String Password = password.getText().toString();


        String validationMessage = validateInputs(Email,Password);
        if (!validationMessage.isEmpty()) {
          Toast.makeText(LoginActivity.this, validationMessage, Toast.LENGTH_SHORT).show();
        }

        else {

          SharedPreferences localPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
          String savedEmail = localPref.getString("email", "");
          String savedPassword = localPref.getString("password", "");

          if (Email.equals(savedEmail) && Password.equals(savedPassword)) {
            SharedPreferences.Editor editor = localPref.edit();
            editor.putBoolean("isLogin", true);
            editor.apply();

            Intent classLectureIntent = new Intent(LoginActivity.this, ContactListActivity.class);
            startActivity(classLectureIntent);
            finish();
          } else {
            Toast.makeText(LoginActivity.this, "Invalid input. Please check your entries.", Toast.LENGTH_SHORT).show();
          }
        }
      }
    });
    signupNow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent classLectureIntent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(classLectureIntent);

      }
    });
  }
  private String validateInputs(String email, String password) {
    boolean isEmailValid = email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    boolean isPasswordValid = password.matches("^(?=.*[a-zA-Z])(?=.*\\d).{6,}$");


    if (!isEmailValid) {
      return "Invalid UserID.";
    } else if (!isPasswordValid) {
      return "Invalid Password.";
    }

    return "";
  }
}