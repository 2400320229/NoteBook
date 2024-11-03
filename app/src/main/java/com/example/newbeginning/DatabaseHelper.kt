package com.example.newbeginning

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Parcel
import android.os.Parcelable
import android.provider.ContactsContract



class DatabaseHelper(context: Context):SQLiteOpenHelper(context,DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME="note.db"
        private const val DATABASE_VERSION=1
        const val TABLE_NOTES = "notes"
        const val COLUMN_ID = "id"
        const val COLUMN_TIME = "time"
        const val COLUMN_CONTENT = "content"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NOTES (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TIME TEXT," +
                "$COLUMN_CONTENT TEXT)"
        if (db != null) {
            db.execSQL(createTable)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
            onCreate(db)
        }
    }
    fun addNote(time:String,context:String){
        val db=writableDatabase
        val value=ContentValues().apply {
            put(COLUMN_TIME,time)
            put(COLUMN_CONTENT,context)

        }
        db.insert(TABLE_NOTES,null,value)
        db.close()
    }

    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NOTES,
            null,
            null,
            null,
            null,
            null,
            null)

        if (cursor.moveToFirst()) {
            do {
                val note = Note(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TIME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
                )
                notes.add(note)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return notes
    }
    fun updateNote(note: Note){
        val db=writableDatabase
        val value=ContentValues().apply {
            put(COLUMN_TIME, note.time)
            put(COLUMN_CONTENT, note.context)
        }
        db.update(TABLE_NOTES, value, "$COLUMN_ID = ?", arrayOf(note.id.toString()))
        db.close()

    }
    fun deleteNOte(id: Int){
        val db=writableDatabase
        db.delete(TABLE_NOTES,"$COLUMN_ID =?", arrayOf(id.toString()))
        db.close()
    }
    fun getNoteById(noteId: Int): String? {
        val db=writableDatabase
        val cursor = db.query(TABLE_NOTES,
            null,
            "$COLUMN_ID = ?",
            arrayOf(noteId.toString()),
            null,
            null,
            null)
        return if (cursor != null&&cursor.moveToFirst()){
            val note=Note(
                cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TIME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT))
            )
            cursor.close()
            db.close()
            note.context
        }else{
            cursor?.close()
            db.close()
            null // 如果没有找到笔记，返回 null
        }
    }

}

//Note 类实现 Parcelable 接口，以便可以通过 Bundle 传递
data class Note(val id:Int,val time:String,val context: String):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()?:"",
        parcel.readString()?:""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(time)
        parcel.writeString(context)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }

}