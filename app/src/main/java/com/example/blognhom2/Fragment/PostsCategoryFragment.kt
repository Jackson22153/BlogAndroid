package com.example.blognhom2.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blognhom2.API.PostApi
import com.example.blognhom2.Adapter.PostAdapter
import com.example.blognhom2.databinding.FragmentHomeBinding
import com.example.blognhom2.model.PostInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class PostsCategoryFragment : Fragment() {
    private var isLoading = false
    private var visibleThreshold = 5 // Number of items from the bottom of the list at which loading more is triggered
    private var offset = 0 // The offset for loading more posts
    lateinit var category: String;
    private var _binding: FragmentHomeBinding? = null

    lateinit var adapter : PostAdapter
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initImageView()
        preparePostData()
        SetPostAdapter()
        SetSearchView()
        // Inflate the layout for this fragment
        val view = binding.root
        return view
    }

    fun initImageView() {

    }

    private var postSet = linkedSetOf<PostInfo>()

    private fun preparePostData() : List<PostInfo> {
        postSet.clear()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(PostApi::class.java)
        val call = api.getPostsByCategory(category, 0);

        call.enqueue(object : Callback<List<PostInfo>> {
            override fun onResponse(call: Call<List<PostInfo>>, response: Response<List<PostInfo>>) {
                println("ResponsePost")
                println(response)
                if (!response.isSuccessful) {
                    println("Code: " + response.code())
                    return
                }

                val posts = response.body()
                posts?.let {
                    postSet.addAll(it);
                    println(postSet)
                }
                updateAdapter()
            }

            override fun onFailure(call: Call<List<PostInfo>>, t: Throwable) {
                println(t.message)
            }
        })

        return postSet.toList();
    }

    private fun SetPostAdapter() {
        adapter = PostAdapter(postSet.toMutableList())
        val layoutManager = LinearLayoutManager(requireContext())
        binding.PostRecyclerView.layoutManager = layoutManager
        binding.PostRecyclerView.adapter = adapter
        binding.PostRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= (firstVisibleItemPosition + visibleItemCount + visibleThreshold)) {
                    println("Loading more...")
                    loadMorePosts()
                }
            }
        })
    }

    private fun updateAdapter() {
        println("Updating adapter...")
        adapter.setFilteredList(postSet.toMutableList())
    }

    fun setData(category: String) {
        this.category = category
    }

    private fun loadMorePosts() {
        println("Loading more posts...")
        isLoading = true
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(PostApi::class.java)
        val call = api.getPostsByCategory(category, offset);

        call.enqueue(object : Callback<List<PostInfo>> {
            override fun onResponse(call: Call<List<PostInfo>>, response: Response<List<PostInfo>>) {
                println("ResponseLoadMore")
                println(response)
                if (!response.isSuccessful) {
                    println("Code: " + response.code())
                    return
                }

                val posts = response.body()
                posts?.let {
                    postSet.addAll(it);
                }
                offset += visibleThreshold
                isLoading = false
                updateAdapter()
            }

            override fun onFailure(call: Call<List<PostInfo>>, t: Throwable) {
                println("Error")
                println(t)
            }
        })
    }

    private fun SetSearchView(){
        binding.seachView.clearFocus()
        binding.seachView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList(newText)
                return true
            }

        } )
    }
    private fun searchList(query : String?, ){
        if (query != null){
            searchPost(query) { postInfos ->
                if (postInfos != null) {
                    val filteredList = linkedSetOf<PostInfo>();
                    filteredList.addAll(postInfos);
                    if(filteredList.isEmpty()){
                        Toast.makeText(requireContext(), "Không tìm thấy bài viết", Toast.LENGTH_LONG).show()
                    } else{
                        adapter.setFilteredList(filteredList.toMutableList())
                    }
                } else {
                    Toast.makeText(requireContext(), "Search failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun searchPost(query: String, onResult: (List<PostInfo>?) -> Unit){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(PostApi::class.java)
        val call = api.searchPostsByCategory(category, 0, query);
        println("searchPost")

        call.enqueue(object : Callback<List<PostInfo>> {
            override fun onResponse(call: Call<List<PostInfo>>, response: Response<List<PostInfo>>) {
                println("ResponsePost")
                println(response)
                if (!response.isSuccessful) {
                    println("Code: " + response.code())
                    onResult(null);
                    return
                }
                val posts = response.body()
                onResult(posts);
            }

            override fun onFailure(call: Call<List<PostInfo>>, t: Throwable) {
                println(t.message)
                onResult(null);
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}