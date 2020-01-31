package sh.phoenix.ilovezappos.ui

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.animation.LinearInterpolator
import kotlinx.android.synthetic.main.activity_splash.*
import sh.phoenix.ilovezappos.R

class SplashActivity : AppCompatActivity() {
    private var pixelsToTranslate: Float = 0f

    private val animationTime = 1500L
    private val splashTime = 2500L

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        calculatePixelsToTranslate()

        startAnimation()

        handler = Handler()
        handler.postDelayed({
            startMainActivity()
        }, splashTime)
    }

    private fun calculatePixelsToTranslate() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight: Float = displayMetrics.heightPixels.toFloat()
        pixelsToTranslate = (screenHeight / 2f) - (screenHeight / 7.5f)
    }

    private fun startAnimation() {
        val valueAnimatorTop = ValueAnimator.ofFloat(0f, pixelsToTranslate)
        valueAnimatorTop.addUpdateListener {
            val value = it.animatedValue as Float
            textI.translationY = value
        }

        valueAnimatorTop.interpolator = LinearInterpolator()
        valueAnimatorTop.duration = animationTime

        val valueAnimatorBottom = ValueAnimator.ofFloat(0f, -pixelsToTranslate)
        valueAnimatorBottom.addUpdateListener {
            val value = it.animatedValue as Float
            textZappos.translationY = value
        }

        valueAnimatorBottom.interpolator = LinearInterpolator()
        valueAnimatorBottom.duration = animationTime

        val animatorSet = AnimatorSet()
        animatorSet.play(valueAnimatorTop).with(valueAnimatorBottom)
        animatorSet.duration = animationTime
        animatorSet.start()
    }

    private fun startMainActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }
}
