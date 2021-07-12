package com.carlyadam.github.ui.github

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlyadam.github.R
import com.carlyadam.github.data.api.model.User
import com.carlyadam.github.databinding.FragmentGithubBinding
import com.carlyadam.github.ui.github.adapter.GithubAdapter
import com.carlyadam.github.ui.github.adapter.GithubLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.carlyadam.github.data.db.model.User as DbUser

@AndroidEntryPoint
class GithubFragment :
    Fragment(R.layout.fragment_github),
    GithubAdapter.AdapterListener {

    private var _binding: FragmentGithubBinding? = null
    private val binding get() = _binding!!
    private lateinit var githubdapter: GithubAdapter
    private val githubViewModel: GithubViewModel by viewModels()
    private lateinit var searchView: SearchView
    private lateinit var searchItem: MenuItem
    private val args by navArgs<GithubFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGithubBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        githubdapter = GithubAdapter(this, requireActivity())

        binding.apply {
            githubViewModel.saveQuery(args.query)

            recyclerGithub.layoutManager = LinearLayoutManager(requireActivity())

            githubdapter.withLoadStateFooter(
                footer = GithubLoadStateAdapter { githubdapter.retry() },
            )

            recyclerGithub.adapter = githubdapter

            buttonRetry.setOnClickListener {
                githubdapter.retry()
            }

            githubdapter.addLoadStateListener { loadState ->
                binding.apply {

                    shimmerViewContainer.isVisible = loadState.source.refresh is LoadState.Loading
                    recyclerGithub.isVisible =
                        loadState.source.refresh is LoadState.NotLoading
                    buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                    textViewError.isVisible = loadState.source.refresh is LoadState.Error

                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        githubdapter.itemCount < 1
                    ) {
                        recyclerGithub.isVisible = false
                        textViewEmpty.isVisible = true
                    } else {
                        textViewEmpty.isVisible = false
                    }
                }
            }
        }

        setHasOptionsMenu(true)

        githubViewModel.getQuery()?.apply {
            searchUsers(this)
        }
    }

    private fun searchUsers(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            githubViewModel.users(query).collectLatest { pagingData ->
                githubdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_search, menu)

        searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem!!.actionView as SearchView

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.recyclerGithub.scrollToPosition(0)
                    githubViewModel.saveQuery(query)
                    searchView.clearFocus()
                    searchUsers(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }
        })
        githubViewModel.getQuery()?.apply {
            searchItem.expandActionView()
            searchView.setQuery(this, false)
            searchView.clearFocus()
        }
    }

    override fun onItemTap(user: User) {
        // TODO: 7/11/21
    }

    override fun onFavoriteTap(user: User, favorite: Boolean) {
        val userDb = DbUser(user.id, user.login, user.avatar_url, favorite, user.score)
        setUserFavorite(userDb, favorite)
    }

    private fun setUserFavorite(userDb: DbUser, favorite: Boolean) {
            githubViewModel.setUserFavorite(userDb, favorite)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
