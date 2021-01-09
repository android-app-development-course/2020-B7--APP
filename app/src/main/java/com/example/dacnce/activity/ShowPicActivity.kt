package com.example.dacnce.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.dacnce.R
import com.example.dacnce.adapter.MyPageAdapter
import kotlinx.android.synthetic.main.activity_show_pic.*
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class ShowPicActivity : AppCompatActivity() {
    private var imagesUrl: ArrayList<String>? = null
    var current = 0
    private var pagerAdapter: MyPageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_show_pic)

        initData()
    }

    /**
     *
     */
    private fun initData() {

        if (intent != null) {
            val intent = intent
            imagesUrl = intent.getStringArrayListExtra("images")
            current = intent.getIntExtra("position", 0)
        }
        pagerAdapter = MyPageAdapter(imagesUrl, applicationContext)

        viewPager.adapter = pagerAdapter
        viewPager.currentItem = current
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                current = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    /**
     * 保存图片
     * 根据图片的url路径获得Bitmap对象
     */
    private fun decodeUriAsBitmapFromNet(url: String): Bitmap? {
        var fileUrl: URL? = null
        var bitmap: Bitmap? = null
        try {
            fileUrl = URL(url)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        try {
            val conn: HttpURLConnection = fileUrl
                ?.openConnection() as HttpURLConnection
            conn.doInput = true
            conn.connect()
            val inputStream: InputStream = conn.getInputStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }



}