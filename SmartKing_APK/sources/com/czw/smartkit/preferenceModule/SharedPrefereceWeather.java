package com.czw.smartkit.preferenceModule;

import com.czw.smartkit.netModule.entity.WeatherEntity;
import com.czw.smartkit.util.SaveObjectUtils;

/* loaded from: classes.dex */
public class SharedPrefereceWeather {
    public static void clear() {
        save(null);
    }

    public static WeatherEntity read() {
        return (WeatherEntity) SaveObjectUtils.getObject("weather", WeatherEntity.class);
    }

    public static void save(WeatherEntity weatherEntity) {
        SaveObjectUtils.setObject("weather", weatherEntity);
    }
}
