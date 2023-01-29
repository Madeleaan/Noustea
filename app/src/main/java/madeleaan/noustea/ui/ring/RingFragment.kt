package madeleaan.noustea.ui.ring

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import madeleaan.noustea.R
import madeleaan.noustea.RequestHandler
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

class RingFragment: Fragment() {

    private lateinit var curView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        curView = inflater.inflate(R.layout.fragment_ring, container, false)

        var lastTime: Long = System.currentTimeMillis()
        val handler = RequestHandler(context)

        val seekBarRed: SeekBar = curView.findViewById(R.id.ring_col_red)
        val seekBarGreen: SeekBar = curView.findViewById(R.id.ring_col_green)
        val seekBarBlue: SeekBar = curView.findViewById(R.id.ring_col_blue)
        setColor(255, 255, 255)
        seekBarRed.progress = 255
        seekBarGreen.progress = 255
        seekBarBlue.progress = 255

        seekBarRed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { setColor(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress)}
        })
        seekBarGreen.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { setColor(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress)}
        })
        seekBarBlue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { setColor(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress)}
        })

        val seekBarSpeed: SeekBar = curView.findViewById(R.id.ring_speed)
        seekBarSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) { handler.setRingSpeed((seekBarSpeed.progress / 100.0 * 30000 + 30000).toInt()) }
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if(System.currentTimeMillis() - lastTime > 1000) {
                    handler.setRingSpeed((seekBarSpeed.progress / 100.0 * 30000 + 30000).toInt())
                    lastTime = System.currentTimeMillis()
                }
                curView.findViewById<GifImageView>(R.id.ring_ani).setImageDrawable(GifDrawable(resources, R.drawable.ani_ring).also { it.setSpeed(
                    (seekBarSpeed.progress.toFloat() * 2 / 100)
                )})
            }
        })
        return curView
    }

    fun setColor(red: Int, green: Int, blue: Int) {
        curView.findViewById<Button>(R.id.ring_col_prev).setBackgroundColor(Color.argb(255, red, green, blue))
    }
}