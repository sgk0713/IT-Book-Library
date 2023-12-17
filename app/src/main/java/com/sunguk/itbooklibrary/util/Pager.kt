package com.sunguk.itbooklibrary.util

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

object Pager {
    private val coroutineScopeMap = hashMapOf<View, CoroutineScope>()

    fun <T : Any> initPaging(
        recyclerView: RecyclerView,
        pageable: Pageable<T>,
        loadInit: Boolean = false,
        initialPage: Int = 1,
    ) {
        val autoScrollLoader = AutoScrollLoader(pageable)
        if (loadInit) {
            autoScrollLoader.loadPage(initialPage)
        }
        pageable.loadSuccessPageSet.clear()
        coroutineScopeMap[recyclerView]?.cancel()
        coroutineScopeMap[recyclerView] = autoScrollLoader
        recyclerView.addOnScrollListener(autoScrollLoader)
        recyclerView.addOnAttachStateChangeListener(object :
            View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) = Unit

            override fun onViewDetachedFromWindow(v: View) {
                autoScrollLoader.cancel()
                coroutineScopeMap.remove(recyclerView)
            }
        })
    }
}

class AutoScrollLoader<T>(
    private val pageable: Pageable<T>,
) : RecyclerView.OnScrollListener(), CoroutineScope {
    override val coroutineContext: CoroutineContext =
        SupervisorJob() + Dispatchers.Default

    fun loadPage(requestPage: Int) {
        launch {
            val hasNotBeenLoaded =
                pageable.loadSuccessPageSet.add(requestPage) // it returns true if not contains the specified element

            if (requestPage > 0 && hasNotBeenLoaded) {
                runCatching {
                    pageable.onNewPageRequest(requestPage)
                }.fold(
                    onSuccess = {
                        pageable.resultCallback.invoke(it)
                    },
                    onFailure = {
                        it.printStackTrace()
                        pageable.loadSuccessPageSet.remove(requestPage)
                        pageable.resultCallback.invoke(Pageable.Result.Failure(requestPage, it))
                    }
                )
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        (recyclerView.layoutManager as LinearLayoutManager).apply {
            val firstVisibleAdapterPosition = findFirstVisibleItemPosition()
            val lastVisibleAdapterPosition = findLastVisibleItemPosition()
            if (firstVisibleAdapterPosition + lastVisibleAdapterPosition < 0) {
                return
            }

            val middleOfAdapterPosition: Int = listOf(
                firstVisibleAdapterPosition,
                lastVisibleAdapterPosition
            ).average().toInt()

            val currentPage = pageable.getPageByPosition(middleOfAdapterPosition)
            val scrollOffsetDiff = recyclerView.computeVerticalScrollOffset()

            when {
                scrollOffsetDiff > 0 -> loadPage(currentPage + 1)
                scrollOffsetDiff < 0 -> loadPage(currentPage - 1)
                else -> {
                    loadPage(currentPage + 1)
                    loadPage(currentPage - 1)
                }
            }
        }
    }
}