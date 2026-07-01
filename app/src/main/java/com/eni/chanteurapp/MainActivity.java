package com.eni.chanteurapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eni.chanteurapp.database.DBHelper;
import com.eni.chanteurapp.model.Chanteur;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBHelper       dbHelper;
    private ChanteurAdapter adapter;
    private RecyclerView   recyclerView;
    private TextView       tvVide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation de la base de données
        dbHelper = new DBHelper(this);

        // Récupération des vues depuis le XML
        recyclerView       = findViewById(R.id.recyclerView);
        tvVide             = findViewById(R.id.tvVide);
        Button btnAjouter  = findViewById(R.id.btnAjouter);
        Button btnRecherche = findViewById(R.id.btnRecherche);

        // Configuration du RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Création de l'adapter avec les callbacks de clic
        adapter = new ChanteurAdapter(this, dbHelper.getAllChanteurs(),
            new ChanteurAdapter.OnChanteurClickListener() {
                @Override
                public void onItemClick(Chanteur chanteur) {
                    // Ouvrir AddEditActivity en mode "modification"
                    Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                    intent.putExtra("id", chanteur.getIdchant());
                    startActivity(intent);
                }

                @Override
                public void onDeleteClick(Chanteur chanteur) {
                    // Demander confirmation avant de supprimer
                    new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Supprimer " + chanteur.getNom() + " ?")
                        .setPositiveButton("Supprimer", (dialog, which) -> {
                            dbHelper.deleteChanteur(chanteur.getIdchant());
                            rafraichirListe();
                        })
                        .setNegativeButton("Annuler", null)
                        .show();
                }
            }
        );

        recyclerView.setAdapter(adapter);

        // Bouton Ajouter → ouvrir AddEditActivity en mode "ajout" (sans id)
        btnAjouter.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditActivity.class);
            startActivity(intent);
        });

        // Bouton Recherche → ouvrir SearchActivity
        btnRecherche.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });

        rafraichirListe();
    }

    // Appelée à chaque retour sur cet écran (après ajout/modification)
    @Override
    protected void onResume() {
        super.onResume();
        rafraichirListe();
    }

    // Met à jour la liste et gère l'affichage du message "liste vide"
    private void rafraichirListe() {
        List<Chanteur> liste = dbHelper.getAllChanteurs();
        adapter.updateListe(liste);
        if (liste.isEmpty()) {
            tvVide.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvVide.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}