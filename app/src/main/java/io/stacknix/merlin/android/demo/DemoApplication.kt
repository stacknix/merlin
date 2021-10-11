package io.stacknix.merlin.android.demo

import android.app.Application
import android.util.Log
import io.stacknix.merlin.android.demo.models.Product
import io.stacknix.merlin.db.Merlin
import io.stacknix.merlin.db.queries.Filter


class DemoApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        val result = Merlin.where(Product::class.java).equal("name", "ashish")
        val f = Filter()
        val q = f.not().build()
        Log.i("Build:", q.toString())
    }
}