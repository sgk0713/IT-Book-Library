package com.sunguk.itbooklibrary.ui.bookdetail.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunguk.domain.entity.PdfInfo
import com.sunguk.itbooklibrary.R
import com.sunguk.itbooklibrary.databinding.ItemPdfBinding
import com.sunguk.itbooklibrary.util.createView

class PDFsAdapter constructor(
    private val controller: BookDetailController,
) : RecyclerView.Adapter<PDFViewHolder>() {

    private val pdfs = mutableListOf<PdfInfo>()

    @SuppressLint("NotifyDataSetChanged")
    fun replaceItems(items: List<PdfInfo>) {
        pdfs.clear()
        pdfs.addAll(items)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PDFViewHolder, position: Int) {
        holder.onBind(pdfs[position], controller)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PDFViewHolder {
        val view = parent.createView(R.layout.item_pdf)
        return PDFViewHolder(ItemPdfBinding.bind(view))
    }

    override fun getItemCount(): Int {
        return pdfs.size
    }
}