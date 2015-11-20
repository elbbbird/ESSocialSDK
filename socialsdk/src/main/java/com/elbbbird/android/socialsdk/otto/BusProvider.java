package com.elbbbird.android.socialsdk.otto;

import com.squareup.otto.Bus;

/**
 * Otto Bus Provider
 * Created by zhanghailong-ms on 2015/11/20.
 */
public class BusProvider {

    private static Bus bus;

    public static Bus getInstance() {
        if (bus == null)
            bus = new Bus();

        return bus;
    }

}
