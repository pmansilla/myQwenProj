package com.mob.wrappers;

import com.mob.paysdk.AliPayAPI;
import com.mob.paysdk.MobPayAPI;
import com.mob.paysdk.OnPayListener;
import com.mob.paysdk.PayResult;
import com.mob.paysdk.PaySDK;
import com.mob.paysdk.WXPayAPI;
import com.mob.tools.proguard.PublicMemberKeeper;

/* loaded from: classes2.dex */
public class PaySDKWrapper extends SDKWrapper implements PublicMemberKeeper {
    private static int state;

    /* loaded from: classes2.dex */
    public static class LinkagePaySDKError extends Error {
    }

    /* loaded from: classes2.dex */
    public static abstract class Order {
    }

    /* loaded from: classes2.dex */
    public interface PayCallback {
        void onPayEnd(Order order, int i, int i2);

        boolean onWillPay(Order order, int i, String str);
    }

    /* loaded from: classes2.dex */
    public static class PayOrder extends Order implements PublicMemberKeeper {
        private com.mob.paysdk.PayOrder order;

        public PayOrder() {
            if (PaySDKWrapper.access$200()) {
                this.order = new com.mob.paysdk.PayOrder();
            }
        }

        public int getAmount() {
            if (PaySDKWrapper.access$200()) {
                return this.order.getAmount();
            }
            return 0;
        }

        public String getBody() {
            return PaySDKWrapper.access$200() ? this.order.getBody() : "";
        }

        public String getDescription() {
            return PaySDKWrapper.access$200() ? this.order.getDescription() : "";
        }

        public String getMetadata() {
            return PaySDKWrapper.access$200() ? this.order.getMetadata() : "";
        }

        public String getOrderNo() {
            return PaySDKWrapper.access$200() ? this.order.getOrderNo() : "";
        }

        public String getSubject() {
            return PaySDKWrapper.access$200() ? this.order.getSubject() : "";
        }

        public String getTicketId() {
            return PaySDKWrapper.access$200() ? this.order.getTicketId() : "";
        }

        public void setAmount(int i) {
            if (PaySDKWrapper.access$200()) {
                this.order.setAmount(i);
            }
        }

        public void setBody(String str) {
            if (PaySDKWrapper.access$200()) {
                this.order.setBody(str);
            }
        }

        public void setDescription(String str) {
            if (PaySDKWrapper.access$200()) {
                this.order.setDescription(str);
            }
        }

        public void setMetadata(String str) {
            if (PaySDKWrapper.access$200()) {
                this.order.setMetadata(str);
            }
        }

        public void setOrderNo(String str) {
            if (PaySDKWrapper.access$200()) {
                this.order.setOrderNo(str);
            }
        }

        public void setSubject(String str) {
            if (PaySDKWrapper.access$200()) {
                this.order.setSubject(str);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class TicketOrder extends Order implements PublicMemberKeeper {
        private com.mob.paysdk.TicketOrder order;

        public TicketOrder() {
            if (PaySDKWrapper.access$200()) {
                this.order = new com.mob.paysdk.TicketOrder();
            }
        }

        public String getTicketId() {
            return PaySDKWrapper.access$200() ? this.order.getTicketId() : "";
        }

        public void setTicketId(String str) {
            if (PaySDKWrapper.access$200()) {
                this.order.setTicketId(str);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class UnknowOrder extends Exception {
    }

    /* loaded from: classes2.dex */
    public static class UnsupportedPayPlatform extends Exception {
    }

    static /* synthetic */ boolean access$200() {
        return isAvailable();
    }

    private static synchronized boolean isAvailable() {
        boolean z;
        synchronized (PaySDKWrapper.class) {
            if (state == 0) {
                state = isAvailable("PAYSDK");
            }
            z = state == 1;
        }
        return z;
    }

    public static int pay(final Order order, final int i, final PayCallback payCallback) throws LinkagePaySDKError, UnknowOrder, UnsupportedPayPlatform {
        com.mob.paysdk.PayOrder payOrder;
        Class<AliPayAPI> cls;
        if (!isAvailable()) {
            throw new LinkagePaySDKError();
        }
        if (order instanceof PayOrder) {
            payOrder = ((PayOrder) order).order;
        } else {
            if (!(order instanceof TicketOrder)) {
                throw new UnknowOrder();
            }
            payOrder = ((TicketOrder) order).order;
        }
        if (50 == i) {
            cls = AliPayAPI.class;
        } else {
            if (22 != i) {
                throw new UnsupportedPayPlatform();
            }
            cls = WXPayAPI.class;
        }
        PaySDK.createMobPayAPI(cls).pay(payOrder, new OnPayListener() { // from class: com.mob.wrappers.PaySDKWrapper.1
            public void onPayEnd(PayResult payResult, Object obj, MobPayAPI mobPayAPI) {
                if (PayCallback.this != null) {
                    PayCallback.this.onPayEnd(order, i, payResult.ordinal());
                }
            }

            public boolean onWillPay(String str, Object obj, MobPayAPI mobPayAPI) {
                return PayCallback.this.onWillPay(order, i, str);
            }
        });
        return 0;
    }
}
