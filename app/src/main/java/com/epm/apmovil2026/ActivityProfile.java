package com.epm.apmovil2026;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ActivityProfile extends AppCompatActivity {

    private EditText editBithday, editAddress, editProfession, editWeb;
    private Spinner spinnerGender;
    private Button btnSaveProfile;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //vincular UX con los IDs de tu XML
        editBithday = findViewById(R.id.editBirthdate);
        editAddress = findViewById(R.id.editAddress);
        editProfession = findViewById(R.id.editProfession);
        editWeb = findViewById(R.id.editWeb);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        //inicializar Firebase Auth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
            userId = mAuth.getCurrentUser().getUid();
        }

        loadUserData(); //obtener datos de fbase

        if(!editBithday.getText().toString().isEmpty())
            btnSaveProfile.setOnClickListener(v -> saveUserData()); // guardar datos en fbase
        else
            Toast.makeText(this, "Por favor complete el campo Fecha de Nacimiento. El mismo es obligatorio", Toast.LENGTH_SHORT).show();

    }

    private void saveUserData() {
        //"POST"
        String birthdate = editBithday.getText().toString();
        String profession = editProfession.getText().toString();
        String address = editAddress.getText().toString();
        String web = editWeb.getText().toString();
        String gender = spinnerGender.getSelectedItem().toString();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("birthdate", birthdate);
        userMap.put("profession", profession);
        userMap.put("address", address);
        userMap.put("web", web);
        userMap.put("gender", gender);

        db.collection("Users").document(userId).set(userMap).
                addOnSuccessListener(aVoid -> Toast.makeText(ActivityProfile.this, "Perfil Actualizado", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ActivityProfile.this, "Error al guardar - "+e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadUserData() {
        db.collection("Users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                editBithday.setText(documentSnapshot.getString("birthdate"));
                editProfession.setText(documentSnapshot.getString("profession"));
                editAddress.setText(documentSnapshot.getString("address"));
                editWeb.setText(documentSnapshot.getString("web"));
                // Para el spinner se requiere lógica adicional para buscar el index,
                // por brevedad se omite o se puede usar documentSnapshot.getString("gender")
            }
        });
    }
}