package io.stacknix.merlin.android.demo.samples

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.stacknix.merlin.android.demo.databinding.ItemProductViewBinding
import io.stacknix.merlin.android.demo.models.Product
import io.stacknix.merlin.db.Merlin
import io.stacknix.merlin.db.MerlinResult
import io.stacknix.merlin.db.android.DiffUtilWrapper


class TestAdapter(val context: Context) :
    RecyclerView.Adapter<TestAdapter.ViewHolder>() {

    var result: MerlinResult<Product> = Merlin.where(Product::class.java).find()

    init {
        result.listen {
            val dispatcher = DiffUtilWrapper.calculate(result, it)
            Handler(Looper.getMainLooper()).post {
                this.result = it
                dispatcher.dispatchUpdatesTo(this)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProductViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            result[position].apply {
                binding.itemTitle.text = name
            }
        }
    }

    inner class ViewHolder(val binding: ItemProductViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }
}