package kr.ac.mjc.mjstargram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PhotoAdaptier(var context : Context, var postlist:ArrayList<Post>) : RecyclerView.Adapter<PhotoAdaptier.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.item_photo,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postlist.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var post=postlist[position]
        holder.bind(post)

    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var imageIv:ImageView=itemView.findViewById(R.id.image_iv)

        fun bind(post:Post){
            Glide.with(imageIv).load(post.imageUrl).into(imageIv)
        }
    }
}