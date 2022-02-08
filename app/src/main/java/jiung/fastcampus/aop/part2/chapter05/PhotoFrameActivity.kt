package jiung.fastcampus.aop.part2.chapter05

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {

    private var currentPosition = 0

    private var timer: Timer? = null

    private val photoList = mutableListOf<Uri>()

    private val photoImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.photoImageView)
    }

    private val backgroundPhotoImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.backgroundPhotoImageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoframe)
        //Log.d("PhotoFrame", "onCreate!! timer start")
        getPhotoUriFromIntent()
        //startTimer()

    }

    private fun getPhotoUriFromIntent() {
        val size = intent.getIntExtra("photoListSize", 0)
        for (i in 0..size) {
            // null 값을 받으면 let 문이 실행이 되지 않음
            // null 이 아니라면 포토 리스트에 추가한다.
            // 바로 추가하지 않고 parse를 통해 String 을 Uri 객체로 변환한 후 배열에 추가
            intent.getStringExtra("photo$i")?.let {
                photoList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer() {
        timer = timer(period = 5 * 1000) {
            runOnUiThread {
                val current = currentPosition
                val next = if (photoList.size <= currentPosition + 1) 0 else currentPosition + 1
                //Log.d("PhotoFrame", "startTimer!! 5초 지나감")
                backgroundPhotoImageView.setImageURI(photoList[current])

                photoImageView.alpha = 0f
                photoImageView.setImageURI(photoList[next])
                photoImageView.animate()
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()
                currentPosition = next
            }
        }
    }


    // 더이상 사용 되지 않는 상태
    override fun onStop() {
        super.onStop()
        //Log.d("PhotoFrame", "onStop!! timer cancel")
        timer?.cancel()
    }

    // 다시 시작 시 메서드 실행
    override fun onStart() {
        super.onStart()
        //Log.d("PhotoFrame", "onStart!! timer start")
        startTimer()
    }

    // 확인 사살
    override fun onDestroy() {
        super.onDestroy()
        //Log.d("PhotoFrame", "onDestory!! timer cancel")
        timer?.cancel()
    }
}