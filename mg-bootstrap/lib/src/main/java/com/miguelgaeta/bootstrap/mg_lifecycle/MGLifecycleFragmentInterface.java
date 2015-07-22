package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.os.Bundle;
import android.view.View;

/**
 * Created by Miguel Gaeta on 7/21/15.
 */
interface MGLifecycleFragmentInterface {

    void onCreateOrResume();

    void onCreateView(Bundle savedInstanceState, View view);
}
