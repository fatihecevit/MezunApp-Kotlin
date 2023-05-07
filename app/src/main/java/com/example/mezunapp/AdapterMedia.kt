import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mezunapp.R
import com.squareup.picasso.Picasso

class AdapterMedia :RecyclerView.Adapter<AdapterMedia.MediaViewHolder>() {

    private var mediaList = ArrayList<item_Media>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_media, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val mediaItem = mediaList[position]

        val media = mediaList[position]

        holder.adSoyadTextView.text = media.adSoyad

        val imageUrl = media.mediaUrl

        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get().load(imageUrl).into(holder.imageView)
        }
    }

    override fun getItemCount() = mediaList.size

    interface OnItemClickListener {
        fun onItemClick(itemMedia: item_Media)
    }
    fun setMezunList(mediaList: ArrayList<item_Media>) {
        this.mediaList = mediaList
        notifyDataSetChanged()
    }

    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val adSoyadTextView: TextView = itemView.findViewById(R.id.text_view_ad_soyad)
    }
}
