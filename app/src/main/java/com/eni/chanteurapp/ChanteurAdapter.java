package com.eni.chanteurapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eni.chanteurapp.model.Chanteur;

import java.util.List;

public class ChanteurAdapter extends RecyclerView.Adapter<ChanteurAdapter.ChanteurViewHolder> {

    // Interface pour communiquer les clics vers MainActivity
    public interface OnChanteurClickListener {
        void onItemClick(Chanteur chanteur);        // modifier
        void onDeleteClick(Chanteur chanteur);      // supprimer
    }

    private Context context;
    private List<Chanteur> chanteurs;
    private OnChanteurClickListener listener;

    public ChanteurAdapter(Context context, List<Chanteur> chanteurs, OnChanteurClickListener listener) {
        this.context   = context;
        this.chanteurs = chanteurs;
        this.listener  = listener;
    }

    // Crée une nouvelle vue pour un item (appelée par RecyclerView quand nécessaire)
    @NonNull
    @Override
    public ChanteurViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
            .inflate(R.layout.item_chanteur, parent, false);
        return new ChanteurViewHolder(view);
    }

    // Remplit la vue avec les données du chanteur à la position donnée
    @Override
    public void onBindViewHolder(@NonNull ChanteurViewHolder holder, int position) {
        Chanteur chanteur = chanteurs.get(position);

        holder.tvNom.setText(chanteur.getNom());
        holder.tvDatenais.setText(
            chanteur.getDatenais() != null && !chanteur.getDatenais().isEmpty()
                ? chanteur.getDatenais()
                : "Date inconnue"
        );

        // Affiche la photo si elle existe, sinon garde l'image par défaut
        if (chanteur.getPhoto() != null && !chanteur.getPhoto().isEmpty()) {
            holder.imgPhoto.setImageURI(Uri.parse(chanteur.getPhoto()));
        } else {
            holder.imgPhoto.setImageResource(R.drawable.ic_launcher_foreground);
        }

        // Clic sur la carte → ouvrir l'écran de modification
        holder.itemView.setOnClickListener(v -> listener.onItemClick(chanteur));

        // Clic sur le bouton supprimer
        holder.btnSupprimer.setOnClickListener(v -> listener.onDeleteClick(chanteur));
    }

    @Override
    public int getItemCount() {
        return chanteurs.size();
    }

    // Méthode pour mettre à jour la liste depuis MainActivity
    public void updateListe(List<Chanteur> nouvelleListe) {
        this.chanteurs = nouvelleListe;
        notifyDataSetChanged();
    }

    // ViewHolder — représente une seule ligne de la liste
    // Il "retient" les références aux vues pour éviter des appels répétés à findViewById()
    static class ChanteurViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView  tvNom;
        TextView  tvDatenais;
        Button    btnSupprimer;

        public ChanteurViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto    = itemView.findViewById(R.id.imgPhoto);
            tvNom       = itemView.findViewById(R.id.tvNom);
            tvDatenais  = itemView.findViewById(R.id.tvDatenais);
            btnSupprimer = itemView.findViewById(R.id.btnSupprimer);
        }
    }
}