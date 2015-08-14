package com.miguelgaeta.bootstrap.mg_text;

import java.util.List;

/**
 * Created by Miguel Gaeta on 8/13/15.
 */
public interface MGTextEditMentionCallbacks<T> {

    /**
     * Given a tag data object, convert it into a string.
     */
    String tagDataToString(T tagData);

    void onTagsMatched(List<String> tags);

    MGTextEditMentionItem onBindViewHolder(MGTextEditMentionAdapter adapter);

    void onTagClicked(String tag);
}
