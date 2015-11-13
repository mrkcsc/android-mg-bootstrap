package com.miguelgaeta.bootstrap.views;

import android.content.Context;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Text view that only responds to touch events of links.
 */
@SuppressWarnings("UnusedDeclaration")
public class LinkifyTextView extends TextView {

    public LinkifyTextView(Context context) {
        super(context);
    }

    public LinkifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinkifyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @Linkify applies to a movementMethod to the textView @LinkMovementMethod. That movement method thought it
     * implements a scrolling vertically method it overrides any other scrolling method the parent has.
     *
     * Although touchEvent can be dispatched to the parent, the specific parent ScrollView needed the whole sequence
     * ACTION_DOWN , ACTION_MOVE, ACTION_UP to perform (sweep detection). So the solution to this problem is after
     * applying @Linkify we need to remove the textView's scrolling method and handle the @LinkMovementMethod link
     * detection action in onTouchEvent of the textView.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final TextView widget = this;
        final Object text = widget.getText();

        if (text instanceof Spanned) {

            if (event.getAction() == MotionEvent.ACTION_UP) {

                int x = (int)event.getX();
                int y = (int)event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                final Layout layout = widget.getLayout();
                final int line = layout.getLineForVertical(y);
                final int off = layout.getOffsetForHorizontal(line, x);

                final ClickableSpan[] link = ((Spanned)text).getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {

                    link[0].onClick(widget);

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);

        this.setMovementMethod(null);
    }
}
