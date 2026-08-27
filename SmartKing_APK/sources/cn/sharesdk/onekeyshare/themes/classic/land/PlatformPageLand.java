package cn.sharesdk.onekeyshare.themes.classic.land;

import cn.sharesdk.onekeyshare.OnekeyShareThemeImpl;
import cn.sharesdk.onekeyshare.themes.classic.PlatformPage;
import cn.sharesdk.onekeyshare.themes.classic.PlatformPageAdapter;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class PlatformPageLand extends PlatformPage {
    public PlatformPageLand(OnekeyShareThemeImpl onekeyShareThemeImpl) {
        super(onekeyShareThemeImpl);
    }

    @Override // cn.sharesdk.onekeyshare.themes.classic.PlatformPage
    protected PlatformPageAdapter newAdapter(ArrayList<Object> arrayList) {
        return new PlatformPageAdapterLand(this, arrayList);
    }

    @Override // cn.sharesdk.onekeyshare.themes.classic.PlatformPage, com.mob.tools.FakeActivity
    public void onCreate() {
        requestSensorLandscapeOrientation();
        super.onCreate();
    }
}
