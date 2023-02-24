package madeleaan.noustea.ui.strany

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import madeleaan.noustea.R
import madeleaan.noustea.RequestHandler

class ModuleDialog: DialogFragment() {
    private lateinit var curView: View
    private var moduleChar = ' '
    private var modulePos = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        curView = inflater.inflate(R.layout.dialog_module, container, false)
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        val handler: RequestHandler = RequestHandler(context)
        var module: StranyFragment.Modules = StranyFragment.Modules.MODULE1
        moduleChar = arguments?.getChar("moduleId")!!
        modulePos = arguments?.getInt("modulePos")!!
        when(moduleChar) {
            'a' -> module = StranyFragment.Modules.MODULE1
            'b' -> module = StranyFragment.Modules.MODULE2
            'c' -> module = StranyFragment.Modules.MODULE3
            'd' -> module = StranyFragment.Modules.MODULE4
        }

        val textType: TextView = curView.findViewById(R.id.module_info_type)
        val textDesc: TextView = curView.findViewById(R.id.module_info_desc)
        val textMass: TextView = curView.findViewById(R.id.module_info_mass)
        val textMassLayout: RelativeLayout = curView.findViewById(R.id.module_layout_mass)

        val editTypeText: EditText = curView.findViewById(R.id.module_edit_type_text)
        val editTypeButton: ImageButton = curView.findViewById(R.id.module_edit_type_btn)
        val editTypeLayout: RelativeLayout = curView.findViewById(R.id.module_edit_type)

        val editDescText: EditText = curView.findViewById(R.id.module_edit_desc_text)
        val editDescButton: ImageButton = curView.findViewById(R.id.module_edit_desc_btn)
        val editDescLayout: RelativeLayout = curView.findViewById(R.id.module_edit_desc)

        val editMassText: EditText = curView.findViewById(R.id.module_edit_mass_text)
        val editMassButton: ImageButton = curView.findViewById(R.id.module_edit_mass_btn)
        val editMassLayout: RelativeLayout = curView.findViewById(R.id.module_edit_mass)

        fun EditText.onDone(callback: () -> Unit) {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    callback.invoke()
                    true
                }
                false
            }
        }
        fun View.showKeyboard() {
            this.requestFocus()
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
        fun View.hideKeyboard() {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        }
        fun setInfo(infoType: InfoTypes, value: String) {
             sharedPref!!.edit().putString(infoType.saveKey + moduleChar, value).apply()
        }

        fun exitEditType() {
            textType.text = editTypeText.text
            editTypeText.hideKeyboard()
            editTypeLayout.visibility = View.GONE
            textType.visibility = View.VISIBLE
        }
        fun exitEditDesc() {
            textDesc.text = editDescText.text
            editDescText.hideKeyboard()
            editDescLayout.visibility = View.GONE
            textDesc.visibility = View.VISIBLE
        }
        fun exitEditMass() {
            textMass.text = editMassText.text
            editMassText.hideKeyboard()
            editMassLayout.visibility = View.GONE
            textMassLayout.visibility = View.VISIBLE
        }

        textType.apply {
            setOnClickListener {
                editTypeText.setText(textType.text)
                textType.visibility = View.GONE
                editTypeLayout.visibility = View.VISIBLE
                editTypeText.requestFocus()
                editTypeText.showKeyboard()
            }
            text = sharedPref!!.getString(InfoTypes.TYPE.saveKey + moduleChar, getString(R.string.module_placeholder_type))
        }
        editTypeText.onDone {
            exitEditType()
            setInfo(InfoTypes.TYPE, textType.text.toString())
        }
        editTypeText.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) {
                exitEditType()
                setInfo(InfoTypes.TYPE, textType.text.toString())
            }
        }
        editTypeButton.setOnClickListener {
            exitEditType()
            setInfo(InfoTypes.TYPE, textType.text.toString())
        }

        textDesc.apply {
            setOnClickListener {
                editDescText.setText(textDesc.text)
                textDesc.visibility = View.GONE
                editDescLayout.visibility = View.VISIBLE
                editDescText.requestFocus()
                editDescText.showKeyboard()
            }
            text = sharedPref!!.getString(InfoTypes.DESC.saveKey + moduleChar, getString(R.string.module_placeholder_desc))
        }
        editDescText.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) {
                exitEditDesc()
                setInfo(InfoTypes.DESC, textDesc.text.toString())
            }
        }
        editDescButton.setOnClickListener {
            exitEditDesc()
            setInfo(InfoTypes.DESC, textDesc.text.toString())
        }

        textMass.apply {
            setOnClickListener {
                editMassText.setText(textMass.text)
                textMassLayout.visibility = View.GONE
                editMassLayout.visibility = View.VISIBLE
                editMassText.requestFocus()
                editMassText.showKeyboard()
            }
            text = sharedPref!!.getString(InfoTypes.MASS.saveKey + moduleChar, getString(R.string.module_placeholder_mass))
        }
        editMassText.onDone {
            exitEditMass()
            setInfo(InfoTypes.MASS, textMass.text.toString())
        }
        editMassText.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) {
                exitEditMass()
                setInfo(InfoTypes.MASS, textMass.text.toString())
            }
        }
        editMassButton.setOnClickListener {
            exitEditMass()
            setInfo(InfoTypes.MASS, textMass.text.toString())
        }

        val spinner: Spinner = curView.findViewById(R.id.module_edit_color)
        ArrayAdapter.createFromResource(
            curView.context, R.array.colors_array, android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.apply {
            setSelection(sharedPref!!.getInt("module_color_$moduleChar", 0))
        }

        curView.findViewById<ImageButton>(R.id.module_info_apply).setOnClickListener {
            sharedPref!!.edit().putInt("module_color_$moduleChar", spinner.selectedItemPosition).apply()
            handler.setModuleColor(modulePos, spinner.selectedItemPosition)
        }

        return curView
    }
    private enum class InfoTypes(val saveKey: String) {
        TYPE("module_type_"), DESC("module_desc_"), MASS("module_mass_")
    }
}