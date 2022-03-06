package io.stacknix.merlin.android.demo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
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
import java.lang.Exception
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val service = JsonRPCService(Project::class.java, AuthUtilSample.getClient())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addData.setOnClickListener {
            val item = Project()
            item.uuid = UUID.randomUUID().toString()
            item.name = "Something"
            item.save()

            GlobalScope.launch(IO) {
                try {
                    val service = JsonRPCService(Project::class.java, AuthUtilSample.getClient())
                    val result = service.create(item)
                }catch (e: Exception){
                    // check
                }
            }
        }

        val result = Merlin.where(Project::class.java)
            .notIn("id", arrayOf(200L, 700L))
            .sort("id", Order.DESC)
            .find()
        binding.recyclerView.adapter = ProductAdapter(this, this, result, service)



        binding.refreshData.setOnClickListener {
            GlobalScope.launch {
                withContext(IO) {
                    //service.search(applicationContext)
                }
            }
        }

        GlobalScope.launch {
            withContext(IO) {
                //service.search(applicationContext)
            }
        }

        FirebaseService.registerFirebaseDevice(AuthUtilSample.getClient())
    }

}

internal class ProductAdapter(private val lifecycleOwner: LifecycleOwner, private val context: Context,
                              result: MerlinResult<Project>,
                              private val service: JsonRPCService<Project>) :
    RecyclerAdapterSample<ItemProductViewBinding, Project>(lifecycleOwner, result) {

    override fun getBinding(inflater: LayoutInflater, parent: ViewGroup): ItemProductViewBinding {
        return ItemProductViewBinding.inflate(inflater, parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onBind(holder: ViewHolder<ItemProductViewBinding>, item: Project) {
        with(holder.binding) {
            itemTitle.text = item.name + ": " + item.id
            holder.itemView.setOnClickListener {
               // item.flag = Flag.NEED_UNLINK
                item.save()
                UploadWorker.sync(context)
            }
            holder.itemView.setOnLongClickListener {
                item.name = item.name+":"
               // item.flag = Flag.NEED_WRITE
                item.save()
                UploadWorker.sync(context)
                true
            }
        }
    }
}
