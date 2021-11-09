package io.stacknix.merlin.db.android

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import io.stacknix.merlin.db.MerlinObject
import io.stacknix.merlin.db.MerlinResult
import io.stacknix.merlin.db.android.DiffUtilWrapper


abstract class RecyclerAdapter<B : ViewBinding, T : MerlinObject>(private var result: MerlinResult<T>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder<B>>() {

    init {
        result.listen {
            val dispatcher = DiffUtilWrapper.calculate(result, it)
            Handler(Looper.getMainLooper()).post {
                this.result = it
                dispatcher.dispatchUpdatesTo(this)
            }
        }
    }

    abstract fun getBinding(inflater: LayoutInflater, parent: ViewGroup): B

    abstract fun onBind(holder: ViewHolder<B>, item: T)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<B> {
        return ViewHolder(getBinding(LayoutInflater.from(parent.context), parent))
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
        with(holder) {
            onBind(this, result[position])
        }
    }

    class ViewHolder<B : ViewBinding>(val binding: B) :
        RecyclerView.ViewHolder(binding.root)
}