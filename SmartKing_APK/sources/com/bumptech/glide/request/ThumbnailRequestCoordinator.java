package com.bumptech.glide.request;

import android.support.annotation.Nullable;

/* loaded from: classes.dex */
public class ThumbnailRequestCoordinator implements RequestCoordinator, Request {

    @Nullable
    private RequestCoordinator coordinator;
    private Request full;
    private boolean isRunning;
    private Request thumb;

    public ThumbnailRequestCoordinator() {
        this(null);
    }

    public ThumbnailRequestCoordinator(RequestCoordinator requestCoordinator) {
        this.coordinator = requestCoordinator;
    }

    private boolean parentCanNotifyStatusChanged() {
        return this.coordinator == null || this.coordinator.canNotifyStatusChanged(this);
    }

    private boolean parentCanSetImage() {
        return this.coordinator == null || this.coordinator.canSetImage(this);
    }

    private boolean parentIsAnyResourceSet() {
        return this.coordinator != null && this.coordinator.isAnyResourceSet();
    }

    @Override // com.bumptech.glide.request.Request
    public void begin() {
        this.isRunning = true;
        if (!this.thumb.isRunning()) {
            this.thumb.begin();
        }
        if (!this.isRunning || this.full.isRunning()) {
            return;
        }
        this.full.begin();
    }

    @Override // com.bumptech.glide.request.RequestCoordinator
    public boolean canNotifyStatusChanged(Request request) {
        return parentCanNotifyStatusChanged() && request.equals(this.full) && !isAnyResourceSet();
    }

    @Override // com.bumptech.glide.request.RequestCoordinator
    public boolean canSetImage(Request request) {
        return parentCanSetImage() && (request.equals(this.full) || !this.full.isResourceSet());
    }

    @Override // com.bumptech.glide.request.Request
    public void clear() {
        this.isRunning = false;
        this.thumb.clear();
        this.full.clear();
    }

    @Override // com.bumptech.glide.request.RequestCoordinator
    public boolean isAnyResourceSet() {
        return parentIsAnyResourceSet() || isResourceSet();
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isCancelled() {
        return this.full.isCancelled();
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isComplete() {
        return this.full.isComplete() || this.thumb.isComplete();
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isFailed() {
        return this.full.isFailed();
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isPaused() {
        return this.full.isPaused();
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isResourceSet() {
        return this.full.isResourceSet() || this.thumb.isResourceSet();
    }

    @Override // com.bumptech.glide.request.Request
    public boolean isRunning() {
        return this.full.isRunning();
    }

    @Override // com.bumptech.glide.request.RequestCoordinator
    public void onRequestSuccess(Request request) {
        if (request.equals(this.thumb)) {
            return;
        }
        if (this.coordinator != null) {
            this.coordinator.onRequestSuccess(this);
        }
        if (this.thumb.isComplete()) {
            return;
        }
        this.thumb.clear();
    }

    @Override // com.bumptech.glide.request.Request
    public void pause() {
        this.isRunning = false;
        this.full.pause();
        this.thumb.pause();
    }

    @Override // com.bumptech.glide.request.Request
    public void recycle() {
        this.full.recycle();
        this.thumb.recycle();
    }

    public void setRequests(Request request, Request request2) {
        this.full = request;
        this.thumb = request2;
    }
}
