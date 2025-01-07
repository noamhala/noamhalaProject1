package com.ariel.noamhalaproject1.screens;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.models.User;
import com.ariel.noamhalaproject1.services.AuthenticationService;
import com.ariel.noamhalaproject1.services.DatabaseService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "RegisterActivity";

    private EditText etFname, etLname, etPhone, etEmail,etPass;
    private String fname, lname, phone, email, password, gender;
    private  Button btnRegister;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    private AuthenticationService authenticationService;
    private DatabaseService databaseService;


    String city;
    Spinner spCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();
        initViews();


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        btnRegister.setOnClickListener(this);
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etPhone = findViewById(R.id.etPhone);
        etPass = findViewById(R.id.etPass);
        spCity = findViewById(R.id.spCity);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        spCity.setOnItemSelectedListener(this);
    }


    @Override
    public void onClick(View view) {
        fname = etFname.getText().toString();
        lname =etLname.getText().toString();
        phone= etPhone.getText().toString();
        email = etEmail.getText().toString();
        password = etPass.getText().toString();
        if(!email.contains("@")){
            etEmail.setError("Enter correct email");
        }

        //check if registration is valid
        Boolean isValid=true;
        if (fname.length()<2){

            etFname.setError("שם פרטי קצר מדי");
            isValid = false;
        }
        if (lname.length()<2){
            Toast.makeText(Register.this,"שם משפחה קצר מדי", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if (phone.length()<9||phone.length()>10){
            Toast.makeText(Register.this,"מספר הטלפון לא תקין", Toast.LENGTH_LONG).show();
            isValid = false;
        }

        if (!email.contains("@")){
            Toast.makeText(Register.this,"כתובת האימייל לא תקינה", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(password.length()<6){
            Toast.makeText(Register.this,"הסיסמה קצרה מדי", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(password.length()>20){
            Toast.makeText(Register.this,"הסיסמה ארוכה מדי", Toast.LENGTH_LONG).show();
            isValid = false;
        }

        if (isValid==true){

            authenticationService.signUp(email, password, new AuthenticationService.AuthCallback<String>() {
                @Override
                public void onCompleted(String id) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success");
                    User newUser=new User(id, fname, lname,phone, email,password,city, gender);
                    databaseService.createNewUser(newUser, new DatabaseService.DatabaseCallback<Void>() {
                        @Override
                        public void onCompleted(Void object) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString("email", email);
                            editor.putString("password", password);

                            editor.commit();
                            Intent goLog=new Intent(getApplicationContext(), Login.class);
                            startActivity(goLog);
                        }

                        @Override
                        public void onFailed(Exception e) {

                        }
                    });

                }

                @Override
                public void onFailed(Exception e) {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure", e);
                    Toast.makeText(Register.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        city = (String) adapterView.getItemAtPosition(i);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
