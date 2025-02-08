package com.example.grocerymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class GroceryActivity extends AppCompatActivity {

    private EditText editTextGroceryName;
    private Spinner spinnerCategory;
    private Button buttonSave, buttonDelete;
    private ListView listViewGroceries;

    private DatabaseHelper databaseHelper;
    private ArrayList<String> groceryList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private HashMap<String, String> itemCategoryMap = new HashMap<>();
    private String selectedItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery);

        editTextGroceryName = findViewById(R.id.editTextGroceryName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);
        listViewGroceries = findViewById(R.id.listViewGroceries);

        databaseHelper = new DatabaseHelper(this);

        // Configurar o Spinner com categorias
        String[] categories = {"Fruits", "Dairy", "Vegetables", "Meat", "Beverages", "Other"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(spinnerAdapter);

        // Configurar ListView com os itens armazenados no banco de dados
        loadGroceryItems();

        // Botão Save - Adiciona ou Atualiza um item
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = editTextGroceryName.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();

                if (itemName.isEmpty()) {
                    Toast.makeText(GroceryActivity.this, "Enter grocery item name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (databaseHelper.saveItem(itemName, category)) {
                    Toast.makeText(GroceryActivity.this, "Item saved!", Toast.LENGTH_SHORT).show();
                    loadGroceryItems();
                } else {
                    Toast.makeText(GroceryActivity.this, "Error saving item", Toast.LENGTH_SHORT).show();
                }
                editTextGroceryName.setText("");
                selectedItem = null;
            }
        });

        // Botão Delete - Remove um item
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItem != null) {
                    if (databaseHelper.deleteItem(selectedItem.split("\n")[0])) {
                        Toast.makeText(GroceryActivity.this, "Item deleted!", Toast.LENGTH_SHORT).show();
                        loadGroceryItems();
                    } else {
                        Toast.makeText(GroceryActivity.this, "Error deleting item", Toast.LENGTH_SHORT).show();
                    }
                    selectedItem = null;
                } else {
                    Toast.makeText(GroceryActivity.this, "Select an item to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Seleção de item no ListView
        listViewGroceries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = groceryList.get(position);
                editTextGroceryName.setText(selectedItem.split("\n")[0]);
            }
        });
    }

    // Método para carregar os itens do SQLite
    private void loadGroceryItems() {
        groceryList.clear();
        itemCategoryMap.clear();

        ArrayList<String[]> items = databaseHelper.getAllItems();
        for (String[] item : items) {
            String name = item[0];
            String category = item[1];
            String displayItem = name + "\n" + category;
            groceryList.add(displayItem);
            itemCategoryMap.put(displayItem, category);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, groceryList);
        listViewGroceries.setAdapter(adapter);
    }
}
