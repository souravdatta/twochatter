package com.sourav.apps;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

/**
 *
 * @author sourdatt
 */
public class PiCodec {
    public static String encode(String message) {
        URLCodec codec = new URLCodec("utf-8");
        try {
            return codec.encode(message);
        } catch (EncoderException urlex) {
            return message;
        }
    }
    
    public static String decode(String message) {
        URLCodec codec = new URLCodec("utf-8");
        try {
            return codec.decode(message);
        } catch (DecoderException urlex) {
            return message;
        }
    }
}
