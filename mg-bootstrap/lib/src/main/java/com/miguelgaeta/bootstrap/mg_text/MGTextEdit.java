package com.miguelgaeta.bootstrap.mg_text;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelgaeta.bootstrap.R;
import com.miguelgaeta.bootstrap.mg_log.MGLog;

import java.util.List;
import java.util.Map;

import lombok.NonNull;

/**
 * Created by mrkcsc on 5/23/15.
 */
@SuppressWarnings("unused")
public class MGTextEdit extends EditText {

    private final MGTextEditHasText hasText = new MGTextEditHasText(this);

    /**
     * Given a text view and a label, set a long click listener
     * that will copy the text to the clipboard on
     * a long press action.
     */
    public static void copyTextOnLongPress(@NonNull TextView textView, @NonNull String label) {

        textView.setOnLongClickListener(view -> {

            if (view != null && view instanceof TextView) {

                ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(label, ((TextView) view).getText());

                clipboard.setPrimaryClip(clip);

                Toast.makeText(view.getContext(), R.string.shared_copied_to_clipboard, Toast.LENGTH_SHORT).show();
            }

            return false;
        });
    }

    public MGTextEdit(Context context) {
        super(context);
    }

    public MGTextEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MGTextEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String toStringSafe() {

        try {

            return getText().toString();

        } catch (Exception e) {

            // Should not happen but log if it does.
            MGLog.e(e, "Unable to get edit text string");

            return "";
        }
    }

    /**
     * Fetch cursor position.  If no position
     * is available, assume end of content.
     */
    public int getCursorPosition() {

        int position = toStringSafe().length();

        if (getSelectionEnd() >= 0) {

            position = getSelectionEnd();
        }

        return position;
    }

    public void insert(CharSequence charSequence, int start, int end) {

        getText().replace(Math.min(start, end), Math.max(start, end), charSequence, 0, charSequence.length());
    }

    public void insert(CharSequence charSequence) {

        // See: http://bit.ly/1ArIVnX
        insert(charSequence, Math.max(getSelectionStart(), 0), Math.max(getSelectionEnd(), 0));
    }

    public void setOnHasTextListener(OnHasTextListener onHasTextListener) {

        hasText.setOnHasTextListener(onHasTextListener);
    }

    public interface OnHasTextListener {

        void hasText(boolean hasText);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// Mentions Functionality
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private final MGTextEditMention mention = new MGTextEditMention(this);

    @Override
    protected void onSelectionChanged(int start, int end) {
        super.onSelectionChanged(start, end);

        if (mention != null) {
            mention.processMentions(this, false);
        }
    }

    /**
     * Fetch a list of currently entered mention
     * tags run through the stringify function.
     */
    public List<String> getMentions() {

        return mention.getMentions();
    }

    /**
     * Insert a mention tag into the edit text,
     * replacing any current partial
     * mention entered.
     */
    public void insertMention(@NonNull String mention) {

        // Fetch position of cursor.
        int position = getCursorPosition();

        String lastToken = MGTextEditMentionUtils.getPartialMentionToken(this);

        if (lastToken != null) {

            int positionStart = position - lastToken.length();

            // Insert tag, replacing any partial tag.
            insert(mention + "  ", positionStart, position);

            // Selection hack to prevent next user inputted key
            // press from overwriting what just got input.
            setSelection(positionStart + mention.length() + 1);
        }
    }

    public void setOnMentionsMatchedListener(OnMentionsMatchedListener onMentionsMatchedListener) {

        mention.setOnMentionsMatchedListener(onMentionsMatchedListener);
    }

    @SuppressWarnings("unchecked")
    public <T> void setMentionsData(Map<String, T> tags, OnMentionsStringify stringify) {

        mention.setMentionsData((Map<String, Object>) tags, stringify);
    }

    public void setMentionsRecycler(RecyclerView recycler, OnMentionsRecyclerItem onItem) {

        mention.setRecyclerView(recycler, onItem);
    }


    public interface OnMentionsMatchedListener {

        void mentionsMatched(List<String> tags);
    }

    public interface OnMentionsRecyclerItem {

        /**
         * Given an associated mentions list adapter, asks the
         * callee to generate a mention list item.
         */
        MGTextEditMentionItem onItem(MGTextEditMentionAdapter adapter);
    }

    public interface OnMentionsStringify {

        String stringify(String tag);
    }
}