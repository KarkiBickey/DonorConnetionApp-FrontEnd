package com.buuzz.DonorConnectionApp.ui.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.buuzz.DonorConnectionApp.data.model.response.GetPostsResponse
import com.buuzz.DonorConnectionApp.data.model.response.Post
import com.buuzz.DonorConnectionApp.databinding.FragmentBookMarkBinding
import com.buuzz.DonorConnectionApp.ui.base.BaseFragment
import com.buuzz.DonorConnectionApp.ui.home.adapter.PostAdapter
import com.buuzz.DonorConnectionApp.ui.post.ActionType
import com.buuzz.DonorConnectionApp.ui.post.view.PostActivity
import com.buuzz.DonorConnectionApp.utils.apihelper.safeapicall.ApiCallListener
import com.buuzz.DonorConnectionApp.utils.helpers.IntentParams
import com.buuzz.DonorConnectionApp.utils.helpers.OnActionClicked
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookMarkFragment : BaseFragment(), OnActionClicked {
    private lateinit var binding: FragmentBookMarkBinding

    companion object {
        fun newInstance() = BookMarkFragment()
    }

    private val viewModel: BookMarkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookMarkBinding.inflate(layoutInflater)
        fetchList()
        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchList()
        }
        return binding.root
    }

    private fun fetchList() {
        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.getBookmarkList(object : ApiCallListener {
            override fun onSuccess(response: String?) {
                val data = Gson().fromJson(response, GetPostsResponse::class.java)
                data.posts?.let { setPostList(it) }
                binding.swipeRefreshLayout.isRefreshing = false
                binding.errorLyt.root.isVisible = false
            }

            override fun onError(errorMessage: String?) {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.errorLyt.apply {
                    root.isVisible = true
                    message.text = errorMessage
                    actionBtn.text = "Reload"
                    actionBtn.setOnClickListener {
                        onClick(ActionType.RELOAD.name)
                    }
                }
                showTopSnackBar(binding.root, errorMessage ?: "Error fetching your bookmark lists")
            }

        })
    }


    private fun setPostList(postList: List<Post?>) {
        binding.mainList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            viewModel.getUserId { userId ->
                adapter = PostAdapter(postList, userId, this@BookMarkFragment)
            }
        }

    }

    override fun onClick(type: String, value: String?) {
        when (type) {
            ActionType.VIEW_POST.name -> {
                getPostById(postId = value)
            }
            ActionType.RELOAD.name ->{
                fetchList()
            }
        }
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

}