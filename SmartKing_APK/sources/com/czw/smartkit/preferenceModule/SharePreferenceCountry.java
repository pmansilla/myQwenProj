package com.czw.smartkit.preferenceModule;

import com.czw.smartkit.entity.CountryEntity;
import com.czw.smartkit.util.SaveObjectUtils;

/* loaded from: classes.dex */
public class SharePreferenceCountry {
    public static void clearAll() {
        save(null);
    }

    public static CountryEntity read() {
        return (CountryEntity) SaveObjectUtils.getObject("cfg_country", CountryEntity.class);
    }

    public static void save(CountryEntity countryEntity) {
        SaveObjectUtils.setObject("cfg_country", countryEntity);
    }
}
