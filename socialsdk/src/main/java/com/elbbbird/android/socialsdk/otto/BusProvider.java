package com.elbbbird.android.socialsdk.otto;

/**
 * Otto Bus Provider
 * Created by zhanghailong-ms on 2015/11/20.
 */
public class BusProvider {

    private static MainThreadBus bus;

    public static MainThreadBus getInstance() {
        if (bus == null)
            bus = new MainThreadBus();

        return bus;
    }

}
