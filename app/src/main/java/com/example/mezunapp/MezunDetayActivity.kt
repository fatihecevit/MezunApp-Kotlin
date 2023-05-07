package com.example.mezunapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

private fun Bundle?.getParcelable(s: String, classLoader: ClassLoader?) {

}

class MezunDetayActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mezun_detay)

        val mezun = intent.getSerializableExtra("mezun", item_MezunEdit::class.java)
        val mezunKayit = mezun as item_MezunEdit

        val textViewAdSoyad = findViewById<TextView>(R.id.textViewAdSoyad)
        val textViewGirisTarihi = findViewById<TextView>(R.id.textViewGirisTarihi)
        val textViewMezuniyetTarihi = findViewById<TextView>(R.id.textViewMezuniyetTarihi)
        val textViewEgitim = findViewById<TextView>(R.id.textViewEgitim)
        val textViewUlke = findViewById<TextView>(R.id.textViewUlke)
        val textViewSehir = findViewById<TextView>(R.id.textViewSehir)
        val textViewFirma = findViewById<TextView>(R.id.textViewFirma)
        val textViewIletisimMail = findViewById<TextView>(R.id.textViewIletisimMail)
        val textViewIletisimTelNo = findViewById<TextView>(R.id.textViewIletisimTelNo)
        val textViewSmedyaInstagram = findViewById<TextView>(R.id.textViewSmedyaInstagram)
        val textViewSmedyaLinkedin = findViewById<TextView>(R.id.textViewSmedyaLinkedin)

        val sendmail = findViewById<Button>(R.id.sendmail)
        val subject: String = "MezunApp İletişim"
        val message:String="Merhaba Seni MezunApp'tan buldum. Nasılsın?"

        // TextView'lara verileri yerleştirme
        mezunKayit?.let {
            textViewAdSoyad.text = it.adSoyad
            textViewGirisTarihi.text = it.girisTarihi
            textViewMezuniyetTarihi.text = it.mezuniyetTarihi
            textViewEgitim.text = it.egitim
            textViewUlke.text = it.ulke
            textViewSehir.text = it.sehir
            textViewFirma.text = it.firma
            textViewIletisimMail.text = it.iletisim_mail
            textViewIletisimTelNo.text = it.iletisim_telno
            textViewSmedyaInstagram.text = it.smedya_instagram
            textViewSmedyaLinkedin.text = it.smedya_linkedin
        }
        val recipientEmailAddress = textViewIletisimMail.text.toString()
        sendmail.setOnClickListener(){
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmailAddress))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Konu")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Mesaj")
            emailIntent.setPackage("com.google.android.gm")
            startActivity(Intent.createChooser(emailIntent, "E-posta uygulaması seçin"))
        }
    }
}