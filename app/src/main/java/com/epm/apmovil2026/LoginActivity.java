package com.epm.apmovil2026;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputRePassword;
    private Button btnAceptar, btnCancelar;

    // 1. Declarar FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 2. Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 3. Vincular vistas con los IDs de tu XML
        inputEmail = findViewById(R.id.inputUserName); // Usaremos este para el Email
        inputPassword = findViewById(R.id.inputUserPassword);
        inputRePassword = findViewById(R.id.inputReUserPassword);
        btnAceptar = findViewById(R.id.btnAceptar);
        btnCancelar = findViewById(R.id.btnCancelar);

        btnAceptar.setOnClickListener(v -> registrarUsuario());

        btnCancelar.setOnClickListener(v -> finish());
    }

    private void registrarUsuario() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String rePassword = inputRePassword.getText().toString().trim();

        // Validaciones básicas
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(rePassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Crear usuario en Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Éxito
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Registro exitoso: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        // Aquí puedes redirigir a otra pantalla (Intent)
                    } else {
                        // Error
                        Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}