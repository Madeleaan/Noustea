package madeleaan.noustea.ui.sol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        val handler = RequestHandler(context)
        curView.findViewById<Button>(R.id.button_refresh).setOnClickListener {
            handler.sendRequest(RequestHandler.Devices.RING, "")
        }

        return curView
    }
}