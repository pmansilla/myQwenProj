package basecamera.module.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import basecamera.module.activity.model.Album;
import basecamera.module.cfg.BaseCameraCfg;
import basecamera.module.cfg.BaseCameraResHelper;
import basecamera.module.lib.R;
import basecamera.module.utils.FileUtils;
import basecamera.module.utils.ImageUtils;
import basecamera.module.utils.StringUtils;
import basecamera.module.views.CameraTitleBar;
import basecamera.module.views.PagerSlidingTabStrip;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class BaseCameraGalleryActivity extends FragmentActivity {
    private Map<String, Album> albums;
    private CameraTitleBar cameraTitleBar;
    ViewPager pager;
    private List<String> paths = new ArrayList();
    PagerSlidingTabStrip tab;

    /* loaded from: classes.dex */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return BaseCameraGalleryActivity.this.paths.size();
        }

        @Override // android.support.v4.app.FragmentPagerAdapter
        public Fragment getItem(int i) {
            return BaseCameraAlbumFragment.newInstance(((Album) BaseCameraGalleryActivity.this.albums.get(BaseCameraGalleryActivity.this.paths.get(i))).getPhotos());
        }

        @Override // android.support.v4.view.PagerAdapter
        public CharSequence getPageTitle(int i) {
            Album album = (Album) BaseCameraGalleryActivity.this.albums.get(BaseCameraGalleryActivity.this.paths.get(i));
            if (StringUtils.equalsIgnoreCase(FileUtils.getInst(BaseCameraGalleryActivity.this).getSystemPhotoPath(), album.getAlbumUri())) {
                return BaseCameraResHelper.galleryName;
            }
            if (album.getTitle().length() <= 13) {
                return album.getTitle();
            }
            return album.getTitle().substring(0, 11) + "...";
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StatusBarHelperUtils.translucent(this);
        StatusBarHelperUtils.setStatusBarLightMode(this);
        setContentView(R.layout.basecamera_activity_album);
        this.tab = (PagerSlidingTabStrip) findViewById(R.id.indicator);
        this.pager = (ViewPager) findViewById(R.id.pager);
        this.albums = ImageUtils.findGalleries(this, this.paths, 0L);
        this.pager.setAdapter(new TabPageIndicatorAdapter(getSupportFragmentManager()));
        this.tab.setViewPager(this.pager);
        this.cameraTitleBar = (CameraTitleBar) findViewById(R.id.titleBar);
        this.cameraTitleBar.setTitle(BaseCameraCfg.galleryTitle);
        this.cameraTitleBar.setLeftImage(R.mipmap.basecamera_ico_title_back);
        this.cameraTitleBar.setLeftViewOnClickListener(new View.OnClickListener() { // from class: basecamera.module.activity.BaseCameraGalleryActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                BaseCameraGalleryActivity.this.finish();
            }
        });
    }
}
