package org.agmas.holidaylib;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;

public class Holidaylib implements ModInitializer {

    @Override
    public void onInitialize() {
        Log.info(LogCategory.GENERAL, "Merry christmas from HolidayLib! Ho ho ho! :D");
    }
}
