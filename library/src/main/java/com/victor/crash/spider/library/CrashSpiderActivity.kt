package com.victor.crash.spider.library

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import com.victor.crash.spider.library.databinding.ActivityCrashSpiderBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat


/*
 * -----------------------------------------------------------------
 * Copyright (C) 2025-2035, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: CrashActivity
 * Author: Victor
 * Date: 2025/12/12 17:03
 * Description: 
 * -----------------------------------------------------------------
 */

class CrashSpiderActivity: AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCrashSpiderBinding

    private val df = SimpleDateFormat("yyyy-MM-dd HH:mm")
    private var model: CrashMessageInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrashSpiderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        initData(intent)
    }

    private fun initData(intent: Intent?) {
        model = intent?.getSerializableExtra(Constants.INTENT_DATA_KEY) as CrashMessageInfo?
        binding.mTvCrashMessage.text = getShareText(model)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_crash_spider, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_cope_txt -> {
                val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
                if (cm != null) {
                    val mClipData = ClipData.newPlainText("crash", getShareText(model))
                    cm.setPrimaryClip(mClipData)
                    showToast("拷贝成功")
                }
                true
            }
            R.id.action_share_txt -> {
                shareText(getShareText(model))
                true
            }
            R.id.action_share_img -> {
                shareImage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
    }

    private fun showToast(text: String?) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun BitmapToFile(bitmap: Bitmap?): File? {
        if (bitmap == null) return null
        val path = filesDir.path
        val imageFile = File(path, "spiderMan-" + df.format(model!!.time) + ".jpg")
        try {
            val out = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            bitmap.recycle()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return imageFile
    }


    fun getBitmapByView(view: NestedScrollView?): Bitmap? {
        if (view == null) return null

        var height = 0
        for (i in 0 until view.childCount) {
            height += view.getChildAt(i).height
        }

        val bitmap = Bitmap.createBitmap(view.width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawRGB(255, 255, 255)
        view.draw(canvas)
        return bitmap
    }

    private fun shareImage() {
        val file: File? =
            BitmapToFile(getBitmapByView(binding.mNsvMessage))
        if (file == null || !file.exists()) {
            showToast("图片文件不存在")
            return
        }

        val intent = Intent()
        intent.setAction(Intent.ACTION_SEND)
        intent.setType("image/*")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val contentUri = FileProvider.getUriForFile(
                applicationContext,
                applicationContext.packageName + ".spiderfileprovider", file
            )
            intent.putExtra(Intent.EXTRA_STREAM, contentUri)
        } else {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intent, "分享图片"))
    }

    private fun getShareText(model: CrashMessageInfo?): String {
        val builder = StringBuilder()

        builder.append("崩溃信息:")
            .append("\n")
            .append(model?.exceptionMsg)
            .append("\n")
        builder.append("\n")

        builder.append("类名:")
            .append(model?.fileName).append("\n")
        builder.append("\n")

        builder.append("方法:").append(model?.methodName).append("\n")
        builder.append("\n")

        builder.append("行数:").append(model?.lineNumber).append("\n")
        builder.append("\n")

        builder.append("类型:").append(model?.exceptionType).append("\n")
        builder.append("\n")

        builder.append("时间").append(df.format(model?.time)).append("\n")
        builder.append("\n")

        builder.append("设备名称:").append(model?.device?.model).append("\n")
        builder.append("\n")

        builder.append("设备厂商:").append(model?.device?.brand).append("\n")
        builder.append("\n")

        builder.append("系统版本:").append(model?.device?.version).append("\n")
        builder.append("\n")

        builder.append("全部信息:")
            .append("\n")
            .append(model?.fullException).append("\n")

        return builder.toString()
    }

    private fun shareText(text: String?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_SUBJECT, "崩溃信息：")
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(Intent.createChooser(intent, "分享到"))
    }

}