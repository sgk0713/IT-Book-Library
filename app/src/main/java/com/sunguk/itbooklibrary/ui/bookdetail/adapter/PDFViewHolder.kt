package com.sunguk.itbooklibrary.ui.bookdetail.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sunguk.domain.entity.PdfInfo
import com.sunguk.itbooklibrary.databinding.ItemPdfBinding

class PDFViewHolder(
    private val binding: ItemPdfBinding,
) : ViewHolder(binding.root) {

    fun onBind(result: PdfInfo, controller: BookDetailController) {
        with(binding) {
            title.text = result.title
            itemView.setOnClickListener {
                controller.onPdfLinkClicked(result.url)
            }
        }
    }
}