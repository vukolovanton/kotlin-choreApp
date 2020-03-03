package com.example.choreapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.choreapp.R
import com.example.choreapp.data.ChoreListAdapter
import com.example.choreapp.data.ChoresDatabaseHandler
import com.example.choreapp.model.Chore
import kotlinx.android.synthetic.main.activity_chore_list.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup.view.*

class ChoreListActivity : AppCompatActivity() {
    private var dbHandler: ChoresDatabaseHandler? = null

    private var adapter: ChoreListAdapter? = null
    private var choreList: ArrayList<Chore>? = null
    private var choreListItems: ArrayList<Chore>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var dialogBuilder: AlertDialog.Builder? = null
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chore_list)

        //Инстанс бд
        dbHandler = ChoresDatabaseHandler(this)

        //Устанавливаем адаптер
        choreList = ArrayList<Chore>()
        choreListItems = ArrayList()
        layoutManager = LinearLayoutManager(this)
        adapter = ChoreListAdapter(choreListItems!!, this)

        //setup list = recyclerview
        recyclerViewId.layoutManager = layoutManager
        recyclerViewId.adapter = adapter

        //Загружаем задачи
        choreList = dbHandler!!.readChores()
        choreList!!.reverse()

        for (i in choreList!!.iterator()) {
            val chore = Chore()
            chore.choreName = i.choreName
            chore.assignedBy = i.assignedBy
            chore.assignedTo = i.assignedTo
            chore.id = i.id

            choreListItems!!.add(chore)
        }
        adapter!!.notifyDataSetChanged()

    }

    //Создаем кнопку меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override  fun onOptionsItemSelected(item: MenuItem): Boolean {
        createPopupDialog()
        return super.onOptionsItemSelected(item)
    }

    private fun createPopupDialog() {
        val view = layoutInflater.inflate(R.layout.popup, null)
        //Создаем референсы кнопок
        val choreName = view.popEnterChore
        val assignedBy = view.popEnterAssignedBy
        val assignedTo = view.popEnterAssignedTo
        val saveButton = view.popSaveChour

        dialogBuilder = AlertDialog.Builder(this).setView(view)
        dialog = dialogBuilder!!.create()
        dialog?.show()

        saveButton.setOnClickListener {
            //Записываем содержимое текстовых полей в переменные
            val name = choreName.text.toString()
            val aBy = assignedBy.text.toString()
            val aTo = assignedTo.text.toString()

            if (!TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(aBy)
                && !TextUtils.isEmpty(aTo)
            ) {
                val chore = Chore()
                chore.choreName = name
                chore.assignedBy = aBy
                chore.assignedTo = aTo

                dbHandler!!.createChore(chore)
                dialog!!.dismiss()

                startActivity(Intent(this, ChoreListActivity::class.java))
                Toast.makeText(this, "Chore added!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
