package com.olite.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.olite.app.R
import com.olite.app.databinding.ItemBannerBinding

class BannerAdapter(private val cantidad: Int = 3) :
    RecyclerView.Adapter<BannerAdapter.BannerVH>() {

    // Lista de imágenes locales del banner
    private val imagenes = listOf(
        R.drawable.banner1,
        R.drawable.banner2,
        R.drawable.banner3
    )

    inner class BannerVH(val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerVH {
        val binding = ItemBannerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BannerVH(binding)
    }

    override fun onBindViewHolder(holder: BannerVH, position: Int) {
        holder.binding.imgBanner.setImageResource(imagenes[position])
    }

    override fun getItemCount(): Int = imagenes.size
}