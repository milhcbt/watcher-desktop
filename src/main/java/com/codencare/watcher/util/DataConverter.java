/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.util;

import java.security.InvalidParameterException;

/**
 * Data Conversion class
 * @author Iman L Hakim <imanlhakim at gmail.com>
 */
public class DataConverter {
    /**
     * TODO: unsafe
     * @param raw only 4 Bytes
     * @return 
     */
     public static long bytesToLong(byte[] raw){
       if(raw.length != 4){
           throw new InvalidParameterException("raw must be 4 byte length");
       }
       return
      ((raw [0] & 0xFFl) << (3*8)) + 
      ((raw [1] & 0xFFl) << (2*8)) +
      ((raw [2] & 0xFFl) << (1*8)) +
      (raw [3] &  0xFFl); 
    }
    
}
