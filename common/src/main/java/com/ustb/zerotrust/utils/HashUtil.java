package com.ustb.zerotrust.utils;

/**
 * @program: simple_tools
 * @description:
 * @author: ChenWenLong
 * @create: 2019-05-23 16:01
 **/
public class HashUtil {

    private static final int R1_32 = 15;
    private static final int R2_32 = 13;
    private static final int M_32 = 5;
    private static final int N_32 = 0xe6546b64;
    private static final int C1_32 = 0xcc9e2d51;
    private static final int C2_32 = 0x1b873593;
    private static final int DEFAULT_SEED = 0;

    /**
     * 加法hash
     *
     * @param key 字符串
     * @param prime 一个质数
     * @return hash结果
     */
    public static int additiveHash(String key, int prime) {
        int hash, i;
        for (hash = key.length(), i = 0; i < key.length(); i++) {
            hash += key.charAt(i);
        }
        return hash % prime;
    }

    /**
     * 旋转hash
     *
     * @param key 输入字符串
     * @param prime 质数
     * @return hash值
     */
    public static int rotatingHash(String key, int prime) {
        int hash, i;
        for (hash = key.length(), i = 0; i < key.length(); ++i) {
            hash = (hash << 4) ^ (hash >> 28) ^ key.charAt(i);
        }
        return hash % prime;
    }

    /**
     * 一次一个hash
     *
     * @param key 输入字符串
     * @return 输出hash值
     */
    public static int oneByOneHash(String key) {
        int hash, i;
        for (hash = 0, i = 0; i < key.length(); ++i) {
            hash += key.charAt(i);
            hash += (hash << 10);
            hash ^= (hash >> 6);
        }
        hash += (hash << 3);
        hash ^= (hash >> 11);
        hash += (hash << 15);
        return hash;
    }

    /**
     * Bernstein's hash
     *
     * @param key 输入字节数组
     * @return 结果hash
     */
    public static int bernstein(String key) {
        int hash = 0;
        int i;
        for (i = 0; i < key.length(); ++i) {
            hash = 33 * hash + key.charAt(i);
        }
        return hash;
    }

    /**
     * Universal Hashing
     *
     * @param key 字节数组
     * @param mask 掩码
     * @param tab tab
     * @return hash值
     */
    public static int universal(char[] key, int mask, int[] tab) {
        int hash = key.length, i, len = key.length;
        for (i = 0; i < (len << 3); i += 8) {
            char k = key[i >> 3];
            if ((k & 0x01) == 0) {
                hash ^= tab[i + 0];
            }
            if ((k & 0x02) == 0) {
                hash ^= tab[i + 1];
            }
            if ((k & 0x04) == 0) {
                hash ^= tab[i + 2];
            }
            if ((k & 0x08) == 0) {
                hash ^= tab[i + 3];
            }
            if ((k & 0x10) == 0) {
                hash ^= tab[i + 4];
            }
            if ((k & 0x20) == 0) {
                hash ^= tab[i + 5];
            }
            if ((k & 0x40) == 0) {
                hash ^= tab[i + 6];
            }
            if ((k & 0x80) == 0) {
                hash ^= tab[i + 7];
            }
        }
        return (hash & mask);
    }

    /**
     * Zobrist Hashing
     *
     * @param key 字节数组
     * @param mask 掩码
     * @param tab tab
     * @return hash值
     */
    public static int zobrist(char[] key, int mask, int[][] tab) {
        int hash, i;
        for (hash = key.length, i = 0; i < key.length; ++i) {
            hash ^= tab[i][key[i]];
        }
        return (hash & mask);
    }

