package io.stacknix.merlin.android.demo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import io.stacknix.merlin.android.demo.databinding.ActivityMainBinding
import io.stacknix.merlin.android.demo.databinding.ItemProductViewBinding
import io.stacknix.merlin.android.demo.models.Project
import io.stacknix.merlin.android.demo.samples.AuthUtilSample
import io.stacknix.merlin.android.demo.samples.RecyclerAdapterSample
import io.stacknix.merlin.db.Merlin
import io.stacknix.merlin.db.MerlinResult
import io.stacknix.merlin.db.annotations.Order
import io.stacknix.merlin.db.commons.Flag
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val service = JsonRPCService(Project::class.java, AuthUtilSample.getClient())

        binding.addData.setOnClickListener {
            val first = Merlin.where(Project::class.java)
                .sort("id", Order.DESC).first()

            val product = Project()
            product.uuid = UUID.randomUUID().toString()
            product.name = "Something"
            product.id = if (first == null) 1 else first.id + 1
            product.flag = Flag.NEED_CREATE
            product.save()

            GlobalScope.launch {
                withContext(IO) {
                   UploadWorker.sync(applicationContext)
                }
            }
        }

        val result = Merlin.where(Project::class.java)
            .notIn("id", arrayOf(200L, 700L))
            .sort("id", Order.DESC)
            .find()
        binding.recyclerView.adapter = ProductAdapter(this, result, service)


        GlobalScope.launch {
            withContext(IO) {
                service.search(applicationContext)
            }
        }

        FirebaseService.registerFirebaseDevice(AuthUtilSample.getClient())
    }


}

internal class ProductAdapter(private val context: Context,
                              result: MerlinResult<Project>,
                              private val service: JsonRPCService<Project>) :
    RecyclerAdapterSample<ItemProductViewBinding, Project>(result) {

    override fun getBinding(inflater: LayoutInflater, parent: ViewGroup): ItemProductViewBinding {
        return ItemProductViewBinding.inflate(inflater, parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onBind(holder: ViewHolder<ItemProductViewBinding>, item: Project) {
        with(holder.binding) {
            itemTitle.text = item.name + ": " + item.id
            holder.itemView.setOnClickListener {
                item.flag = Flag.NEED_UNLINK
                item.save()
                UploadWorker.sync(context)
            }
            holder.itemView.setOnLongClickListener {
                item.name = item.name+":"
                item.flag = Flag.NEED_WRITE
                item.save()
                UploadWorker.sync(context)
                true
            }
        }
    }
}
