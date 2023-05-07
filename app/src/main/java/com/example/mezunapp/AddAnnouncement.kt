package com.example.mezunapp

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import item_Announcement
import java.io.IOException
import java.util.*

class AddAnnouncement : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference

    private lateinit var dImageView: ImageView
    private lateinit var metin_baslik: EditText
    private lateinit var metin_duyuru: EditText
    private lateinit var metin_sontarih: EditText
    private lateinit var upload_button: Button
    private var selectedUri: Uri? = null
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_announcement)

        dImageView = findViewById(R.id.uploadImage)
        metin_baslik = findViewById(R.id.metin_baslik)
        metin_duyuru = findViewById(R.id.metin_duyuru)
        metin_sontarih = findViewById(R.id.metin_sontarih)
        upload_button = findViewById(R.id.upload_button)
        database = FirebaseDatabase.getInstance()

        dImageView.setOnClickListener {
            openGallery()
        }
        upload_button.setOnClickListener {
            val baslik = metin_baslik.text.toString()
            val icerik = metin_duyuru.text.toString()
            val sontarih = metin_sontarih.text.toString()

            val pattern = Regex("^\\d{2}/\\d{2}/\\d{4}\$")
            val isMatch = pattern.matches(sontarih)

            if (baslik.isEmpty()) {
                metin_baslik.error = "Lüften başlık giriniz!"
            } else if (icerik.isEmpty()) {
                metin_duyuru.error = "Lüften duyuru metni giriniz!"
            }else if (icerik.isEmpty()) {
                metin_sontarih.error = "Lüften ilan için son tarih tarihinizi giriniz!"
            }
            else if (!isMatch) {
                metin_sontarih.error = "Lütfen gg/aa/yyyy formatında girin"
            }else{
                uploadMediaToFirebase()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                selectedUri = data.data!!
                try {
                    val source: ImageDecoder.Source = ImageDecoder.createSource(this.contentResolver, selectedUri!!
                    )
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    dImageView.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        selectImageLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }

    private fun uploadMediaToFirebase() {
       /* if (selectedUri == null) {
            Toast.makeText(this, "Lütfen bir resim seçiniz", Toast.LENGTH_SHORT).show()
            return
        }*/
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/announcement/$filename")

        ref.putFile(selectedUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveMediaToDatabase(downloadUri.toString())
                    selectedUri=null
                    dImageView.setImageResource(R.drawable.uploadimg)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload announcement", Toast.LENGTH_SHORT).show()
            }
    }


    private fun saveMediaToDatabase(mediaUrl: String) {

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = database.reference.child("users").child(userId)

            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val adSoyad = dataSnapshot.child("adSoyad").value as String?
                    val uidmedia = UUID.randomUUID().toString()
                    val uid = FirebaseAuth.getInstance().uid ?: return
                    val ref = FirebaseDatabase.getInstance().getReference("/announcement")
                    val baslik = metin_baslik.text.toString()
                    val icerik = metin_duyuru.text.toString()
                    val sontarih = metin_sontarih.text.toString()

                    val duyuru = item_Announcement(uid, uidmedia,mediaUrl,baslik,icerik,sontarih,adSoyad)
                    ref.child(uidmedia).setValue(duyuru)
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext, "Announcement uploaded successfully", Toast.LENGTH_SHORT).show()
                            val sıfırla = ""
                            val editable = Editable.Factory.getInstance().newEditable(sıfırla)
                            metin_baslik.text= editable
                            metin_duyuru.text= editable
                            metin_sontarih.text= editable
                            //finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext, "Announcement to save media to database", Toast.LENGTH_SHORT).show()
                        }


                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle cancelled event
                }
            })
        }
    }
}