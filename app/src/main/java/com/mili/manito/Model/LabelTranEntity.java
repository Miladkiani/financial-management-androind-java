package com.mili.manito.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "lbl_tran",
primaryKeys ={"lbl_id","tran_id"},
foreignKeys = {@ForeignKey(entity = LabelsEntity.class,
                    parentColumns = "lbl_id",
                    childColumns = "lbl_id",
               onDelete = CASCADE),
        @ForeignKey(entity = TransactionEntity.class,
        parentColumns = "tran_id",
        childColumns = "tran_id",
                onDelete = CASCADE)},
        indices = @Index("tran_id")
)
public class LabelTranEntity {

    @ColumnInfo(name = "lbl_id")
    @NonNull
    private Integer label_id;

    @NonNull
    @ColumnInfo(name = "tran_id")
    private Integer tran_id;

    @NonNull
    public Integer getLabel_id() {
        return label_id;
    }

    @NonNull
    public Integer getTran_id() {
        return tran_id;
    }

    public LabelTranEntity(@NonNull Integer label_id, @NonNull Integer tran_id) {
        this.label_id = label_id;
        this.tran_id = tran_id;
    }
}
