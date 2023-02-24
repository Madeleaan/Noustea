package madeleaan.noustea.ui.rad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import madeleaan.noustea.R
import madeleaan.noustea.RequestHandler

class RadFragment: Fragment() { // TODO: BETTER UI
    private lateinit var curView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        curView = inflater.inflate(R.layout.fragment_rad, container, false)
        val handler: RequestHandler = RequestHandler(context)
        var lastTime: Long = System.currentTimeMillis()

        val seekBarRad: SeekBar = curView.findViewById(R.id.rad)

        seekBarRad.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val servoSpeed: Int = (seekBarRad.progress + 3000)
                handler.setServo(0, servoSpeed)
            }
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(System.currentTimeMillis() - lastTime > 300) {
                    val servoSpeed: Int = (seekBarRad.progress + 3000) //TODO: ZPOMALIT, I U SOL
                    handler.setServo(0, servoSpeed)
                    lastTime = System.currentTimeMillis()
                }
            }
        })

        return curView
    }
}