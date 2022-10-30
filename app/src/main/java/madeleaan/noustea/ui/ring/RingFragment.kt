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

class RingFragment: Fragment() {

    private lateinit var curView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        curView = inflater.inflate(R.layout.fragment_ring, container, false)
        val seekBarRed: SeekBar = curView.findViewById(R.id.ring_col_red)
        val seekBarGreen: SeekBar = curView.findViewById(R.id.ring_col_green)
        val seekBarBlue: SeekBar = curView.findViewById(R.id.ring_col_blue)
        val seekBarAlpha: SeekBar = curView.findViewById(R.id.ring_col_alpha)
        setColor(255, 255, 255, 255)
        seekBarRed.progress = 255
        seekBarGreen.progress = 255
        seekBarBlue.progress = 255
        seekBarAlpha.progress = 255
        seekBarRed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { setColor(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress, seekBarAlpha.progress)}
        })
        seekBarGreen.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { setColor(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress, seekBarAlpha.progress)}
        })
        seekBarBlue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { setColor(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress, seekBarAlpha.progress)}
        })
        seekBarAlpha.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { setColor(seekBarRed.progress, seekBarGreen.progress, seekBarBlue.progress, seekBarAlpha.progress)}
        })
        return curView
    }

    fun setColor(red: Int, green: Int, blue: Int, alpha: Int) {
        curView.findViewById<Button>(R.id.ring_col_prev).setBackgroundColor(Color.argb(alpha, red, green, blue))
    }
}