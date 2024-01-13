package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SaldoActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView textViewSaldo;
    private EditText editTextRecarga;
    private Button btnRecargar;

    private String userId; // ID del usuario actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo);

        // Obtener el ID del usuario desde el Intent
        userId = getIntent().getStringExtra("userId");

        // Inicializar las vistas
        textViewSaldo = findViewById(R.id.textViewSaldo);
        editTextRecarga = findViewById(R.id.editTextRecargar);
        btnRecargar = findViewById(R.id.buttonRecargar);


        // Obtener referencia a la base de datos usando el ID del usuario
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios").child(userId);

        // Mostrar el saldo actual
        mostrarSaldoActual();

        // Configurar el click del botón de recarga
        btnRecargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recargarSaldo();
            }
        });
        // Configurar el click del botón de cerrar sesión
        Button btnCerrarSesion = findViewById(R.id.buttonCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
    }

    private void mostrarSaldoActual() {
        // Obtener el saldo actual de la base de datos y mostrarlo en el TextView
        databaseReference.child("Saldo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Obtener el saldo y mostrarlo en el TextView
                    double saldo = dataSnapshot.getValue(Double.class);
                    textViewSaldo.setText("Saldo Actual: $" + saldo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores, si es necesario
            }
        });
    }

    private void recargarSaldo() {
        String montoRecargaStr = editTextRecarga.getText().toString().trim();

        if (!montoRecargaStr.isEmpty()) {
            double montoRecarga = Double.parseDouble(montoRecargaStr);

            databaseReference.child("Saldo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        double saldoActual = dataSnapshot.getValue(Double.class);
                        double nuevoSaldo = saldoActual + montoRecarga;

                        // Agrega mensajes de registro para verificar qué parte se está ejecutando
                        Log.d("DEBUG", "Saldo actual: " + saldoActual);
                        Log.d("DEBUG", "Monto de recarga: " + montoRecarga);
                        Log.d("DEBUG", "Nuevo saldo: " + nuevoSaldo);

                        // Actualizar el saldo en la base de datos
                        databaseReference.child("Saldo").setValue(nuevoSaldo);

                        // Actualizar y mostrar el nuevo saldo en el TextView
                        textViewSaldo.setText("Saldo Actual: $" + nuevoSaldo);

                        // Mostrar mensaje de éxito
                        Toast.makeText(SaldoActivity.this, "Recarga exitosa. Nuevo saldo: $" + nuevoSaldo, Toast.LENGTH_SHORT).show();

                        // Limpiar el campo de recarga
                        editTextRecarga.setText("");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar errores, si es necesario
                    Log.e("ERROR", "Error al recargar saldo: " + databaseError.getMessage());
                }
            });
        } else {
            // Mostrar mensaje de error si el campo de recarga está vacío
            Toast.makeText(SaldoActivity.this, "Ingrese un monto de recarga", Toast.LENGTH_SHORT).show();
        }
    }
    private void cerrarSesion() {
        // Puedes agregar aquí cualquier lógica de cierre de sesión necesaria

        // Regresar a la actividad MainActivity
        Intent intent = new Intent(SaldoActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Cierra la actividad actual
    }
}



