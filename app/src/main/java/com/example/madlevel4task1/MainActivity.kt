package com.example.madlevel4task1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val products = arrayListOf<Product>()
    private val shoppingListAdapter = ShoppingListAdapter(products)

    private lateinit var productRepository: ProductRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        productRepository = ProductRepository(applicationContext)
        getShoppingListFromDatabase()

        initViews()

        fab_delete.setOnClickListener {
            onListDelete()
        }

        fab_add.setOnClickListener {
            onAddProduct()
        }
    }


    private fun initViews() {
        rv_shoppinglist.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        rv_shoppinglist.adapter = shoppingListAdapter
        rv_shoppinglist.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

        //createItemTouchHelper

        rv_shoppinglist.apply {
            setHasFixedSize(true)
        }
    }

    private fun getShoppingListFromDatabase() {
        mainScope.launch {
            val shoppingList = withContext(Dispatchers.IO) {
                productRepository.getAllProducts()
            }
            this@MainActivity.products.clear()
            this@MainActivity.products.addAll(shoppingList)
            this@MainActivity.shoppingListAdapter.notifyDataSetChanged()
        }
    }



    private fun onListDelete() {
        // Delete the entire list!
    }

    private fun onAddProduct() {
        // Open popup pls
    }

}