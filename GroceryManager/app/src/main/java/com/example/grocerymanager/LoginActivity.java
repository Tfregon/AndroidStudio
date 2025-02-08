package com.example.grocerymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button buttonGoToGrocery; // Nome da variável corrigido

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializando o botão
        buttonGoToGrocery = findViewById(R.id.buttonLogin); // Deve corresponder ao ID do botão no XML

        // Evento de clique no botão Login
        buttonGoToGrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redireciona para a GroceryActivity
                Intent intent = new Intent(LoginActivity.this, GroceryActivity.class);
                startActivity(intent);
                finish(); // Fecha a tela de Login para evitar voltar pressionando "Voltar"
            }
        });
    }
}
