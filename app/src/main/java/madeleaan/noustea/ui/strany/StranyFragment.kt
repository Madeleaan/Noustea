package madeleaan.noustea.ui.strany

import android.graphics.Color
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import madeleaan.noustea.R

class StranyFragment: Fragment() {
    private lateinit var curView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        curView = inflater.inflate(R.layout.fragment_strany, container, false)
        curView.findViewById<FloatingActionButton>(R.id.strany_fab).setOnClickListener { updateModules(curView) }
        updateModules(curView)
        return curView
    }

    private fun updateModules(view: View) { //1 is left up, 2 is right up, 3 is left down, 4 is right down
                          //a is MODULE1, b is MODULE2, c is MODULE3, d is MODULE4
        for(i in 1..4) view.findViewById<ImageView>(view.resources.getIdentifier("module_img_$i", "id", view.context.packageName)).alpha = 0f
        for(i in 1..4) view.findViewById<ImageView>(view.resources.getIdentifier("strany_corner_$i", "id", view.context.packageName)).alpha = 0.3f
        for(i in 1..4) view.findViewById<Button>(view.resources.getIdentifier("strany_btn_$i", "id", view.context.packageName)).apply {
            setBackgroundColor(Color.argb(255, 128, 128, 128))
            setOnClickListener {  }
        }

        val req = StringRequest(
            Request.Method.GET, "http://192.168.43.35/get_modules",
            {
                response ->
                if(response.isNotEmpty()) {
                    response.split(";").forEach {
                        view.findViewById<ImageView>(view.resources.getIdentifier("module_img_${it[0]}", "id", view.context.packageName)).animate()
                            .alpha(1f).setDuration(1000).setListener(null)
                        view.findViewById<ImageView>(view.resources.getIdentifier("strany_corner_${it[0]}", "id", view.context.packageName)).animate()
                            .alpha(1f).setDuration(1000).setListener(null)
                        val transition = TransitionDrawable(arrayOf(Color.argb(255, 128, 128, 128).toDrawable(), Color.parseColor("#4A00E0").toDrawable()))
                        view.findViewById<Button>(view.resources.getIdentifier("strany_btn_${it[0]}", "id", view.context.packageName)).background = transition
                        transition.startTransition(1000)
                        view.findViewById<Button>(view.resources.getIdentifier("strany_btn_${it[0]}", "id", view.context.packageName)).setOnClickListener {_ ->
                            val dialogFragment = ModuleDialog()
                            val bundle = Bundle()
                            bundle.putChar("moduleId", it[1])
                            bundle.putInt("modulePos", it[0].digitToInt())
                            dialogFragment.arguments = bundle
                            dialogFragment.show(parentFragmentManager, "Module Dialog")
                        }
                    }
                } else Toast.makeText(curView.context, "response somehow empty!", Toast.LENGTH_LONG).show()
            },
            {error -> Toast.makeText(curView.context, "Request failed: $error", Toast.LENGTH_LONG).show()})
        println("Sending a request to ${req.url}")
        Volley.newRequestQueue(context).add(req)
    }

    enum class Modules(val mass: String, val type: String, val description: String) {
        MODULE1("1 t", "Module type 1", """
            |This is a cool description for the module 1
            |it can even support multiple lines
            |wow so cool!
        """.trimMargin()),
        MODULE2("2 t", "Module type 2", """
            |This is a cool description for the module 2
            |it can even support multiple lines
            |wow so cool!
        """.trimMargin()),
        MODULE3("3 t", "Module type 3", """
            |This is a cool description for the module 3
            |it can even support multiple lines
            |wow so cool!
        """.trimMargin()),
        MODULE4("4 t", "Module type 4", """
            |This is a cool description for the module 4
            |it can even support multiple lines
            |wow so cool!
        """.trimMargin()),

    }
}