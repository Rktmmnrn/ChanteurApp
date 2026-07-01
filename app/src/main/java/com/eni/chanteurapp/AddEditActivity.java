package com.eni.chanteurapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.eni.chanteurapp.database.DBHelper;
import com.eni.chanteurapp.model.Chanteur;

public class AddEditActivity extends AppCompatActivity {

    private EditText  etNom, etDatenais;
    private ImageView imgPhoto;
    private String    photoUri = "";
    private DBHelper  dbHelper;
    private int       chanteurId = -1; // -1 = mode ajout, sinon = mode modification

    // Launcher moderne pour ouvrir la galerie et récupérer le résultat
    private final ActivityResultLauncher<Intent> pickImageLauncher =
        registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        // Demander la permission de lire ce fichier durablement
                        getContentResolver().takePersistableUriPermission(
                            uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );
                        photoUri = uri.toString();
                        imgPhoto.setImageURI(uri);
                    }
                }
            }
        );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        dbHelper  = new DBHelper(this);
        etNom     = findViewById(R.id.etNom);
        etDatenais = findViewById(R.id.etDatenais);
        imgPhoto  = findViewById(R.id.imgPhoto);
        Button btnEnregistrer = findViewById(R.id.btnEnregistrer);

        // Récupère l'id passé depuis MainActivity (si mode modification)
        chanteurId = getIntent().getIntExtra("id", -1);

        if (chanteurId != -1) {
            // Mode modification : on pré-remplit les champs
            setTitle("Modifier le chanteur");
            Chanteur chanteur = dbHelper.getChanteurById(chanteurId);
            if (chanteur != null) {
                etNom.setText(chanteur.getNom());
                etDatenais.setText(chanteur.getDatenais());
                photoUri = chanteur.getPhoto();
                if (photoUri != null && !photoUri.isEmpty()) {
                    imgPhoto.setImageURI(Uri.parse(photoUri));
                }
            }
        } else {
            // Mode ajout
            setTitle("Ajouter un chanteur");
        }

        // Clic sur la photo → ouvrir la galerie
        imgPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pickImageLauncher.launch(intent);
        });

        // Clic sur Enregistrer
        btnEnregistrer.setOnClickListener(v -> sauvegarder());
    }

    private void sauvegarder() {
        String nom      = etNom.getText().toString().trim();
        String datenais = etDatenais.getText().toString().trim();

        // Validation : le nom est obligatoire
        if (nom.isEmpty()) {
            etNom.setError("Le nom est obligatoire");
            etNom.requestFocus();
            return;
        }

        if (chanteurId != -1) {
            // Mode modification → UPDATE
            Chanteur chanteur = new Chanteur(chanteurId, nom, datenais, photoUri);
            dbHelper.updateChanteur(chanteur);
            Toast.makeText(this, "Chanteur modifié ✅", Toast.LENGTH_SHORT).show();
        } else {
            // Mode ajout → INSERT
            Chanteur chanteur = new Chanteur(nom, datenais, photoUri);
            dbHelper.addChanteur(chanteur);
            Toast.makeText(this, "Chanteur ajouté ✅", Toast.LENGTH_SHORT).show();
        }

        finish(); // Ferme cet écran et revient à MainActivity
    }
}