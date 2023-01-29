package madeleaan.noustea.ui.viewport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import madeleaan.noustea.R
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView


class ViewportFragment: Fragment() {
    private lateinit var curView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        curView =  inflater.inflate(R.layout.fragment_viewport, container, false)
        curView.findViewById<GifImageView>(R.id.viewport_rad_down).setImageDrawable(GifDrawable(resources, R.drawable.ani_rad_down).also { it.setSpeed(0.8f) })
        curView.findViewById<GifImageView>(R.id.viewport_rad_up).setImageDrawable(GifDrawable(resources, R.drawable.ani_rad_up).also { it.setSpeed(0.8f) })
        curView.findViewById<GifImageView>(R.id.viewport_sol_down).setImageDrawable(GifDrawable(resources, R.drawable.ani_sol_down).also { it.setSpeed(0.8f) })
        curView.findViewById<GifImageView>(R.id.viewport_sol_down_back).setImageDrawable(GifDrawable(resources, R.drawable.ani_sol_down_back).also { it.setSpeed(0.8f) })
        curView.findViewById<GifImageView>(R.id.viewport_sol_up).setImageDrawable(GifDrawable(resources, R.drawable.ani_sol_up).also { it.setSpeed(0.8f) })
        curView.findViewById<GifImageView>(R.id.viewport_sol_up_back).setImageDrawable(GifDrawable(resources, R.drawable.ani_sol_up_back).also { it.setSpeed(0.8f) })

        return curView
    }
}