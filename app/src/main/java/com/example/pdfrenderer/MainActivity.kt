package com.example.pdfrenderer

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        showPdfFile()
    }

    private fun showPdfFile() {
        val file = File("/storage/emulated/0/Download/Mercedes.pdf")
        val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(fileDescriptor)
        val pageCount = pdfRenderer.pageCount
        Toast.makeText(
            this,
            "Страница $pageCount",
            Toast.LENGTH_LONG
        ).show()
        val rendererPage = pdfRenderer.openPage(currentPage)
        val rendererPageWidth = rendererPage.width
        val rendererPageHeight: Int = rendererPage.height
        val bitmap = Bitmap.createBitmap(
            rendererPageWidth,
            rendererPageHeight,
            Bitmap.Config.ARGB_8888
        )
        rendererPage.render(
            bitmap, null, null,
            PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
        )
        imageView.setImageBitmap(bitmap)

        rendererPage.close()
        pdfRenderer.close()
        fileDescriptor.close()
    }
}
