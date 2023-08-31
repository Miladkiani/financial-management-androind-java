package com.mili.manito.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "labels" ,
        foreignKeys = @ForeignKey(entity =  UserEntity.class,
                parentColumns = "user_id",
                childColumns = "user_id",
                onDelete = CASCADE),indices = {@Index("user_id")}
)
public class LabelsEntity {

    public LabelsEntity(@NonNull Integer label_id, @NonNull String label_name, @NonNull Integer user_id,@NonNull String label_color) {
        this.label_id = label_id;
        this.label_name = label_name;
        this.user_id = user_id;
        this.label_color = label_color;
    }

    @Ignore
    public LabelsEntity(@NonNull String label_name, @NonNull Integer user_id , @NonNull String label_color) {
        this.label_name = label_name;
        this.user_id = user_id;
        this.label_color = label_color;
    }

    public void setLabel_id(@NonNull Integer label_id) {
        this.label_id = label_id;
    }

    @NonNull
    public Integer getLabel_id() {
        return label_id;
    }

    @NonNull
    public String getLabel_name() {
        return label_name;
    }

    @NonNull
    public Integer getUser_id() {
        return user_id;
    }

    @NonNull
    public String getLabel_color() {
        return label_color;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "lbl_id")
    private Integer label_id;

    @NonNull
    @ColumnInfo(name = "lbl_name")
    private String label_name;

    @NonNull
    @ColumnInfo(name = "user_id")
    private Integer user_id;

    @NonNull
    @ColumnInfo(name = "lbl_color")
    private String label_color;
}
