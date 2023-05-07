package com.example.mezunapp
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var login_Mail: EditText
    private lateinit var login_Password: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login_Button = findViewById<Button>(R.id.login_button)
        val signupRedirectText = findViewById<TextView>(R.id.signupRedirectText)
        login_Mail = findViewById(R.id.login_email)
        login_Password = findViewById(R.id.login_password)

        firebaseAuth = FirebaseAuth.getInstance()

        signupRedirectText.setOnClickListener  {
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
        }
        login_Mail.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_Q && event.isAltPressed) {
                login_Mail.text?.append("@")
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        login_Button.setOnClickListener  {

            val email = login_Mail.text.toString()
            val password =login_Password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if (it.isSuccessful){
                        val intent = Intent(this, AnaEkran::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

    }
}