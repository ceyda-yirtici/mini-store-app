package com.example.ministore.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ministore.R
import com.example.ministore.model.Product
import com.example.ministore.ui.products.ProductsFragment

class ProductRecyclerAdapter() : RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder>() {

    private var productList: ArrayList<Product> = arrayListOf()


    fun updateList(item: List<Product>) {
        productList.clear()
        item.let {
            productList.addAll(it)
        }
        notifyDataSetChanged()

    }


    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            val photo: ImageView = itemView.findViewById(R.id.photoGrid)
            val name: TextView = itemView.findViewById(R.id.title)
            val price: TextView = itemView.findViewById(R.id.price)
            val cartProductAmount: TextView = itemView.findViewById(R.id.cartProductAmount)

            Glide.with(photo).load(product.imageUrl).into(photo)
            name.text = product.name
            price.text = product.currency + product.price
            cartProductAmount.text = "0"
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