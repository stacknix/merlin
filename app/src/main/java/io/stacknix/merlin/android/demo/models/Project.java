package io.stacknix.merlin.android.demo.models;

import io.stacknix.merlin.db.MerlinObject;
import io.stacknix.merlin.db.annotations.Ignore;
import io.stacknix.merlin.db.annotations.Model;
import io.stacknix.merlin.db.annotations.Order;
import io.stacknix.merlin.db.annotations.PrimaryKey;
import io.stacknix.merlin.db.annotations.SortKey;

@Model("project.project")
public class Project extends MerlinObject {
    public String name;
}
