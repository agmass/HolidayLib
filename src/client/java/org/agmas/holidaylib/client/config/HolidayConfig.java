package org.agmas.holidaylib.client.config;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "holidaylib")
public class HolidayConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip()
    public boolean disableShaders = false;


}
