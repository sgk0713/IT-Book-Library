package com.sunguk.itbooklibrary.ui.search

import com.sunguk.itbooklibrary.ui.base.BaseViewModel
import com.sunguk.itbooklibrary.ui.search.intent.SearchEvent
import com.sunguk.itbooklibrary.ui.search.intent.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
) : BaseViewModel<SearchState, SearchEvent>(SearchState()) {

}