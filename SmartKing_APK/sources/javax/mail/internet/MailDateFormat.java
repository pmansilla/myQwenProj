package javax.mail.internet;

import com.sun.mail.util.MailLogger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import me.panpf.sketch.SLog;
import org.apache.commons.lang.CharUtils;

/* loaded from: classes2.dex */
public class MailDateFormat extends SimpleDateFormat {
    private static final int LEAP_SECOND = 60;
    private static final String PATTERN = "EEE, d MMM yyyy HH:mm:ss Z (z)";
    private static final int UNKNOWN_DAY_NAME = -1;
    private static final long serialVersionUID = -8148227605210628779L;
    private static final MailLogger LOGGER = new MailLogger((Class<?>) MailDateFormat.class, SLog.LEVEL_NAME_DEBUG, false, System.out);
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static abstract class AbstractDateParser {
        static final int INVALID_CHAR = -1;
        static final int MAX_YEAR_DIGITS = 8;
        final ParsePosition pos;
        final String text;

        AbstractDateParser(String str, ParsePosition parsePosition) {
            this.text = str;
            this.pos = parsePosition;
        }

        final int getAsciiDigit() {
            int i = getChar();
            if (48 <= i && i <= 57) {
                return Character.digit((char) i, 10);
            }
            if (i != -1) {
                this.pos.setIndex(this.pos.getIndex() - 1);
            }
            return -1;
        }

        final int getChar() {
            if (this.pos.getIndex() >= this.text.length()) {
                return -1;
            }
            char charAt = this.text.charAt(this.pos.getIndex());
            this.pos.setIndex(this.pos.getIndex() + 1);
            return charAt;
        }

        boolean isValidZoneOffset(int i) {
            return i % 100 < 60;
        }

        final Date parse() {
            int index = this.pos.getIndex();
            try {
                return tryParse();
            } catch (Exception e) {
                if (MailDateFormat.LOGGER.isLoggable(Level.FINE)) {
                    MailDateFormat.LOGGER.log(Level.FINE, "Bad date: '" + this.text + "'", (Throwable) e);
                }
                this.pos.setErrorIndex(this.pos.getIndex());
                this.pos.setIndex(index);
                return null;
            }
        }

        final int parseAsciiDigits(int i) throws java.text.ParseException {
            return parseAsciiDigits(i, i);
        }

        final int parseAsciiDigits(int i, int i2) throws java.text.ParseException {
            return parseAsciiDigits(i, i2, false);
        }

        final int parseAsciiDigits(int i, int i2, boolean z) throws java.text.ParseException {
            String str;
            int i3 = 0;
            int i4 = 0;
            while (i3 < i2 && peekAsciiDigit()) {
                i4 = (i4 * 10) + getAsciiDigit();
                i3++;
            }
            if (i3 >= i && (i3 != i2 || z || !peekAsciiDigit())) {
                return i4;
            }
            this.pos.setIndex(this.pos.getIndex() - i3);
            if (i == i2) {
                str = Integer.toString(i);
            } else {
                str = "between " + i + " and " + i2;
            }
            throw new java.text.ParseException("Invalid input: expected " + str + " ASCII digits", this.pos.getIndex());
        }

        final void parseChar(char c) throws java.text.ParseException {
            if (skipChar(c)) {
                return;
            }
            throw new java.text.ParseException("Invalid input: expected '" + c + "'", this.pos.getIndex());
        }

        final int parseDayName() throws java.text.ParseException {
            int i = getChar();
            if (i == -1) {
                throw new java.text.ParseException("Invalid day-name", this.pos.getIndex());
            }
            if (i != 70) {
                if (i != 77) {
                    if (i != 87) {
                        switch (i) {
                            case 83:
                                if (skipPair('u', 'n')) {
                                    return 1;
                                }
                                if (skipPair('a', 't')) {
                                    return 7;
                                }
                                break;
                            case 84:
                                if (skipPair('u', 'e')) {
                                    return 3;
                                }
                                if (skipPair('h', 'u')) {
                                    return 5;
                                }
                                break;
                        }
                    } else if (skipPair('e', 'd')) {
                        return 4;
                    }
                } else if (skipPair('o', 'n')) {
                    return 2;
                }
            } else if (skipPair('r', 'i')) {
                return 6;
            }
            this.pos.setIndex(this.pos.getIndex() - 1);
            throw new java.text.ParseException("Invalid day-name", this.pos.getIndex());
        }

        final void parseFoldingWhiteSpace() throws java.text.ParseException {
            if (!skipFoldingWhiteSpace()) {
                throw new java.text.ParseException("Invalid input: expected FWS", this.pos.getIndex());
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:10:0x0032, code lost:
        
            if (r11 == false) goto L107;
         */
        /* JADX WARN: Code restructure failed: missing block: B:3:0x001a, code lost:
        
            if (r11 == false) goto L21;
         */
        /* JADX WARN: Code restructure failed: missing block: B:4:0x001d, code lost:
        
            if (r11 == false) goto L28;
         */
        /* JADX WARN: Code restructure failed: missing block: B:5:0x0020, code lost:
        
            if (r11 == false) goto L35;
         */
        /* JADX WARN: Code restructure failed: missing block: B:6:0x0023, code lost:
        
            if (r11 == false) goto L42;
         */
        /* JADX WARN: Code restructure failed: missing block: B:7:0x0026, code lost:
        
            if (r11 == false) goto L64;
         */
        /* JADX WARN: Code restructure failed: missing block: B:8:0x002a, code lost:
        
            if (r11 == false) goto L94;
         */
        /* JADX WARN: Code restructure failed: missing block: B:9:0x002e, code lost:
        
            if (r11 == false) goto L100;
         */
        /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0015. Please report as an issue. */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        final int parseMonthName(boolean r11) throws java.text.ParseException {
            /*
                Method dump skipped, instructions count: 470
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.mail.internet.MailDateFormat.AbstractDateParser.parseMonthName(boolean):int");
        }

        final int parseZoneOffset() throws java.text.ParseException {
            int i = getChar();
            if (i != 43 && i != 45) {
                if (i != -1) {
                    this.pos.setIndex(this.pos.getIndex() - 1);
                }
                throw new java.text.ParseException("Invalid zone", this.pos.getIndex());
            }
            int parseAsciiDigits = parseAsciiDigits(4, 4, true);
            if (isValidZoneOffset(parseAsciiDigits)) {
                return (i != 43 ? 1 : -1) * (((parseAsciiDigits / 100) * 60) + (parseAsciiDigits % 100));
            }
            this.pos.setIndex(this.pos.getIndex() - 5);
            throw new java.text.ParseException("Invalid zone", this.pos.getIndex());
        }

        final boolean peekAsciiDigit() {
            return this.pos.getIndex() < this.text.length() && '0' <= this.text.charAt(this.pos.getIndex()) && this.text.charAt(this.pos.getIndex()) <= '9';
        }

        final boolean peekChar(char c) {
            return this.pos.getIndex() < this.text.length() && this.text.charAt(this.pos.getIndex()) == c;
        }

        boolean peekFoldingWhiteSpace() {
            return this.pos.getIndex() < this.text.length() && (this.text.charAt(this.pos.getIndex()) == ' ' || this.text.charAt(this.pos.getIndex()) == '\t' || this.text.charAt(this.pos.getIndex()) == '\r');
        }

        final boolean skipAlternative(char c, char c2) {
            return skipChar(c) || skipChar(c2);
        }

        final boolean skipAlternativePair(char c, char c2, char c3, char c4) {
            if (!skipAlternative(c, c2)) {
                return false;
            }
            if (skipAlternative(c3, c4)) {
                return true;
            }
            this.pos.setIndex(this.pos.getIndex() - 1);
            return false;
        }

        final boolean skipAlternativeTriple(char c, char c2, char c3, char c4, char c5, char c6) {
            if (!skipAlternativePair(c, c2, c3, c4)) {
                return false;
            }
            if (skipAlternative(c5, c6)) {
                return true;
            }
            this.pos.setIndex(this.pos.getIndex() - 2);
            return false;
        }

        final boolean skipChar(char c) {
            if (this.pos.getIndex() >= this.text.length() || this.text.charAt(this.pos.getIndex()) != c) {
                return false;
            }
            this.pos.setIndex(this.pos.getIndex() + 1);
            return true;
        }

        boolean skipFoldingWhiteSpace() {
            if (skipChar(' ')) {
                if (!peekFoldingWhiteSpace()) {
                    return true;
                }
                this.pos.setIndex(this.pos.getIndex() - 1);
            } else if (!peekFoldingWhiteSpace()) {
                return false;
            }
            int index = this.pos.getIndex();
            if (!skipWhiteSpace()) {
                if (skipNewline() && skipWhiteSpace()) {
                    return true;
                }
                this.pos.setIndex(index);
                return false;
            }
            while (skipNewline()) {
                if (!skipWhiteSpace()) {
                    this.pos.setIndex(index);
                    return false;
                }
            }
            return true;
        }

        final boolean skipNewline() {
            return skipPair(CharUtils.CR, '\n');
        }

        final boolean skipPair(char c, char c2) {
            if (!skipChar(c)) {
                return false;
            }
            if (skipChar(c2)) {
                return true;
            }
            this.pos.setIndex(this.pos.getIndex() - 1);
            return false;
        }

        final boolean skipWhiteSpace() {
            int index = this.pos.getIndex();
            do {
            } while (skipAlternative(' ', '\t'));
            return this.pos.getIndex() > index;
        }

        abstract Date tryParse() throws java.text.ParseException;
    }

    /* loaded from: classes2.dex */
    private class Rfc2822LenientParser extends Rfc2822StrictParser {
        private Boolean hasDefaultFws;

        Rfc2822LenientParser(String str, ParsePosition parsePosition) {
            super(str, parsePosition);
        }

        @Override // javax.mail.internet.MailDateFormat.Rfc2822StrictParser
        boolean isMonthNameCaseSensitive() {
            return false;
        }

        @Override // javax.mail.internet.MailDateFormat.AbstractDateParser
        boolean isValidZoneOffset(int i) {
            return true;
        }

        @Override // javax.mail.internet.MailDateFormat.Rfc2822StrictParser
        int parseDay() throws java.text.ParseException {
            skipFoldingWhiteSpace();
            return parseAsciiDigits(1, 3);
        }

        @Override // javax.mail.internet.MailDateFormat.Rfc2822StrictParser
        void parseFwsBetweenTimeOfDayAndZone() throws java.text.ParseException {
            skipFoldingWhiteSpace();
        }

        @Override // javax.mail.internet.MailDateFormat.Rfc2822StrictParser
        void parseFwsInMonth() throws java.text.ParseException {
            if (this.hasDefaultFws == null) {
                this.hasDefaultFws = Boolean.valueOf(!skipChar('-'));
                skipFoldingWhiteSpace();
            } else if (this.hasDefaultFws.booleanValue()) {
                skipFoldingWhiteSpace();
            } else {
                parseChar('-');
            }
        }

        @Override // javax.mail.internet.MailDateFormat.Rfc2822StrictParser
        int parseHour() throws java.text.ParseException {
            return parseAsciiDigits(1, 2);
        }

        @Override // javax.mail.internet.MailDateFormat.Rfc2822StrictParser
        int parseMinute() throws java.text.ParseException {
            return parseAsciiDigits(1, 2);
        }

        @Override // javax.mail.internet.MailDateFormat.Rfc2822StrictParser
        int parseOptionalBegin() {
            while (this.pos.getIndex() < this.text.length() && !peekAsciiDigit()) {
                this.pos.setIndex(this.pos.getIndex() + 1);
            }
            return -1;
        }

        @Override // javax.mail.internet.MailDateFormat.Rfc2822StrictParser
        int parseSecond() throws java.text.ParseException {
            return parseAsciiDigits(1, 2);
        }

        @Override // javax.mail.internet.MailDateFormat.Rfc2822StrictParser
        int parseYear() throws java.text.ParseException {
            int parseAsciiDigits = parseAsciiDigits(1, 8);
            return parseAsciiDigits >= 1000 ? parseAsciiDigits : parseAsciiDigits >= 50 ? parseAsciiDigits + 1900 : parseAsciiDigits + 2000;
        }

        @Override // javax.mail.internet.MailDateFormat.Rfc2822StrictParser
        int parseZone() throws java.text.ParseException {
            int i;
            try {
                if (this.pos.getIndex() >= this.text.length()) {
                    throw new java.text.ParseException("Missing zone", this.pos.getIndex());
                }
                if (!peekChar('+') && !peekChar('-')) {
                    if (skipAlternativePair('U', 'u', 'T', 't') || skipAlternativeTriple('G', 'g', 'M', 'm', 'T', 't')) {
                        return 0;
                    }
                    if (skipAlternative('E', 'e')) {
                        i = 4;
                    } else if (skipAlternative('C', 'c')) {
                        i = 5;
                    } else if (skipAlternative('M', 'm')) {
                        i = 6;
                    } else {
                        if (!skipAlternative('P', 'p')) {
                            throw new java.text.ParseException("Invalid zone", this.pos.getIndex());
                        }
                        i = 7;
                    }
                    if (skipAlternativePair('S', 's', 'T', 't')) {
                        i++;
                    } else if (!skipAlternativePair('D', 'd', 'T', 't')) {
                        this.pos.setIndex(this.pos.getIndex() - 1);
                        throw new java.text.ParseException("Invalid zone", this.pos.getIndex());
                    }
                    return i * 60;
                }
                return parseZoneOffset();
            } catch (java.text.ParseException e) {
                if (MailDateFormat.LOGGER.isLoggable(Level.FINE)) {
                    MailDateFormat.LOGGER.log(Level.FINE, "No timezone? : '" + this.text + "'", (Throwable) e);
                }
                return 0;
            }
        }

        @Override // javax.mail.internet.MailDateFormat.AbstractDateParser
        boolean peekFoldingWhiteSpace() {
            return super.peekFoldingWhiteSpace() || (this.pos.getIndex() < this.text.length() && this.text.charAt(this.pos.getIndex()) == '\n');
        }

        @Override // javax.mail.internet.MailDateFormat.AbstractDateParser
        boolean skipFoldingWhiteSpace() {
            boolean peekFoldingWhiteSpace = peekFoldingWhiteSpace();
            while (this.pos.getIndex() < this.text.length()) {
                char charAt = this.text.charAt(this.pos.getIndex());
                if (charAt != '\r' && charAt != ' ') {
                    switch (charAt) {
                        case '\t':
                        case '\n':
                            break;
                        default:
                            return peekFoldingWhiteSpace;
                    }
                }
                this.pos.setIndex(this.pos.getIndex() + 1);
            }
            return peekFoldingWhiteSpace;
        }
    }

    /* loaded from: classes2.dex */
    private class Rfc2822StrictParser extends AbstractDateParser {
        Rfc2822StrictParser(String str, ParsePosition parsePosition) {
            super(str, parsePosition);
        }

        boolean isMonthNameCaseSensitive() {
            return true;
        }

        int parseDay() throws java.text.ParseException {
            skipFoldingWhiteSpace();
            return parseAsciiDigits(1, 2);
        }

        void parseFwsBetweenTimeOfDayAndZone() throws java.text.ParseException {
            parseFoldingWhiteSpace();
        }

        void parseFwsInMonth() throws java.text.ParseException {
            parseFoldingWhiteSpace();
        }

        int parseHour() throws java.text.ParseException {
            return parseAsciiDigits(2);
        }

        int parseMinute() throws java.text.ParseException {
            return parseAsciiDigits(2);
        }

        int parseMonth() throws java.text.ParseException {
            parseFwsInMonth();
            int parseMonthName = parseMonthName(isMonthNameCaseSensitive());
            parseFwsInMonth();
            return parseMonthName;
        }

        int parseOptionalBegin() throws java.text.ParseException {
            if (peekAsciiDigit()) {
                return -1;
            }
            skipFoldingWhiteSpace();
            int parseDayName = parseDayName();
            parseChar(',');
            return parseDayName;
        }

        int parseSecond() throws java.text.ParseException {
            return parseAsciiDigits(2);
        }

        int parseYear() throws java.text.ParseException {
            int parseAsciiDigits = parseAsciiDigits(4, 8);
            if (parseAsciiDigits >= 1900) {
                return parseAsciiDigits;
            }
            this.pos.setIndex(this.pos.getIndex() - 4);
            while (this.text.charAt(this.pos.getIndex() - 1) == '0') {
                this.pos.setIndex(this.pos.getIndex() - 1);
            }
            throw new java.text.ParseException("Invalid year", this.pos.getIndex());
        }

        int parseZone() throws java.text.ParseException {
            return parseZoneOffset();
        }

        @Override // javax.mail.internet.MailDateFormat.AbstractDateParser
        Date tryParse() throws java.text.ParseException {
            int parseOptionalBegin = parseOptionalBegin();
            int parseDay = parseDay();
            int parseMonth = parseMonth();
            int parseYear = parseYear();
            parseFoldingWhiteSpace();
            int parseHour = parseHour();
            parseChar(':');
            int parseMinute = parseMinute();
            int parseSecond = skipChar(':') ? parseSecond() : 0;
            parseFwsBetweenTimeOfDayAndZone();
            try {
                return MailDateFormat.this.toDate(parseOptionalBegin, parseDay, parseMonth, parseYear, parseHour, parseMinute, parseSecond, parseZone());
            } catch (IllegalArgumentException unused) {
                throw new java.text.ParseException("Invalid input: some of the calendar fields have invalid values, or day-name is inconsistent with date", this.pos.getIndex());
            }
        }
    }

    public MailDateFormat() {
        super(PATTERN, Locale.US);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        super.applyPattern(PATTERN);
    }

    private void superApplyPattern(String str) {
        super.applyPattern(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Date toDate(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int i9 = i7 == 60 ? 59 : i7;
        TimeZone timeZone = this.calendar.getTimeZone();
        try {
            this.calendar.setTimeZone(UTC);
            this.calendar.clear();
            this.calendar.set(i4, i3, i2, i5, i6, i9);
            if (i != -1 && i != this.calendar.get(7)) {
                throw new IllegalArgumentException("Inconsistent day-name");
            }
            this.calendar.add(12, i8);
            return this.calendar.getTime();
        } finally {
            this.calendar.setTimeZone(timeZone);
        }
    }

    private Object writeReplace() throws ObjectStreamException {
        MailDateFormat mailDateFormat = new MailDateFormat();
        mailDateFormat.superApplyPattern("EEE, d MMM yyyy HH:mm:ss 'XXXXX' (z)");
        mailDateFormat.setTimeZone(getTimeZone());
        return mailDateFormat;
    }

    @Override // java.text.SimpleDateFormat
    public void applyLocalizedPattern(String str) {
        throw new UnsupportedOperationException("Method applyLocalizedPattern() shouldn't be called");
    }

    @Override // java.text.SimpleDateFormat
    public void applyPattern(String str) {
        throw new UnsupportedOperationException("Method applyPattern() shouldn't be called");
    }

    @Override // java.text.SimpleDateFormat, java.text.DateFormat, java.text.Format
    public MailDateFormat clone() {
        return (MailDateFormat) super.clone();
    }

    @Override // java.text.SimpleDateFormat, java.text.DateFormat
    public StringBuffer format(Date date, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        return super.format(date, stringBuffer, fieldPosition);
    }

    @Override // java.text.SimpleDateFormat
    public Date get2DigitYearStart() {
        throw new UnsupportedOperationException("Method get2DigitYearStart() shouldn't be called");
    }

    @Override // java.text.SimpleDateFormat, java.text.DateFormat
    public Date parse(String str, ParsePosition parsePosition) {
        if (str == null || parsePosition == null) {
            throw new NullPointerException();
        }
        if (parsePosition.getIndex() < 0 || parsePosition.getIndex() >= str.length()) {
            return null;
        }
        return isLenient() ? new Rfc2822LenientParser(str, parsePosition).parse() : new Rfc2822StrictParser(str, parsePosition).parse();
    }

    @Override // java.text.SimpleDateFormat
    public void set2DigitYearStart(Date date) {
        throw new UnsupportedOperationException("Method set2DigitYearStart() shouldn't be called");
    }

    @Override // java.text.DateFormat
    public void setCalendar(Calendar calendar) {
        throw new UnsupportedOperationException("Method setCalendar() shouldn't be called");
    }

    @Override // java.text.SimpleDateFormat
    public void setDateFormatSymbols(DateFormatSymbols dateFormatSymbols) {
        throw new UnsupportedOperationException("Method setDateFormatSymbols() shouldn't be called");
    }

    @Override // java.text.DateFormat
    public void setNumberFormat(NumberFormat numberFormat) {
        throw new UnsupportedOperationException("Method setNumberFormat() shouldn't be called");
    }
}
