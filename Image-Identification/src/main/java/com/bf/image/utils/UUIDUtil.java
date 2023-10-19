package com.bf.image.utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDUtil {
    public static Long generateUUID() {
        UUID uuid = UUID.randomUUID();

        long mostSignificantBits = uuid.getMostSignificantBits();
        long leastSignificantBits = uuid.getLeastSignificantBits();

        byte[] bytes = ByteBuffer.allocate(16)
                .putLong(mostSignificantBits)
                .putLong(leastSignificantBits)
                .array();

        bytes[0] &= 0x7F; // 将最高位的符号位设为0，确保生成的UUID为正数

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getLong();
    }

    public static void main(String[] args) {
        Long aLong = generateUUID();
        System.out.println("aLong = " + aLong);
    }
}
