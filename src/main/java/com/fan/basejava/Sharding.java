package com.fan.basejava;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.CRC32;

public class Sharding {
    public static void main(String[] args) {
        String idString = "17573925";
        CRC32 crc32 = new CRC32();
        try {
            crc32.update(idString.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        long id = crc32.getValue();

        TreeMap<Long, String> nodes = new TreeMap<>();
        Map.Entry<Long,String> entry = nodes.ceilingEntry(id);
        if (entry == null){
            entry = nodes.firstEntry();
        }

        String val = entry.getValue();
        System.out.println(val);
    }
}
