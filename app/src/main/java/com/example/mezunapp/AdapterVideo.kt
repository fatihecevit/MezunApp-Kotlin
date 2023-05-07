import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.mezunapp.R

class AdapterVideo : RecyclerView.Adapter<AdapterVideo.MediaViewHolder>() {

    private var mediaList = ArrayList<item_Media>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val media = mediaList[position]

        holder.adSoyadTextView.text = media.adSoyad

        val videoUrl = media.mediaUrl

        if (!videoUrl.isNullOrEmpty()) {
            holder.videoView.setVideoURI(Uri.parse(videoUrl))
            holder.videoView.setOnPreparedListener {
                it.isLooping = true
                it.start()
            }
        }
    }

    override fun getItemCount() = mediaList.size


    fun setVideoList(mediaList: ArrayList<item_Media>) {
        this.mediaList = mediaList
        notifyDataSetChanged()
    }


    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoView: VideoView = itemView.findViewById(R.id.video_view)
        val adSoyadTextView: TextView = itemView.findViewById(R.id.text_view_ad_soyad)
    }
}
