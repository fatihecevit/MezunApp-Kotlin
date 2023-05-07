package com.example.mezunapp

import AdapterMezun
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class frag_ListeMezunlar : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var adapter: AdapterMezun

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mezunlar_liste, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        database = FirebaseDatabase.getInstance().getReference("users")
        adapter = AdapterMezun(requireContext())
        recyclerView.adapter = adapter

        val listele = view.findViewById<Button>(R.id.listele)
        val filtre_adsoyad = view.findViewById<TextView>(R.id.filtre_adsoyad)
        val filtre_ulke = view.findViewById<TextView>(R.id.filtre_ulke)
        val filtre_sehir = view.findViewById<TextView>(R.id.filtre_sehir)
        val filtre_mezunyili = view.findViewById<TextView>(R.id.filtre_mezunyili)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemMezunList = ArrayList<item_MezunEdit>()

                val userId = FirebaseAuth.getInstance().uid

                for (userSnapshot in snapshot.children) {
                    val adSoyad = userSnapshot.child("adSoyad").value.toString()
                    val email = userSnapshot.child("email").value.toString()
                    val password = userSnapshot.child("password").value.toString()
                    val girisTarihi = userSnapshot.child("girisTarihi").value.toString()
                    val mezuniyetTarihi = userSnapshot.child("mezuniyetTarihi").value.toString()
                    val imageUrl = userSnapshot.child("imageUrl").value.toString()
                    val egitim = userSnapshot.child("egitim").value.toString()
                    val ulke = userSnapshot.child("ulke").value.toString()
                    val sehir = userSnapshot.child("sehir").value.toString()
                    val firma = userSnapshot.child("firma").value.toString()
                    val iletisim_mail = userSnapshot.child("iletisim_mail").value.toString()
                    val iletisim_telno = userSnapshot.child("iletisim_telno").value.toString()
                    val smedya_instagram = userSnapshot.child("smedya_instagram").value.toString()
                    val smedya_linkedin = userSnapshot.child("smedya_linkedin").value.toString()
                    val uid = userSnapshot.child("uid").value.toString()

                    val itemMezun = item_MezunEdit(
                        adSoyad, girisTarihi, mezuniyetTarihi, email, password,
                        egitim, ulke, sehir, firma, iletisim_mail, iletisim_telno,
                        smedya_instagram, smedya_linkedin, imageUrl
                    )

                    itemMezunList.add(itemMezun)


                }

                adapter.setMezunList(itemMezunList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Veri alınırken hata oluştuğunda yapılacak işlemler
            }
        })

        listele.setOnClickListener  {

            val filtre_adSoyad= filtre_adsoyad.text.toString().lowercase()
            val filtre_ulke=filtre_ulke.text.toString().lowercase()
            val filtre_sehir=filtre_sehir.text.toString().lowercase()
            val filtre_mezunyili=filtre_mezunyili.text.toString()

            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val itemMezunList = ArrayList<item_MezunEdit>()

                    for (userSnapshot in snapshot.children) {

                        val userId = userSnapshot.key.toString()
                        val adSoyad = userSnapshot.child("adSoyad").value.toString()
                        val email = userSnapshot.child("email").value.toString()
                        val password = userSnapshot.child("password").value.toString()
                        val girisTarihi = userSnapshot.child("girisTarihi").value.toString()
                        val mezuniyetTarihi = userSnapshot.child("mezuniyetTarihi").value.toString()
                        val imageUrl = userSnapshot.child("imageUrl").value.toString()
                        val egitim = userSnapshot.child("egitim").value.toString()
                        val ulke = userSnapshot.child("ulke").value.toString()
                        val sehir = userSnapshot.child("sehir").value.toString()
                        val firma = userSnapshot.child("firma").value.toString()
                        val iletisim_mail = userSnapshot.child("iletisim_mail").value.toString()
                        val iletisim_telno = userSnapshot.child("iletisim_telno").value.toString()
                        val smedya_instagram = userSnapshot.child("smedya_instagram").value.toString()
                        val smedya_linkedin = userSnapshot.child("smedya_linkedin").value.toString()

                        if(filtre_adSoyad in adSoyad.lowercase() && filtre_ulke in ulke.lowercase() && filtre_sehir in sehir.lowercase() && filtre_mezunyili in mezuniyetTarihi ) {
                            val itemMezun = item_MezunEdit(adSoyad,girisTarihi, mezuniyetTarihi,email,password,egitim,ulke,sehir,firma,iletisim_mail ,iletisim_telno,smedya_instagram,smedya_linkedin,imageUrl)
                            itemMezunList.add(itemMezun)
                        }
                    }
                    adapter.setMezunList(itemMezunList)
                }
                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
        return view
    }

}
