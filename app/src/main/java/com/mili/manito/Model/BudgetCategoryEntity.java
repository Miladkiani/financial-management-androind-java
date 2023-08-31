package com.mili.manito.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "budget_category",
        primaryKeys = {"bud_id","cate_id"},
        foreignKeys = { @ForeignKey(
                entity = BudgetEntity.class,
                parentColumns = "bud_id",
                childColumns = "bud_id",
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = CategoryEntity.class,
                parentColumns = "cate_id",
                childColumns = "cate_id",
                onDelete = ForeignKey.CASCADE
        )},  indices = {@Index(value = "cate_id")}
)
public class BudgetCategoryEntity {

    @NonNull
    @ColumnInfo(name = "bud_id")
    private int budget_id;

    @NonNull
    @ColumnInfo(name = "cate_id")
    private int category_id;

    @NonNull
    @ColumnInfo(name = "cost")
    private Long budget_cate_cost;

    public BudgetCategoryEntity(int budget_id, int category_id, @NonNull Long budget_cate_cost) {
        this.budget_id = budget_id;
        this.category_id = category_id;
        this.budget_cate_cost = budget_cate_cost;
    }

    public int getBudget_id() {
        return budget_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    @NonNull
    public Long getBudget_cate_cost() {
        return budget_cate_cost;
    }
}
