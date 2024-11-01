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
import kotlinx.coroutines.flow.callbackFlow

interface OnDataPass{
    fun OnDataPass(context: String,note: Note)
}

class Edit_fragment : Fragment() {
    private lateinit var note: Note
    private lateinit var editTextContext:EditText


    private lateinit var dataPassr: OnDataPass
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPassr =context as? OnDataPass
            ?:throw ClassCastException("$context must implement OnDataPass")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note= it.getParcelable("note_key")?:Note(1,"","")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view=inflater.inflate(R.layout.fragment_edit_fragment, container, false)


        view?.let {

            editTextContext = view.findViewById(R.id.new_context)
            val button:Button=view.findViewById(R.id.save)

            editTextContext.setText(note.context)
            button.setOnClickListener{

                val inputContext=editTextContext.text.toString().trim()
                if(inputContext.isNotEmpty()){
                    dataPassr.OnDataPass(inputContext,note)


                }else {

                    Toast.makeText(context, "请填写所有信息", Toast.LENGTH_SHORT).show()
                }
            }

        }
        return view

    }


}