package com.example.newbeginning

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue


class MainActivity : AppCompatActivity(),NoteAdapter.OnNoteClickListener,OnDataPass {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private var notesList:MutableList<Note> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val editText_time:EditText=findViewById(R.id.et_time)
        val editText_context:EditText=findViewById(R.id.et_context)
        val addbutton:Button=findViewById(R.id.add_note_button)


        databaseHelper=DatabaseHelper(this)
        recyclerView=findViewById(R.id.recyclerView)
        recyclerView.layoutManager=LinearLayoutManager(this)

        //设置间隔为 10dp
        val spacingInPixels=TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()
        recyclerView.addItemDecoration(SpaceItemDecoration(spacingInPixels))

        adapter=NoteAdapter(notesList,this)
        recyclerView.adapter=adapter

        loadNotes()

        addbutton.setOnClickListener{
            val addtime=editText_time.text.toString().trim()
            val addcontent=editText_context.text.toString().trim()
            if(addtime.isNotEmpty()||addcontent.isNotEmpty()) {
                addNote(addtime, addcontent)
                editText_time.text.clear()
                editText_context.text.clear()
            }else{
                Toast.makeText(this,"请输入信息~",Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun loadNotes(){
        notesList.clear()
        notesList.addAll(databaseHelper.getAllNotes())
        adapter.notifyDataSetChanged()
    }
    private fun addNote(addtime:String,addcontext:String){
        val time = addtime
        val content = addcontext
        databaseHelper.addNote(time, content)
        loadNotes()
    }


    override fun showFragment(bundle1: Bundle) {

        val fragment=Edit_fragment().apply {
            arguments=bundle1
        }
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.addToBackStack(null) // 可选，允许后退
        fragmentTransaction.commit()
        databaseHelper.getAllNotes()

    }

    private fun onEdit(note: Note,context: String) {
        databaseHelper.updateNote(note.copy(context = context))
        loadNotes()
    }

    override fun onDelete(note: Note) {
        val builder=AlertDialog.Builder(this)
        builder.setTitle("确定要删除这条笔记吗?")
        builder.setMessage("确定要删除这条笔记吗1")
        builder.setPositiveButton("确认"){dialog,which->
            databaseHelper.deleteNOte(note.id)
            loadNotes()
        }
        builder.setNegativeButton("取消"){dialog,which->
            dialog.dismiss()
        }
        builder.create().show()
    }
    private fun removeF(){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment != null) {
            fragmentTransaction.remove(fragment)
        }
        fragmentTransaction.commit()
    }

    override fun OnDataPass( context: String,note: Note) {

        onEdit(note,context)

        removeF()
    }


}



