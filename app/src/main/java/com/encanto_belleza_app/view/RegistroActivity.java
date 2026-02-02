package com.encanto_belleza_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.encanto_belleza_app.R;
import com.encanto_belleza_app.controller.UsuarioController;

public class RegistroActivity extends AppCompatActivity {
    private EditText etNombre, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister, btnLogin;
    private UsuarioController usuarioController;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        
        usuarioController = new UsuarioController(this);
        
        btnRegister.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            
            // Validaciones
            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, R.string.error_campos, Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (password.length() < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Registrar usuario
            boolean registrado = usuarioController.registrarUsuario(email, password, nombre);
            if (registrado) {
                Toast.makeText(this, R.string.registro_exitoso, Toast.LENGTH_SHORT).show();
                
                // Iniciar sesión automáticamente
                Intent intent = new Intent(RegistroActivity.this, CatalogoActivity.class);
                intent.putExtra("usuario_email", email);
                intent.putExtra("usuario_nombre", nombre);
                intent.putExtra("usuario_tipo", "cliente");
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, R.string.error_registro, Toast.LENGTH_SHORT).show();
            }
        });
        
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
