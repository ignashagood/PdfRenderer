package com.example.pdfrenderer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showPdfFile()
    }

    private fun showPdfFile() {
        val file = File(filesDir, "Mercedes.pdf")
        val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(fileDescriptor)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val displayMetrics = resources.displayMetrics
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PdfAdapter(pdfRenderer, displayMetrics.widthPixels)
    }
}

suspend fun PdfRenderer.Page.renderAndClose(width: Int) =
    withContext(Dispatchers.Default) {
        Log.d("Thread", "Thread - ${Thread.currentThread().name}")
        use {
            val bitmap = createBitmap(width)
            render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            bitmap
        }
    }

private fun PdfRenderer.Page.createBitmap(bitmapWidth: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(
        bitmapWidth, (bitmapWidth.toFloat() / width * height).toInt(), Bitmap.Config.ARGB_8888
    )
    Log.d("BitMap", Thread.currentThread().name)
    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.WHITE)
    canvas.drawBitmap(bitmap, 0f, 0f, null)

    return bitmap
}
