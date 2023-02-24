package madeleaan.noustea

import android.content.Context
import android.widget.Toast
import cc.mvdan.accesspoint.WifiApControl
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class RequestHandler (private val ctx: Context?) {
    private var latestResponse: String = ""

    enum class Devices(val addr: String) {
        MAIN("28:CD:C1:05:9D:F6"),
    }

    fun sendRequest(device: Devices, path: String) {
        val req = StringRequest(
            Request.Method.GET, "http://${getAddress(device.addr)}$path",
            { response -> latestResponse = response },
            { error -> Toast.makeText(ctx, "Request failed: $error", Toast.LENGTH_LONG).show()})
        println("Sending a request to ${req.url}")
        Volley.newRequestQueue(ctx).add(req)
    }

     fun getAddress(hwAddress: String): String {
        val clients: List<WifiApControl.Client> = WifiApControl.getInstance(ctx).clients
        var address = ""
        if(clients.isNotEmpty()) {
            for (client in clients) {
                if(client.hwAddr.equals(hwAddress, true)) {
                    address = client.ipAddr
                    break
                }
            }
        } else {
            Toast.makeText(ctx, "No backend servers connected!", Toast.LENGTH_LONG).show()
        }
        if(address == "") Toast.makeText(ctx, "Backend server not connected!", Toast.LENGTH_LONG).show()
        return address
    }

    fun setModuleColor(modulePos: Int, colorId: Int) {
        sendRequest(Devices.MAIN, "?set_module_col=${modulePos}|${colorId}")
    }

    fun setServo(servoId: Int, speed: Int) {
        sendRequest(Devices.MAIN, "?set_servo=${servoId}|${speed}")
    }

    fun setMotor(speed: Int) {
        sendRequest(Devices.MAIN, "?set_motor=${speed}")
    }
}