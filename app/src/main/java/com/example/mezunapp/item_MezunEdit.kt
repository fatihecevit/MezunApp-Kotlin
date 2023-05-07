package com.example.mezunapp

/*class item_MezunEdit (
    val adSoyad: String,
    val girisTarihi: String,
    val mezuniyetTarihi: String,
    val email: String,
    val password: String,
    val egitim: String= "",
    val ulke: String= "-",
    val sehir: String= "-",
    val firma: String= "-",
    val iletisim_mail: String= "-",
    val iletisim_telno: String= "-",
    val smedya_instagram: String= "-",
    val smedya_linkedin: String= "-",
    var imageUrl: String = "-",
)*/
import java.io.Serializable

class item_MezunEdit (
    val adSoyad: String,
    val girisTarihi: String,
    val mezuniyetTarihi: String,
    val email: String,
    val password: String,
    val egitim: String= "",
    val ulke: String= "-",
    val sehir: String= "-",
    val firma: String= "-",
    val iletisim_mail: String= "-",
    val iletisim_telno: String= "-",
    val smedya_instagram: String= "-",
    val smedya_linkedin: String= "-",
    var imageUrl: String = "-",
) : Serializable {
    // ...
}
