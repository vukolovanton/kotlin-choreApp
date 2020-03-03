package com.example.choreapp.data

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.choreapp.R
import com.example.choreapp.activity.ChoreListActivity
import com.example.choreapp.activity.MainActivity
import com.example.choreapp.model.Chore

class ChoreListAdapter(private val list: ArrayList<Chore>, private val context: Context): RecyclerView.Adapter<ChoreListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ChoreListAdapter.ViewHolder {
        //Создаем view из xml
        val view = LayoutInflater.from(context).inflate(R.layout.listrow, parent, false)
        return ViewHolder(view, context, list)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChoreListAdapter.ViewHolder, position: Int) {
        holder.bindViews(list[position])
    }

    inner class ViewHolder(itemView: View, context: Context, list: ArrayList<Chore>): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        //Находим поля в активности
        private var choreName = itemView.findViewById(R.id.listChoreName) as TextView
        private var assignedBy = itemView.findViewById(R.id.listAssignedBy) as TextView
        private var assignedDate = itemView.findViewById(R.id.listDate) as TextView
        private var assignedTo = itemView.findViewById(R.id.listAssignedTo) as TextView

        private var deleteButton = itemView.findViewById(R.id.listDeleteButton) as Button

        //Вставляем в поля данные
        fun bindViews(chore: Chore) {
            choreName.text = chore.choreName
            assignedBy.text = chore.assignedBy
            assignedTo.text = chore.assignedTo
            assignedDate.text = chore.showHumanDate(System.currentTimeMillis())

            deleteButton.setOnClickListener(this)
        }

        //Кнопка "удалить"
        override fun onClick(v: View?) {
            val mPosition: Int = adapterPosition
            val chore = list[mPosition]

            when(v!!.id) {
                deleteButton.id -> {
                    deleteChore(chore.id!!.toInt())
                    list.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }
            }
        }

        private fun deleteChore(id: Int) {
            val db: ChoresDatabaseHandler = ChoresDatabaseHandler(context)
            db.deleteChore(id)

            val itemsCount = db.getChoresCount()
            if (itemsCount == 0) {
                Toast.makeText(context, "Time to add new tasks", Toast.LENGTH_LONG).show()
                val intent = Intent(context, MainActivity::class.java)
                startActivity(context, intent, null)
            }

        }
    }

}