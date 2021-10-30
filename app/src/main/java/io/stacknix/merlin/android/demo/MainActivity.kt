package io.stacknix.merlin.android.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.fabric.io.jsonrpc2.JsonRPCClient
import io.stacknix.merlin.android.demo.databinding.ActivityMainBinding
import io.stacknix.merlin.android.demo.databinding.ItemProductViewBinding
import io.stacknix.merlin.android.demo.models.Project
import io.stacknix.merlin.android.demo.samples.RecyclerAdapter
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

        val service = JsonRPCService(Project::class.java, getClient())

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
                withContext(IO){
                    service.synchronize(applicationContext)
                }
            }
        }

        val result = Merlin.where(Project::class.java)
            .notIn("id", arrayOf(200L, 700L))
            .sort("id", Order.DESC)
            .find()
        binding.recyclerView.adapter = ProductAdapter(result)


        GlobalScope.launch {
            withContext(IO){
                service.search(applicationContext)
            }
        }

        FirebaseService.registerFirebaseDevice(getClient())
    }

    private fun getClient(): JsonRPCClient{
        val url = "http://192.168.43.60:9000/api/v1/jsonrpc"
        val headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = "Bearer f04b2d14-2d36-44e7-8a2f-b7fa7e4fbe26"
        return JsonRPCClient(url, headers)
    }
}

internal class ProductAdapter(result: MerlinResult<Project>) :
    RecyclerAdapter<ItemProductViewBinding, Project>(result) {

    override fun getBinding(inflater: LayoutInflater, parent: ViewGroup): ItemProductViewBinding {
        return ItemProductViewBinding.inflate(inflater, parent, false)
    }

    override fun onBind(binding: ItemProductViewBinding, item: Project) {
        with(binding) {
            itemTitle.text = item.name + ":" + item.id
        }
    }
}
