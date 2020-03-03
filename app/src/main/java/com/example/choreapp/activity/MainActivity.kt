package com.example.choreapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.choreapp.R
import com.example.choreapp.data.ChoreListAdapter
import com.example.choreapp.data.ChoresDatabaseHandler
import com.example.choreapp.model.Chore
import kotlinx.android.synthetic.main.activity_chore_list.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var dbHandler: ChoresDatabaseHandler? = null
    private var adapter: ChoreListAdapter? = null
    private var choreList: ArrayList<Chore>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Инстанс бд
        dbHandler = ChoresDatabaseHandler(this)

        //Проверяем, если в бд что-то есть - сразу показываем экран со списком задач
        checkDB()

        saveChoreId.setOnClickListener {
            //Проверяем заполнены ли поля
            if (!TextUtils.isEmpty(enterChoreId.text.toString())
                && !TextUtils.isEmpty(assignedById.text.toString())
                && !TextUtils.isEmpty(assignToId.text.toString())
            ) {
                //Создаем новый экземпляр класса
                val chore = Chore()
                chore.choreName = enterChoreId.text.toString()
                chore.assignedBy = assignToId.text.toString()
                chore.assignedTo = assignedById.text.toString()
                //Сохраняем в бд
                saveToDB(chore)

                startActivity(Intent(this, ChoreListActivity::class.java))

            } else {
                Toast.makeText(this, "Please enter a chore", Toast.LENGTH_LONG).show()
            }
        }


    }

    private fun saveToDB(chore: Chore) {
        dbHandler!!.createChore(chore)
    }

    private fun checkDB() {
        val itemsCount = dbHandler!!.getChoresCount()
        if (itemsCount > 0) {
            startActivity(Intent(this, ChoreListActivity::class.java))
        }
    }
}