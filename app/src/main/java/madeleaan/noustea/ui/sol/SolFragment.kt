package madeleaan.noustea.ui.sol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import madeleaan.noustea.R
import madeleaan.noustea.RequestHandler

class SolFragment: Fragment() {
    private lateinit var curView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        curView = inflater.inflate(R.layout.fragment_sol, container, false)
        val handler:RequestHandler = RequestHandler(context)
        var lastTime: Long = System.currentTimeMillis()

        val seekBarFront: SeekBar = curView.findViewById(R.id.sol_front)
        val seekBarBack: SeekBar = curView.findViewById(R.id.sol_back)

        seekBarFront.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val servoSpeed: Int = (seekBarFront.progress + 3000)
                handler.setServo(2, servoSpeed)
            }
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(System.currentTimeMillis() - lastTime > 300) {
                    val servoSpeed: Int = (seekBarFront.progress + 3000)
                    handler.setServo(2, servoSpeed)
                    lastTime = System.currentTimeMillis()
                }
            }
        })

        seekBarBack.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val servoSpeed: Int = (seekBarBack.progress + 3000)
                handler.setServo(1, servoSpeed)
            }
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(System.currentTimeMillis() - lastTime > 300) {
                    val servoSpeed: Int = (seekBarBack.progress + 3000)
                    handler.setServo(1, servoSpeed)
                    lastTime = System.currentTimeMillis()
                }
            }
        })

        return curView
    }
}