package com.example.redevi
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonSignUp: Button
    private lateinit var checkBoxRememberMe: CheckBox
    private lateinit var checkBoxShowPassword: CheckBox
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextEmail = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe)
        checkBoxShowPassword = findViewById(R.id.checkBoxShowPassword)

        auth = FirebaseAuth.getInstance()

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInWithEmailAndPassword(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        buttonSignUp.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                createAccountWithEmailAndPassword(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        checkBoxShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Mostrar contraseña
                editTextPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                // Ocultar contraseña
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // El inicio de sesión fue exitoso, puedes redirigir al usuario a otra actividad
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                } else {
                    // Ocurrió un error en el inicio de sesión
                    Toast.makeText(
                        this,
                        "Login failed. Invalid email or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun createAccountWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // La cuenta se creó correctamente, puedes redirigir al usuario a otra actividad
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                } else {
                    // Ocurrió un error al crear la cuenta
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        // El usuario ya existe
                        Toast.makeText(
                            this,
                            "An account with this email already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Otro tipo de error
                        Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}

