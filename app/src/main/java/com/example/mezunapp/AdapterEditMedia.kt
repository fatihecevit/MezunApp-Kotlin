
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mezunapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class AdapterEditMedia : RecyclerView.Adapter<AdapterEditMedia.EditMediaViewHolder>() {

    private var mediaList = ArrayList<item_Media>()
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditMediaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_editmedia, parent, false)
        return EditMediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: EditMediaViewHolder, position: Int) {
        val media = mediaList[position]
        val imageUrl = media.mediaUrl
        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get().load(imageUrl).into(holder.imageView)
        }

        holder.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Silme işlemini onaylıyor musunuz?")
                .setPositiveButton("Evet") { dialog, which ->
                    val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
                    storageRef.delete().addOnSuccessListener {
                        val dbRef = Firebase.database.reference.child("media").child(media.uidmedia)
                        dbRef.removeValue().addOnSuccessListener {
                           /* if (mediaList.size > 0) { // mediaList boş değilse silme işlemini gerçekleştir
                                mediaList.removeAt(position)
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position, mediaList.size)
                            }*/
                            // Veritabanından güncel listeyi tekrar çek ve RecyclerView'e set et
                            Firebase.database.reference.child("media")
                                .get()
                                .addOnSuccessListener { dataSnapshot ->
                                    var updatedMediaList = ArrayList<item_Media>()
                                    for (data in dataSnapshot.children) {

                                        val mediaItem = data.getValue(item_Media::class.java)
                                        val userId = FirebaseAuth.getInstance().uid
                                        val uid = mediaItem?.uid.toString()

                                        if (uid == userId) {
                                            mediaItem!!.uidmedia = data.key.toString()
                                            updatedMediaList.add(mediaItem!!)
                                        }

                                    }
                                    setMediaList(updatedMediaList)
                                }
                                .addOnFailureListener {
                                    // Hata oluşursa kullanıcıya bildir
                                    Toast.makeText(holder.itemView.context, "Hata: Veritabanı güncellenemedi", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }
                .setNegativeButton("Hayır") { dialog, which ->
                    // Kullanıcı hayır dediği zaman hiçbir işlem yapma
                }
                .show()
        }


        holder.itemView.setOnClickListener {
            listener?.onItemClick(media)
        }
    }

    override fun getItemCount() = mediaList.size

    interface OnItemClickListener {
        fun onItemClick(itemMedia: item_Media)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    fun setMediaList(mediaList: ArrayList<item_Media>) {
        this.mediaList = mediaList
        notifyDataSetChanged()
    }

    class EditMediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }
}

