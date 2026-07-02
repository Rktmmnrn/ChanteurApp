package com.eni.chanteurapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eni.chanteurapp.database.DBHelper;
import com.eni.chanteurapp.model.Chanteur;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private DBHelper        dbHelper;
    private ChanteurAdapter adapter;
    private TextView        tvAucunResultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("Rechercher un chanteur");

        dbHelper        = new DBHelper(this);
        EditText etRecherche    = findViewById(R.id.etRecherche);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSearch);
        tvAucunResultat = findViewById(R.id.tvAucunResultat);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter initialisé avec liste vide
        adapter = new ChanteurAdapter(this, dbHelper.searchChanteurs(""),
            new ChanteurAdapter.OnChanteurClickListener() {
                @Override
                public void onItemClick(Chanteur chanteur) {
                    // Ouvrir AddEditActivity en mode modification
                    Intent intent = new Intent(SearchActivity.this, AddEditActivity.class);
                    intent.putExtra("id", chanteur.getIdchant());
                    startActivity(intent);
                }

                @Override
                public void onDeleteClick(Chanteur chanteur) {
                    // Boîte de confirmation avant suppression
                    new AlertDialog.Builder(SearchActivity.this)
                        .setTitle("Confirmer la suppression")
                        .setMessage("Êtes-vous sûr de vouloir supprimer " + chanteur.getNom() + " ?")  
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Supprimer", (dialog, which) -> {
                            dbHelper.deleteChanteur(chanteur.getIdchant());
                            Toast.makeText(SearchActivity.this, "✅ Chanteur supprimé", Toast.LENGTH_SHORT).show();
                            lancerRecherche(etRecherche.getText().toString());
                        })
                        .setNegativeButton("Annuler", null)
                        .show();
                }
            }
        );

        recyclerView.setAdapter(adapter);
        afficherResultats(dbHelper.searchChanteurs(""));

        // TextWatcher : recherche en temps réel à chaque lettre tapée
        etRecherche.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lancerRecherche(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void lancerRecherche(String keyword) {
        List<Chanteur> resultats = dbHelper.searchChanteurs(keyword);
        afficherResultats(resultats);
    }

    private void afficherResultats(List<Chanteur> resultats) {
        adapter.updateListe(resultats);
        if (resultats.isEmpty()) {
            tvAucunResultat.setVisibility(View.VISIBLE);
        } else {
            tvAucunResultat.setVisibility(View.GONE);
        }
    }
}