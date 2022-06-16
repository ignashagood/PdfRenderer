package com.example.pdfrenderer

import android.graphics.pdf.PdfRenderer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PdfAdapter(
    private val renderer: PdfRenderer,
    private val pageWidth: Int
) : RecyclerView.Adapter<PdfAdapter.ViewHolder>() {

    class ViewHolder(
        view: View,
        private val renderer: PdfRenderer,
        private val pageWidth: Int
    ) : RecyclerView.ViewHolder(view) {
        private val coroutineScope = CoroutineScope(Dispatchers.Main)
        var job: Job? = null
        fun bind(position: Int) {
            job?.cancel()
            job = coroutineScope.launch {
                (itemView as ImageView)
                    .setImageBitmap(renderer.openPage(position).renderAndClose(pageWidth))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pdf_page, parent, false),
            renderer, pageWidth
        )

    override fun getItemCount() = renderer.pageCount

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)
}
