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
import com.example.ministore.util.Constants
import com.example.movieproject.room.CartProduct
import java.util.Locale

class ProductRecyclerAdapter: RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder>() {

    private var productList: ArrayList<Product> = arrayListOf()
    private var cartList: List<CartProduct> = listOf()
    private var page: Int = 1
    private lateinit var listener: OnClickListener
    interface OnClickListener {
        fun onAddButtonClick(
            product: Product
        )
        fun onReduceButtonClick(
            product: Product
        )

    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(item: List<Product>) {
        productList.clear()
        item.let {
            productList.addAll(it)
        }
        notifyDataSetChanged()

    }

    fun updatePageNo(item:Int){
        page = item
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateCartList(it: List<CartProduct>) {
        cartList = listOf()
        it.let {
            cartList = it
        }
        notifyDataSetChanged()

    }


    fun setOnClickListener(listener: OnClickListener){
        this.listener = listener
    }


    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {

            val addView : TextView = itemView.findViewById(R.id.productAdd)
            val reduceView: TextView = itemView.findViewById(R.id.productReduce)
            addView.setOnClickListener {
                val position = adapterPosition
                listener.onAddButtonClick(productList[position])
            }
            reduceView.setOnClickListener {
                val position = adapterPosition
                listener.onReduceButtonClick(productList[position])
            }

        }

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            val photo: ImageView = itemView.findViewById(R.id.photoGrid)
            val name: TextView = itemView.findViewById(R.id.title)
            val price: TextView = itemView.findViewById(R.id.price)
            val cartProductAmount: TextView = itemView.findViewById(R.id.cartProductAmount)
            val reduceProductAmount : TextView = itemView.findViewById(R.id.productReduce)


            Glide.with(photo).load(product.imageUrl).
            placeholder(R.drawable.baseline_placeholder_24) // drawable as a placeholder
                .error(R.drawable.baseline_placeholder_24).into(photo)

            name.text = product.name

            val amountOfProduct = cartList.find { it.product_id == product.id }?.amount ?: 0
            if (page == Constants.PRODUCTS_PAGE) {

                // If any item present display +, - buttons and quantity if not only the plus button.
                price.text = product.currency + String.format(Locale.US, Constants.PRICE_FORMAT, product.price)
                if (amountOfProduct == 0) {
                    cartProductAmount.visibility = View.GONE
                    reduceProductAmount.visibility = View.GONE
                } else {
                    cartProductAmount.visibility = View.VISIBLE
                    reduceProductAmount.visibility = View.VISIBLE
                }
            }
            else {
                price.text = product.currency +
                        String.format(Locale.US, Constants.PRICE_FORMAT,product.price*amountOfProduct)
            }
            cartProductAmount.text = amountOfProduct.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var layout: Int
        if (page == Constants.PRODUCTS_PAGE)  layout = R.layout.item_product
        else layout = R.layout.item_cart_product
        val itemView = inflater.inflate(layout, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])

    }


}