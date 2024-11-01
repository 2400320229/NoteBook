package com.example.newbeginning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(private val notes:MutableList<Note>, private val listener:OnNoteClickListener):RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {


    interface OnNoteClickListener{
        fun showFragment(note: Bundle)
        fun onDelete(note: Note)
    }
    inner class NoteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private val timeTextView: TextView = itemView.findViewById(R.id.note_time)
        private val contentTextView: TextView = itemView.findViewById(R.id.note_content)
        private val editButton: Button = itemView.findViewById(R.id.edit_button)
        private val deleteButton: Button = itemView.findViewById(R.id.delete_button)

        fun bind(note: Note){
            timeTextView.text=note.time
            contentTextView.text=note.context


            editButton.setOnClickListener {
                val bundle= Bundle().apply {
                    putParcelable("note_key",note)
                    putString("note_key1",note.context)
                }

                listener.showFragment(bundle)
            }
            deleteButton.setOnClickListener { listener.onDelete(note) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.note_item,parent,false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }


    override fun getItemCount()=notes.size

}