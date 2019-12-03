package kr.ac.mjc.mjstargram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class FeedAdapter(var context : Context ,var postList:ArrayList<Post>) : RecyclerView.Adapter<FeedAdapter.ViewHolder>(){

    var firestore=FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.item_feed,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postList[position])

    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var profileIv:ImageView=itemView.findViewById(R.id.profile_iv)
        var emailTv:TextView=itemView.findViewById(R.id.email_tv)
        var imageIv:ImageView=itemView.findViewById(R.id.image_iv)
        var descriptionTv:TextView=itemView.findViewById(R.id.description_tv)

        fun bind(post:Post){
            firestore.collection("User").document(post.userId!!)
                .get()
                .addOnCompleteListener {
                    task ->
                    if (task.isSuccessful){
                        var user=task.result?.toObject(User::class.java)
                        Glide.with(profileIv).load(user?.imageUrl).into(profileIv)
                    }
                }
            emailTv.text=post.userId
            Glide.with(imageIv).load(post.imageUrl).into(imageIv)
            descriptionTv.text=post.description
        }

    }
}