    /**
     * 改进的32位FNV算法1
     *
     * @param data 数组
     * @return hash结果
     */
    public static int fnvHash(byte[] data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (byte b : data) {
            hash = (hash ^ b) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }

    /**
     * 改进的32位FNV算法1
     *
     * @param data 字符串
     * @return hash结果
     */
    public static int fnvHash(String data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < data.length(); i++) {
            hash = (hash ^ data.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }

    /**
     * Thomas Wang的算法，整数hash
     *
     * @param key 整数
     * @return hash值
     */
    public static int intHash(int key) {
        key += ~(key << 15);
        key ^= (key >>> 10);
        key += (key << 3);
        key ^= (key >>> 6);
        key += ~(key << 11);
        key ^= (key >>> 16);
        return key;
    }

    /**
     * RS算法hash
     *
     * @param str 字符串
     * @return hash值
     */
    public static int rsHash(String str) {
        int b = 378551;
        int a = 63689;
        int hash = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = hash * a + str.charAt(i);
            a = a * b;
        }

        return hash & 0x7FFFFFFF;
    }

    /**
     * JS算法
     *
     * @param str 字符串
     * @return hash值
     */
    public static int jsHash(String str) {
        int hash = 1315423911;

        for (int i = 0; i < str.length(); i++) {
            hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
        }

        return hash & 0x7FFFFFFF;
    }

    /**
     * PJW算法
     *
     * @param str 字符串
     * @return hash值
     */
    public static int pjwHash(String str) {
        int bitsInUnsignedInt = 32;
        int threeQuarters = (bitsInUnsignedInt * 3) / 4;
        int oneEighth = bitsInUnsignedInt / 8;
        int highBits = 0xFFFFFFFF << (bitsInUnsignedInt - oneEighth);
        int hash = 0;
        int test = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = (hash << oneEighth) + str.charAt(i);

            if ((test = hash & highBits) != 0) {
                hash = ((hash ^ (test >> threeQuarters)) & (~highBits));
            }
        }

        return hash & 0x7FFFFFFF;
    }

    /**
     * ELF算法
     *
     * @param str 字符串
     * @return hash值
     */
    public static int elfHash(String str) {
        int hash = 0;
        int x = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = (hash << 4) + str.charAt(i);
            if ((x = (int) (hash & 0xF0000000L)) != 0) {
                hash ^= (x >> 24);
                hash &= ~x;
            }
        }

