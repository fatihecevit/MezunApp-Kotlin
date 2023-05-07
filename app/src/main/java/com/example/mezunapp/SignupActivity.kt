package com.example.mezunapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class SignupActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signup_Adsoyad: EditText
    private lateinit var signup_Giristarih: EditText
    private lateinit var signup_Mezuntarih: EditText
    private lateinit var signup_Email: EditText
    private lateinit var signup_Password: EditText
    private lateinit var signup_Confirm: EditText
    private lateinit var signup_Button: Button
    private lateinit var openGalery: Button
    private lateinit var openCamera: Button
    private lateinit var mImageView: ImageView
    private lateinit var imageUri: Uri
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var requestPermission: ActivityResultLauncher<String>


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        signup_Adsoyad = findViewById(R.id.signup_adsoyad)
        signup_Giristarih = findViewById(R.id.signup_giristarih)
        signup_Mezuntarih = findViewById(R.id.signup_mezuntarih)
        signup_Email = findViewById(R.id.signup_email)
        signup_Password = findViewById(R.id.signup_password)
        signup_Confirm = findViewById(R.id.signup_confirm)
        signup_Button = findViewById(R.id.signup_button)
        openGalery = findViewById(R.id.openGalery)
        openCamera = findViewById(R.id.openCamera)
        mImageView = findViewById(R.id.uploadImage)

        dbRef = FirebaseDatabase.getInstance().getReference("users")
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference
        signup_Email.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            mImageView.setImageURI(uri)
        }

        takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                mImageView.setImageURI(imageUri)
            }
        }

        requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        openCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                requestPermission.launch(Manifest.permission.CAMERA)
            }
        }
        signup_Button.setOnClickListener {
            saveEmployeeData()
        }

        openGalery.setOnClickListener {
            selectImage()
        }
    }
    private fun openCamera() {
        val photoFile = createImageFile()
        imageUri = FileProvider.getUriForFile(this, "com.example.myapp.fileprovider", photoFile)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        takePicture.launch(imageUri)
    }
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    companion object {
        private var currentPhotoPath: String? = null
    }
    @RequiresApi(Build.VERSION_CODES.P)
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                imageUri = data.data!!
                try {
                    val source: ImageDecoder.Source = ImageDecoder.createSource(this.contentResolver, imageUri)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    mImageView.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        selectImageLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }

    private fun uploadImage(userId: String, onSuccess: (imageUrl: String) -> Unit) {
        val fileName = userId + ".jpg" // .jpg uzantısını resim adına ekle
        val ref: StorageReference = storageReference.child("users/$fileName")
        ref.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                ref.downloadUrl.addOnSuccessListener { uri ->
                    // Handle successful upload and get download URL
                    val imageUrl = uri.toString() // resim url'sini al
                    onSuccess(imageUrl)
                }
            }
            .addOnFailureListener { exception ->
                // Handle unsuccessful upload
            }
    }
    private fun saveEmployeeData() {
        //getting values
        val Adsoyad = signup_Adsoyad.text.toString()
        val Giristarih = signup_Giristarih.text.toString()
        val Mezuntarih = signup_Mezuntarih.text.toString()
        val Email = signup_Email.text.toString()
        val Password = signup_Password.text.toString()
        val Confirm = signup_Confirm.text.toString()

        if (Adsoyad.isEmpty()) {
            signup_Adsoyad.error = "Lüften isim-soyisminizi giriniz!"
        } else if (Giristarih.isEmpty()) {
            signup_Giristarih.error = "Lüften okula giriş tarihinizi giriniz!"
        } else if (Mezuntarih.isEmpty()) {
            signup_Mezuntarih.error = "Lüften okuldan mezun tarihinizi giriniz!"
        } else if (Email.isEmpty()) {
            signup_Email.error = "Lüften mail hesabı giriniz!"
        } else if (Password.isEmpty()) {
            signup_Password.error = "Lüften şifre giriniz!"
        } else if (Confirm.isEmpty()) {
            signup_Confirm.error = "Lüften şifrenizi tekrar girin kısmını doldurun!"
        } else {
            if (Password == Confirm) {
                firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userId = FirebaseAuth.getInstance().currentUser!!.uid
                        val users = item_MezunEdit(Adsoyad, Giristarih, Mezuntarih, Email, Password)
                        uploadImage(userId) { imageUrl ->
                            users.imageUrl = imageUrl // resim URL'sini kaydet
                            dbRef.child(userId).setValue(users).addOnCompleteListener {
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                Toast.makeText(this, "Başarı ile kayıt oldunuz", Toast.LENGTH_LONG).show()
                            }.addOnFailureListener { err ->
                                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Password does not matched", Toast.LENGTH_SHORT).show()
            }
        }
    }


}