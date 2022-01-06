package com.sharkilver.lk.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sharkilver.lk.R;
import com.sharkilver.lk.ui.home.ModelCars;

import java.util.ArrayList;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemViewHolder> {

    private Context context; // point d'accroche
    private ArrayList<ModelCars> itemArrayList; // tableau qui va comprendre toutes les informations nécessaires

    public AdapterItem(Context context, ArrayList<ModelCars> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Accrocher la view - on caspule la vie
        View view = LayoutInflater.from(context).inflate(R.layout.item_cars_booking_recycler, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ModelCars currentItem = itemArrayList.get(position); // recuperer la position

        // recuperer les datas
        String idcar = currentItem.getIdcar();
        Double lat = currentItem.getLat();
        Double lng = currentItem.getLng();

        holder.tvIdcar.setText(idcar);
        holder.tvLat.setText(lat.toString());
        holder.tvLng.setText(lng.toString());



        /************************ GLIDE *************************************/
        // inserer glide pour afficher les images

        RequestOptions options = new RequestOptions() // gestion des erreurs
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher);


/**
        // si jarrive pas a connecter
        Context context = holder.ivimageView.getContext();
        Glide
                .with(context)// avec la vue
                .load(imageUrl) // mettre une url / uri / text
                .apply(options)
                .fitCenter() // recadre image
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivimageView); // emplacement ou afficher


        **/

        /************************ GLIDE *************************************/

    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivimageView;
        public TextView tvIdcar, tvLat, tvLng;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivimageView = itemView.findViewById(R.id.ivimageView);
            tvIdcar = itemView.findViewById(R.id.tvIdcar);
            tvLat = itemView.findViewById(R.id.tvLat);
            tvLng = itemView.findViewById(R.id.tvLng);
        }
    }

}
