package io.stacknix.merlin.android.demo

import android.app.Application
import android.util.Log
import com.stacknix.merlin.compiler.generated.MerlinDatabase
import io.stacknix.merlin.android.demo.models.Product
import io.stacknix.merlin.db.Merlin
import io.stacknix.merlin.db.queries.Filter


class DemoApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        MerlinDatabase.init(this)
    }
}