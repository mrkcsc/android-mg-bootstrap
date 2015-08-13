package com.miguelgaeta.bootstrap.mg_text;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelgaeta.bootstrap.R;
import com.miguelgaeta.bootstrap.mg_log.MGLog;

import lombok.NonNull;
import lombok.Setter;

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

    @Setter
    private MGTextEditMention mentionsModule;

    @Override
    protected void onSelectionChanged(int start, int end) {
        super.onSelectionChanged(start, end);

        if (mentionsModule != null) {
            mentionsModule.processMentions(this, false);
        }
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
}