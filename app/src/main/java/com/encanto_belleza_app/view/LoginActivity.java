package com.encanto_belleza_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.encanto_belleza_app.R;
import com.encanto_belleza_app.controller.UsuarioController;
import com.encanto_belleza_app.model.Usuario;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private UsuarioController usuarioController;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        
        usuarioController = new UsuarioController(this);
        
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Usuario usuario = usuarioController.iniciarSesion(email, password);
            if (usuario != null) {
                String mensajeBienvenida = String.format(getString(R.string.bienvenido), usuario.getNombre());
                Toast.makeText(this, mensajeBienvenida, Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(LoginActivity.this, CatalogoActivity.class);
                intent.putExtra("usuario_email", usuario.getEmail());
                intent.putExtra("usuario_nombre", usuario.getNombre());
                intent.putExtra("usuario_tipo", usuario.getTipo());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, R.string.error_credenciales, Toast.LENGTH_SHORT).show();
            }
        });
        
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }
}
