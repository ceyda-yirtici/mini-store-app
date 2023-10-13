package com.example.ministore.ui

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ministore.R
import com.example.ministore.model.Product

class ProductRecyclerAdapter( ) : RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder>() {

    private var productList: ArrayList<Product> = arrayListOf()


    fun updateList(item: List<Product>) {
        productList.clear()
        item.let {
            productList.addAll(it)
        }
        notifyDataSetChanged()

    }


    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(product: Product) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])

    }


}