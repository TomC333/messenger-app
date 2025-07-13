package com.sgvas21.ddadi21.messengerapp.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgvas21.ddadi21.messengerapp.data.model.User
import com.sgvas21.ddadi21.messengerapp.domain.usecase.chat.CreateChatUseCase
import com.sgvas21.ddadi21.messengerapp.domain.usecase.chat.GetChatByIdUseCase
import com.sgvas21.ddadi21.messengerapp.domain.usecase.user.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val getChatByIdUseCase: GetChatByIdUseCase,
    private val createChatUseCase: CreateChatUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> = _searchResults.asStateFlow()

    init {
        setupSearchFlow()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun setupSearchFlow() {
        _searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .filter { query ->
                query.isEmpty() || query.length >= 3
            }
            .flatMapLatest { query ->
                flow { emit(searchUseCase.execute(query)) }
            }
            .onEach { results ->
                _searchResults.value = results
                Log.d("SearchViewModel", "Received ${results.size} search results for query: ${_searchQuery.value}")

            }
            .launchIn(viewModelScope)
    }

    /**
     * Updates the current search query. This method is called from the UI.
     * @param query The new search string.
     */
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    suspend fun getOrCreateChatId(currentUser: String, otherUser: String): String {
        val sortedId = listOf(currentUser, otherUser).sorted().joinToString("_")

        val existingChat = getChatByIdUseCase(sortedId)
        return if (existingChat != null) {
            sortedId
        } else {
            createChatUseCase(currentUser, otherUser)
        }
    }
}
