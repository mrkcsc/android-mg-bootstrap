package com.miguelgaeta.recycler;

import android.view.View;
import android.widget.TextView;

import com.miguelgaeta.R;
import com.miguelgaeta.bootstrap.mg_log.MGLog;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerViewHolder;

import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by mrkcsc on 4/27/15.
 */
public class TestActivityRecyclerViewHolder extends MGRecyclerViewHolder<TestActivityRecyclerAdapter> {

    @InjectView(R.id.recycler_item_text) TextView itemText;

    public TestActivityRecyclerViewHolder(View itemView, TestActivityRecyclerAdapter adapter) {
        super(itemView, adapter);
    }

    @Override
    protected void onCreate(int position) {
        super.onCreate(position);

        itemText.setText("Position: " + position);
    }

    @Override
    protected void onResume(int position) {
        super.onResume(position);

        TestActivityRecycler.getTestStream().get()
            .takeUntil(getPaused())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnUnsubscribe(() -> MGLog.e("killed, position: " + position)).subscribe(aBoolean -> {

                MGLog.e("data, position: " + position + " val: " + aBoolean);
            });
    }
}
