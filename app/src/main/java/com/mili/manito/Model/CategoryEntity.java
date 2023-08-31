package com.mili.manito.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "category",
        indices = {@Index(value = "cate_name",unique = true)})
public class CategoryEntity {

    public CategoryEntity(int category_id, @NonNull String category_name,@NonNull String category_color) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_color =category_color;
    }

    @Ignore
    public CategoryEntity(@NonNull String category_name,@NonNull String category_color) {
        this.category_name = category_name;
        this.category_color = category_color;
    }

    @Ignore
    public CategoryEntity() {
    }

    @Ignore
    public CategoryEntity(int category_id, Long category_cost, @NonNull String category_name, @NonNull String category_color) {
        this.category_id = category_id;
        this.category_cost = category_cost;
        this.category_name = category_name;
        this.category_color = category_color;
    }

    @Ignore
    public CategoryEntity(Long category_cost, Long paid_cost, @NonNull String category_name, @NonNull String category_color) {
        this.category_cost = category_cost;
        this.paid_cost = paid_cost;
        this.category_name = category_name;
        this.category_color = category_color;
    }

    public int getCategory_id() {
        return category_id;
    }

    @NonNull
    public String getCategory_name() {
        return category_name;
    }


    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "cate_id")
    private int category_id;

    @Ignore
    //for budgeting
    private Long category_cost;

    @Ignore
    public Long getCategory_cost() {
        return category_cost;
    }

    @Ignore
    //for budgeting
    private Long paid_cost;

    @Ignore
    public Long getPaid_cost() {
        return paid_cost;
    }


    @NonNull
    @ColumnInfo(name = "cate_name")
    private String category_name;

    @NonNull
    @ColumnInfo(name = "cate_color")
    private String category_color;

    @NonNull
    public String getCategory_color() {
        return category_color;
    }

    public void setCategory_cost(Long category_cost) {
        this.category_cost = category_cost;
    }

    @Ignore
    public static CategoryEntity[] populateDefaultCategory(){
        return new CategoryEntity[]{
                new CategoryEntity("سرگرمی","#ff4081"),
                new CategoryEntity("پوشاک","#FFAA66CC"),
                new CategoryEntity("وسایل خانه","#FF9933CC"),
                new CategoryEntity("آموزشی","#FF33B5E5"),
                new CategoryEntity("حقوق و دستمزد","#FF0099CC"),
                new CategoryEntity("دارو و پزشکی","#66cdaa"),
                new CategoryEntity("تعمیرات","#FFFFBB33"),
                new CategoryEntity("خودرو","#f4a460"),
                new CategoryEntity("حمل و نقل","#FFFF8800"),
                new CategoryEntity("خوراکی","#FFFF4444"),
                new CategoryEntity("وام","#DC143C"),
                new CategoryEntity("مسکن","#FFCC0000"),
                new CategoryEntity("قبض","#d2691e"),
                new CategoryEntity("سایر","#bc8f8f"),

        };
    }

}
