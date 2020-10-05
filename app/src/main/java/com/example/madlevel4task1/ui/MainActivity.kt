package com.example.madlevel4task1.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel4task1.R
import com.example.madlevel4task1.repository.ProductRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val products = arrayListOf<Product>()
    private val shoppingListAdapter =
        ShoppingListAdapter(products)

    private lateinit var productRepository: ProductRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        productRepository =
            ProductRepository(
                applicationContext
            )
        getShoppingListFromDatabase()

        initViews()

        fab_delete.setOnClickListener {
            removeAllProducts()
        }

        fab_add.setOnClickListener {
            showAddProductDialog()
        }
    }


    private fun initViews() {
        rv_shoppinglist.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        rv_shoppinglist.adapter = shoppingListAdapter
        rv_shoppinglist.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))

        createItemTouchHelper().attachToRecyclerView(rv_shoppinglist)

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



    private fun removeAllProducts() {
        // Delete the entire list!
        mainScope.launch {
            withContext(Dispatchers.IO) {
                productRepository.deleteAllProducts()
            }
            getShoppingListFromDatabase()
        }
    }

    @SuppressLint("InflateParams")
    private fun showAddProductDialog() {
        /*These changes here solve the errors I have, though unfortunately it does not look like
         *the dialog window in the video. */
        val builder = AlertDialog.Builder(this,
            R.style.dialogTheme
        )
        builder.setTitle(getString(R.string.add_product_dialog_title))
        val dialogLayout = layoutInflater.inflate(R.layout.add_product_dialog, null)
        val productName = dialogLayout.findViewById<EditText>(R.id.txt_product_name)
        val amount  = dialogLayout.findViewById<EditText>(R.id.txt_amount)

        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.dialog_ok_btn) {
            _:DialogInterface, _: Int ->
            addProduct(productName, amount)
        }
        builder.show()
    }

    private fun addProduct(txtProductName: EditText, txtAmount: EditText) {
        if (validateFields(txtProductName, txtAmount)) {
            mainScope.launch {
                val product = Product(
                    name = txtProductName.text.toString(),
                    quantity = txtAmount.text.toString().toInt()
                )

                withContext(Dispatchers.IO) {
                    productRepository.insertProduct(product)
                }

                getShoppingListFromDatabase()
            }
        }
    }

    private fun validateFields(txtProductName: EditText, txtAmount: EditText):Boolean {
        return if (txtProductName.text.toString().isNotBlank()
                && txtAmount.text.toString().isNotBlank()) {
            true
        } else {
            Toast.makeText(applicationContext, "Please fill in the fields", Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val productToDelete = products[position]
                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        productRepository.deleteProduct(productToDelete)
                    }
                    getShoppingListFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }
}