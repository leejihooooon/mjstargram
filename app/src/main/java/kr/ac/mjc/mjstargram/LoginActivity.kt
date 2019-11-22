package kr.ac.mjc.mjstargram

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity:AppCompatActivity(){

    lateinit var loadingPb:ProgressBar
    lateinit var emailEt:EditText
    lateinit var passwordEt:EditText
    lateinit var emailLoginBtn:Button

    lateinit var auth:FirebaseAuth
    lateinit var firestore:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loadingPb=findViewById(R.id.loading_pb)
        emailEt=findViewById(R.id.email_et)
        passwordEt=findViewById(R.id.password_et)
        emailLoginBtn=findViewById(R.id.email_login_btn)
        auth= FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()

        emailLoginBtn.setOnClickListener {
            emailLogin()
        }
        moveMain(auth.currentUser)
    }
    fun emailLogin(){
        var email=emailEt.text.toString()
        var password=passwordEt.text.toString()
        if (email.equals("")){
            Toast.makeText(this,"이메일을 입력해라",Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length<6) {
            Toast.makeText(this, "패스워드를 6자 이상 입력해라", Toast.LENGTH_SHORT).show()
            return
        }
        startLoading()
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                task ->
                endLoading()
                if(task.isSuccessful){
                    var user=User()
                    user.email=auth.currentUser?.email
                    startLoading()
                    firestore.collection("User").document(user.email!!).set(user)
                        .addOnSuccessListener {
                            endLoading()
                            moveMain(auth.currentUser)
                        }

                }
                else if (task.exception?.message.isNullOrEmpty()){
                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()
                }
                else{
                    emailSignIn()
                }
            }

    }
    fun emailSignIn(){
        var email=emailEt.text.toString()
        var password=passwordEt.text.toString()

        startLoading()
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                endLoading()
                if (task.isSuccessful){
                    moveMain(auth.currentUser)
                }
                else{
                    Toast.makeText(this,"이메일이나 패스워드가 틀렸다",Toast.LENGTH_SHORT).show()
                }
            }

    }
    fun moveMain(user: FirebaseUser?){
        if (user!=null){
            var intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun startLoading(){
        loadingPb.visibility=VISIBLE
    }
    fun endLoading(){
        loadingPb.visibility=GONE
    }
}