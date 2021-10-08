package io.stacknix.merlin.android.demo.models;

import io.stacknix.merlin.db.MerlinObject;
import io.stacknix.merlin.db.annotations.Model;

@Model("sale.sale")
public class Sale extends MerlinObject {

    public long id;
    public String name;
    public float list_price;

}