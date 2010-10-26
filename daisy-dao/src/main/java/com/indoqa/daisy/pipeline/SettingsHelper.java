package com.indoqa.daisy.pipeline;

import org.apache.cocoon.configuration.Settings;
import org.apache.cocoon.configuration.SettingsDefaults;
import org.apache.commons.lang.Validate;

public class SettingsHelper {

    public static boolean useLast(Settings settings) {
        Validate.notNull(settings, "A settings object must be passed.");

        if (!settings.getRunningMode().equals(SettingsDefaults.DEFAULT_RUNNING_MODE)) {
            return true;
        }

        return false;
    }
}
