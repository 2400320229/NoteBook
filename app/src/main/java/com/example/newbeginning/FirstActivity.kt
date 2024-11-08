package com.example.newbeginning

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frist_layout)
        val button2: Button =findViewById(R.id.button2)
        button2.setOnClickListener {

            val intent1 = Intent(this, zhu_ce::class.java)
            startActivity(intent1)
        }

        val button1:Button=findViewById(R.id.button1)
        val editText1: EditText =findViewById(R.id.e1)
        val editText2: EditText =findViewById(R.id.e2)

        button1.setOnClickListener {
            val sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE)
            val inputTextUserName = editText1.text.toString().trim()
            val inputTextPassWord = editText2.text.toString().trim()

            if(inputTextUserName.isEmpty()&&inputTextPassWord.isEmpty() ){
                    Toast.makeText(this,"请输入用户名和密码",
                        Toast.LENGTH_SHORT).show()
            }else if(inputTextPassWord==sharedPreferences.getString(inputTextUserName,"")) {

                val intent0 = Intent(this,MainActivity::class.java)
                startActivity(intent0)
                Toast.makeText(
                    this, "登录成功",
                    Toast.LENGTH_SHORT
                ).show()//toast 在点击这个button时可以提示引号中的内容
            }
            else {
                Toast.makeText(this,"密码或用户名输入错误",Toast.LENGTH_SHORT).show()
            }
        }

    }

   /* override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }*/

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_item->Toast.makeText(this,"you clicked add",
                Toast.LENGTH_SHORT).show()
            R.id.remove_item->Toast.makeText(this,"you clicked remove",
                Toast.LENGTH_SHORT).show()
        }
        return true
    }*/
}