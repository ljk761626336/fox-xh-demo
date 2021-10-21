package com.titan.mobile.arcruntime.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by zy on 2017/7/19.
 */
public final class StringEngine {

    private static String test1 = "CREATE TABLE TB_uploadlog (_id integer primary key autoincrement, uploadfilepath varchar(250), sourceid varchar(20),threadid integer)";

    private StringEngine() {
    }


    //首字母大写
    public static String upperFirst(String text) {
        char[] cs = text.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    public static String onlyUpperFirst(String text) {
        text = text.toLowerCase();
        char[] cs = text.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }


    /**
     * 不区分大小写
     * 指定字符串 某个部分（item）右侧 的 第i字符 (非空，空的不记录)
     *
     * @param item
     * @param i
     * @return
     */
    public static String StringRightIChar(String string, String item, int i) {
        if (string != null && item != null) {
            string = string.toLowerCase();
            //string = string.replaceAll(" ", "");
            int itemLength = item.length();
            int itemPosition = string.indexOf(item);
            int startPosition = itemLength + itemPosition;
            char c = string.charAt(startPosition + i);
            return c + "";
        } else {
            return null;
        }
    }

    /**
     * 指定字符串 某个部分（item） 的 第i字符 是不是指定的(非空，空的不记录)
     *
     * @param string
     * @param item
     * @param i
     * @param s
     * @return
     */
    public static boolean StringRightIChar(String string, String item, int i, String s) {
        String res = StringRightIChar(string, item, i);
        if (res != null && s != null) {
            if (res.endsWith(s)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 读取字符串第i次出现特定符号的位置
     *
     * @param string
     * @param i
     * @return
     */
    public static int getCharacterPosition(String string, String character, int i) {
        try {
            //这里是获取"/"符号的位置
            // Matcher slashMatcher = Pattern.compile("/").matcher(string);
            if (character.equals("(")) {
                character = "\\(";
            }
            if (character.equals(")")) {
                character = "\\)";
            }
            if (character.equals("+")) {
                character = "\\+";
            }
            Matcher slashMatcher = Pattern.compile(character).matcher(string);
            int mIdx = 0;
            while (slashMatcher.find()) {
                mIdx++;
                //当"/"符号第三次出现的位置
                if (mIdx == i) {
                    break;
                }
            }
            return slashMatcher.start();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 某个字符串 右侧 item字符 第 i次出现的位置
     *
     * @param string
     * @param item
     * @param character
     * @param i
     * @return
     */
    public static int StringRightICharPosition(String string, String item, String character, int i) {
        try {
            int stringLength = string.length();
            int itemLength = item.length();
            int itemIndex = string.toLowerCase().indexOf(item);
            int startPosition = itemIndex + itemLength;
            String rightString = string.substring(startPosition, stringLength);
            int rightPos = getCharacterPosition(rightString, character, i);
            return startPosition + rightPos;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取一个字符串出现的次数
     *
     * @param srcText
     * @param findText
     * @return
     */
    public static int StringCharNumber(String srcText, String findText) {
        int count = 0;
        int index = 0;
        while ((index = srcText.indexOf(findText, index)) != -1) {
            index = index + findText.length();
            count++;
        }
        return count;
    }

    /**
     * 删除字符串之间多余的空格，只保留一个
     *
     * @param string
     * @return
     */
    public static String StringKeepOneSpace(String string) {
        StringBuffer sb = new StringBuffer();//用其他方法实现
        int flag;
        for (int i = 0; i < string.length(); i++) {
            flag = 0;
            if (string.charAt(i) != ' ') {
                sb.append(string.charAt(i));
            } else {
                flag = 1;
            }
            try {
                if (string.charAt(i) == ' ' && string.charAt(i + 1) != ' ') {
                    sb.append(' ');
                }
            } catch (Exception e) {
                continue;
            }
        }
        return sb.toString();
    }

    public static String removeArrayString(String string, String[] array) {
        if (string != null) {
            for (int i = 0; i < array.length; i++) {
                String item = array[i];
                if (item != null) {
                    string = string.replace(item, "");
                }
            }
        }
        return string;
    }


    /**
     * @param string
     * @param item
     * @param character
     * @param i
     * @return
     */
    public static int StringRightKH(String string, String item, String character, int i) {
        int stringLength = string.length();
        int itemLength = item.length();
        int itemIndex = string.toLowerCase().indexOf(item);
        int startPosition = itemIndex + itemLength;
        String rightString = string.substring(startPosition, stringLength);
        int firstIndexRight = rightString.indexOf(")");
        String middleItem = rightString.substring(0, firstIndexRight);
        return 11;
    }


    /**
     * 字符对 匹配
     *
     * @param string
     * @param c1
     * @param c2
     * @return
     */
    public static List<String> extractMessage(String string, char c1, char c2) {

        List<String> list = new ArrayList<String>();
        int start = 0;
        int startFlag = 0;
        int endFlag = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == c1) {
                startFlag++;
                if (startFlag == endFlag + 1) {
                    start = i;
                }
            } else if (string.charAt(i) == c2) {
                endFlag++;
                if (endFlag == startFlag) {
                    list.add(string.substring(start + 1, i));
                }
            }
        }
        return list;
    }


    public static String subRightByStringAndIndex(String string, String c, int i) {
        try {
            if (c != null) {
                int index = getCharacterPosition(string, c, i);
                string = string.substring(0, index);
            }
            return string;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }


    public static String getMinJsonStructure(String jsonString, String keyWord) {
        int indexStart = jsonString.indexOf(keyWord);
        indexStart = indexStart + keyWord.length();
        String leftString = jsonString.substring(0, jsonString.indexOf(keyWord));
        int keyWordLeft = leftString.lastIndexOf("{");
        String rightString = jsonString.substring(indexStart);
        int rightLen = rightString.length();
        int keyWordRight = 0;
        int sum = 1;
        char c;
        for (int i = 0; i < rightLen; i++) {
            c = rightString.charAt(i);
            if (c == '{') {
                sum++;
            }
            if (c == '}') {
                sum--;
            }
            if (sum == 0) {
                keyWordRight = i;
                break;
            }
        }
        String resLeft = jsonString.substring(keyWordLeft, indexStart);
        String resRight = rightString.substring(0, keyWordRight + 1);
        String res = resLeft + resRight;
        return res;
    }

    /**
     * 字符串的压缩
     *
     * @param str 待压缩的字符串
     * @return 返回压缩后的字符串
     */
    public static String compress(String str) {
        try {
            if (null == str || str.length() <= 0) {
                return str;
            }
            // 创建一个新的 byte 数组输出流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 使用默认缓冲区大小创建新的输出流
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            // 将 b.length 个字节写入此输出流
            gzip.write(str.getBytes());
            gzip.close();
            // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
            return out.toString("ISO-8859-1");
        } catch (Exception e) {
            return str;
        }
    }


    /**
     * 字符串的解压
     *
     * @param str 对字符串解压
     * @return 返回解压缩后的字符串
     */
    public static String unCompress(String str) {
        try {
            if (null == str || str.length() <= 0) {
                return str;
            }
            // 创建一个新的 byte 数组输出流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
            ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
            // 使用默认缓冲区大小创建新的输入流
            GZIPInputStream gzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n = 0;
            // 将未压缩数据读入字节数组
            while ((n = gzip.read(buffer)) >= 0) {
                // 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
                out.write(buffer, 0, n);
            }
            // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
            return out.toString("UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    /*方法二：推荐，速度最快
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static void main(String[] args) {
        boolean s = StringRightIChar(test1, "table", 1, "b");
        System.out.println(s);

        List<String> list = extractMessage(test1, '(', ')');
        System.out.println(list);

        //"CREATE TABLE TB_uploadlog (_id integer primary key autoincrement, uploadfilepath varchar(250), sourceid varchar(20),threadid integer)";
        int index = getCharacterPosition(test1, "C", 1);

        StringRightICharPosition(test1, "table", "i", 2);
        int count = StringCharNumber("321321321", "32");

        StringRightKH(test1, "table", "i", 2);

        subRightByStringAndIndex("11+22", "+", 1);
    }


}
