package madeleaan.noustea.ui.sol

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import cc.mvdan.accesspoint.WifiApControl
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import madeleaan.noustea.R

class SolFragment: Fragment() {
    private lateinit var curView: View
    private var addr1: String = "28:cd:c1:05:9d:f4"
    private var addr2: String = ""
    private var pi1: String = ""
    private var pi2: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        curView = inflater.inflate(R.layout.fragment_sol, container, false)
        refreshStatus()
        curView.findViewById<Button>(R.id.button_on).setOnClickListener {
            makeRequest("http://$pi1/?led=on")
        }
        curView.findViewById<Button>(R.id.button_off).setOnClickListener {
            makeRequest("http://$pi1/?led=off")
        }
        curView.findViewById<Button>(R.id.button_refresh).setOnClickListener {
            refreshStatus()
        }

        return curView
    }

    private fun makeRequest(url: String) {
        val req = StringRequest(Request.Method.GET, url,
            { response -> curView.findViewById<TextView>(R.id.test).text = response},
            { response -> curView.findViewById<TextView>(R.id.test).text = response.toString()})
        println("Sending a request to $url")
        Volley.newRequestQueue(context).add(req)
    }

    private fun refreshStatus() {
        val clients: List<WifiApControl.Client> = WifiApControl.getInstance(context).clients
        for (client in clients) {
            println("${client.hwAddr} | ${client.ipAddr}")
            when(client.hwAddr) {
                addr1 -> pi1 = client.ipAddr
                addr2 -> pi2 = client.ipAddr
            }
        }
        if(pi1 == "") {
            curView.findViewById<TextView>(R.id.pi1_status).text = "Pi 1: Offline"
            curView.findViewById<TextView>(R.id.pi1_status).setTextColor(Color.RED)
            return
        }

        val req = StringRequest(Request.Method.GET, "http://$pi1",
            { response -> curView.findViewById<TextView>(R.id.pi1_status).text = "Pi 1: Online"
            curView.findViewById<TextView>(R.id.pi1_status).setTextColor(Color.GREEN)},
            { response -> curView.findViewById<TextView>(R.id.pi1_status).text = "Pi 1: Offline"
                curView.findViewById<TextView>(R.id.pi1_status).setTextColor(Color.RED)})
        Volley.newRequestQueue(context).add(req)
    }
}