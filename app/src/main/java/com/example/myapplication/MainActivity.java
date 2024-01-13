package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private EditText editTextUsuario;
    private EditText editTextContraseña;
    private Button btnLogin;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextUsuario = findViewById(R.id.editTextEmail);
        editTextContraseña = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Inicializa la referencia a la base de datos
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        final String usuario = editTextUsuario.getText().toString().trim();
        final String contraseña = editTextContraseña.getText().toString().trim();

        // Consulta la base de datos para el usuario proporcionado
        databaseReference.orderByChild("usuario").equalTo(usuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Itera sobre los nodos secundarios (ID de usuario)
                        Usuario usuarioDB = userSnapshot.getValue(Usuario.class);
                        if (usuarioDB != null && usuarioDB.getContraseña().equals(contraseña)) {
                            // Autenticación exitosa
                            Toast.makeText(MainActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();

                            // Obtener el ID del usuario
                            String userId = userSnapshot.getKey();

                            // Redirigir a la actividad de saldo y pasar el ID del usuario
                            Intent intent = new Intent(MainActivity.this, SaldoActivity.class);
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                            finish(); //
                            return; //
                        }
                    }


                    // Contraseña incorrecta
                    Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                } else {
                    // Usuario no encontrado
                    Toast.makeText(MainActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejo de errores, si es necesario
            }
        });
    }
}



