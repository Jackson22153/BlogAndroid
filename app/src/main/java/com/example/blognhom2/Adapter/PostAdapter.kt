package com.example.blognhom2.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blognhom2.API.PostApi
import com.example.blognhom2.Fragment.PostContentFragment
import com.example.blognhom2.R
import com.example.blognhom2.model.PostDetail
import com.example.blognhom2.model.PostInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostAdapter(var postList : List<PostInfo>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    var genericList : MutableList<PostInfo> = postList as MutableList<PostInfo>

    inner class PostViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val postImage: ImageView = itemView.findViewById(R.id.articleImage)
        val postTitle: TextView = itemView.findViewById(R.id.articleTitle)
        var postTime: TextView = itemView.findViewById(R.id.articleDateTime)
        var postCategory: TextView = itemView.findViewById(R.id.articleCategories)
        val postContent : TextView = itemView.findViewById(R.id.articleDescription)
        val postLayout : ConstraintLayout = itemView.findViewById(R.id.postLayout)
    }
    public fun setFilteredList(filteredList: List<PostInfo>){
        this.genericList = filteredList as MutableList<PostInfo>
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        println(genericList.size)
        return genericList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.itemView.apply {
            holder.postLayout.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim))
            holder.postTitle.text = genericList[position].title
            holder.postTime.text = genericList[position].time.toString()
            holder.postCategory.text = genericList[position].categories
//            holder.postContent.text = genericList[position].postContent
            Glide.with(holder.itemView.context)
                .load(genericList[position].postImg)
                .into(holder.postImage)

            setOnClickListener {
                val post = genericList[position]
                val fragment = PostContentFragment() // Replace YourFragment with your actual fragment class
                println("PostID is : "+post.id)

                getPostDetail(post.id) { postDetail ->
                    if (postDetail != null) {
                        println("Data is received")
//                        val fragment = PostContentFragment()
                        fragment.setData(postDetail)
                        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.frame_layout, fragment)
                        transaction.addToBackStack(null)
                        transaction.commit()
                    } else {
                        println("Missing data")
                        // Handle error
                    }
                }

//
////                fragment.setData(post)
//                val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
//                transaction.replace(R.id.frame_layout, fragment)
//                transaction.addToBackStack(null)
//                transaction.commit()
            }

        }
    }

    private fun getPostDetail(id: Int, onResult: (PostDetail?) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(PostApi::class.java)
        var call = api.getPostDetail(id);
        println("get Post Detail")
        call.enqueue(object : Callback<PostDetail> {
            override fun onResponse(call: Call<PostDetail>, response: Response<PostDetail>) {
                println("ResponsePost")
                println(response)
                if (!response.isSuccessful) {
                    println("Code: " + response.code())
                    onResult(null)
                    return
                }
                println("PostDetail response body")
                val postDetail = response.body()
                println(postDetail)
                onResult(postDetail)
                // Do something with the posts
//                    println(posts)
            }

            override fun onFailure(call: Call<PostDetail>, t: Throwable) {
                println("Error occurs")
                println(t.message)
                onResult(null)
            }
        })
    }

}