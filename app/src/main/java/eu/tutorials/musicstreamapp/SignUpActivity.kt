package eu.tutorials.musicstreamapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import eu.tutorials.musicstreamapp.databinding.ActivitySignUpBinding
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    lateinit var binding :ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createAccountBts.setOnClickListener {

            val email = binding.emailEdittext.text.toString()
            val password = binding.passwordEdittext.text.toString()
            val confirmPassword = binding.confirmPasswordEdittext.text.toString()

           if(! Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(),email)){
               binding.emailEdittext.setError("Invalid Email")
               return@setOnClickListener
           }

            if(password.length < 6){
                binding.passwordEdittext.setError("Lenth should be 6 letter or character")
                return@setOnClickListener
            }

            if(!password.equals(confirmPassword)){
                binding.confirmPasswordEdittext.setError("Password not matched")
                return@setOnClickListener

                }


            createAccountWithFirebase(email,password)


        }


        binding.goToLoginBtn.setOnClickListener {
            finish()
        }
    }


    fun createAccountWithFirebase(email : String,password: String){
        setInProgress(true)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                setInProgress(false)
                Toast.makeText(applicationContext,"User created successfully",Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                setInProgress(false)
                Toast.makeText(applicationContext,"Create account failed",Toast.LENGTH_SHORT).show()
            }
    }



    fun setInProgress(inProgress : Boolean ){
        if(inProgress){
            binding.createAccountBts.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.createAccountBts.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    }