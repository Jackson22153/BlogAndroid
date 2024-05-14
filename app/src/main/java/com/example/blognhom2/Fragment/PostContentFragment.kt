package com.example.blognhom2.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.blognhom2.API.PostApi
import com.example.blognhom2.databinding.FragmentPostContentBinding
import com.example.blognhom2.model.Category
import com.example.blognhom2.model.Post
import com.example.blognhom2.model.PostDetail
import com.example.blognhom2.model.PostInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PostContentFragment : Fragment() {

    lateinit var post : PostDetail;
    private var _binding: FragmentPostContentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostContentBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        SetDataForPostContent()
        val view = binding.root
        return view
    }

    fun setData(post: PostDetail) {
        this.post = post
    }

    fun SetDataForPostContent(){
        binding.postTitle.text = "      "+ post.title
        binding.postContent.text =  "      "+ post.content
        binding.postUser.text = post.user
        binding.postCategories.text = post.categories
        binding.postTime.text = post.time.toString()
        Glide.with(requireContext())
            .load(post.postImg)
            .into(binding.postImage)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}