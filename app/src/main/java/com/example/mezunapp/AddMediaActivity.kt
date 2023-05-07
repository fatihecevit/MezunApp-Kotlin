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
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mezunapp.databinding.ActivityAddMediaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import item_Media
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddMediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMediaBinding
    private var selectedUri: Uri? = null
    private lateinit var mmImageView: ImageView
    private lateinit var opencamera: Button
    private lateinit var chooseMedia_Button: Button
    private lateinit var uploadMedia_Button: Button
    private lateinit var database: FirebaseDatabase

    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var requestPermission: ActivityResultLauncher<String>

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_media)

        database = FirebaseDatabase.getInstance()
        mmImageView = findViewById(R.id.mmimageView)
        opencamera = findViewById(R.id.opencamera)
        chooseMedia_Button = findViewById(R.id.buttonChooseMedia)
        uploadMedia_Button = findViewById(R.id.buttonUpload)

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            mmImageView.setImageURI(uri)
        }

        takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                mmImageView.setImageURI(selectedUri)
            }
        }

        requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        opencamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                requestPermission.launch(Manifest.permission.CAMERA)
            }
        }

        chooseMedia_Button.setOnClickListener {
            openGallery()
        }

        uploadMedia_Button.setOnClickListener {
            uploadMediaToFirebase()
        }
    }
    private fun openCamera() {
        val photoFile = createImageFile()
        selectedUri = FileProvider.getUriForFile(this, "com.example.myapp.fileprovider", photoFile)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, selectedUri)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        takePicture.launch(selectedUri)
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
                selectedUri = data.data!!
                try {
                    val source: ImageDecoder.Source = ImageDecoder.createSource(this.contentResolver, selectedUri!!
                    )
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    mmImageView.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/* video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        selectImageLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }

    private fun uploadMediaToFirebase() {
        if (selectedUri == null) {
            Toast.makeText(this, "Lütfen bir resim seçiniz", Toast.LENGTH_SHORT).show()
            return
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/media/$filename")

        ref.putFile(selectedUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveMediaToDatabase(downloadUri.toString())
                    selectedUri=null
                    mmImageView.setImageResource(R.drawable.uploadimg)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload media", Toast.LENGTH_SHORT).show()
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
                    val ref = FirebaseDatabase.getInstance().getReference("/media")
                    val media = item_Media(uid, uidmedia,mediaUrl,adSoyad)
                    ref.child(uidmedia).setValue(media)
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext, "Media uploaded successfully", Toast.LENGTH_SHORT).show()
                            //finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext, "Failed to save media to database", Toast.LENGTH_SHORT).show()
                        }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle cancelled event
                }
            })
        }
    }
}

