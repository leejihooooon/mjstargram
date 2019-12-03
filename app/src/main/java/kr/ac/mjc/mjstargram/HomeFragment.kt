package kr.ac.mjc.mjstargram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeFragment :Fragment(){

    lateinit var feedRv:RecyclerView
    lateinit var feedAdapter:FeedAdapter

    var postList=ArrayList<Post>()

    lateinit var firestore:FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_home,container,false)
        feedRv=view.findViewById(R.id.feed_rv)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedAdapter= FeedAdapter(context!!,postList)
        firestore= FirebaseFirestore.getInstance()

        feedRv.adapter=feedAdapter
        feedRv.layoutManager=LinearLayoutManager(context)
        postList.clear()
        firestore.collection("Post").orderBy("date",Query.Direction.ASCENDING)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot!=null){
                    for (dc in querySnapshot.documentChanges){
                        var post=dc.document.toObject(Post::class.java)
                        postList.add(0,post)
                    }
                    feedAdapter.notifyDataSetChanged()
                }
            }
    }
}