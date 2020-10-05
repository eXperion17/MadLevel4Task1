package com.example.madlevel4task1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val products = arrayListOf<Product>()
    private val shoppingListAdapter = ShoppingListAdapter(products)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    private fun initViews() {
        rv_shoppinglist.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        rv_shoppinglist.adapter = shoppingListAdapter
        rv_shoppinglist.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

        fab_delete.setOnClickListener {
            onListDelete()
        }

        fab_add.setOnClickListener {
            onAddProduct()
        }
    }


    private fun onListDelete() {
        // Delete the entire list!
    }

    private fun onAddProduct() {
        // Open popup pls
    }

}