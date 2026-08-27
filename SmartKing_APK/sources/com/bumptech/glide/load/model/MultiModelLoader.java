package com.bumptech.glide.load.model;

import android.support.annotation.Nullable;
import android.support.v4.util.Pools;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
class MultiModelLoader<Model, Data> implements ModelLoader<Model, Data> {
    private final Pools.Pool<List<Exception>> exceptionListPool;
    private final List<ModelLoader<Model, Data>> modelLoaders;

    /* loaded from: classes.dex */
    static class MultiFetcher<Data> implements DataFetcher<Data>, DataFetcher.DataCallback<Data> {
        private DataFetcher.DataCallback<? super Data> callback;
        private int currentIndex;
        private final Pools.Pool<List<Exception>> exceptionListPool;

        @Nullable
        private List<Exception> exceptions;
        private final List<DataFetcher<Data>> fetchers;
        private Priority priority;

        MultiFetcher(List<DataFetcher<Data>> list, Pools.Pool<List<Exception>> pool) {
            this.exceptionListPool = pool;
            Preconditions.checkNotEmpty(list);
            this.fetchers = list;
            this.currentIndex = 0;
        }

        private void startNextOrFail() {
            if (this.currentIndex >= this.fetchers.size() - 1) {
                this.callback.onLoadFailed(new GlideException("Fetch failed", new ArrayList(this.exceptions)));
            } else {
                this.currentIndex++;
                loadData(this.priority, this.callback);
            }
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public void cancel() {
            Iterator<DataFetcher<Data>> it = this.fetchers.iterator();
            while (it.hasNext()) {
                it.next().cancel();
            }
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public void cleanup() {
            if (this.exceptions != null) {
                this.exceptionListPool.release(this.exceptions);
            }
            this.exceptions = null;
            Iterator<DataFetcher<Data>> it = this.fetchers.iterator();
            while (it.hasNext()) {
                it.next().cleanup();
            }
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public Class<Data> getDataClass() {
            return this.fetchers.get(0).getDataClass();
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public DataSource getDataSource() {
            return this.fetchers.get(0).getDataSource();
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public void loadData(Priority priority, DataFetcher.DataCallback<? super Data> dataCallback) {
            this.priority = priority;
            this.callback = dataCallback;
            this.exceptions = this.exceptionListPool.acquire();
            this.fetchers.get(this.currentIndex).loadData(priority, this);
        }

        @Override // com.bumptech.glide.load.data.DataFetcher.DataCallback
        public void onDataReady(Data data) {
            if (data != null) {
                this.callback.onDataReady(data);
            } else {
                startNextOrFail();
            }
        }

        @Override // com.bumptech.glide.load.data.DataFetcher.DataCallback
        public void onLoadFailed(Exception exc) {
            this.exceptions.add(exc);
            startNextOrFail();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MultiModelLoader(List<ModelLoader<Model, Data>> list, Pools.Pool<List<Exception>> pool) {
        this.modelLoaders = list;
        this.exceptionListPool = pool;
    }

    @Override // com.bumptech.glide.load.model.ModelLoader
    public ModelLoader.LoadData<Data> buildLoadData(Model model, int i, int i2, Options options) {
        ModelLoader.LoadData<Data> buildLoadData;
        int size = this.modelLoaders.size();
        ArrayList arrayList = new ArrayList(size);
        Key key = null;
        for (int i3 = 0; i3 < size; i3++) {
            ModelLoader<Model, Data> modelLoader = this.modelLoaders.get(i3);
            if (modelLoader.handles(model) && (buildLoadData = modelLoader.buildLoadData(model, i, i2, options)) != null) {
                key = buildLoadData.sourceKey;
                arrayList.add(buildLoadData.fetcher);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return new ModelLoader.LoadData<>(key, new MultiFetcher(arrayList, this.exceptionListPool));
    }

    @Override // com.bumptech.glide.load.model.ModelLoader
    public boolean handles(Model model) {
        Iterator<ModelLoader<Model, Data>> it = this.modelLoaders.iterator();
        while (it.hasNext()) {
            if (it.next().handles(model)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "MultiModelLoader{modelLoaders=" + Arrays.toString(this.modelLoaders.toArray(new ModelLoader[this.modelLoaders.size()])) + '}';
    }
}
