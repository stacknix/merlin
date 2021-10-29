package io.stacknix.merlin.android.demo

import android.app.Application
import com.stacknix.merlin.compiler.generated.MerlinDatabase


class DemoApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        MerlinDatabase.init(this)
    }
}