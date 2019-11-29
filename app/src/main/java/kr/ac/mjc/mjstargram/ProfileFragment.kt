package kr.ac.mjc.mjstargram

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.drm.DrmStore
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(){

    lateinit var loadingPb:ProgressBar
    lateinit var profileIv:ImageView
    lateinit var emailTv:TextView

    lateinit var auth:FirebaseAuth
    lateinit var firestore:FirebaseFirestore
    lateinit var storage:FirebaseStorage

    val IMAGE_PICK = 1111

    var user:User?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_profile,container,false)

        loadingPb=view.findViewById(R.id.loading_pb)
        profileIv=view.findViewById(R.id.profile_iv)
        emailTv=view.findViewById(R.id.email_tv)

        return view
    }
    fun startLoading(){
        loadingPb.visibility=VISIBLE
    }
    fun endLoading(){
        loadingPb.visibility=GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth=FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()
        storage=FirebaseStorage.getInstance()

        startLoading()
        firestore.collection("User").document(auth.currentUser?.email!!)
            .get().addOnCompleteListener {
                task ->
                endLoading()
                if (task.isSuccessful){
                    user=task.result?.toObject(User::class.java)
                    if(user?.imageUrl!=null){
                        Glide.with(profileIv).load(user?.imageUrl).into(profileIv)
                    }
                    emailTv.text=user?.email
                }
            }

        profileIv.setOnClickListener {
            var intent=Intent(ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,IMAGE_PICK)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==IMAGE_PICK&&resultCode==RESULT_OK){
            var imageUri=data?.data
            startLoading()
            storage.getReference().child("profile").child(auth.currentUser?.email!!)
                .putFile(imageUri!!)
                .addOnSuccessListener{
                    taskSnapshot->
                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener{
                                task->
                                user?.imageUrl=task.toString()
                                firestore.collection("User").document(user?.email!!)
                                    .set(user!!)
                                    .addOnSuccessListener {
                                        endLoading()
                                        profileIv.setImageURI(imageUri)
                                    }
                            }
                }
        }
    }
}
