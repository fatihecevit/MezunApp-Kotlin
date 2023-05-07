package com.example.mezunapp
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import item_Announcement

class AdapterAnnouncement (private val context: Context): RecyclerView.Adapter<AdapterAnnouncement.AnnouncementViewHolder>() {

    private var itemAnnouncementList = ArrayList<item_Announcement>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_announcement, parent, false)
        return AnnouncementViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val announcement = itemAnnouncementList[position]

        holder.r_baslik.text = announcement.baslik
        holder.r_adSoyad.text = announcement.adSoyad
        holder.r_icerik.text = announcement.icerik

        val imageUrl = announcement.imageurl

        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get().load(imageUrl).into(holder.r_fotoImageView)
        }
    }

    override fun getItemCount(): Int {
        return itemAnnouncementList.size
    }

    fun setAnnouncementList(itemAnnouncementList: ArrayList<item_Announcement>) {
        this.itemAnnouncementList = itemAnnouncementList
        notifyDataSetChanged()
    }

    inner class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val r_fotoImageView: ImageView = itemView.findViewById(R.id.r_fotoImageView)
        val r_baslik: TextView = itemView.findViewById(R.id.r_baslik)
        val r_adSoyad: TextView = itemView.findViewById(R.id.r_adSoyad)
        val r_icerik: TextView = itemView.findViewById(R.id.r_icerik)
    }
}

