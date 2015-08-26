package com.miguelgaeta.bootstrap.mg_text;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
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

    public static void copyTextOnPress(@NonNull TextView textView, boolean longPress) {

        copyTextOnPress(textView, textView, R.string.shared_copied_to_clipboard, longPress);
    }

    public static void copyTextOnPress(@NonNull TextView textView) {

        copyTextOnPress(textView, textView);
    }

    public static void copyTextOnPress(@NonNull View view, @NonNull TextView textView) {

        copyTextOnPress(view, textView, R.string.shared_copied_to_clipboard);
    }

    public static void copyTextOnPress(@NonNull View view, @NonNull TextView textView, @StringRes int copiedMessage) {

        copyTextOnPress(view, textView, copiedMessage, false);
    }

    public static void copyTextOnPress(@NonNull View view, @NonNull TextView textView, @StringRes int copiedMessage, boolean longPress) {

        if (longPress) {

            view.setOnClickListener(v -> copyText(textView, copiedMessage));

        } else {

            view.setOnLongClickListener(v -> {

                copyText(textView, copiedMessage);

                return false;
            });
        }
    }

    public static void copyText(@NonNull TextView textView) {

        copyText(textView, R.string.shared_copied_to_clipboard);
    }

    public static void copyText(@NonNull TextView textView, @StringRes int copiedMessage) {

        ClipboardManager clipboard = (ClipboardManager) textView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", textView.getText());

        clipboard.setPrimaryClip(clip);

        Toast.makeText(textView.getContext(), copiedMessage, Toast.LENGTH_SHORT).show();
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
}