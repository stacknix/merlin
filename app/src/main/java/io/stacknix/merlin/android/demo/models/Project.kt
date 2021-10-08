package io.stacknix.merlin.android.demo.models

import io.stacknix.merlin.db.MerlinObject
import io.stacknix.merlin.db.annotations.Model

@Model("billing.suresh")
open class Project(val long: Long, var name: String, var price: Float) : MerlinObject()
