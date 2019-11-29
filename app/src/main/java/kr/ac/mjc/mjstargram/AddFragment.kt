package kr.ac.mjc.mjstargram

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddFragment: Fragment(){
    lateinit var loadingPb:ProgressBar
    lateinit var imageIv:ImageView
    lateinit var descriptionEt:EditText
    lateinit var submitBtn:Button

    lateinit var auth:FirebaseAuth
    lateinit var firestore:FirebaseFirestore
    lateinit var storage:FirebaseStorage

    val IMAGE_PICK=2222

    var imageUri: Uri?=null

    var post=Post()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_add,container,false)

        loadingPb=view.findViewById(R.id.loading_pb)
        imageIv=view.findViewById(R.id.image_iv)
        descriptionEt=view.findViewById(R.id.description_et)
        submitBtn=view.findViewById(R.id.submit_btn)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        auth= FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()
        storage= FirebaseStorage.getInstance()

        imageIv.setOnClickListener {
            var intent= Intent(ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,IMAGE_PICK)
        }
        submitBtn.setOnClickListener {
            submit()
        }
    }
    fun submit(){
        if(imageUri==null){
            Toast.makeText(context,"이미지를 선택해라",Toast.LENGTH_SHORT).show()
            return
        }
        post.description=descriptionEt.text.toString()
        post.userId=auth.currentUser?.email
        startLoading()
        storage.getReference().child("post").child(UUID.randomUUID().toString())
            .putFile(imageUri!!)
            .addOnSuccessListener {
                taskSnapshot ->
                taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                    downloadUrl->

                    post.imageUrl=downloadUrl.toString()
                    firestore.collection("Post").document().set(post)
                        .addOnSuccessListener {
                            endLoading()
                            descriptionEt.text.clear()
                            imageIv.setImageDrawable(resources.getDrawable(R.mipmap.ic_launcher))
                        }
                }
            }
    }

    fun startLoading(){
        loadingPb.visibility=VISIBLE
    }
    fun endLoading(){
        loadingPb.visibility=GONE

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==IMAGE_PICK&&resultCode==RESULT_OK){
            imageUri=data?.data
            imageIv.setImageURI(imageUri)
        }
    }
}
