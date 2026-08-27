package javax.mail;

/* loaded from: classes2.dex */
public class Quota {
    public String quotaRoot;
    public Resource[] resources;

    /* loaded from: classes2.dex */
    public static class Resource {
        public long limit;
        public String name;
        public long usage;

        public Resource(String str, long j, long j2) {
            this.name = str;
            this.usage = j;
            this.limit = j2;
        }
    }

    public Quota(String str) {
        this.quotaRoot = str;
    }

    public void setResourceLimit(String str, long j) {
        if (this.resources == null) {
            this.resources = new Resource[1];
            this.resources[0] = new Resource(str, 0L, j);
            return;
        }
        for (int i = 0; i < this.resources.length; i++) {
            if (this.resources[i].name.equalsIgnoreCase(str)) {
                this.resources[i].limit = j;
                return;
            }
        }
        Resource[] resourceArr = new Resource[this.resources.length + 1];
        System.arraycopy(this.resources, 0, resourceArr, 0, this.resources.length);
        resourceArr[resourceArr.length - 1] = new Resource(str, 0L, j);
        this.resources = resourceArr;
    }
}
