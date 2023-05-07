package com.example.mezunapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class frag_EditProfile : Fragment() {

    private lateinit var database: FirebaseDatabase
    private val storage = Firebase.storage

    private lateinit var storageRef: StorageReference
    private lateinit var edit_Adsoyad: EditText
    private lateinit var edit_Giristarih: EditText
    private lateinit var edit_Mezuntarih: EditText
    private lateinit var edit_Egitim: Spinner
    private lateinit var edit_Ulkeler: Spinner
    private lateinit var edit_Sehir: EditText
    private lateinit var edit_Firma: EditText
    private lateinit var edit_MailBilgi: EditText
    private lateinit var edit_TelnoBilgi: EditText
    private lateinit var edit_Instagram: EditText
    private lateinit var edit_Linkedin: EditText

    private lateinit var edit_Button: Button
    private lateinit var changePassword: Button
    private lateinit var mImageView: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_frag__edit_profile, container, false)

        database = FirebaseDatabase.getInstance()
        storageRef = storage.reference

        edit_Adsoyad = view.findViewById(R.id.edit_adsoyad)
        edit_Giristarih = view.findViewById(R.id.edit_giristarih)
        edit_Mezuntarih = view.findViewById(R.id.edit_mezuntarih)
        edit_Egitim = view.findViewById(R.id.edit_spinner_egitim_bilgileri)
        edit_Ulkeler = view.findViewById(R.id.edit_spinner_ulkeler)
        edit_Sehir = view.findViewById(R.id.edit_sehir)
        edit_Firma = view.findViewById(R.id.edit_firma)
        edit_MailBilgi = view.findViewById(R.id.edit_mailbilgi)
        edit_TelnoBilgi = view.findViewById(R.id.edit_telnobilgi)
        edit_Instagram = view.findViewById(R.id.edit_instagram)
        edit_Linkedin = view.findViewById(R.id.edit_linkedin)


        edit_Button = view.findViewById(R.id.edit_button)
        changePassword = view.findViewById(R.id.Password)
        mImageView = view.findViewById(R.id.editImage)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = database.reference.child("users").child(userId)

            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    edit_Adsoyad.setText(dataSnapshot.child("adSoyad").value as String?)
                    edit_Giristarih.setText(dataSnapshot.child("girisTarihi").value as String?)
                    edit_Mezuntarih.setText(dataSnapshot.child("mezuniyetTarihi").value as String?)

                    if (context != null) {
                        val spinnerEgitim= resources.getStringArray(R.array.egitim_seviyeleri)
                        val PositionEgitim = spinnerEgitim.indexOf(dataSnapshot.child("egitim").value as String?)
                        edit_Egitim.setSelection(PositionEgitim)
                        val spinnerUlke = resources.getStringArray(R.array.ulkeler)
                        val PositionUlke = spinnerUlke.indexOf(dataSnapshot.child("ulke").value as String?)
                        edit_Ulkeler.setSelection(PositionUlke)
                    }

                    edit_Sehir.setText(dataSnapshot.child("sehir").value as String?)
                    edit_Firma.setText(dataSnapshot.child("firma").value as String?)
                    edit_MailBilgi.setText(dataSnapshot.child("iletisim_mail").value as String?)

                    edit_TelnoBilgi.setText(dataSnapshot.child("iletisim_telno").value as String?)
                    edit_Instagram.setText(dataSnapshot.child("smedya_instagram").value as String?)
                    edit_Linkedin.setText(dataSnapshot.child("smedya_linkedin").value as String?)

                    //Log.d("DataSnapshot", dataSnapshot.toString())

                    val imageUrl = dataSnapshot.child("imageUrl").value as String?
                    if (!imageUrl.isNullOrEmpty()) {
                        Picasso.get().load(imageUrl).into(mImageView)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }

        edit_Button.setOnClickListener {
            if (currentUser != null) {
                val userId = currentUser.uid
                val userRef = database.reference.child("users").child(userId)

                var adSoyad = edit_Adsoyad.text.toString()
                if(adSoyad==""){
                    adSoyad="-"
                }
                var giristarih = edit_Giristarih.text.toString()
                if(giristarih==""){
                    giristarih="-"
                }
                var mezuntarih = edit_Mezuntarih.text.toString()
                if(mezuntarih==""){
                    mezuntarih="-"
                }
                var egitim_bilgileri = edit_Egitim.selectedItem.toString()
                if(egitim_bilgileri==""){
                    egitim_bilgileri="-"
                }
                var ulke = edit_Ulkeler.selectedItem.toString()
                if(ulke==""){
                    ulke="-"
                }
                var sehir = edit_Sehir.text.toString()
                if(sehir==""){
                    sehir="-"
                }
                var firma = edit_Firma.text.toString()
                if(firma==""){
                    firma="-"
                }
                var mailBilgi = edit_MailBilgi.text.toString()
                if(mailBilgi==""){
                    mailBilgi="-"
                }
                var telnoBilgi = edit_TelnoBilgi.text.toString()
                if(telnoBilgi==""){
                    telnoBilgi="-"
                }
                var instagram = edit_Instagram.text.toString()
                if(instagram==""){
                    instagram="-"
                }
                var linkedin = edit_Linkedin.text.toString()
                if(linkedin==""){
                    linkedin="-"
                }


                userRef.child("adSoyad").setValue(adSoyad)
                userRef.child("giristarih").setValue(giristarih)
                userRef.child("mezuniyetTarihi").setValue(mezuntarih)
                userRef.child("egitim").setValue(egitim_bilgileri)
                userRef.child("ulke").setValue(ulke)
                userRef.child("sehir").setValue(sehir)
                userRef.child("firma").setValue(firma)
                userRef.child("iletisim_mail").setValue(mailBilgi)
                userRef.child("iletisim_telno").setValue(telnoBilgi)
                userRef.child("smedya_instagram").setValue(instagram)
                userRef.child("smedya_linkedin").setValue(linkedin)
            }
        }

        changePassword.setOnClickListener {
            val intent = Intent(activity, ChangePassword::class.java)
            startActivity(intent)
        }

        return view
    }
}
