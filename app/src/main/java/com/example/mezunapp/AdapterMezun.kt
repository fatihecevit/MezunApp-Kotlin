import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mezunapp.MezunDetayActivity
import com.example.mezunapp.R
import com.example.mezunapp.item_MezunEdit
import com.squareup.picasso.Picasso

class AdapterMezun (private val context: Context): RecyclerView.Adapter<AdapterMezun.MezunViewHolder>() {

    private var itemMezunList = ArrayList<item_MezunEdit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MezunViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_mezun, parent, false)
        return MezunViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MezunViewHolder, position: Int) {
        val mezun = itemMezunList[position]

        holder.adSoyadTextView.text = mezun.adSoyad
        holder.emailTextView.text = mezun.iletisim_mail
        holder.girisTextView.text = mezun.girisTarihi
        holder.mezuniyetTextView.text = mezun.mezuniyetTarihi

        val imageUrl = mezun.imageUrl
        print(imageUrl)

        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get().load(imageUrl).into(holder.fotoImageView)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MezunDetayActivity::class.java)
            intent.putExtra("mezun", mezun)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemMezunList.size
    }

    fun setMezunList(itemMezunList: ArrayList<item_MezunEdit>) {
        this.itemMezunList = itemMezunList
        notifyDataSetChanged()
    }

    inner class MezunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val adSoyadTextView: TextView = itemView.findViewById(R.id.adSoyadTextView)
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        val girisTextView: TextView = itemView.findViewById(R.id.girisTextView)
        val mezuniyetTextView: TextView = itemView.findViewById(R.id.mezuniyetTextView)
        val fotoImageView: ImageView = itemView.findViewById(R.id.fotoImageView)
    }
}

