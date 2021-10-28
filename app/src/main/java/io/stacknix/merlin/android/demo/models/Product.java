package io.stacknix.merlin.android.demo.models;

import io.stacknix.merlin.db.MerlinObject;
import io.stacknix.merlin.db.annotations.Ignore;
import io.stacknix.merlin.db.annotations.Model;
import io.stacknix.merlin.db.annotations.Order;
import io.stacknix.merlin.db.annotations.SortKey;

@Model("product.product")
public class Product extends MerlinObject {
    public String name;
    public long id;
    public float price;
}
