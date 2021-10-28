package io.stacknix.merlin.android.demo

import android.os.Bundle
import android.telecom.TelecomManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import io.stacknix.merlin.android.demo.databinding.ActivityMainBinding
import io.stacknix.merlin.android.demo.databinding.ItemProductViewBinding
import io.stacknix.merlin.android.demo.models.Product
import io.stacknix.merlin.android.demo.samples.RecyclerAdapter
import io.stacknix.merlin.db.Merlin
import io.stacknix.merlin.db.MerlinResult
import io.stacknix.merlin.db.annotations.Order


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addData.setOnClickListener {
            val first = Merlin.where(Product::class.java)
                .sort("id", Order.DESC).first()
            val product = Product()
            product.name = "Something"
            product.id = if (first == null) 1 else first.id + 1
            product.save()

        }

        val result = Merlin.where(Product::class.java)
            .equal("name", "Something")
            .notIn("id", arrayOf(200L, 700L))
            .sort("id", Order.DESC)
            .find()
        binding.recyclerView.adapter = ProductAdapter(result)


    }
}

internal class ProductAdapter(result: MerlinResult<Product>) :
    RecyclerAdapter<ItemProductViewBinding, Product>(result) {

    override fun getBinding(inflater: LayoutInflater, parent: ViewGroup): ItemProductViewBinding {
        return ItemProductViewBinding.inflate(inflater, parent, false)
    }

    override fun onBind(binding: ItemProductViewBinding, item: Product) {
        with(binding) {
            itemTitle.text = item.name + ":" + item.id
        }
    }
}
