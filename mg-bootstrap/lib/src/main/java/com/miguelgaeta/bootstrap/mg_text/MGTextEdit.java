package com.miguelgaeta.bootstrap.mg_text;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelgaeta.bootstrap.R;

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

    public static void copyTextOnPress(@NonNull View view, @NonNull final TextView textView, @StringRes final int copiedMessage, boolean longPress) {

        if (longPress) {

            view.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    copyText(textView, copiedMessage);

                    return false;
                }
            });

        } else {

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    copyText(textView, copiedMessage);
                }
            });
        }
    }

    public static void copyText(@NonNull TextView textView) {

        copyText(textView.getContext(), textView.getText());
    }

    public static void copyText(@NonNull TextView textView, @StringRes int copiedMessage) {

        copyText(textView.getContext(), textView.getText(), copiedMessage);
    }

    public static void copyText(@NonNull Context context, CharSequence text) {

        copyText(context, text, R.string.shared_copied_to_clipboard);
    }

    public static void copyText(@NonNull Context context, CharSequence text, @StringRes int copiedMessage) {

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", text);

        clipboard.setPrimaryClip(clip);

        Toast.makeText(context, copiedMessage, Toast.LENGTH_SHORT).show();
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
            Log.e("MGTextEdit", "Unable to get edit text string", e);

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