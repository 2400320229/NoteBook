package com.example.newbeginning

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragment : Fragment() {
    private lateinit var dataPass:transAddNote

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPass = context as? transAddNote
           ?: throw ClassCastException("$context must implement OnDataPass")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit, container, false)
        val editText_time:EditText=view.findViewById(R.id.et_time)
        val editText_context:EditText=view.findViewById(R.id.et_context)
        val button:Button=view.findViewById(R.id.add_note_button)
        button.setOnClickListener {
            val addtime = editText_time.text.toString().trim()
            val addcontent = editText_context.text.toString().trim()
            if (addtime.isNotEmpty()||addcontent.isNotEmpty()) {
                dataPass.addNote(addtime, addcontent)
                editText_context.text.clear()
                editText_time.text.clear()
                Toast.makeText(context,"添加成功",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"请输入信息",Toast.LENGTH_SHORT).show()
            }

        }
        return view
    }

    companion object {


    }
}