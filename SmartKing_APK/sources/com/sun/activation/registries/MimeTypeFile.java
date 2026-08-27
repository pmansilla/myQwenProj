package com.sun.activation.registries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.StringTokenizer;

/* loaded from: classes2.dex */
public class MimeTypeFile {
    private String fname;
    private Hashtable type_hash;

    public MimeTypeFile() {
        this.fname = null;
        this.type_hash = new Hashtable();
    }

    public MimeTypeFile(InputStream inputStream) throws IOException {
        this.fname = null;
        this.type_hash = new Hashtable();
        parse(new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1")));
    }

    public MimeTypeFile(String str) throws IOException {
        this.fname = null;
        this.type_hash = new Hashtable();
        this.fname = str;
        FileReader fileReader = new FileReader(new File(this.fname));
        try {
            parse(new BufferedReader(fileReader));
        } finally {
            try {
                fileReader.close();
            } catch (IOException unused) {
            }
        }
    }

    private void parse(BufferedReader bufferedReader) throws IOException {
        String str;
        String readLine;
        loop0: while (true) {
            str = null;
            while (true) {
                readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break loop0;
                }
                if (str != null) {
                    readLine = String.valueOf(str) + readLine;
                }
                int length = readLine.length();
                if (readLine.length() > 0) {
                    int i = length - 1;
                    if (readLine.charAt(i) == '\\') {
                        str = readLine.substring(0, i);
                    }
                }
            }
            parseEntry(readLine);
        }
        if (str != null) {
            parseEntry(str);
        }
    }

    private void parseEntry(String str) {
        String trim = str.trim();
        if (trim.length() == 0 || trim.charAt(0) == '#') {
            return;
        }
        if (trim.indexOf(61) <= 0) {
            StringTokenizer stringTokenizer = new StringTokenizer(trim);
            if (stringTokenizer.countTokens() == 0) {
                return;
            }
            String nextToken = stringTokenizer.nextToken();
            while (stringTokenizer.hasMoreTokens()) {
                String nextToken2 = stringTokenizer.nextToken();
                MimeTypeEntry mimeTypeEntry = new MimeTypeEntry(nextToken, nextToken2);
                this.type_hash.put(nextToken2, mimeTypeEntry);
                if (LogSupport.isLoggable()) {
                    LogSupport.log("Added: " + mimeTypeEntry.toString());
                }
            }
            return;
        }
        LineTokenizer lineTokenizer = new LineTokenizer(trim);
        String str2 = null;
        while (lineTokenizer.hasMoreTokens()) {
            String nextToken3 = lineTokenizer.nextToken();
            String nextToken4 = (lineTokenizer.hasMoreTokens() && lineTokenizer.nextToken().equals("=") && lineTokenizer.hasMoreTokens()) ? lineTokenizer.nextToken() : null;
            if (nextToken4 == null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("Bad .mime.types entry: " + trim);
                    return;
                }
                return;
            }
            if (nextToken3.equals("type")) {
                str2 = nextToken4;
            } else if (nextToken3.equals("exts")) {
                StringTokenizer stringTokenizer2 = new StringTokenizer(nextToken4, ",");
                while (stringTokenizer2.hasMoreTokens()) {
                    String nextToken5 = stringTokenizer2.nextToken();
                    MimeTypeEntry mimeTypeEntry2 = new MimeTypeEntry(str2, nextToken5);
                    this.type_hash.put(nextToken5, mimeTypeEntry2);
                    if (LogSupport.isLoggable()) {
                        LogSupport.log("Added: " + mimeTypeEntry2.toString());
                    }
                }
            }
        }
    }

    public void appendToRegistry(String str) {
        try {
            parse(new BufferedReader(new StringReader(str)));
        } catch (IOException unused) {
        }
    }

    public String getMIMETypeString(String str) {
        MimeTypeEntry mimeTypeEntry = getMimeTypeEntry(str);
        if (mimeTypeEntry != null) {
            return mimeTypeEntry.getMIMEType();
        }
        return null;
    }

    public MimeTypeEntry getMimeTypeEntry(String str) {
        return (MimeTypeEntry) this.type_hash.get(str);
    }
}
