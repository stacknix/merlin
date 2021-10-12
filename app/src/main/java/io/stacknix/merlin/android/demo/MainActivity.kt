package io.stacknix.merlin.android.demo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import io.stacknix.merlin.android.demo.samples.RecyclerAdapter
import io.stacknix.merlin.android.demo.databinding.ActivityMainBinding
import io.stacknix.merlin.android.demo.databinding.ItemProductViewBinding
import io.stacknix.merlin.android.demo.models.Product
import io.stacknix.merlin.db.Merlin
import io.stacknix.merlin.db.MerlinResult

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var i: Long = 0;
        binding.addData.setOnClickListener {
            i++
            val product = Product()
            product.name = "Something"
            product.id = i;
            product.save()
        }

        val result = Merlin.where(Product::class.java)
            .equal("name", "Something")
            .notIn("id", arrayOf(2L, 7L))
            .find()
        binding.recyclerView.adapter = ProductAdapter(result)

    }
}

internal class ProductAdapter(result: MerlinResult<Product>): RecyclerAdapter<ItemProductViewBinding, Product>(result){

    override fun getBinding(inflater: LayoutInflater, parent: ViewGroup): ItemProductViewBinding {
        return ItemProductViewBinding.inflate(inflater, parent, false)
    }

    override fun onBind(binding: ItemProductViewBinding, item: Product) {
        with(binding){
            itemTitle.text = item.name+":"+item.id
        }
    }

}
