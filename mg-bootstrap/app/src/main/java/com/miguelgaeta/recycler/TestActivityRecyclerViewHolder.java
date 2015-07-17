package com.miguelgaeta.recycler;

import android.support.annotation.LayoutRes;
import android.widget.TextView;

import com.miguelgaeta.R;
import com.miguelgaeta.bootstrap.mg_log.MGLog;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerViewHolder;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 4/27/15.
 */
public class TestActivityRecyclerViewHolder extends MGRecyclerViewHolder<TestActivityRecyclerAdapter> {

    @InjectView(R.id.recycler_item_text) TextView itemText;

    public TestActivityRecyclerViewHolder(@LayoutRes int layout, TestActivityRecyclerAdapter adapter) {
        super(layout, adapter);

        onClick(itemText, (view, position) -> MGLog.e("Clicked at: " + position));
    }

    @Override
    protected void onConfigure(int position) {
        super.onConfigure(position);

        itemText.setText("Position: " + position + " data: " + getAdapter().getData().get().get(position));
    }
}
