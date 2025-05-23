package com.buuzz.DonorConnectionApp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.buuzz.DonorConnectionApp.R
import com.buuzz.DonorConnectionApp.data.model.response.Post
import com.buuzz.DonorConnectionApp.databinding.FragmentHomeBinding
import com.buuzz.DonorConnectionApp.ui.base.BaseFragment
import com.buuzz.DonorConnectionApp.ui.home.adapter.PostAdapter
import com.buuzz.DonorConnectionApp.ui.home.filter.FilterFragment
import com.buuzz.DonorConnectionApp.ui.post.ActionType
import com.buuzz.DonorConnectionApp.ui.post.view.PostActivity
import com.buuzz.DonorConnectionApp.ui.postmap.PostMapActivity
import com.buuzz.DonorConnectionApp.utils.apihelper.safeapicall.ApiCallListener
import com.buuzz.DonorConnectionApp.utils.helpers.AppLogger
import com.buuzz.DonorConnectionApp.utils.helpers.IntentParams
import com.buuzz.DonorConnectionApp.utils.helpers.OnActionClicked
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment(), OnActionClicked {
    private lateinit var binding: FragmentHomeBinding
    private var selectedCategory: String? = null


    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setViews()
        return binding.root
    }

    private fun setViews() {
        setCategoryTabs()
        setOnClicks()
    }

    private fun setOnClicks() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            getPostsByCategory()
        }
        binding.notificationBtn.setOnClickListener {
            startActivity(Intent(requireContext(), PostMapActivity::class.java))
        }

        binding.filterSearch.setOnClickListener {
            showFilterOptions()
        }
        binding.searchView.setOnCloseListener {
            fetchPostByCategory("All")
            return@setOnCloseListener false
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    getPostsBySearchQuery(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun setCategoryTabs() {
        viewModel.getCategoryList { tags ->
            binding.categoryList.removeAllTabs()
            binding.categoryList.addTab(binding.categoryList.newTab().apply {
                text = "All"
                tag = "All"
            })
            tags.forEach { data ->
                binding.categoryList.addTab(binding.categoryList.newTab().apply {
                    text = data.name
                    tag = data.id
                })
            }
            val tabs = binding.categoryList.getChildAt(0) as ViewGroup
            AppLogger.logD("POST", "$tabs")
            for (i in 0 until tabs.childCount) {

                val tab = tabs.getChildAt(i)
                val layoutParams = tab.layoutParams as LinearLayout.LayoutParams
                layoutParams.height = resources.getDimensionPixelSize(R.dimen._40dp)
                layoutParams.marginEnd = resources.getDimensionPixelSize(R.dimen._5dp)
                if (i == 0) {
                    layoutParams.marginStart = resources.getDimensionPixelSize(R.dimen._5dp)
                }
                tab.layoutParams = layoutParams
            }
            binding.categoryList.requestLayout()
        }

        binding.categoryList.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                AppLogger.logD("POST", "category ${tab?.tag.toString()}")

                fetchPostByCategory(tab?.tag.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun getPostById(postId: String?) {
        binding.loading.isVisible = true
        viewModel.getPostById(postId, object : ApiCallListener {
            override fun onSuccess(response: String?) {
                binding.loading.isVisible = false
                val intent = Intent(requireContext(), PostActivity::class.java)
                intent.putExtra(IntentParams.POST_DETAIL, response)
                startActivity(intent)
            }

            override fun onError(errorMessage: String?) {
                binding.loading.isVisible = false
                showTopSnackBar(binding.root, errorMessage ?: "Failed to Fetch Posts")
            }

        })
    }

    private fun getPostsBySearchQuery(query: String) {
        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.getPostsBySearch(query, object : ApiCallListener {
            override fun onSuccess(response: String?) {
                val listType = object : TypeToken<List<Post>>() {}.type
                val postList = Gson().fromJson<List<Post>>(response, listType)
                if (!postList.isNullOrEmpty()) {
                    setPostList(postList)
                }
                binding.swipeRefreshLayout.isRefreshing = false
            }

            override fun onError(errorMessage: String?) {
                binding.swipeRefreshLayout.isRefreshing = false
                showTopSnackBar(binding.root, errorMessage ?: "Failed to Fetch Posts")
            }

        })
    }

    private fun getPostsByCategory() {
        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.getPostsByCategory(selectedCategory, object : ApiCallListener {
            override fun onSuccess(response: String?) {
                val listType = object : TypeToken<List<Post>>() {}.type
                val postList = Gson().fromJson<List<Post>>(response, listType)
                if (!postList.isNullOrEmpty()) {
                    setPostList(postList)
                }
                binding.swipeRefreshLayout.isRefreshing = false
            }

            override fun onError(errorMessage: String?) {
                binding.swipeRefreshLayout.isRefreshing = false
                showTopSnackBar(binding.root, errorMessage ?: "Failed to Fetch Posts")
            }

        })
    }

    private fun getPostsByTag(tagList: String) {
        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.getPostsByTag(tagList, object : ApiCallListener {
            override fun onSuccess(response: String?) {
                val listType = object : TypeToken<List<Post>>() {}.type
                val postList = Gson().fromJson<List<Post>>(response, listType)
                if (!postList.isNullOrEmpty()) {
                    setPostList(postList)
                }
                binding.swipeRefreshLayout.isRefreshing = false
            }

            override fun onError(errorMessage: String?) {
                binding.swipeRefreshLayout.isRefreshing = false
                showTopSnackBar(binding.root, errorMessage ?: "Failed to Fetch Posts")
            }

        })
    }

    private fun showFilterOptions() {
        viewModel.getTagList { list ->
            val dialogFragment = FilterFragment.newInstance(list)
            dialogFragment.setCallback(this)
            dialogFragment.show(childFragmentManager, "FilterDialog")
        }
    }

    private fun setPostList(postList: List<Post>) {
        binding.mainList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            viewModel.getUserId { userId ->
                adapter = PostAdapter(postList, userId, this@HomeFragment)
            }
        }

    }

    private fun fetchPostByCategory(categoryId: String?) {
        selectedCategory = if (categoryId != "All") {
            categoryId
        } else {
            null
        }
        getPostsByCategory()
    }

    override fun onClick(type: String, value: String?) {
        when (type) {
            ActionType.VIEW_POST.name -> {
                getPostById(postId = value)
            }

            ActionType.APPLY_FILTER.name -> {
                val listType = object : TypeToken<List<Int>>() {}.type
                val list: List<Int> = Gson().fromJson(value, listType)
                val tagList = list.joinToString(separator = ", ")
                getPostsByTag(tagList)
            }
        }
    }


}