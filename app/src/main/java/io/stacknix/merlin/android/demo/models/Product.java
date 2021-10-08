package io.stacknix.merlin.android.demo.models;

import io.stacknix.merlin.db.MerlinObject;
import io.stacknix.merlin.db.annotations.Ignore;
import io.stacknix.merlin.db.annotations.Model;

@Model("product.product")
public class Product extends MerlinObject {
    @Ignore
    public String tag;
    public String name;
    public long id;
    public float price;
}
