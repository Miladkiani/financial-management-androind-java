package com.mili.manito.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.mili.manito.Model.LabelsEntity;
import com.mili.manito.Model.TransactionEntity;
import com.mili.manito.Model.TransactionList;
import com.mili.manito.R;
import com.mili.manito.Utilities.DateUtil;
import com.mili.manito.Utilities.PersianDigitConverter;
import com.mili.manito.Utilities.PrefManager;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class TransactionListAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MoreListener moreListener;
    private Context mContext;
    private List<TransactionList> transactionLists;
    private static final HashMap<String,Integer> BG_IMAGE = new HashMap<String, Integer>() {
        {
            put("#2196F3", R.drawable.label_bl);
            put("#F44336", R.drawable.label_rd);
            put("#4CAF50", R.drawable.label_gr);
            put("#FF5722", R.drawable.label_or);
            put("#9C27B0", R.drawable.label_pr);
            put("#E91E63", R.drawable.label_pi);
            put("#CDDC39", R.drawable.label_li);
            put("#795548", R.drawable.label_br);
        }
    };

    public TransactionListAdapter(Context context, List<TransactionList> transactionLists , MoreListener moreListener) {
        this.mContext = context;
        this.transactionLists = transactionLists;
        this.moreListener = moreListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView;
        if (viewType == TransactionEntity.TRANSACTION_TRANSFER) {
            rootView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.recycler_list_tran_transfer
                            , parent, false);
            return new TransferHolder(rootView);
        } else  {
            rootView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.recycler_list_tran
                            , parent, false);
            return new NormalHolder(rootView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TransactionList transaction = transactionLists.get(position);

        if (getItemViewType(position) == TransactionEntity.TRANSACTION_TRANSFER) {
            ((TransferHolder)holder).setTransfer(transaction);
        } else {
            ((NormalHolder)holder).setNormal(transaction);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return transactionLists.get(position).getTransaction_type();
    }

    private static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public void setList(List<TransactionList> list){
        this.transactionLists.clear();
        this.transactionLists.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return transactionLists.size();
    }

    public class NormalHolder extends RecyclerView.ViewHolder {
        private TextView cate_name, cate_color, account_name, transaction_cost,
                transaction_date, transaction_time, transaction_buy, unit;
        private ImageButton transaction_more;
        private LinearLayout linearLayout;
        private HorizontalScrollView scrollView;


        NormalHolder(@NonNull View itemView) {
            super(itemView);

            cate_name = itemView.findViewById(R.id.tran_title_txt);
            cate_color = itemView.findViewById(R.id.textview41);
            account_name = itemView.findViewById(R.id.tran_accname_txt);
            transaction_cost = itemView.findViewById(R.id.tran_cost_txt);
            linearLayout = itemView.findViewById(R.id.tran_tag_lin);
            transaction_date = itemView.findViewById(R.id.tran_date_txt);
            transaction_time = itemView.findViewById(R.id.tran_time_txt);
            transaction_buy = itemView.findViewById(R.id.tran_acctitle_txt);
            transaction_more = itemView.findViewById(R.id.tran_more_imb);
            scrollView = itemView.findViewById(R.id.tran_tag_scroll);
            scrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    scrollView.fullScroll(View.FOCUS_RIGHT);
                }
            });
            unit = itemView.findViewById(R.id.tran_unit_txt);

            transaction_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mContext, transaction_more);
                    popupMenu.getMenuInflater().inflate(R.menu.tran_more_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int itemId = item.getItemId();
                            switch (itemId) {
                                case R.id.remove_item:
                                    moreListener.removeTransaction(
                                            transactionLists.get(getAdapterPosition())
                                                    .getTransaction_id());
                                    return true;
                                case R.id.update_item:
                                    moreListener.updateTransaction(
                                            transactionLists.get(getAdapterPosition())
                                                    .getTransaction_id()

                                    );
                                    return true;
                            }
                            return true;
                        }
                    });

                    popupMenu.show();
                }
            });
        }

        private void setNormal(TransactionList list){
            int type = list.getTransaction_type();

            cate_name.setText(list.getCategory_name());
            cate_color.setBackgroundColor(Color.parseColor(list.getCategory_color()));

            if (type == TransactionEntity.TRANSACTION_GET) {
                account_name.setText(list.getAccount_name_get());
                account_name.setTextColor(mContext.getResources().getColor(R.color.green));
                transaction_buy.setText(mContext.getResources().getString
                        (R.string.account_transaction_get));
            }else {
                transaction_buy.setText(mContext.getResources().getString
                        (R.string.account_transaction_pay));
                account_name.setText(list.getAccount_name_pay());
                account_name.setTextColor(mContext.getResources().getColor(R.color.red));
            }

            long cost = list.getTransaction_cost();

            if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)) {
                cost = cost / 10;
            }

            unit.setText(PrefManager.getUnitPref());

            transaction_cost.setText(
                    PersianDigitConverter.PersianNumber(
                            PersianDigitConverter.NumberFormat(cost)));

            NumberFormat numberFormat = new DecimalFormat("00");
            PersianCalendar tranDate = list.getTransaction_date();

            transaction_date.setText(PersianDigitConverter.PersianNumber(
                    tranDate.getPersianYear() +
                            "/"
                            + numberFormat.format(tranDate.getPersianMonth() + 1)
                            + "/"
                            + numberFormat.format(tranDate.getPersianDay())
            ));


            transaction_time.setText(PersianDigitConverter.PersianNumber(
                    DateUtil.getTimeString(tranDate.get(PersianCalendar.HOUR_OF_DAY),
                            tranDate.get(PersianCalendar.MINUTE)))
            );

          /*  scrollView.postDelayed(new Runnable() {
                public void run() {
                    scrollView.scrollTo(scrollView.getRight(), scrollView.getTop());
                }
            }, 100L);*/


            linearLayout.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(30));
            if (list.getTags() != null && list.getTags().size() != 0) {
                for (LabelsEntity lbl : list.getTags()) {
                    final TextView textView = new TextView(mContext);
                    textView.setText(lbl.getLabel_name());
                    textView.setBackgroundResource(
                            BG_IMAGE.get(lbl.getLabel_color()));
                    params.setMargins(4, 0, 0, 0);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.parseColor("#FFFFFF"));
                    textView.setTextSize(mContext.getResources().getDimensionPixelSize
                            (R.dimen.tagFontSize));
                    textView.setLayoutParams(params);
                    linearLayout.addView(textView);
                }
            }
        }
    }

        public class TransferHolder extends RecyclerView.ViewHolder {
            private TextView account_name_pay, account_name_get, transfer_cost,
                    transfer_date, transfer_time, unit;
            private ImageButton transfer_more;

            TransferHolder(@NonNull View itemView) {
                super(itemView);

                account_name_pay = itemView.findViewById(R.id.tran_accname_pay_txt);
                account_name_get = itemView.findViewById(R.id.tran_accname_get_txt);
                transfer_cost = itemView.findViewById(R.id.tran_cost_transfer_txt);
                transfer_date = itemView.findViewById(R.id.transfer_date_txt);
                transfer_time = itemView.findViewById(R.id.transfer_time_txt);
                transfer_more = itemView.findViewById(R.id.tran_more_transfer_imb);
                unit = itemView.findViewById(R.id.tran_unit_transfer_txt);

                transfer_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(mContext, transfer_more);
                        popupMenu.getMenuInflater().inflate(R.menu.tran_more_menu, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                int itemId = item.getItemId();
                                switch (itemId) {
                                    case R.id.remove_item:
                                        moreListener.removeTransaction(
                                                transactionLists.get(getAdapterPosition())
                                                        .getTransaction_id()
                                                );
                                        return true;
                                    case R.id.update_item:
                                        moreListener.updateTransfer(
                                                transactionLists.get(getAdapterPosition())
                                                        .getTransaction_id()
                                        );
                                        return true;
                                }
                                return true;
                            }
                        });

                        popupMenu.show();
                    }
                });
            }
            private void setTransfer(TransactionList list){

                    account_name_pay.setText(list.getAccount_name_pay());

                    account_name_get.setText(list.getAccount_name_get());

                long cost = list.getTransaction_cost();

                if (PrefManager.getUnitPref().equals(PrefManager.TOOMAN_UNIT_VALUE)) {
                    cost = cost / 10;
                }

                unit.setText(PrefManager.getUnitPref());

                transfer_cost.setText(
                        PersianDigitConverter.PersianNumber(
                                PersianDigitConverter.NumberFormat(cost)));

                NumberFormat numberFormat = new DecimalFormat("00");
                PersianCalendar tranDate = list.getTransaction_date();

                transfer_date.setText(PersianDigitConverter.PersianNumber(
                        tranDate.getPersianYear() +
                                "/"
                                + numberFormat.format(tranDate.getPersianMonth() + 1)
                                + "/"
                                + numberFormat.format(tranDate.getPersianDay())
                ));

                transfer_time.setText(PersianDigitConverter.PersianNumber(
                        DateUtil.getTimeString(tranDate.get(PersianCalendar.HOUR_OF_DAY),
                                tranDate.get(PersianCalendar.MINUTE)))
                );

            }
    }
    public interface MoreListener{
        void updateTransaction(int id);
        void updateTransfer(int id);
        void removeTransaction(int id);
    }
}
