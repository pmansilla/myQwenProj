package cn.smssdk.gui;

import android.content.Context;
import cn.smssdk.utils.SMSLog;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import io.reactivex.annotations.SchedulerSupport;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

/* loaded from: classes.dex */
public class SearchEngine {
    private static final String DB_FILE = "smssdk_pydb";
    private static HashMap<String, Object> hanzi2Pinyin;
    private boolean caseSensitive;
    private ArrayList<SearchIndex> index;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SearchIndex {
        private String code;
        private ArrayList<String> firstLatters;
        private ArrayList<String> pinyin;
        private String text;

        public SearchIndex(String str, String str2, HashMap<String, Object> hashMap) {
            this.text = str;
            this.code = str2;
            this.pinyin = new ArrayList<>();
            this.firstLatters = new ArrayList<>();
            createPinyinList(hashMap);
        }

        public SearchIndex(String str, HashMap<String, Object> hashMap) {
            this(str, null, hashMap);
        }

        private void createPinyinList(HashMap<String, Object> hashMap) {
            if (hashMap == null || hashMap.size() <= 0) {
                return;
            }
            char[] charArray = this.text.toCharArray();
            ArrayList<String[]> arrayList = new ArrayList<>();
            for (char c : charArray) {
                ArrayList arrayList2 = (ArrayList) hashMap.get(String.valueOf(c));
                int size = arrayList2 == null ? 0 : arrayList2.size();
                String[] strArr = new String[size];
                for (int i = 0; i < size; i++) {
                    String str = (String) ((HashMap) arrayList2.get(i)).get("yin");
                    if (SchedulerSupport.NONE.equals(str)) {
                        str = "";
                    }
                    strArr[i] = str;
                }
                arrayList.add(strArr);
            }
            HashSet<String> hashSet = new HashSet<>();
            HashSet<String> hashSet2 = new HashSet<>();
            toPinyinArray("", "", hashSet, hashSet2, arrayList);
            this.pinyin.addAll(hashSet);
            this.firstLatters.addAll(hashSet2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean match(String str, boolean z) {
            if (str == null || str.trim().length() <= 0) {
                return true;
            }
            if (!z) {
                str = str.toLowerCase();
            }
            if (this.text != null && this.text.toLowerCase().contains(str)) {
                return true;
            }
            if (this.code != null && this.code.contains(str)) {
                return true;
            }
            Iterator<String> it = this.pinyin.iterator();
            while (it.hasNext()) {
                if (it.next().contains(str)) {
                    return true;
                }
            }
            Iterator<String> it2 = this.firstLatters.iterator();
            while (it2.hasNext()) {
                if (it2.next().contains(str)) {
                    return true;
                }
            }
            return false;
        }

        private void toPinyinArray(String str, String str2, HashSet<String> hashSet, HashSet<String> hashSet2, ArrayList<String[]> arrayList) {
            if (arrayList.size() <= 0) {
                hashSet.add(str);
                hashSet2.add(str2);
                return;
            }
            String[] strArr = arrayList.get(0);
            ArrayList<String[]> arrayList2 = new ArrayList<>();
            arrayList2.addAll(arrayList);
            arrayList2.remove(0);
            for (String str3 : strArr) {
                if (str3.length() > 0) {
                    toPinyinArray(str + str3, str2 + str3.charAt(0), hashSet, hashSet2, arrayList2);
                } else {
                    toPinyinArray(str, str2, hashSet, hashSet2, arrayList2);
                }
            }
        }

        public String getText() {
            return this.text;
        }

        public String toString() {
            HashMap hashMap = new HashMap();
            hashMap.put("text", this.text);
            hashMap.put("pinyin", this.pinyin);
            hashMap.put("firstLatters", this.firstLatters);
            return hashMap.toString();
        }
    }

    public static void prepare(final Context context, final Runnable runnable) {
        Runnable runnable2 = new Runnable() { // from class: cn.smssdk.gui.SearchEngine.1
            /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0054 -> B:19:0x0063). Please report as a decompilation issue!!! */
            @Override // java.lang.Runnable
            public void run() {
                synchronized (SearchEngine.DB_FILE) {
                    if (SearchEngine.hanzi2Pinyin == null || SearchEngine.hanzi2Pinyin.size() <= 0) {
                        try {
                            int rawRes = ResHelper.getRawRes(context, SearchEngine.DB_FILE);
                            if (rawRes <= 0) {
                                HashMap unused = SearchEngine.hanzi2Pinyin = new HashMap();
                            } else {
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(context.getResources().openRawResource(rawRes))));
                                String readLine = bufferedReader.readLine();
                                bufferedReader.close();
                                HashMap unused2 = SearchEngine.hanzi2Pinyin = new Hashon().fromJson(readLine);
                            }
                        } catch (Throwable th) {
                            SMSLog.getInstance().w(th);
                            HashMap unused3 = SearchEngine.hanzi2Pinyin = new HashMap();
                        }
                    }
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            }
        };
        if (runnable != null) {
            new Thread(runnable2).start();
        } else {
            runnable2.run();
        }
    }

    public ArrayList<String> match(String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (this.index == null) {
            return arrayList;
        }
        Iterator<SearchIndex> it = this.index.iterator();
        while (it.hasNext()) {
            SearchIndex next = it.next();
            if (next.match(str, this.caseSensitive)) {
                arrayList.add(next.getText());
            }
        }
        return arrayList;
    }

    public void setCaseSensitive(boolean z) {
        this.caseSensitive = z;
    }

    public void setIndex(ArrayList<String> arrayList) {
        this.index = new ArrayList<>();
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            this.index.add(new SearchIndex(it.next(), hanzi2Pinyin));
        }
    }

    public void setIndexSet(ArrayList<String[]> arrayList) {
        this.index = new ArrayList<>();
        Iterator<String[]> it = arrayList.iterator();
        while (it.hasNext()) {
            String[] next = it.next();
            if (next != null && next.length >= 2) {
                this.index.add(new SearchIndex(next[0], next[1], hanzi2Pinyin));
            }
        }
    }
}
