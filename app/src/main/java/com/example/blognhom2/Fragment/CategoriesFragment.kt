package com.example.blognhom2.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.blognhom2.API.PostApi
import com.example.blognhom2.API.UnsplashApi
import com.example.blognhom2.Adapter.CategoriesAdapter
import com.example.blognhom2.databinding.FragmentCategoriesBinding
import com.example.blognhom2.model.Category
import com.example.blognhom2.model.Post
import com.example.blognhom2.model.PostInfo
import com.example.blognhom2.model.SearchResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CategoriesFragment : Fragment() {
    private var categoriesList = mutableListOf<Category>()
    private var posts = mutableListOf<PostInfo>()
    lateinit var adapter: CategoriesAdapter
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private var oneTime : Boolean = true



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)


        //cal prepareDate 1 lần duy nhất
        if(oneTime) {
            prepareData()

            oneTime = false
        }
        SetCategoriesAdapter()
        val view = binding.root
        return view
    }

    private fun prepareData(){
//        posts = HomeFragment.preparePostData() as MutableList<PostInfo>

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(PostApi::class.java)
        var call = api.getCategories();
        call.enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                println("ResponsePost")
                println(response)
                if (!response.isSuccessful) {
                    println("Code: " + response.code())
                    return
                }

                val categories = response.body()
                println(categories)
                categories?.let {
                    categoriesList.addAll(it)
                }

                updateAdapter()
                // Do something with the posts
//                    println(posts)
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                println(t.message)
            }
        })
    }
    public fun GetImageFromUnsplash(query : String, imageView: ImageView, context :Context)  {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(UnsplashApi::class.java)
        val call = api.searchPhotos(query, "LYo9pogkxTIKrzOQ3kVA_Hp-2etxevmfGig1iqPt0Zg")
        call.enqueue(object : Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                val photoUrl = response.body()?.results?.randomOrNull()?.urls?.small
                // Bây giờ bạn có URL của bức ảnh đầu tiên trong kết quả tìm kiếm
                Glide.with(context)
                    .load(photoUrl)
                    .into(imageView)
            }

            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                // Xử lý lỗi
            }
        })

    }

    private fun SetCategoriesAdapter(){
        adapter = CategoriesAdapter(categoriesList, posts)
        binding.categoriesRecyclerView.adapter = adapter
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun updateAdapter() {
        // Assuming 'adapter' is a global variable
        adapter.setData(categoriesList, posts)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}