package com.example.newbeginning

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import androidx.activity.result.ActivityResult
import androidx.fragment.app.Fragment
interface transAddNote{
     fun addNote(addtime:String,addcontext:String)
}

class MainActivity : AppCompatActivity(),NoteAdapter.OnNoteClickListener,OnDataPass,transAddNote {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private var notesList:MutableList<Note> = mutableListOf()

    private lateinit var imageButton: ImageButton
    private val PICK_IMAGE_REQUEST = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val addbutton:Button=findViewById(R.id.add_note)
        val clearbutton:Button=findViewById(R.id.clear_list)


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
            showAddFragment()
        }
        clearbutton.setOnClickListener{
            val builder=AlertDialog.Builder(this)
            builder.setTitle("确认删除笔记")
            builder.setMessage("您确定要删除所有笔记吗？此操作无法撤销。")
            builder.setPositiveButton("确定") {dialog,which->
                clearData()
            }
            builder.setNegativeButton("取消"){dialog,which->
                dialog.dismiss()
            }
            builder.create().show()
        }



    }
    //打开文件夹还需要去AndroidManifest.xml中设置权限
    private fun openFileChoose(){
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,this.PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==PICK_IMAGE_REQUEST&&resultCode== Activity.RESULT_OK&&data!=null){
            val imageUri=data.data
            imageButton.setImageURI(imageUri) // 设置选中的图片
            imageButton.visibility = View.VISIBLE // 显示 ImageView
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1){
            if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                // 权限被授予，继续操作
            } else {
                // 权限被拒绝，处理相应逻辑
            }
        }
    }



    private fun loadNotes(){
        notesList.clear()
        notesList.addAll(databaseHelper.getAllNotes())
        adapter.notifyDataSetChanged()
    }
    override fun addNote(addtime:String,addcontext:String){
        val time = addtime
        val content = addcontext
        databaseHelper.addNote(time, content)
        loadNotes()
        removeAddF()
    }
    private fun clearData(){
        val db=databaseHelper.writableDatabase
        try {
            db.execSQL("DELETE FROM ${DatabaseHelper.TABLE_NOTES}")
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='${DatabaseHelper.TABLE_NOTES}'") // 可选：重置自增长
            loadNotes()
        }catch (e:Exception){
            Log.e("clear error","数据清理出错:${e.message}")
        }finally {
            db.close()
        }

    }
    private fun showAddFragment(){
        val fragment=AddFragment()
        val fragmentTransaction=supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.add_fragment,fragment)
        fragmentTransaction.addToBackStack(null) // 可选，允许后退
        fragmentTransaction.commit()
        databaseHelper.getAllNotes()
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
    private fun removeEditF(){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment != null) {
            fragmentTransaction.remove(fragment)
        }
        fragmentTransaction.commit()
    }
    private fun removeAddF(){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = supportFragmentManager.findFragmentById(R.id.add_fragment)
        if (fragment != null) {
            fragmentTransaction.remove(fragment)
        }
        fragmentTransaction.commit()
    }

    override fun OnDataPass( context: String,note: Note) {

        onEdit(note,context)

        removeEditF()
    }


}