        return hash & 0x7FFFFFFF;
    }

    /**
     * BKDR算法
     *
     * @param str 字符串
     * @return hash值
     */
    public static int bkdrHash(String str) {
        int seed = 131; // 31 131 1313 13131 131313 etc..
        int hash = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = (hash * seed) + str.charAt(i);
        }

        return hash & 0x7FFFFFFF;
    }

    /**
     * SDBM算法
     *
     * @param str 字符串
     * @return hash值
     */
    public static int sdbmHash(String str) {
        int hash = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
        }

        return hash & 0x7FFFFFFF;
    }

    /**
     * DJB算法
     *
     * @param str 字符串
     * @return hash值
     */
    public static int djbHash(String str) {
        int hash = 5381;

        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) + hash) + str.charAt(i);
        }

        return hash & 0x7FFFFFFF;
    }

    /**
     * DEK算法
     *
     * @param str 字符串
     * @return hash值
     */
    public static int dekHash(String str) {
        int hash = str.length();

        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
        }

        return hash & 0x7FFFFFFF;
    }

    /**
     * AP算法
     *
     * @param str 字符串
     * @return hash值
     */
    public static int apHash(String str) {
        int hash = 0;

        for (int i = 0; i < str.length(); i++) {
            hash ^= ((i & 1) == 0) ? ((hash << 7) ^ str.charAt(i) ^ (hash >> 3)) : (~((hash << 11) ^ str.charAt(i) ^ (hash >> 5)));
        }

        // return (hash & 0x7FFFFFFF);
        return hash;
    }

    /**
     * TianL Hash算法
     *
     * @param str 字符串
     * @return Hash值
     */
    public static long tianlHash(String str) {
        long hash = 0;

        int iLength = str.length();
        if (iLength == 0) {
            return 0;
        }

        if (iLength <= 256) {
            hash = 16777216L * (iLength - 1);
        } else {
            hash = 4278190080L;
        }

        int i;

        char ucChar;

        if (iLength <= 96) {
            for (i = 1; i <= iLength; i++) {
                ucChar = str.charAt(i - 1);
                if (ucChar <= 'Z' && ucChar >= 'A') {
                    ucChar = (char) (ucChar + 32);
                }
                hash += (3 * i * ucChar * ucChar + 5 * i * ucChar + 7 * i + 11 * ucChar) % 16777216;
            }
        } else {
            for (i = 1; i <= 96; i++) {
                ucChar = str.charAt(i + iLength - 96 - 1);
                if (ucChar <= 'Z' && ucChar >= 'A') {
                    ucChar = (char) (ucChar + 32);
                }
                hash += (3 * i * ucChar * ucChar + 5 * i * ucChar + 7 * i + 11 * ucChar) % 16777216;
            }
        }
        if (hash < 0) {
            hash *= -1;
        }
        return hash;
    }

    /**
     * JAVA自己带的算法
     *
     * @param str 字符串
     * @return hash值
     */
    public static int javaDefaultHash(String str) {
        int h = 0;
        int off = 0;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            h = 31 * h + str.charAt(off++);
        }
        return h;
    }

    /**
     * 混合hash算法，输出64位的值
     *
     * @param str 字符串
     * @return hash值
     */
    public static long mixHash(String str) {
        long hash = str.hashCode();
        hash <<= 32;
        hash |= fnvHash(str);
        return hash;
    }

    /**
     * 根据对象的内存地址生成相应的Hash值
     *
     * @param obj 对象
     * @return hash值
     * @since 4.2.2
     */
    public static int identityHashCode(Object obj) {
        return System.identityHashCode(obj);
    }

    /**
     * MurmurHash算法32-bit实现
     *
     * @param data 数据
     * @return hash值
     * @since 4.3.3
     */
    public static int murmur32(byte[] data) {
        int length = data.length;
        int hash = DEFAULT_SEED;
        final int nblocks = length >> 2;

        // body
        for (int i = 0; i < nblocks; i++) {
            int i_4 = i << 2;
            int k = (data[i_4] & 0xff) //
                    | ((data[i_4 + 1] & 0xff) << 8) //
                    | ((data[i_4 + 2] & 0xff) << 16) //
                    | ((data[i_4 + 3] & 0xff) << 24);

            // mix functions
            k *= C1_32;
            k = Integer.rotateLeft(k, R1_32);
            k *= C2_32;
            hash ^= k;
            hash = Integer.rotateLeft(hash, R2_32) * M_32 + N_32;
        }

        // tail
        int idx = nblocks << 2;
        int k1 = 0;
        switch (length - idx) {
            case 3:
                k1 ^= data[idx + 2] << 16;
            case 2:
                k1 ^= data[idx + 1] << 8;
            case 1:
                k1 ^= data[idx];

                // mix functions
                k1 *= C1_32;
                k1 = Integer.rotateLeft(k1, R1_32);
                k1 *= C2_32;
                hash ^= k1;
        }

        // finalization
        hash ^= length;
        hash ^= (hash >>> 16);
        hash *= 0x85ebca6b;
        hash ^= (hash >>> 13);
        hash *= 0xc2b2ae35;
        hash ^= (hash >>> 16);

        return hash;
    }

    /**
     * MurmurHash算法64-bit实现
     *
     * @param data 数据
     * @return hash值
     * @since 4.3.3
     */
    public static long murmur64(byte[] data) {
        return hash64(data);
    }

    private static long hash64(byte[] data) {
        return 0;
    }

    /**
     * MurmurHash算法128-bit实现
     *
     * @param data 数据
     * @return hash值
     * @since 4.3.3
     */
    public static long[] murmur128(byte[] data) {
        return hash128(data);
    }

    private static long[] hash128(byte[] data) {
        return new long[0];
    }
}


