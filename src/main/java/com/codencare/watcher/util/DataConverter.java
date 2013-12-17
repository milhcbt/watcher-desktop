/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.util;

import java.security.InvalidParameterException;

/**
 * Data Conversion class
 *
 * @author Iman L Hakim <imanlhakim at gmail.com>
 */
public class DataConverter {

    public static final String STRING_COME = ",";
    public static final String STRING_NEWLINE = "\n";
    public static final String STRING_EMPTY = "";
    public static final String REGEX_SPACE = "[\\s]";
    public static final String REGEX_START_WITH_ZERO = "^0";
    public static final String INDO_COUNTRYCODE = "+62";
    public static final String REGEX_PHONE_FORMAT = "\\+[0-9]{6,15}";

    /**
     * TODO: unsafe
     *
     * @param raw only 4 Bytes
     * @return
     */
    public static long bytesToLong(byte[] raw) {
        if (raw.length != 4) {
            throw new InvalidParameterException("raw must be 4 byte length");
        }
        return ((raw[0] & 0xFFl) << (3 * 8))
                + ((raw[1] & 0xFFl) << (2 * 8))
                + ((raw[2] & 0xFFl) << (1 * 8))
                + (raw[3] & 0xFFl);
    }

    /**
     * Convert phone list in "any white space" separated value usually from
     * Text-box or TextArea into Array of Strings of phone numbers
     *
     * @param wssv phone number from TextAre
     * @return Array of phone number
     */
    public static String[] wssvToArray(String wssv) {
        return wssv.split(REGEX_SPACE);
    }

    /**
     * Convert List of phone number into Come separated value.
     *
     * @param phoneList list of phone numbers in String
     * @return phone number list in CSV format.
     */
    public static String listToCSV(String[] phoneList) {
        StringBuilder sb = new StringBuilder();
        for (String s : phoneList) {
            sb.append(s);
            sb.append(STRING_COME);
        }
        sb.delete(sb.lastIndexOf(STRING_COME), sb.lastIndexOf(STRING_EMPTY));//remove last come
        return sb.toString();
    }

    /**
     * convert come separated value into string with "new line" separated value.
     *
     * @param csv string with coma separated value.
     * @return string with "new line" separated value.
     */
    public static String csvToNlvs(String csv) {
        return csv.replace(STRING_COME, STRING_NEWLINE);
    }

    
     /**
     * validate phone number
     * valid number must have contry code, starts with "+"
     * @param phone
     * @return 
     */
    public static boolean validatePhone(String phone) {
        return phone.matches(REGEX_PHONE_FORMAT);
    }
    

    /**
     * Make sure a phone number has country code.
     * some modem only send text to number with country code.
     * assume all number without country code is Indonesia number (+62)
     * @param phone phone number without country code.
     * @return phone number with country code.
     */
    public static String normalizePhone(String phone) {
        return phone.replaceFirst(REGEX_START_WITH_ZERO, INDO_COUNTRYCODE);
    }
}
