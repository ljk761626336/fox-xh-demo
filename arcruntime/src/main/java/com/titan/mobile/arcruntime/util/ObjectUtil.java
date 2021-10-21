package com.titan.mobile.arcruntime.util;

import android.graphics.Color;
import android.os.Build;
import android.util.Base64;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public final class ObjectUtil {

    public static class Common {

        public static <T> T convert(Object val, Type type) {
            return Json.convertT(val, type);
        }

        public static <T> T convert(Object val, Class clazz) {
            return Json.convertO(val, clazz);
        }

        public static String convert(Object val) {
            if (val == null) return null;
            return Json.obj2Json(val);
        }

        public static boolean objFieldsIsEqual(Object o1, Object o2, String... fields) {
            boolean isEqual = false;
            Object v1, v2;
            for (String f : fields) {
                v1 = Reflect.getObjFieldVal(o1, f);
                v2 = Reflect.getObjFieldVal(o2, f);
                isEqual = baseTypeIsEqual(v1, v2, false);
                if (!isEqual) break;
            }
            return isEqual;
        }

        public static boolean baseTypeIsEqual(Object o1, Object o2, boolean nullEqual) {
            if (nullEqual) {
                if (o1 == null && o2 == null) {
                    return true;
                } else if (o1 != null && o2 == null || o1 == null && o2 != null) {
                    return false;
                } else {
                    return baseTypeIsEqual(o1, o2);
                }
            } else {
                if (o1 == null && o2 == null) {
                    return false;
                } else if ((o1 == null && o2 != null) || (o1 != null && o2 == null)) {
                    return false;
                } else {
                    return baseTypeIsEqual(o1, o2);
                }
            }
        }

        public static boolean baseTypeIsEqual(Object o1, Object o2) {
            if (o1 instanceof Number && o2 instanceof Number) {
                if (o1.toString().equals((o2.toString()))) {
                    return true;
                }
            }
            if (o1 instanceof Integer && o2 instanceof Integer) {
                if (((Integer) o1).intValue() == ((Integer) o2).intValue()) {
                    return true;
                }
            }
            if (o1 instanceof String && o2 instanceof String) {
                if (o1.equals(o2)) {
                    return true;
                }
            }
            if (o1 instanceof Long && o2 instanceof Long) {
                if (((Long) o1).longValue() == ((Long) o2).longValue()) {
                    return true;
                }
            }
            if (o1 instanceof Double && o2 instanceof Double) {
                if (((Double) o1).doubleValue() == ((Double) o2).doubleValue()) {
                    return true;
                }
            }
            if (o1 instanceof Short && o2 instanceof Short) {
                if (((Short) o1).shortValue() == ((Short) o2).shortValue()) {
                    return true;
                }
            }
            if (o1 instanceof Float && o2 instanceof Float) {
                if (((Float) o1).floatValue() == ((Float) o2).floatValue()) {
                    return true;
                }
            }
            if (o1 instanceof Boolean && o2 instanceof Boolean) {
                if ((Boolean) o1 == false && (Boolean) o2 == false) {
                    return true;
                }
                return ((Boolean) o1 && (Boolean) o2);
            }
            return false;
        }

        public static boolean baseTypeLike(Object baseObj, Object obj) {
            if (baseObj == null || obj == null) return false;
            if (baseObj instanceof String && obj instanceof String) {
                if (((String) baseObj).contains((String) obj)) {
                    return true;
                }
            }
            return false;
        }

        public static boolean faceEqual(Object o1, Object o2) {
            if (o1 == null) return false;
            String s1 = convert(o1, String.class);
            String s2 = convert(o2, String.class);
            return s1.equals(s2);
        }

        public static int compareTo(Object o1, Object o2) {
            if (o1 == null && o2 == null) return 0;
            if (o1 == null && o2 != null) return 1;
            if (o1 != null && o2 == null) return -1;
            if (o1 instanceof Integer && o2 instanceof Integer) {
                return ((Integer) o1).compareTo(((Integer) o2));
            }
            if (o1 instanceof String && o2 instanceof String) {
                return ((String) o1).compareTo(((String) o2));
            }
            if (o1 instanceof Long && o2 instanceof Long) {
                return ((Long) o1).compareTo(((Long) o2));
            }
            if (o1 instanceof Double && o2 instanceof Double) {
                return ((Double) o1).compareTo(((Double) o2));
            }
            if (o1 instanceof Short && o2 instanceof Short) {
                return ((Short) o1).compareTo(((Short) o2));
            }
            if (o1 instanceof Float && o2 instanceof Float) {
                return ((Float) o1).compareTo(((Float) o2));
            }
            if (o1 instanceof Boolean && o2 instanceof Boolean) {
                return ((Boolean) o1).compareTo(((Boolean) o2));
            }
            return 0;
        }

        public static <T> T deepClone(Object obj) {
            if (obj == null) return null;
            T[] objects = (T[]) new Object[1];
            objects[0] = (T) obj;
            T[] array = (T[]) new Object[1];
            System.arraycopy(objects, 0, array, 0, 1);
            return array[0];
        }

        /**
         * 显示时候使用
         *
         * @param obj
         * @return
         */
        public static String display(Object obj) {
            if (obj == null) return "";
            if (obj instanceof Date) {
                return Time.dateTimeToStr((Date) obj);
            } else {
                return obj + "";
            }
        }

        /**
         * 显示时候使用
         *
         * @param obj
         * @return
         */
        public static String removeNull(Object obj) {
            if (obj == null) return "";
            else return obj.toString();
        }

        /**
         * 保存时候使用
         *
         * @param obj
         * @return
         */
        public static String removeEmpty(String obj) {
            if ("".equals(obj)) return null;
            else return obj;
        }

        /**
         * 数据为空检查
         */
        public static boolean isEmpty(final Object obj) {
            if (obj == null) {
                return true;
            }
            if (obj instanceof CharSequence && obj.toString().length() == 0) {
                return true;
            }
            if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
                return true;
            }
            if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
                return true;
            }
            if (obj instanceof Map && ((Map) obj).isEmpty()) {
                return true;
            }
            if (obj instanceof SimpleArrayMap && ((SimpleArrayMap) obj).isEmpty()) {
                return true;
            }
            if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
                return true;
            }
            if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
                return true;
            }
            if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
                return true;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                    return true;
                }
            }
            if (obj instanceof LongSparseArray && ((LongSparseArray) obj).size() == 0) {
                return true;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (obj instanceof android.util.LongSparseArray
                        && ((android.util.LongSparseArray) obj).size() == 0) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 从一个对象里将属性赋到另一个对象里
         *
         * @param src
         * @param trg
         * @param fields
         * @return
         */
        public static Object copyFields(Object src, Object trg, Map<String, String> fields) {
            if (src == null || trg == null) return trg;
            if (fields == null || fields.size() == 0) return trg;
            Map<String, Object> property = new HashMap<>();
            Object o;
            for (String field : fields.keySet()) {
                o = Reflect.getObjFieldVal(src, field);
                property.put(fields.get(field), o);
            }
            Reflect.setFieldValues(trg, property);
            return trg;
        }


        /**
         * object搜索
         *
         * @param object
         */
        public static void objSearch(Object object) {
            if (object == null) return;
            if (object instanceof Map) {
                Map map = (Map) object;
                for (Object key : map.keySet()) {
                    Object o = map.get(key);
                    if (o instanceof Map) {
                        objSearch(o);
                    } else {
                        if (o != null && (o.toString().contains("sum") || o.toString().contains("avg"))) {
                            System.out.println(key + ":" + o);
                            //map.put(key, "********");
                        }
                    }
                }
            } else if (object instanceof List) {
                List list = (List) object;
                for (Object o : list) {
                    objSearch(o);
                }
            } else if (object.getClass().isArray()) {
                int len = Array.getLength(object);
                for (int i = 0; i < len; i++) {
                    Object o = Array.get(object, i);
                    objSearch(o);
                }
            } else {
                System.out.println(object);
            }
        }
    }

    public static class Strings {

        public static String[] split(String str, String separatorChars) {
            return splitWorker(str, separatorChars, -1, false);
        }

        private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
            if (str == null) {
                return null;
            } else {
                int len = str.length();
                if (len == 0) {
                    return new String[0];
                } else {
                    List list = new ArrayList();
                    int sizePlus1 = 1;
                    int i = 0;
                    int start = 0;
                    boolean match = false;
                    boolean lastMatch = false;
                    if (separatorChars != null) {
                        if (separatorChars.length() != 1) {
                            label87:
                            while (true) {
                                while (true) {
                                    if (i >= len) {
                                        break label87;
                                    }

                                    if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                                        if (match || preserveAllTokens) {
                                            lastMatch = true;
                                            if (sizePlus1++ == max) {
                                                i = len;
                                                lastMatch = false;
                                            }

                                            list.add(str.substring(start, i));
                                            match = false;
                                        }

                                        ++i;
                                        start = i;
                                    } else {
                                        lastMatch = false;
                                        match = true;
                                        ++i;
                                    }
                                }
                            }
                        } else {
                            char sep = separatorChars.charAt(0);

                            label71:
                            while (true) {
                                while (true) {
                                    if (i >= len) {
                                        break label71;
                                    }

                                    if (str.charAt(i) == sep) {
                                        if (match || preserveAllTokens) {
                                            lastMatch = true;
                                            if (sizePlus1++ == max) {
                                                i = len;
                                                lastMatch = false;
                                            }

                                            list.add(str.substring(start, i));
                                            match = false;
                                        }

                                        ++i;
                                        start = i;
                                    } else {
                                        lastMatch = false;
                                        match = true;
                                        ++i;
                                    }
                                }
                            }
                        }
                    } else {
                        label103:
                        while (true) {
                            while (true) {
                                if (i >= len) {
                                    break label103;
                                }

                                if (Character.isWhitespace(str.charAt(i))) {
                                    if (match || preserveAllTokens) {
                                        lastMatch = true;
                                        if (sizePlus1++ == max) {
                                            i = len;
                                            lastMatch = false;
                                        }

                                        list.add(str.substring(start, i));
                                        match = false;
                                    }

                                    ++i;
                                    start = i;
                                } else {
                                    lastMatch = false;
                                    match = true;
                                    ++i;
                                }
                            }
                        }
                    }

                    if (match || preserveAllTokens && lastMatch) {
                        list.add(str.substring(start, i));
                    }

                    return (String[]) ((String[]) list.toArray(new String[list.size()]));
                }
            }
        }


        public static String[] splitByCharacterType(String str) {
            return splitByCharacterType(str, false);
        }

        public static String[] splitByCharacterTypeCamelCase(String str) {
            return splitByCharacterType(str, true);
        }

        private static String[] splitByCharacterType(String str, boolean camelCase) {
            if (str == null) {
                return null;
            } else if (str.length() == 0) {
                return new String[0];
            } else {
                char[] c = str.toCharArray();
                List list = new ArrayList();
                int tokenStart = 0;
                int currentType = Character.getType(c[tokenStart]);

                for (int pos = tokenStart + 1; pos < c.length; ++pos) {
                    int type = Character.getType(c[pos]);
                    if (type != currentType) {
                        if (camelCase && type == 2 && currentType == 1) {
                            int newTokenStart = pos - 1;
                            if (newTokenStart != tokenStart) {
                                list.add(new String(c, tokenStart, newTokenStart - tokenStart));
                                tokenStart = newTokenStart;
                            }
                        } else {
                            list.add(new String(c, tokenStart, pos - tokenStart));
                            tokenStart = pos;
                        }

                        currentType = type;
                    }
                }

                list.add(new String(c, tokenStart, c.length - tokenStart));
                return (String[]) ((String[]) list.toArray(new String[list.size()]));
            }
        }

        private static String hex(char ch) {
            return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
        }

        public static String escapeJava(String str) {
            return escapeJavaStyleString(str, false, false);
        }

        private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes, boolean escapeForwardSlash) {
            if (str == null) {
                return null;
            } else {
                try {
                    StringWriter writer = new StringWriter(str.length() * 2);
                    escapeJavaStyleString(writer, str, escapeSingleQuotes, escapeForwardSlash);
                    return writer.toString();
                } catch (IOException var4) {
                    var4.printStackTrace();
                    return null;
                }
            }
        }

        private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote, boolean escapeForwardSlash) throws IOException {
            if (out == null) {
                throw new IllegalArgumentException("The Writer must not be null");
            } else if (str != null) {
                int sz = str.length();

                for (int i = 0; i < sz; ++i) {
                    char ch = str.charAt(i);
                    if (ch > 4095) {
                        out.write("\\u" + hex(ch));
                    } else if (ch > 255) {
                        out.write("\\u0" + hex(ch));
                    } else if (ch > 127) {
                        out.write("\\u00" + hex(ch));
                    } else if (ch < 32) {
                        switch (ch) {
                            case '\b':
                                out.write(92);
                                out.write(98);
                                break;
                            case '\t':
                                out.write(92);
                                out.write(116);
                                break;
                            case '\n':
                                out.write(92);
                                out.write(110);
                                break;
                            case '\u000b':
                            default:
                                if (ch > 15) {
                                    out.write("\\u00" + hex(ch));
                                } else {
                                    out.write("\\u000" + hex(ch));
                                }
                                break;
                            case '\f':
                                out.write(92);
                                out.write(102);
                                break;
                            case '\r':
                                out.write(92);
                                out.write(114);
                        }
                    } else {
                        switch (ch) {
                            case '"':
                                out.write(92);
                                out.write(34);
                                break;
                            case '\'':
                                if (escapeSingleQuote) {
                                    out.write(92);
                                }

                                out.write(39);
                                break;
                            case '/':
                                if (escapeForwardSlash) {
                                    out.write(92);
                                }

                                out.write(47);
                                break;
                            case '\\':
                                out.write(92);
                                out.write(92);
                                break;
                            default:
                                out.write(ch);
                        }
                    }
                }

            }
        }

    }

    public static class Format {

        public static String formatDouble(Double data, int accuracy) {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(accuracy); //设置保留多少位小数
            nf.setGroupingUsed(false); // 取消科学计数法
            return nf.format(data); //返回结果
        }
    }

    public static class Json {

        public static class JsonListType implements ParameterizedType {

            private Class clazz;

            JsonListType(Class clazz) {
                this.clazz = clazz;
            }

            @NonNull
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{clazz};
            }

            @NonNull
            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        }

        public static class JsonAdapter extends TypeAdapter<Object> {


            @Override
            public Object read(JsonReader in) throws IOException {
                // 反序列化
                JsonToken token = in.peek();
                switch (token) {
                    case BEGIN_ARRAY:
                        List<Object> list = new ArrayList<>();
                        in.beginArray();
                        while (in.hasNext()) {
                            list.add(read(in));
                        }
                        in.endArray();
                        return list;
                    case BEGIN_OBJECT:
                        Map<String, Object> map = new HashMap<>();
                        in.beginObject();
                        while (in.hasNext()) {
                            map.put(in.nextName(), read(in));
                        }
                        in.endObject();
                        return map;
                    case STRING:
                        return in.nextString();
                    case NUMBER:
                        String num = in.nextString();
                        double dbNum = Double.parseDouble(num);
                        if (num.contains(".")) {
                            return dbNum;
                        } else {
                            if (dbNum > Long.MAX_VALUE) {
                                return dbNum;
                            } else {
                                return (long) dbNum;
                            }
                        }
                    case BOOLEAN:
                        return in.nextBoolean();
                    case NULL:
                        in.nextNull();
                        return null;
                    default:
                        throw new IllegalStateException();
                }
            }

            @Override
            public void write(JsonWriter out, Object value) throws IOException {
            }
        }

        private static Gson gson;

        public static Gson getGSon() {
            if (gson != null) return gson;
            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .registerTypeAdapter(
                            new TypeToken<Map<String, Object>>() {
                            }.getType(), new JsonAdapter())
                    .create();
            return gson;
        }

        public static boolean isBadJson(String json) {
            return !isJson(json);
        }

        public static boolean isJson(String json) {
            if (ObjectUtil.Common.isEmpty(json)) return false;
            try {
                new JsonParser().parse(json);
                return true;
            } catch (JsonParseException e) {
                e.printStackTrace();
                return false;
            }
        }

        public static Boolean isJsonObj(String json) {
            final char[] strChar = json.substring(0, 1).toCharArray();
            final char firstChar = strChar[0];
            if (firstChar == '{') {
                return true;
            } else if (firstChar == '[') {
                return false;
            } else {
                return null;
            }
        }

        public static Boolean isJsonArray(String json) {
            Boolean isJsonObj = isJsonObj(json);
            if (isJsonObj == null) return null;
            return !isJsonObj;
        }

        public static String obj2Json(Object obj) {
            /*
            Log.e("==================",obj.toString());
            String json = obj.toString();
            if(json.contains("NaN")){
                return obj.toString();
            }
            Log.e("==================",""+obj);*/
            String value = getGSon().toJson(obj);
            return value;
        }

        public static String obj2String(Object obj) {
            if (obj instanceof String) return (String) obj;
            String value = getGSon().toJson(obj);
            return value;
        }

        public static String toFormat(String json) {
            try {
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                return gson.toJson(jsonObject);
            } catch (Exception e) {
                return json;
            }
        }

        public static <T> T convertO(Object obj, Class clazz) {
            if (obj == null) return null;
            if (obj instanceof String && clazz == String.class) return (T) obj;
            if (obj instanceof String && clazz == List.class) {
                return (T) convertL(obj, String.class);
            }
            String json = obj2String(obj);
            if (clazz == String.class) return (T) json;
            if (obj instanceof String && clazz == Date.class) {
                return (T) Time.convert2Date(json);
            }
            boolean isBadJson = isBadJson(json);
            if (isBadJson) return null;
            Boolean isJsonObj = isJsonObj(json);
            if (isJsonObj == null) return convertT(json, clazz);
            if (isJsonObj) {
                return convertT(json, clazz);
            } else {
                return (T) convertL(json, clazz);
            }
        }


        public static <T> T convertT(Object obj, Type type) {
            if (obj == null) return null;
            String json = obj2String(obj);
            try {
                if (type == Map.class || type == HashMap.class) {
                    return getGSon().fromJson(json, new TypeToken<Map<String, Object>>() {
                    }.getType());
                }
                if (type == LinkedHashMap.class) {
                    return getGSon().fromJson(json, new TypeToken<LinkedHashMap>() {
                    }.getType());
                }
                if (obj instanceof String && type == String.class) return (T) obj;
                T t = getGSon().fromJson(json, type);
                return t;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static <T> List<T> convertL(Object obj, Class clazz) {
            if (obj == null) return null;
            String json = obj2String(obj);
            try {
                List<T> list = getGSon().fromJson(json, new JsonListType(clazz));
                return list;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static class Reflect {
        private Reflect() {
        }

        public static Object setFieldValues(Object obj, Map<String, Object> attr) {
            Object val;
            for (String field : attr.keySet()) {
                val = attr.get(field);
                setFieldValue(obj, field, val);
            }
            return obj;
        }

        public static void setFieldValue(Object obj, String fieldName, Object fieldValue) {
            Field field = getFieldByName(obj.getClass(), fieldName);
            if (field != null) {
                try {
                    field.setAccessible(true);
                    field.set(obj, fieldValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 反射成员变量
         *
         * @param obj
         * @param name
         * @param <T>
         * @return
         */
        public static <T> T getObjFieldVal(Object obj, String name) {
            if (obj == null) return null;
            if (obj instanceof Map) return (T) ((Map) obj).get(name);
            try {
                Field field = getFieldByName(obj.getClass(), name);
                if (field == null) return null;
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) return null;
                field.setAccessible(true);
                Object val = field.get(obj);
                if (val == null) return null;
                return (T) val;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 反射方法
         *
         * @param obj
         * @param name
         * @param <T>
         * @return
         */
        public static <T> T invokeObjFunc(Object obj, String name, Object... params) {
            if (obj == null) return null;
            if (obj instanceof Map) return (T) ((Map) obj).get(name);
            try {
                Method method = Reflect.getMethod(obj.getClass(), name, params);
                if (method == null) return null;
                int mod = method.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) return null;
                Object val = method.invoke(obj, params);
                if (val == null) return null;
                return (T) val;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        public static Field getFieldByName(Class clazz, String name) {
            if (clazz == null || name == null) return null;
            try {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getName().equals(name)) {
                        return field;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 迭代获取类中的属性
         *
         * @param clazz
         * @return
         */
        public static List<Field> getIterationFields(Class clazz) {
            List<Field> fields = new ArrayList<>();
            Class tempClass = clazz;
            while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
                fields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
                tempClass = tempClass.getSuperclass();
            }
            return fields;
        }

        /**
         * 迭代获取类中的方法
         *
         * @param clazz
         * @return
         */
        public static List<Method> getIterationMethods(Class clazz) {
            List<Method> fields = new ArrayList<>();
            Class tempClass = clazz;
            while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
                fields.addAll(Arrays.asList(tempClass.getDeclaredMethods()));
                tempClass = tempClass.getSuperclass();
            }
            return fields;
        }

        /**
         * 通方法名获取类中的方法
         *
         * @param clazz
         * @param methodName
         * @return
         */
        public static List<Method> getMethod(Class clazz, String methodName) {
            List<Method> methods = getIterationMethods(clazz);
            Method method;
            List<Method> res = new ArrayList<>();
            for (int i = 0; i < methods.size(); i++) {
                method = methods.get(i);
                //System.out.println(method.getName());
                if (method.getName().equals(methodName)) {
                    res.add(method);
                }
            }
            return res;
        }

        public static Method getMethod(Class clazz, String methodName, Class... paramsType) {
            List<Method> methods = getMethod(clazz, methodName);
            Method method;
            Class[] _paramsType;
            boolean isCompare;
            for (int i = 0; i < methods.size(); i++) {
                method = methods.get(i);
                _paramsType = method.getParameterTypes();
                isCompare = paramsCompare(_paramsType, paramsType);
                if (isCompare) return method;
            }
            return null;
        }

        public static Method getMethod(Class clazz, String methodName, Object... params) {
            List<Method> methods = getMethod(clazz, methodName);
            Method method;
            Class[] _paramsType;
            boolean isCompare;
            for (int i = 0; i < methods.size(); i++) {
                method = methods.get(i);
                _paramsType = method.getParameterTypes();
                isCompare = paramsCompare(_paramsType, params);
                if (isCompare) return method;
            }
            return null;
        }


        public static boolean paramsCompare(Class[] types, Class... params) {
            if (params != null && types != null && (params.length == types.length)) {
                boolean compare;
                for (int i = 0; i < types.length; i++) {
                    compare = forceConvert(types[i], params[i]);
                    if (!compare) return false;
                }
                return true;
            } else if (params == null && types == null) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * 基于反射的构造函数匹配
         * 构造函数匹配
         *
         * @return
         */
        public static boolean paramsCompare(Class[] types, Object... params) {
            if (params != null && types != null && (params.length == types.length)) {
                boolean compare;
                for (int i = 0; i < types.length; i++) {
                    compare = forceConvert(types[i], params[i]);
                    if (!compare) return false;
                }
                return true;
            } else if (params == null && types == null) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * @param type
         * @param val
         * @return
         */
        public static boolean forceConvert(Class type, Object val) {
            try {
                Class objType = val.getClass();
                if ((type == Integer.class || type == int.class) && (objType == Integer.class || objType == int.class))
                    return true;
                if ((type == Double.class || type == double.class) && (objType == Double.class || objType == double.class))
                    return true;
                if ((type == Float.class || type == float.class) && (objType == Float.class || objType == float.class))
                    return true;
                if ((type == Short.class || type == short.class) && (objType == Short.class || objType == short.class))
                    return true;
                if ((type == Long.class || type == long.class) && (objType == Long.class || objType == long.class))
                    return true;
                if ((type == Boolean.class || type == boolean.class) && (objType == Boolean.class || objType == boolean.class))
                    return true;
                if ((type == Byte.class || type == byte.class) && (objType == Byte.class || objType == byte.class))
                    return true;
                if ((type == Character.class || type == char.class) && (objType == Character.class || objType == char.class))
                    return true;
                objType.cast(val);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public static <T> T newInstance(String classPath, Object[] params) {
            try {
                Class clazz = Class.forName(classPath);
                return newInstance(clazz, params);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static <T> T newInstance(Class clazz, Object... params) {
            //获取参数为String的构造函数
            Constructor[] constructors = clazz.getConstructors();
            Constructor constructor;
            T t = null;
            boolean compare = true;
            try {
                for (int i = 0; i < constructors.length; i++) {
                    constructor = constructors[i];
                    Class[] types = constructor.getParameterTypes();
                    if ((params == null || params.length == 0) &&
                            (types == null || types.length == 0)) {
                        t = (T) clazz.newInstance();
                        break;
                    }
                    compare = constructorCompare(types, params);
                    if (compare) {
                        t = (T) constructor.newInstance(params);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return t;
        }

        /**
         * 基于反射的构造函数匹配
         * 构造函数匹配
         *
         * @return
         */
        public static boolean constructorCompare(Class[] types, Object... params) {
            if (params != null && types != null && (params.length == types.length)) {
                boolean compare;
                for (int i = 0; i < types.length; i++) {
                    compare = forceConvert(types[i], params[i]);
                    if (!compare) return false;
                }
                return true;
            } else if (params == null && types == null) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static class Time {
        private static final long ONE_DAY = 86400000l;

        // 用来全局控制上一周，本周，下一周的周数变化
        private static int weeks = 0;

        public static boolean isDate(Class<?> o) {
            if (o == GregorianCalendar.class
                    || o == Date.class
                    || o == java.sql.Date.class) {
                return true;
            }
            return false;
        }

        public static boolean isDate(Object o) {
            if (o instanceof GregorianCalendar
                    || o instanceof Date
                    || o instanceof java.sql.Date) {
                return true;
            }
            return false;
        }

        /**
         * @return 返回当前日期字符串，以格式：yyyy-MM-dd输出。
         */
        public static String getCurrentDate() {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String s = df.format(new Date());
            return s;
        }

        /**
         * Timestamp
         *
         * @return
         */
        public static Timestamp getTimestamp() {
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            Date date = new Date();
            try {
                date = ts;
                //System.out.println(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ts;
        }

        /**
         * @return 返回当前时间，以格式：HH:mm:ss输出。
         */
        public static String getCurrentTime() {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            String s = df.format(new Date());
            return s;
        }

        public static String getCurrentTime(String pattern) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            String s = df.format(new Date());
            return s;
        }

        /**
         * utc时间
         *
         * @return
         */
        public static long getUTCTimeStr() {
            Calendar cal = Calendar.getInstance();
            long mills = cal.getTimeInMillis() + 8 * 60 * 60 * 1000;
            //System.out.println("UTC = " + mills);
            return mills;
        }

        /**
         * utc时间
         *
         * @return
         */
        public static long getStandardUtcTime() {
            Calendar cal = Calendar.getInstance();
            long mills = cal.getTimeInMillis();
            return mills;
        }

        /**
         * @return 返回当前时间，以秒形式输出。
         */
        public static String getCurrentm() {
            SimpleDateFormat df = new SimpleDateFormat("ss");
            String s = df.format(new Date());
            return s;
        }

        /**
         * @return返回当前时间，以HHmmss形式输出
         */
        public static String getCurrentHhMmSs() {

            SimpleDateFormat df = new SimpleDateFormat("HHmmss");
            String s = df.format(new Date());
            return s;
        }

        /**
         * @return返回当前年，输出格式为：yyyy。
         */
        public static String getCurrentYear() {
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String s = df.format(new Date());
            return s;
        }

        /**
         * @param d
         * @return根据输入时间，格式化输出年信息：yyyy。
         */
        public static String getYear(Date d) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String s = df.format(d);
            return s;
        }

        /**
         * @return返回当前月信息：MM。
         */
        public static String getCurrentMonth() {
            SimpleDateFormat df = new SimpleDateFormat("MM");
            String s = df.format(new Date());
            return s;
        }

        /**
         * @param sDate
         * @return 获取输入日期是每周的第几天
         */
        public static String getDayInWeek(String sDate) {
            Date date = strToDate(sDate);
            SimpleDateFormat df = new SimpleDateFormat("EEE");
            String s = df.format(date);
            return s;
        }

        /**
         * @param str
         * @return 将字符转换为日期yyyy-MM-dd
         */
        public static Date strToDate(String str) {
            Date date = null;
            if (str != null) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = df.parse(str);
                } catch (ParseException e) {
                    // log.error("DateParse Error!");
                }
            }
            return date;
        }

        /**
         * @param str
         * @return 字符串转日期类型yyyy-MM-dd HH:mm:ss
         */
        public static Date strToDateTime(String str) {
            Date date = null;
            if (str != null) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    date = df.parse(str);
                } catch (ParseException e) {
                    // log.error("DateParse Error!");
                }
            }
            return date;
        }

        /**
         * @return 获取当前时间字符串值
         */
        public static String getDAT() {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String s = df.format(new Date());
            return s;
        }

        /**
         * @return 获取当前时间字符串值
         */
        public static String getDAT(String DateStr, Date date) {
            try {
                SimpleDateFormat df = new SimpleDateFormat(DateStr);
                String s = df.format(date);
                return s;
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * @param date
         * @return 返回指定格式的日期字符串。
         */
        public static String dateTimeToStr(Date date) {
            String str = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (date != null) {
                str = df.format(date);
            }
            return str;
        }

        /**
         * @param date
         * @return 返回指定格式的日期字符串。
         */
        public static String dateTimeToAccessFormat(Date date) {
            String str = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (date != null) {
                str = df.format(date);

            }
            return str.replace(" ", "T");
        }

        /**
         * @param date
         * @return 将指定日期返回特定字符串
         */
        public static String dateToStr(Date date) {
            String str = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (date != null) {
                str = df.format(date);
            }
            return str;
        }

        public static String dateToYearMonthDay(Date date) {
            String str = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (date != null) {
                str = df.format(date);
            }
            return str;
        }

        public static String dateToYearMonth(Date date) {
            String str = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            if (date != null) {
                str = df.format(date);
            }
            return str;
        }

        // 创建日期年月日目录路径
        public static String dateToPathYearMonthDay(Date date) {
            String str = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy年\\MM月\\dd日");
            if (date != null) {
                str = df.format(date);
            }
            return str;
        }

        /**
         * @param date
         * @return 创建日期年月日目录路径 以逗号分割
         */
        public static String dateToPathYearMonthDaySplit(Date date) {
            String str = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy年,MM月,dd日");
            if (date != null) {
                str = df.format(date);
            }
            return str;
        }

        /**
         * @param date
         * @return 创建日期年月目录路径
         */
        public static String dateToPathYearMonth(Date date) {
            String str = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy年\\MM月");
            if (date != null) {
                str = df.format(date);
            }
            return str;
        }

        /**
         * @param date
         * @return 创建日期年月目录路径 以逗号分割
         */
        public static String dateToPathYearMonthSplit(Date date) {
            String str = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy年,MM月");
            if (date != null) {
                str = df.format(date);
            }
            return str;
        }

        /**
         * @param date
         * @return 创建日期年目录路径
         */
        public static String dateToPathYear(Date date) {
            String str = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy年");
            if (date != null) {
                str = df.format(date);
            }
            return str;
        }

        /**
         * @param date
         * @return 返回当前指定日期的中文字符格式
         */
        public static String dateToStrCh(Date date) {
            String str = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy MM dd ");
            if (date != null) {
                str = df.format(date);
            }
            return str;
        }

        /**
         * @param date
         * @param i
         * @return 在指定日期增加指定天数
         */
        public static Date add(Date date, int i) {
            date = new Date(date.getTime() + i * ONE_DAY);
            return date;
        }

        /**
         * @param date
         * @return 日期增加1天
         */
        public static Date add(Date date) {
            return add(date, 1);
        }

        /**
         * @param date
         * @return 日期减一天
         */
        public static Date sub(Date date) {
            return add(date, -1);
        }

        /**
         * @return 返回昨天日期
         */
        public static String getBeforeDate() {
            Date date = sub(new Date());
            return dateToStr(date);

        }

        /**
         * @return 回去当前日期及时间
         */
        public static String getCurrentDateTime() {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String s = df.format(new Date());
            return s;
        }


        /**
         * @return 获取当前日期周
         */
        public static String getCurrentDateWeek() {
            SimpleDateFormat df = new SimpleDateFormat("yyyy MM dd EEE");
            String s = df.format(new Date());
            return s;

        }

        /**
         * @return 获取当前日期周EN格式
         */
        public static String getCurrentDateWeekEn() {
            SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy ", new Locale("en"));
            String s = df.format(new Date());
            return s;

        }

        /**
         * @param startYear
         * @param startMonth
         * @param endYear
         * @param endMonth
         * @return 根据起始年月和终止年月计算共有月数
         */
        public static int compareMonth(String startYear, String startMonth, String endYear, String endMonth) {
            return (Integer.parseInt(endYear) - Integer.parseInt(startYear)) * 12
                    + (Integer.parseInt(endMonth) - Integer.parseInt(startMonth));

        }

        /**
         * @param sDate
         * @return 获取年月日期操作类
         */
        public static String getYearMonth(String sDate) {
            Date date1 = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String s = null;
            try {
                date1 = df.parse(sDate);
                df.applyPattern("yyMM");
                s = df.format(date1);
            } catch (ParseException e) {
                return s;
            }
            return s;
        }

        /**
         * @param date
         * @return 根据当前日期，获取年月信息
         */
        public static String getYearMonth(Date date) {
            SimpleDateFormat df = new SimpleDateFormat("yyMM");
            String s = null;

            s = df.format(date);

            return s;

        }

        /**
         * @param date
         * @return 获取当前时间的月和日信息
         */
        public static String getMonthDay(Date date) {
            SimpleDateFormat df = new SimpleDateFormat("MM dd ");
            String s = null;

            s = df.format(date);

            return s;

        }

        /**
         * @param sDate
         * @return 返回当前日期年月日字符串
         */
        public static String getYearMonthDay(String sDate) {
            Date date1 = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String s = null;
            try {
                date1 = df.parse(sDate);
                df.applyPattern("yyMMdd");
                s = df.format(date1);
            } catch (ParseException e) {
                return s;
            }
            return s;
        }

        /**
         * @param date
         * @return 获取指定日期的当天开始时间
         */
        public static String getStartQueryTime(String date) {
            return dateToStr(strToDate(date)) + " 00:00:00";
        }

        /**
         * @param date
         * @return 获取指定日期的当天结束时间
         */
        public static String getEndQueryTime(String date) {
            return dateToStr(strToDate(date)) + " 23:59:59";
        }

        /**
         * @param sDate
         * @return 获取指定日期对象的月份
         */
        public static String getMonth(String sDate) {
            Date date1 = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String s = null;
            try {
                date1 = df.parse(sDate);
                df.applyPattern("MM");
                s = df.format(date1);
            } catch (ParseException e) {
                return s;
            }
            return s;

        }

        public static Boolean compareDateTime(String start, String after) {
            Date date1 = null;
            Date date2 = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date1 = dateFormat.parse(start);
                date2 = dateFormat.parse(after);
            } catch (ParseException e) {
                return null;
            }
            return date2.after(date1);
        }


        /**
         * @param sDate1
         * @param sDate2
         * @return 获取两个输入日期的时间差，单位：天
         */
        public static int compareDate(String sDate1, String sDate2) {

            Date date1 = null;
            Date date2 = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                date1 = dateFormat.parse(sDate1);
                date2 = dateFormat.parse(sDate2);
            } catch (ParseException e) {

            }

            long dif = 0;
            if (date2.after(date1))
                dif = (date2.getTime() - date1.getTime()) / 1000 / 60 / 60 / 24;
            else
                dif = (date1.getTime() - date2.getTime()) / 1000 / 60 / 60 / 24;

            return (int) dif;
        }

        /**
         * @param sDate
         * @param sTag
         * @return ……
         */
        public static int getDate(String sDate, String sTag) {
            int iSecondMinusPos = sDate.lastIndexOf('-');
            if (sTag.equalsIgnoreCase("y")) {
                return Integer.parseInt(sDate.substring(0, 4));
            } else if (sTag.equalsIgnoreCase("m")) {
                return Integer.parseInt(sDate.substring(5, iSecondMinusPos));
            } else
                return Integer.parseInt(sDate.substring(iSecondMinusPos + 1));
        }

        /**
         * @return 获取本周所剩天数
         */
        public static int getDayOfWeek() {

            Calendar toDay = Calendar.getInstance();

            toDay.setFirstDayOfWeek(Calendar.MONDAY);

            int ret = toDay.get(Calendar.DAY_OF_WEEK) - 1;

            if (ret == 0) {
                ret = 7;
            }

            return ret;
        }

        /**
         * @return 获取当月的第一天
         */
        public static String getFirstDayOfMonth() {
            Calendar ca = Calendar.getInstance();
            ca.setTime(new Date());
            ca.set(Calendar.DAY_OF_MONTH, 1);
            Date firstDate = ca.getTime();
            ca.add(Calendar.MONTH, 1);
            ca.add(Calendar.DAY_OF_MONTH, -1);
            // Date lastDate = ca.getTime();

            return dateToStr(firstDate);

        }

        /**
         * @return 获取当月的最后一天
         */
        public static String getLastDayOfMonth() {
            Calendar ca = Calendar.getInstance();
            ca.setTime(new Date());
            ca.set(Calendar.DAY_OF_MONTH, 1);
            // Date firstDate = ca.getTime();
            ca.add(Calendar.MONTH, 1);
            ca.add(Calendar.DAY_OF_MONTH, -1);
            Date lastDate = ca.getTime();
            return dateToStr(lastDate);
        }

        /**
         * @return 昨天
         */
        public static String yesterday() {
            Calendar calendar = Calendar.getInstance();
            StringBuffer param = new StringBuffer();
            calendar.add(Calendar.DATE, -1);
            param.append(String.valueOf(calendar.get(Calendar.YEAR))).append("-")
                    .append(String.valueOf(calendar.get(Calendar.MONTH) + 1)).append("-")
                    .append(String.valueOf(calendar.get(Calendar.DATE)));
            return param.toString();
        }


        /**
         * @return 最近7天
         */
        public static String lately7Day() {
            Calendar calendar = Calendar.getInstance();
            StringBuffer param = new StringBuffer();
            calendar.add(Calendar.DATE, -7);
            param.append(String.valueOf(calendar.get(Calendar.YEAR))).append("-")
                    .append(String.valueOf(calendar.get(Calendar.MONTH) + 1)).append("-")
                    .append(String.valueOf(calendar.get(Calendar.DATE)));
            return param.toString() + "," + dateToYearMonthDay(new Date());
        }

        /**
         * @return 最近30天
         */
        public static String lately30Day() {
            Calendar calendar = Calendar.getInstance();
            StringBuffer param = new StringBuffer();
            calendar.add(Calendar.DATE, -30);
            param.append(String.valueOf(calendar.get(Calendar.YEAR))).append("-")
                    .append(String.valueOf(calendar.get(Calendar.MONTH) + 1)).append("-")
                    .append(String.valueOf(calendar.get(Calendar.DATE)));
            return param.toString() + "," + dateToYearMonthDay(new Date());
        }

        /**
         * @return 去年
         */
        public static String lastYear() {
            Calendar calendar = Calendar.getInstance();
            StringBuffer param = new StringBuffer();
            calendar.add(Calendar.YEAR, -1);
            param.append(String.valueOf(calendar.get(Calendar.YEAR)));
            return param.toString();
        }

        /**
         * @return 上月
         */
        public static String LastMonth() {
            Calendar calendar = Calendar.getInstance();
            StringBuffer param = new StringBuffer();
            calendar.add(Calendar.MONTH, -1);
            param.append(String.valueOf(calendar.get(Calendar.YEAR))).append("-")
                    .append(String.valueOf(calendar.get(Calendar.MONTH) + 1));
            return param.toString();
        }

        /**
         * @return 本周 以,隔开
         */
        public static String thisWeek() {
            Calendar c = Calendar.getInstance();
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 1;
            //System.out.println("周天数：" + weekday);
            c.add(Calendar.DATE, -weekday);
            String thisWeek = dateToStr(c.getTime());
            //System.out.println("本周开始时间：" + dateToStr(c.getTime()));
            c.add(Calendar.DATE, Calendar.DAY_OF_WEEK);
            //System.out.println("本周开始结束：" + dateToStr(c.getTime()));
            thisWeek += "," + dateToStr(c.getTime());
            return thisWeek;
        }

        /**
         * @return 上周以, 隔开
         */
        public static String lastWeek() {
            /*
             * Calendar calendar = Calendar.getInstance(); int
             * minus=calendar.get(GregorianCalendar.DAY_OF_WEEK)+1;
             * calendar.add(GregorianCalendar.DATE,-minus); String end=new
             * java.sql.Date(calendar.getTime().getTime()).toString();
             * calendar.add(GregorianCalendar.DATE,-4); String begin=new
             * java.sql.Date(calendar.getTime().getTime()).toString();
             */
            String beginTime = getPreviousWeekday();
            String endTime = getPreviousWeekSunday();
            return beginTime + "," + endTime;
        }

        /**
         * @return 获得上周星期日的日期
         */
        public static String getPreviousWeekSunday() {
            weeks = 0;
            weeks--;
            int mondayPlus = getMondayPlus();
            GregorianCalendar currentDate = new GregorianCalendar();
            currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
            Date monday = currentDate.getTime();
            String preMonday = dateToYearMonthDay(monday);
            return preMonday;
        }

        /**
         * @return获得上周星期一的日期
         */
        public static String getPreviousWeekday() {
            weeks--;
            int mondayPlus = getMondayPlus();
            GregorianCalendar currentDate = new GregorianCalendar();
            currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
            Date monday = currentDate.getTime();
            String preMonday = dateToYearMonthDay(monday);
            return preMonday;
        }

        /**
         * @return 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
         */
        private static int getMondayPlus() {
            Calendar cd = Calendar.getInstance();
            //
            int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
            if (dayOfWeek == 1) {
                return 0;
            } else {
                return 1 - dayOfWeek;
            }
        }

        /**
         * 这个方法获取的结果是24小时制的，月份也正确 网络时间
         *
         * @return
         */
        public static String getTimeZone() {
            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
            String ee = dff.format(new Date());
            //System.out.println("ee=" + ee);
            return ee;
        }

        /**
         * @param str
         * @return 字符串转日期类型yyyy-MM-dd HH:mm:ss
         */
        public static Date smartCreate(String str) {
            Date date = null;
            if (str != null) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    date = df.parse(str);
                } catch (ParseException e) {
                    return new Date();
                }
            }
            return date;
        }

        public static String getCurrentSecond() {
            SimpleDateFormat df = new SimpleDateFormat("ss");
            String s = df.format(new Date());
            return s;
        }

        /**
         * @return 返回当前日期字符串，以格式：yyyy-MM-dd输出。
         */
        public static String getDate(Date date) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String s = df.format(date);
            return s;
        }

        public static String getTime(Date date) {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            String s = df.format(date);
            return s;
        }

        public static String convert2DateStr(Object obj) {
            if (obj instanceof Date) {
                return dateTimeToStr((Date) obj);
            } else if (obj instanceof java.sql.Date) {
                return dateTimeToStr((Date) obj);
            } else if (obj instanceof Calendar) {
                return dateTimeToStr(((Calendar) obj).getTime());
            } else if (obj instanceof GregorianCalendar) {
                return dateTimeToStr(((GregorianCalendar) obj).getTime());
            } else if (obj instanceof String) {
                return (String) obj;
            }
            return null;
        }

        public static Date convert2Date(Object obj) {
            if (obj instanceof Date) return (Date) obj;
            if (obj instanceof java.sql.Date) {
                return (Date) obj;
            } else if (obj instanceof Calendar) {
                return ((Calendar) obj).getTime();
            } else if (obj instanceof GregorianCalendar) {
                return ((GregorianCalendar) obj).getTime();
            } else if (obj instanceof String) {
                return strToDate((String) obj);
            }
            return null;
        }
    }

    public static class Collect {

        /**
         * @param objects
         * @param obj
         * @return
         */
        public static Object[] addArray(Object obj, Object[] objects) {
            if (objects == null) return new Object[]{obj};
            Object[] _objects = new Object[objects.length + 1];
            _objects[0] = obj;
            for (int i = 1; i <= objects.length; i++) {
                _objects[i] = objects[i - 1];
            }
            return _objects;
        }

        public interface Item<R, T> {
            public R item(T item);
        }

        public static List filterList(List list, Item<Boolean, Object> item) {
            Object obj;
            List res = new ArrayList();
            boolean flag = false;
            for (int i = 0, len = list.size(); i < len; i++) {
                obj = list.get(i);
                if (item != null) flag = item.item(obj);
                if (!flag) res.add(obj);
            }
            return res;
        }

        /**
         * 集合转数组
         *
         * @param objects
         * @param clazz
         * @param <T>
         * @return
         */
        public static <T> T[] arrayObj2T(Object[] objects, Class clazz) {
            T[] TArray = null;
            if (objects != null && objects.length > 0) {
                TArray = (T[]) Array.newInstance(clazz, objects.length);
                for (int i = 0; i < objects.length; i++) {
                    Array.set(TArray, i, objects[i]);
                }
            }
            return TArray;
        }

        /**
         * 集合转数组
         *
         * @param list
         * @param clazz
         * @param <T>
         * @return
         */
        public static <T> T[] list2TArray(List list, Class clazz) {
            T[] TArray = null;
            if (list != null && list.size() > 0) {
                TArray = (T[]) Array.newInstance(clazz, list.size());
                for (int i = 0; i < list.size(); i++) {
                    Array.set(TArray, i, list.get(i));
                }
            }
            return TArray;
        }

        /**
         * 集合转数组
         *
         * @param list
         * @param clazz
         * @param <T>
         * @return
         */
        public static <T> T[] tArray2List(List list, Class clazz) {
            T[] TArray = null;
            if (list != null && list.size() > 0) {
                TArray = (T[]) Array.newInstance(clazz, list.size());
                for (int i = 0; i < list.size(); i++) {
                    Array.set(TArray, i, list.get(i));
                }
            }
            return TArray;
        }

        /**
         * 数组转集合
         *
         * @param array
         * @param <T>
         * @return
         */
        public static <T> List<T> array2List(T[] array) {
            List<T> list = new ArrayList<>();
            for (int i = 0; i < array.length; i++) {
                T t = array[i];
                list.add(t);
            }
            return list;
        }

        /**
         * 获取 第二个list里的item在 src里的位置
         *
         * @param src
         * @param list
         * @param args
         * @return
         */
        public static List<Integer> indexOf(List src, List list, String... args) {
            if (args == null || src == null || list == null) return new ArrayList<>();
            Object s, t;
            List<Integer> index = new ArrayList<>();
            for (int i = 0; i < src.size(); i++) {
                s = src.get(i);
                for (int j = 0; j < list.size(); j++) {
                    t = list.get(j);
                    if (Common.objFieldsIsEqual(s, t, args)) {
                        index.add(i);
                        break;
                    }
                }
            }
            return index;
        }

        public static <T> List<T> listTFromList(List list, Class clazz, Map<String, String> fields) {
            if (list == null) return null;
            if (list.size() == 0) return new ArrayList<>();
            Object src;
            Object trg;
            List<T> res = new ArrayList<>();
            try {
                for (int i = 0, len = list.size(); i < len; i++) {
                    src = list.get(i);
                    if (src == null) continue;
                    trg = clazz.newInstance();
                    trg = Common.copyFields(src, trg, fields);
                    res.add((T) trg);
                }
                return res;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        public static <T> List<T> listToTList(List list, String fileName) {
            if (list == null) return null;
            if (list.size() == 0) return new ArrayList<>();
            Object src;
            T trg;
            List<T> res = new ArrayList<>();
            try {
                for (int i = 0, len = list.size(); i < len; i++) {
                    src = list.get(i);
                    if (src == null) continue;
                    trg = Reflect.getObjFieldVal(src, fileName);
                    res.add(trg);
                }
                return res;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static <T> List<T> listToTList(List list, String fileName, Item<T, T> item) {
            if (list == null) return null;
            if (list.size() == 0) return new ArrayList<>();
            Object src;
            T trg;
            List<T> res = new ArrayList<>();
            try {
                for (int i = 0, len = list.size(); i < len; i++) {
                    src = list.get(i);
                    if (src == null) continue;
                    trg = Reflect.getObjFieldVal(src, fileName);
                    if (item != null) trg = item.item(trg);
                    res.add(trg);
                }
                return res;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * list 拖拽排序
         *
         * @param list
         * @param from
         * @param to
         * @return
         */
        public static List dragSort(List list, int from, int to) {
            if (from < to) {
                for (int i = from; i < to; i++) {
                    swapItem(list, i, i + 1);
                }
            } else {
                for (int i = from; i > to; i--) {
                    swapItem(list, i, i - 1);
                }
            }
            return list;
        }

        public static List removeDragSort(List list, int from, int to) {
            if (from < to) {
                for (int i = from; i < to; i++) {
                    removeSwapItem(list, i, i + 1);
                }
            } else {
                for (int i = from; i > to; i--) {
                    removeSwapItem(list, i, i - 1);
                }
            }
            return list;
        }

        public static List swapItem(List list, int i, int j) {
            Collections.swap(list, i, j);
            return list;
        }

        public static List removeSwapItem(List list, int i, int j) {
            if (i > j) {
                Object item1 = list.remove(i);
                list.add(j, item1);
                Object item2 = list.remove(j + 1);
                list.add(i, item2);
            } else if (i < j) {
                Object item2 = list.remove(j);
                list.add(i, item2);
                Object item1 = list.remove(i + 1);
                list.add(j, item1);
            }
            return list;
        }


        /**
         * 两个集合交集  以main为准
         *
         * @param main
         * @param fit
         * @return
         */
        public static <M, F> List inset(List<M> main, List<F> fit, ICompare<M, F> compare) {
            if (Common.isEmpty(main) && Common.isEmpty(fit)) return new ArrayList();
            if (Common.isEmpty(main) && !Common.isEmpty(fit)) return fit;
            if (!Common.isEmpty(main) && Common.isEmpty(fit)) return main;
            List list = new ArrayList();
            M m;
            F f;
            int cmd;
            for (int i = 0, lenI = main.size(); i < lenI; i++) {
                m = main.get(i);
                for (int j = 0, lenJ = fit.size(); j < lenJ; j++) {
                    f = fit.get(j);
                    cmd = compare.match(m, f);
                    if (cmd == 1) {
                        list.add(m);
                    }
                    if (cmd == 2) {
                        list.add(f);
                    }

                }
            }
            return list;
        }

        /**
         * 两个集合并集 以main为准
         *
         * @param main
         * @param fit
         * @return
         */
        public static <M, F> List union(List<M> main, List<F> fit, ICompare<M, F> compare) {
            if (Common.isEmpty(main) && Common.isEmpty(fit)) return new ArrayList();
            if (Common.isEmpty(main) && !Common.isEmpty(fit)) return fit;
            if (!Common.isEmpty(main) && Common.isEmpty(fit)) return main;
            List list = new ArrayList();
            M m;
            F f;
            int cmd;
            list.addAll(main);
            list.addAll(fit);
            for (int i = 0, lenI = main.size(); i < lenI; i++) {
                m = main.get(i);
                for (int j = 0, lenJ = fit.size(); j < lenJ; j++) {
                    f = fit.get(j);
                    cmd = compare.match(m, f);
                    if (cmd == 1) {
                        list.remove(f);
                    }
                    if (cmd == 2) {
                        list.remove(m);
                    }
                }
            }
            return list;
        }

        /**
         * 两个集合 求异
         *
         * @param main
         * @param fit
         * @return
         */
        public static <M, F> List diff(List<M> main, List<F> fit, @NonNull ICompare<M, F> compare) {
            if (Common.isEmpty(main) && Common.isEmpty(fit)) return new ArrayList();
            if (Common.isEmpty(main) && !Common.isEmpty(fit)) return fit;
            if (!Common.isEmpty(main) && Common.isEmpty(fit)) return main;
            List list = new ArrayList();
            M m;
            F f;
            int cmd;
            list.addAll(main);
            list.addAll(fit);
            for (int i = 0, lenI = main.size(); i < lenI; i++) {
                m = main.get(i);
                for (int j = 0, lenJ = fit.size(); j < lenJ; j++) {
                    f = fit.get(j);
                    cmd = compare.match(m, f);
                    if (cmd != 0) {
                        list.remove(f);
                        list.remove(m);
                    }


                }
            }
            return list;
        }

        public interface ICompare<T, K> {
            // 0 表示 不匹配，1 表示匹配 t，2表示匹配 k
            public int match(T t, K k);
        }
    }

    public static class Maps {

        public static List removeItem(List<Map> list, String... keys) {
            if (Common.isEmpty(list) || Common.isEmpty(keys)) return list;
            Map item;
            for (int i = 0, lenI = list.size(); i < lenI; i++) {
                item = list.get(i);
                for (int j = 0, lenJ = keys.length; j < lenJ; j++) {
                    item.remove(keys[j]);
                }
            }
            return list;
        }

        /**
         * @param list
         * @param key
         * @param targetKey
         */
        public static List<Map> crossKv(List<Map> list, String key, String targetKey) {
            Object kVal;
            Object val;
            for (Map map : list) {
                kVal = map.get(key);
                val = map.get(targetKey);
                map.remove(targetKey);
                map.put(kVal, val);
            }
            return list;
        }


        private static Integer getIndexI(String item) {
            if (!item.endsWith("]")) return null;
            Integer i = Integer.parseInt(item.substring(item.indexOf("[") + 1, item.indexOf("]")));
            return i;
        }

        private static String getIndexName(String item) {
            if (!item.endsWith("]")) return item;
            String name = item.substring(0, item.indexOf("["));
            return name;
        }


        public static void setValueV1(Map root, String name, Object value) {
            String index = searchIndexV1(root, name);
            if (index == null) return;
            String[] split = index.split("->");
            if (split == null) return;
            if (split.length == 1) {
                if (name.contains("[") && name.endsWith("]")) return;
                else root.put(name, value);
            } else {
                Map map = root;
                for (int i = 0; i < split.length - 1; i++) {
                    Integer num = getIndexI(split[i]);
                    name = getIndexName(split[i]);
                    if (num == null) {
                        map = (Map) map.get(name);
                    } else {
                        map = (Map) ((List) map.get(name)).get(num);
                    }
                }
                name = split[split.length - 1];
                if (map == null) {
                    System.out.println(">>>>>>>>:" + name);
                    return;
                }
                map.put(name, value);
            }
        }

        public static Object getValueV1(Map root, String name) {
            String index = searchIndexV1(root, name);
            if (index == null) return null;
            String[] split = index.split("->");
            if (split == null) return null;
            if (split.length == 1) {
                if (name.contains("[") && name.endsWith("]")) return null;
                else return root.get(name);
            } else {
                Map map = root;
                for (int i = 0; i < split.length - 1; i++) {
                    Integer num = getIndexI(split[i]);
                    name = getIndexName(split[i]);
                    if (num == null) {
                        map = (Map) map.get(name);
                    } else {
                        map = (Map) ((List) map.get(name)).get(num);
                    }
                }
                name = split[split.length - 1];
                if (map == null) {
                    System.err.println(">>>>>>>>map null: " + name);
                    return null;
                }
                return map.get(name);
            }
        }

        /**
         * @param root
         * @param name
         * @return
         */
        public static String searchIndexV1(Map root, String name) {
            Stack<Holder> stack = new Stack<>();
            stack.push(new Holder("", root));
            Holder holder;
            Object val;
            String _key;
            while (!stack.empty()) {
                holder = stack.pop();
                for (Object key : holder.keySet()) {
                    val = holder.get((String) key);
                    _key = holder.getIndex().equals("") ? (String) key : holder.getIndex() + "->" + key;
                    if (val instanceof Map) {
                        if (_key.contains(name)) return _key;
                        stack.push(new Holder(_key, (Map) val));
                    }
                    if (val instanceof List) {
                        Object obj;
                        List list = (List) val;
                        for (int i = 0; i < list.size(); i++) {
                            obj = list.get(i);
                            if (_key.contains(name)) return _key;
                            if (obj instanceof Map)
                                stack.push(new Holder(_key + "[" + i + "]", (Map) obj));
                        }
                    }
                    if (val.getClass().isArray()) {
                        int len = Array.getLength(val);
                        Object obj;
                        for (int i = 0; i < len; i++) {
                            obj = Array.get(val, i);
                            if (_key.contains(name)) return _key;
                            if (obj instanceof Map)
                                stack.push(new Holder(_key + "[" + i + "]", (Map) obj));
                        }
                    } else {
                        if (_key.contains(name)) return _key;
                    }
                }
            }
            return null;
        }

        //------------------------------------------------------------------------------------------
        //--------------------------------------以下有点瑕疵----------------------------------------
        //-----------------------------不支持map里的数组或者集合搜索--------------------------------
        //------------------------------------------------------------------------------------------


        /**
         * 要求 index 为全路径 索引
         *
         * @param root
         * @param index
         * @param splitFlag
         * @return
         */
        public static Object getTreeValueByIndex(Map<String, ?> root, String index, String splitFlag) {
            String[] array = index.split(splitFlag);
            Object obj = null;
            Map temp = root;
            for (int i = 0, len = array.length; i < len; i++) {
                String fieldName = array[i];
                obj = temp.get(fieldName);
                if (obj instanceof Map) {
                    temp = (Map) obj;
                    if (i == len - 1) return obj;
                    else continue;
                } else {
                    if (i != len - 1) return null;
                    else break;
                }
            }
            return obj;
        }

        /**
         * 静态方法链
         *
         * @param map
         * @param oldKey
         * @param newKey
         * @return
         */
        public static ObjectUtil.Maps renameKey(Map map, Object oldKey, Object newKey) {
            Object val = map.get(oldKey);
            map.remove(oldKey);
            map.put(newKey, val);
            return null;
        }

        /**
         * map树节点赋值 支持模糊赋值
         * 根据key搜索   key-key-key-key
         *
         * @param root
         * @param name
         * @return
         */
        public static void setValue(Map root, String name, Object value) {
            String index = searchIndex(root, name);
            if (index == null) return;
            String[] split = index.split("->");
            if (split == null) return;
            if (split.length == 1) {
                root.put(name, value);
            } else {
                Map map = (Map) root.get(split[0]);
                for (int i = 1; i < split.length - 1; i++) {
                    map = (Map) map.get(split[i]);
                }
                map.put(split[split.length - 1], value);
            }
        }

        public static void setValue(Map root, String index, String splitFlag, Object value) {
            if (index == null) return;
            String[] split = index.split(splitFlag);
            if (split == null) return;
            if (split.length == 1) {
                root.put(index, value);
            } else {
                Map map = (Map) root.get(split[0]);
                for (int i = 1; i < split.length - 1; i++) {
                    map = (Map) map.get(split[i]);
                }
                map.put(split[split.length - 1], value);
            }
        }

        public static String searchIndex(Map root, String name) {
            Stack<Holder> stack = new Stack<>();
            stack.push(new Holder("", root));
            Holder holder;
            Object val;
            String _key;
            while (!stack.empty()) {
                holder = stack.pop();
                for (Object key : holder.keySet()) {
                    val = holder.get((String) key);
                    _key = holder.getIndex().equals("") ? (String) key : holder.getIndex() + "->" + key;
                    if (val instanceof Map) {
                        if (_key.contains(name)) {
                            return _key;
                        }
                        stack.push(new Holder(_key, (Map) val));
                    } else {
                        if (_key.contains(name)) {
                            return _key;
                        }
                    }
                }
            }
            return null;
        }

        /**
         * map树搜索
         * 根据key搜索   key-key-key-key
         *
         * @param root
         * @param name
         * @return
         */
        public static <T> T searchValue(Map root, String name, Class clazz) {
            Stack<Holder> stack = new Stack<>();
            stack.push(new Holder("", root));
            Holder holder;
            Object val;
            String _key;
            while (!stack.empty()) {
                holder = stack.pop();
                for (Object key : holder.keySet()) {
                    val = holder.get((String) key);
                    _key = holder.getIndex().equals("") ? (String) key : holder.getIndex() + "->" + key;
                    if (val instanceof Map) {
                        if (_key.contains(name)) {
                            T out = ObjectUtil.Common.convert(val, clazz);
                            return out;
                        }
                        stack.push(new Holder(_key, (Map) val));
                    } else {
                        //System.out.println(_key + ":" + val);
                        if (_key.contains(name)) {
                            T out = ObjectUtil.Common.convert(val, clazz);
                            return out;
                        }
                    }
                }
            }
            return null;
        }

        /**
         * map树搜索
         * 根据key搜索   key-key-key-key
         *
         * @param root
         * @param name
         * @return
         */
        public static <T> T searchValue(Map root, String name) {
            Stack<Holder> stack = new Stack<>();
            stack.push(new Holder("", root));
            Holder holder;
            Object val;
            String _key;
            while (!stack.empty()) {
                holder = stack.pop();
                for (Object key : holder.keySet()) {
                    val = holder.get((String) key);
                    _key = holder.getIndex().equals("") ? (String) key : holder.getIndex() + "->" + key;
                    if (val instanceof Map) {
                        if (_key.contains(name)) return (T) val;
                        stack.push(new Holder(_key, (Map) val));
                    } else {
                        if (_key.contains(name)) return (T) val;
                    }
                }
            }
            return null;
        }

        /**
         * map树搜索
         * 根据key搜索   key-key-key-key
         *
         * @param root
         * @param name
         * @return
         */
        public static <T> T searchValue(Map root, String name, String split) {
            Stack<Holder> stack = new Stack<>();
            stack.push(new Holder("", root));
            Holder holder;
            Object val;
            String _key;
            while (!stack.empty()) {
                holder = stack.pop();
                for (Object key : holder.keySet()) {
                    val = holder.get((String) key);
                    _key = holder.getIndex().equals("") ? (String) key : holder.getIndex() + split + key;
                    if (val instanceof Map) {
                        if (_key.contains(name)) return (T) val;
                        stack.push(new Holder(_key, (Map) val));
                    } else {
                        if (_key.contains(name)) return (T) val;
                    }
                }
            }
            return null;
        }


        /**
         * 替换map树的节点，该树特点是  相同深度的 node的名称相同
         *
         * @param root
         * @param {    "520121": {
         *             "1620": {
         *             "100": {
         *             "MIANJI": 0.42,
         *             "YSSZPJXJ": 0.0
         *             }        * 		},
         *             "1310": {
         *             "100": {
         *             "MIANJI": 320.7600000000001,
         *             "YSSZPJXJ": 0.0
         *             },
         *             "210": {
         *             "MIANJI": 53.910000000000004,
         *             "YSSZPJXJ": 0.0
         *             },
         *             "200": {
         *             "MIANJI": 27.95,
         *             "YSSZPJXJ": 0.0
         *             }
         *             }
         *             }
         * @return
         */
        public static List<Map> replaceTreeNode(Map root, String[] nodeNames) {
            Holder holder;
            List<Map> list1 = new ArrayList<>();
            for (Object key : root.keySet()) {
                Map map = new HashMap();
                map.put(key, root.get(key));
                list1.add(map);
            }

            Object val;
            int deep;
            Map item = null;
            String nodeName;
            List<Map> list = new ArrayList<>();

            for (Map map : list1) {
                Stack<Holder> stack = new Stack<>();
                stack.add(new Maps.Holder(1, map));
                while (!stack.isEmpty()) {
                    holder = stack.pop();
                    //map循环
                    for (Object key : holder.keySet()) {
                        val = holder.get((String) key);
                        deep = holder.deep;
                        if (deep == 1) {
                            item = new HashMap();
                            list.add(item);
                        }
                        if (val instanceof Map) {
                            if (deep <= nodeNames.length - 1) {
                                nodeName = nodeNames[deep - 1];
                                item.put(nodeName, key);
                                deep++;
                            } else {
                                item.put(key, key);
                            }
                            stack.push(new Maps.Holder(deep, (Map) val));
                        } else {
                            item.put(key, val);
                        }
                    }
                }
            }
            return list;
        }

        public static List<Map> replaceTreeNodeV2(Map root, String[] nodeNames) {
            Holder holder = new Holder(0, root);
            Object val;
            int deep;
            Map item = null;
            String nodeName;
            List<Map> list = new ArrayList<>();
            Stack<Holder> stack = new Stack<>();
            stack.add(holder);
            while (!stack.isEmpty()) {
                holder = stack.pop();
                //map循环
                for (Object key : holder.keySet()) {
                    val = holder.get((String) key);
                    deep = holder.deep;
                    if (deep == 1) {
                        item = new HashMap();
                        list.add(item);
                    }
                    if (val instanceof Map) {
                        if (deep <= nodeNames.length - 1) {
                            nodeName = nodeNames[deep - 1];
                            item.put(nodeName, key);
                            deep++;
                        } else {
                            item.put(key, key);
                        }
                        stack.push(new Maps.Holder(deep, (Map) val));
                    } else {
                        item.put(key, val);
                    }
                }
            }
            return list;
        }

        private static class Holder {

            String index;

            //节点的深度
            int deep;

            Map map;

            public Holder(String index, Map map) {
                this.index = index;
                this.map = map;
            }

            public Holder(int deep, Map map) {
                this.deep = deep;
                this.map = map;
            }

            public String getIndex() {
                return index;
            }

            public void setIndex(String index) {
                this.index = index;
            }

            public Map getMap() {
                return map;
            }

            public void setMap(Map map) {
                this.map = map;
            }

            public Object get(String key) {
                return map.get(key);
            }

            public Set keySet() {
                return map.keySet();
            }
        }

    }

    public static class Tree {

        public static class Node {

            private String index;

            private String name;

            private String alias;

            private int level;

            private transient Node parent;

            private List<Node> kids;

            private Object tag;

            public Node(String index, String name) {
                this.index = index;
                this.name = name;
            }

            public String getIndex() {
                return index;
            }

            public void setIndex(String index) {
                this.index = index;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public Node getParent() {
                return parent;
            }

            public void setParent(Node parent) {
                this.parent = parent;
            }

            public List<Node> getKids() {
                return kids;
            }

            public void setKids(List<Node> nodes) {
                this.kids = nodes;
            }

            public Object getTag() {
                return tag;
            }

            public void setTag(Object tag) {
                this.tag = tag;
            }

            public boolean hasNode() {
                return kids != null && kids.size() > 0;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public boolean hasNode(String index) {
                boolean hasNode = hasNode();
                if (!hasNode) return false;
                Node node;
                for (int i = 0; i < kids.size(); i++) {
                    node = kids.get(i);
                    if (Common.faceEqual(node.index, index)) {
                        return true;
                    }
                }
                return false;
            }

            public Node addNode(Node node) {
                if (kids == null) kids = new ArrayList<>();
                kids.add(node);
                return this;
            }

            public Node getKidNode(String index) {
                boolean hasNode = hasNode();
                if (!hasNode) return null;
                Node node;
                for (int i = 0; i < kids.size(); i++) {
                    node = kids.get(i);
                    if (Common.faceEqual(node.index, index)) {
                        return node;
                    }
                }
                return null;
            }

            public void removeKidNode(String index) {
                boolean hasNode = hasNode();
                if (!hasNode) return;
                Node node;
                int indexI = -1;
                for (int i = 0; i < kids.size(); i++) {
                    node = kids.get(i);
                    if (Common.faceEqual(node.index, index)) {
                        indexI = i;
                        break;
                    }
                }
                if (indexI != -1) kids.remove(indexI);
            }
        }

        public static Node createFromPath(String rootName, String parentPath, String... ext) {
            List<File> list = ObjectUtil.Files.fileLeaf(parentPath, false, ext);
            Node root = new Node(parentPath, rootName);
            for (int i = 0, len = list.size(); i < len; i++) {
                File file = list.get(i);
                String index = file.getAbsolutePath();
                index = index.replace(parentPath + File.separator, "");
                if (index == null) continue;
                String[] array = Strings.split(index, File.separator);
                Node node = root;
                index = parentPath;
                for (int j = 0, lenJ = array.length; j < lenJ; j++) {
                    String name = array[j];
                    index = index + File.separator + name;
                    file = new File(index);
                    if (!node.hasNode(index)) {
                        Node _node = new Node(index, name);
                        _node.setTag(file);
                        _node.setLevel(j + 1);
                        _node.setParent(node);
                        node.addNode(_node);
                    }
                    node = node.getKidNode(index);
                }
            }
            return root;
        }


        /**
         * a.b
         * a.e
         * a.b.c
         * a.b.d
         * 构建树结构
         *
         * @param treeIndex
         * @return
         */
        public static Map createFromIndex(List<String> treeIndex, String split) {
            String index;
            String[] array;
            String item;
            Map<String, Map> tree = new HashMap();
            if (treeIndex == null) return tree;
            for (int i = 0, lenI = treeIndex.size(); i < lenI; i++) {
                index = treeIndex.get(i);
                if (index == null) continue;
                array = Strings.split(index, split);
                Map<String, Map> node = tree;
                for (int j = 0, lenJ = array.length; j < lenJ; j++) {
                    item = array[j];
                    if (!node.containsKey(item)) node.put(item, new HashMap<>());
                    node = node.get(item);
                }
            }
            return tree;
        }

        /**
         * 通过目录实体构建目录树
         *
         * @param list
         * @param rootName
         * @param fieldName
         * @param split
         * @return
         */
        public static Node createFrom(List list, String rootName, String fieldName, String split) {
            Node root = new Node(null, rootName);
            for (int i = 0, len = list.size(); i < len; i++) {
                Object tag = list.get(i);
                String index = ObjectUtil.Reflect.getObjFieldVal(tag, fieldName);
                if (index == null) continue;
                String[] array = Strings.split(index, split);
                Node node = root;
                StringBuilder indexBuilder = new StringBuilder();
                for (int j = 0, lenJ = array.length; j < lenJ; j++) {
                    String item = array[j];
                    if (j != 0) indexBuilder.append(split);
                    indexBuilder.append(item);
                    index = indexBuilder.toString();
                    if (!node.hasNode(index)) {
                        Node _node = new Node(index, item);
                        _node.setTag(tag);
                        _node.setLevel(j + 1);
                        node.addNode(_node);
                    }
                    node = node.getKidNode(index);
                }
            }
            return root;
        }


        public static boolean hasIndex(Node tree, String index) {
            if (index == null || tree == null) return false;
            String _index = tree.getIndex();
            if (Common.faceEqual(_index, index)) return true;
            if (!tree.hasNode()) return false;
            List<Node> list = tree.getKids();
            for (Node node : list) {
                boolean flag = hasIndex(node, index);
                if (flag) return true;
            }
            return false;
        }

        public static Node getTreeNodeByIndex(Node tree, String index, String split) {
            if (index == null) return null;
            String[] array = Strings.split(index, split);
            Node node = tree;
            for (int j = 0, lenJ = array.length; j < lenJ; j++) {
                String _index = array[j];
                node = node.getKidNode(_index);
            }
            if (node != null) return node;
            return null;
        }

        public static Node addTreeNode(Node tree, List<Node> nodes, String split) {
            for (Node node : nodes) {
                addTreeNode(tree, node, split);
            }
            return tree;
        }

        public static Node addTreeNode(Node tree, Node node, String split) {
            if (node == null || node.getIndex() == null) return tree;
            String[] array = Strings.split(node.getIndex(), split);
            Node temp = tree;

            String index;
            StringBuilder indexBuilder = new StringBuilder();
            for (int j = 0, lenJ = array.length; j < lenJ; j++) {
                String item = array[j];
                if (j != 0) indexBuilder.append(split);
                indexBuilder.append(item);
                index = indexBuilder.toString();
                if (j == lenJ - 1) {
                    node.setIndex(index);
                    node.setLevel(j);
                    temp.addNode(node);
                } else {
                    if (!temp.hasNode(index)) {
                        Node _node = new Node(index, item);
                        _node.setLevel(j);
                        temp.addNode(_node);
                    }
                }
                temp = temp.getKidNode(index);
            }
            return tree;
        }

        public static Node removeTreeNode(Node tree, String index, String split) {
            if (index == null) return tree;
            boolean hasIndex = hasIndex(tree, index);
            if (!hasIndex) return tree;
            String[] array = Strings.split(index, split);
            Node node = tree;
            Node last = tree;
            for (int j = 0, lenJ = array.length; j < lenJ; j++) {
                String _index = array[j];
                node = node.getKidNode(_index);
                if (j == lenJ - 1) {
                    if (node != null) {
                        last.removeKidNode(_index);
                        break;
                    }
                }
                last = last.getKidNode(_index);
            }
            return tree;
        }

        public static Node removeLeafNode(Node tree, String index, String split) {
            if (index == null) return tree;
            String[] array = Strings.split(index, split);
            Node node = tree;
            for (int j = 0, lenJ = array.length; j < lenJ; j++) {
                String _index = array[j];
                if (!node.hasNode(_index)) break;
                if (j == lenJ - 1) node.removeKidNode(_index);
                node = node.getKidNode(_index);
            }
            return tree;
        }


        public static boolean hasIndex(Map<String, Map> tree, String index, String split) {
            if (index == null) return false;
            String[] array = Strings.split(index, split);
            Map<String, Map> node = tree;
            for (int j = 0, lenJ = array.length; j < lenJ; j++) {
                String item = array[j];
                node = node.get(item);
            }
            if (node != null) return true;
            return false;
        }

        public static Map getTreeNodeByIndex(Map<String, Map> tree, String index, String split) {
            if (index == null) return null;
            String[] array = Strings.split(index, split);
            Map<String, Map> node = tree;
            for (int j = 0, lenJ = array.length; j < lenJ; j++) {
                String item = array[j];
                node = node.get(item);
            }
            if (node != null) return node;
            return null;
        }

        public static Map addTreeNode(Map<String, Map> tree, String index, String split) {
            if (index == null) return tree;
            String[] array = Strings.split(index, split);
            Map<String, Map> node = tree;
            for (int j = 0, lenJ = array.length; j < lenJ; j++) {
                String item = array[j];
                if (!node.containsKey(item)) node.put(item, new HashMap<>());
                node = node.get(item);
            }
            return tree;
        }

        public static Map removeLeafNode(Map<String, Map> tree, String index, String split) {
            if (index == null) return tree;
            String[] array = Strings.split(index, split);
            Map<String, Map> node = tree;
            for (int j = 0, lenJ = array.length; j < lenJ; j++) {
                String item = array[j];
                if (!node.containsKey(item)) break;
                if (j == lenJ - 1) node.remove(item);
                node = node.get(item);
            }
            return tree;
        }

        public static Map removeTreeNode(Map<String, Map> tree, String index, String split) {
            if (index == null) return tree;
            boolean hasIndex = hasIndex(tree, index, split);
            if (!hasIndex) return tree;
            String[] array = Strings.split(index, split);
            Map<String, Map> node = tree;
            Map<String, Map> last = tree;
            for (int j = 0, lenJ = array.length; j < lenJ; j++) {
                String item = array[j];
                node = node.get(item);
                if (node != null && j == lenJ - 1) {
                    last.remove(item);
                    break;
                }
                last = last.get(item);
            }
            return tree;
        }
    }

    public static class Files {

        /**
         * 给定文件路径 获取文件名
         *
         * @param path
         * @return
         */
        public static String getFileName(String path) {
            try {
                String fileName = path.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf('.'));
                return fileName;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static List<File> fileLeaf(String path, boolean withoutEmptyDir, String... extendName) {
            if (withoutEmptyDir) {
                Map<File, String> tree = _fileTree(path, extendName);
                Set<File> files = tree.keySet();
                return new ArrayList<>(files);
            } else {
                Map<File, String> tree = _fileTreeAll(path, extendName);
                Set<File> files = tree.keySet();
                return new ArrayList<>(files);
            }
        }

        /**
         * 只搜索有文件的文件夹
         *
         * @param path
         * @param extendNames 过滤后缀，null 表示全部
         * @return
         */
        private static Map<File, String> _fileTree(String path, String... extendNames) {
            Map<File, String> mapTree = new HashMap<>();
            File file = new File(path);
            if (file.exists()) {
                LinkedList<File> list = new LinkedList<>();
                File[] files = file.listFiles();
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        list.add(file2);
                    } else {
                        String _name = file2.getName();
                        String _path = file2.getAbsolutePath();
                        _path = _path.replace(path + File.separator, "");
                        _path = _path.replace(_name, "");
                        if (extendNames == null) {
                            mapTree.put(file2, _path);
                        } else {
                            for (String ext : extendNames) {
                                if (_name.endsWith(ext)) {
                                    mapTree.put(file2, _path);
                                    break;
                                }
                            }
                        }
                    }
                }
                File temp_file;
                while (!list.isEmpty()) {
                    temp_file = list.removeFirst();
                    files = temp_file.listFiles();
                    for (File file2 : files) {
                        if (file2.isDirectory()) {
                            list.add(file2);
                        } else {
                            String _name = file2.getName();
                            String _path = file2.getAbsolutePath();
                            _path = _path.replace(path + File.separator, "");
                            _path = _path.replace(_name, "");
                            if (extendNames == null) {
                                mapTree.put(file2, _path);
                            } else {
                                for (String ext : extendNames) {
                                    if (_name.endsWith(ext)) {
                                        mapTree.put(file2, _path);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return mapTree;
        }


        /**
         * 只搜索有文件的文件夹
         *
         * @param path
         * @param extendNames 过滤后缀，null 表示全部
         * @return
         */
        private static Map<File, String> _fileTreeAll(String path, String... extendNames) {
            Map<File, String> mapTree = new HashMap<>();
            File file = new File(path);
            if (file.exists()) {
                LinkedList<File> list = new LinkedList<>();
                File[] files = file.listFiles();
                for (File file2 : files) {
                    String _name = file2.getName();
                    String _path = file2.getAbsolutePath();
                    _path = _path.replace(path + File.separator, "");
                    _path = _path.replace(_name, "");
                    if (file2.isDirectory()) {
                        list.add(file2);
                        mapTree.put(file2, _path);
                    } else {
                        if (extendNames == null) {
                            mapTree.put(file2, _path);
                        } else {
                            for (String ext : extendNames) {
                                if (_name.endsWith(ext)) {
                                    mapTree.put(file2, _path);
                                    break;
                                }
                            }
                        }
                    }
                }
                File temp_file;
                while (!list.isEmpty()) {
                    temp_file = list.removeFirst();
                    files = temp_file.listFiles();
                    for (File file2 : files) {
                        String _name = file2.getName();
                        String _path = file2.getAbsolutePath();
                        _path = _path.replace(path + File.separator, "");
                        _path = _path.replace(_name, "");
                        if (file2.isDirectory()) {
                            list.add(file2);
                            mapTree.put(file2, _path);
                        } else {
                            if (extendNames == null) {
                                mapTree.put(file2, _path);
                            } else {
                                for (String ext : extendNames) {
                                    if (_name.endsWith(ext)) {
                                        mapTree.put(file2, _path);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return mapTree;
        }

        public static boolean isExist(String path) {
            File f = new File(path);
            return f.exists();
        }

        public static boolean deleteFile(String path) {
            if (isExist(path)) {
                File f = new File(path);
                return f.delete();
            } else {
                return false;
            }
        }

    }

    public static class Rgb {

        public static String int2HexColor(Object intColor) {
            int c = ObjectUtil.Common.convert(intColor, Integer.class);
            String hex = Integer.toHexString(c);
            hex = hex.length() == 1 ? "0" + hex : hex;
            return hex;
        }

        public static String int2HexColor(Integer intColor) {
            String hex = Integer.toHexString(intColor);
            hex = hex.length() == 1 ? "0" + hex : hex;
            return hex;
        }

        public static int hex2IntColor(String hex) {
            Integer color = Integer.parseInt(hex, 16);
            return color;
        }

        public static int hex2IntColor(Object hex) {
            String h = ObjectUtil.Common.convert(hex, String.class);
            Integer color = Integer.parseInt(h, 16);
            return color;
        }

        public static String int2HexColor(Integer alpha, Integer red, Integer green, Integer blue) {
            String a = int2HexColor(alpha);
            String r = int2HexColor(red);
            String g = int2HexColor(green);
            String b = int2HexColor(blue);
            String color = a + r + g + b;
            return color;
        }

        public static String int2HexArgb(Object alpha, Object red, Object green, Object blue) {
            String a = int2HexColor(alpha);
            String r = int2HexColor(red);
            String g = int2HexColor(green);
            String b = int2HexColor(blue);
            String color = a + r + g + b;
            return color;
        }

        public static Integer argbArray2IntColor(List list) {
            if (list == null) return Color.TRANSPARENT;
            int a = ObjectUtil.Common.convert(list.get(3), Integer.class);
            int r = ObjectUtil.Common.convert(list.get(0), Integer.class);
            int g = ObjectUtil.Common.convert(list.get(1), Integer.class);
            int b = ObjectUtil.Common.convert(list.get(2), Integer.class);
            return Color.argb(a, r, g, b);
        }

        /*public static Color argbArray2Color(List list) {
            Integer color = argbArray2IntColor(list);
            return int2Color(color);
        }*/

        public static Integer argb2IntColor(Object alpha, Object red, Object green, Object blue) {
            int a = ObjectUtil.Common.convert(alpha, Integer.class);
            int r = ObjectUtil.Common.convert(red, Integer.class);
            int g = ObjectUtil.Common.convert(green, Integer.class);
            int b = ObjectUtil.Common.convert(blue, Integer.class);
            return Color.argb(a, r, g, b);
        }

        public static List<Integer> int2ListRgba(Integer color) {
            if (color == null) return null;
            int alpha = Color.alpha(color);
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            List list = new ArrayList();
            list.add(red);
            list.add(green);
            list.add(blue);
            list.add(alpha);
            return list;
        }

        /*public static Color int2Color(Integer color) {
            if (color == null) return null;
            else return Color.valueOf(color);
        }

        public static int color2Int(Color color) {
            if (color == null) return Color.TRANSPARENT;
            else {
                return Color.argb(color.alpha(), color.red(), color.green(), color.blue());
            }
        }*/
    }

    public static class Encry {

        public static String comEncrypt(String content, String password) {
            // 加密
            byte[] encrypt = encrypt(content, password);
            //如果想要加密内容不显示乱码，可以先将密文转换为16进制
            String res = parseByte2HexStr(encrypt);
            return res;
        }

        public static String comDecrypt(String content, String password) {
            //如果的到的是16进制密文，别忘了先转为2进制再解密
            byte[] res = parseHexStr2Byte(content);
            // 解密
            byte[] decrypt = decrypt(res, password);
            return new String(decrypt);
        }

        /**
         * AES加密字符串
         *
         * @param content  需要被加密的字符串
         * @param password 加密需要的密码
         * @return 密文
         */
        public static byte[] encrypt(String content, String password) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
                kgen.init(128, new SecureRandom(password.getBytes()));// 利用用户密码作为随机数初始化出
                // 128位的key生产者
                //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
                SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
                byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器
                byte[] byteContent = content.getBytes("utf-8");
                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器
                byte[] result = cipher.doFinal(byteContent);// 加密
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 解密AES加密过的字符串
         *
         * @param content  AES加密过过的内容
         * @param password 加密时的密码
         * @return 明文
         */
        public static byte[] decrypt(byte[] content, String password) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
                kgen.init(128, new SecureRandom(password.getBytes()));
                SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
                byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器
                cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式的密码器
                byte[] result = cipher.doFinal(content);
                return result; // 明文
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 将二进制转换成16进制
         *
         * @param buf
         * @return
         */
        public static String parseByte2HexStr(byte buf[]) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < buf.length; i++) {
                String hex = Integer.toHexString(buf[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        }

        /**
         * 将16进制转换为二进制
         *
         * @param hexStr
         * @return
         */
        public static byte[] parseHexStr2Byte(String hexStr) {
            if (hexStr.length() < 1)
                return null;
            byte[] result = new byte[hexStr.length() / 2];
            for (int i = 0; i < hexStr.length() / 2; i++) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }
            return result;
        }

        public static String toBase64(String str) {
            return new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
        }

        public static String fromBase64(String str) {
            return new String(Base64.decode(str.getBytes(), Base64.DEFAULT));
        }
    }

    /**
     * 统计帮助类
     */
    public static class Statis {


        public static Map<String, List> regroup(List<Map> data, String divide) {
            List<Map> temp;
            Map<String, List> cache = new LinkedHashMap<>();
            for (Map map : data) {
                String key = (String) map.get(divide);
                if (!cache.containsKey(key)) cache.put(key, new ArrayList());
                temp = cache.get(key);
                //map.remove(divide);
                temp.add(map);
            }
            return cache;
        }

        public static Map<String, Object> regroup(Map<String, Object> data, String divide) {
            Object obj;
            Map<String, Object> res = new LinkedHashMap<>();
            for (String key : data.keySet()) {
                obj = data.get(key);
                if (obj instanceof Map) {
                    Map map = regroup((Map<String, Object>) obj, divide);
                    res.put(key, map);
                }
                if (obj instanceof List) {
                    Map<String, List> map = regroup((List<Map>) obj, divide);
                    res.put(key, map);
                }
            }
            return res;
        }

        public static Number getMaxAsT(List list, String fieldName) {
            if (ObjectUtil.Common.isEmpty(list)) return null;
            Number number = Double.MIN_VALUE;
            Number temp = Double.MIN_VALUE;
            Object val;
            for (Object o : list) {
                val = ObjectUtil.Reflect.getObjFieldVal(o, fieldName);
                temp = ObjectUtil.Common.convert(val, Number.class);
                if (temp != null && temp.doubleValue() > number.doubleValue()) {
                    number = temp;
                }
            }
            return number;
        }

        public static Number getMinAsT(List list, String fieldName) {
            if (ObjectUtil.Common.isEmpty(list)) return null;
            Number number = Double.MAX_VALUE;
            Number temp = Double.MAX_VALUE;
            Object val;
            for (Object o : list) {
                val = ObjectUtil.Reflect.getObjFieldVal(o, fieldName);
                temp = ObjectUtil.Common.convert(val, Number.class);
                if (temp != null && temp.doubleValue() < number.doubleValue()) {
                    number = temp;
                }
            }
            return number;
        }

        public static Double sum(List list, String fieldName) {
            if (ObjectUtil.Common.isEmpty(list)) return null;
            Double sum = 0d;
            Double temp;
            Object val;
            for (Object o : list) {
                val = ObjectUtil.Reflect.getObjFieldVal(o, fieldName);
                temp = ObjectUtil.Common.convert(val, Double.class);
                if (temp != null) sum += temp;
            }
            return sum;
        }

        /**
         * 简单行统计
         *
         * @param data
         * @param sumField
         * @return
         */
        public static Object simpleStatis(List data, String... sumField) {
            if (data == null) return null;
            if (sumField == null) return null;
            String field;
            Object item;
            Double temp;
            Object cell;
            Map<String, Double> res = new HashMap();
            for (int i = 0, len = data.size(); i < len; i++) {
                item = data.get(i);
                if (item == null) continue;
                for (int j = 0, l = sumField.length; j < l; j++) {
                    field = sumField[j];
                    cell = ObjectUtil.Reflect.getObjFieldVal(item, field);
                    temp = ObjectUtil.Common.convert(cell, Double.class);
                    if (res.containsKey(field)) {
                        Double x = res.containsKey(field) ? res.get(field) : 0d;
                        x += temp;
                        res.put(field, x);
                    } else res.put(field, temp);
                }
            }
            return res;
        }

        /**
         * 行间求和    group by 统计核心
         *
         * @param data        要处理的数据
         * @param sumFields   求和字段
         * @param groupFields 分组字段
         * @return
         */
        private static Map groupStatisticsCore(List data, String[] sumFields, String[] groupFields) {
            return groupStatisticsCore(data, sumFields, groupFields, null);
        }

        private static Map groupStatisticsCore(List data, String[] sumFields, String[] groupFields, String precision) {
            if (data == null) return null;
            if (sumFields == null || groupFields == null) return null;
            String groupItem;
            StringBuilder groupIndex;
            Map<String, Double> map;
            Double x, sum;
            Object cell, item, o;
            Map<String, Map<String, Double>> res = new HashMap();
            for (int i = 0, len = data.size(); i < len; i++) {
                item = data.get(i);
                groupIndex = new StringBuilder();
                for (int j = 0, l = groupFields.length; j < l; j++) {
                    cell = ObjectUtil.Reflect.getObjFieldVal(item, groupFields[j]);
                    groupItem = ObjectUtil.Common.convert(cell, String.class);
                    groupIndex.append(groupItem);
                    if (j != l - 1) groupIndex.append("-");
                }
                groupItem = groupIndex.toString();
                if (res.containsKey(groupItem)) {
                    map = res.get(groupItem);
                } else {
                    map = new HashMap<>();
                    res.put(groupIndex.toString(), map);
                }
                for (String cField : sumFields) {
                    o = ObjectUtil.Reflect.getObjFieldVal(item, cField);
                    x = ObjectUtil.Common.convert(o, Double.class);
                    sum = map.containsKey(cField) ? map.get(cField) : 0d;
                    sum += x;
                    if (precision == null) map.put(cField, sum);
                    else {
                        sum = DecimalFormats.formatDouble(sum, precision);
                        map.put(cField, sum);
                    }
                }
            }
            return res;
        }

        /**
         * 分组求和统计
         *
         * @param data
         * @param sumFields
         * @param groupFields
         * @return
         */
        public static List groupStatisticsSimple(List data, String[] sumFields, String[] groupFields) {
            Map<String, Map> map = groupStatisticsCore(data, sumFields, groupFields, "0.00");
            String[] array;
            Map item;
            List<Map> list = new ArrayList<>();
            for (String key : map.keySet()) {
                array = key.split("-");
                item = map.get(key);
                for (int i = 0, len = array.length; i < len; i++) {
                    item.put(groupFields[i], array[i]);
                }
                list.add(item);
            }
            return list;
        }

        /**
         * 分组结构树
         * 列转行复合查询
         *
         * @param data        要处理的数据
         * @param sumFields   求和字段
         * @param groupFields 分组字段
         * @return
         */
        public static Map groupStatisTree(List data, String[] sumFields, String[] groupFields) {
            if (data == null) return null;
            if (sumFields == null || groupFields == null) return null;
            Map map = null;
            Double x, sum;
            String groupIndex;
            Object cell, item, o;
            Map<String, Map> res = new HashMap();
            for (int i = 0, len = data.size(); i < len; i++) {
                item = data.get(i);
                map = res;
                for (int j = 0, l = groupFields.length; j < l; j++) {
                    cell = ObjectUtil.Reflect.getObjFieldVal(item, groupFields[j]);
                    groupIndex = ObjectUtil.Common.convert(cell, String.class);
                    if (!map.containsKey(groupIndex)) map.put(groupIndex, new HashMap());
                    map = (Map) map.get(groupIndex);
                }
                for (String cField : sumFields) {
                    o = ObjectUtil.Reflect.getObjFieldVal(item, cField);
                    x = ObjectUtil.Common.convert(o, Double.class);
                    x = x == null ? 0d : x;
                    sum = map.containsKey(cField) ? (Double) map.get(cField) : 0d;
                    sum += x;
                    map.put(cField, sum);
                }
            }
            return res;
        }

        /**
         * 列转行复合查询
         *
         * @param data        要处理的数据
         * @param sumFields   求和字段
         * @param groupFields 分组字段
         * @return
         */
        public static List<Map> listStatis(List data, String[] sumFields, String[] groupFields) {
            if (data == null) return null;
            if (sumFields == null || groupFields == null) return null;
            Map map = null;
            Double x, sum;
            String groupIndex;
            Object cell, item, o;
            List<Map> list = new ArrayList<>();
            Map<String, Map> res = new HashMap();
            for (int i = 0, len = data.size(); i < len; i++) {
                item = data.get(i);
                map = res;
                for (int j = 0, l = groupFields.length; j < l; j++) {
                    cell = ObjectUtil.Reflect.getObjFieldVal(item, groupFields[j]);
                    groupIndex = ObjectUtil.Common.convert(cell, String.class);
                    if (!map.containsKey(groupIndex)) map.put(groupIndex, new HashMap());
                    map = (Map) map.get(groupIndex);
                }
                for (String cField : sumFields) {
                    o = ObjectUtil.Reflect.getObjFieldVal(item, cField);
                    x = ObjectUtil.Common.convert(o, Double.class);
                    x = x == null ? 0d : x;
                    sum = map.containsKey(cField) ? (Double) map.get(cField) : 0d;
                    sum += x;
                    map.put(cField, sum);
                }
            }
            return list;
        }

        public static void join(List<Map> list, String... ins) {

        }

        /**
         * 列合并成行
         *
         * @param data        简单表结构
         * @param groupFields
         * @return
         */
        public static List groupColumn2Row(List<Map> data, String[] groupFields) {
            Map item;
            Object cell;
            String groupItem;
            StringBuilder groupIndex;
            Map<String, Map> cache = new HashMap<>();
            for (int i = 0, lenI = data.size(); i < lenI; i++) {
                item = data.get(i);
                groupIndex = new StringBuilder();
                for (int j = 0, lenJ = groupFields.length; j < lenJ; j++) {
                    cell = ObjectUtil.Reflect.getObjFieldVal(item, groupFields[j]);
                    groupItem = ObjectUtil.Common.convert(cell, String.class);
                    groupIndex.append(groupItem);
                    if (j != lenJ - 1) groupIndex.append("-");
                }
                String index = groupIndex.toString();
                if (!cache.containsKey(index)) cache.put(index, new HashMap());
                cache.get(index).putAll(item);
            }
            return new ArrayList(cache.values());
        }

        /**
         * 列间求和
         *
         * @param data
         * @param groupFields
         * @return
         */
        public static List sumColumn(List<Map> data, String[] groupFields) {
            return data;
        }

        /**
         * 神级递归
         *
         * @param object
         * @return
         */
        private static String ex(Object object) {
            if (object == null) return null;
            StringBuilder item = new StringBuilder();
            if (object instanceof Map) {
                Map map = (Map) object;
                for (Object key : map.keySet()) {
                    Object o = map.get(key);
                    if (o instanceof Map) {
                        String res = ex(o);
                        if (res != null) item.append(res);
                    } else {
                        if (o == null) continue;
                        if (o.toString().toLowerCase().contains("sum")
                                || o.toString().toLowerCase().contains("avg")) {
                            if (!o.toString().trim().contains("(*)")) continue;
                            for (Object _key : map.keySet()) {
                                Object val = map.get(_key);
                                if (o.toString().equals(val)) continue;
                                if (val instanceof Map) {
                                    String res = ex(val);
                                    if (res != null) item.append(res);
                                } else if (val instanceof List) {
                                    String res = ex(val);
                                    if (res != null) item.append(res);
                                } else if (object.getClass().isArray()) {
                                    String res = ex(val);
                                    if (res != null) item.append(res);
                                } else {
                                    item.append("{").append(_key).append("},");
                                }
                            }
                            String prefix = o.toString().substring(0, o.toString().indexOf("("));
                            String temp = item.toString();
                            if (temp.endsWith(",")) temp = temp.substring(0, temp.lastIndexOf(","));
                            map.put(key, prefix + "(" + temp + ")");
                        }
                    }
                }
            } else if (object instanceof List) {
                List list = (List) object;
                for (Object o : list) {
                    String res = ex(o);
                    if (res != null) item.append(res);
                }
            } else if (object.getClass().isArray()) {
                int len = Array.getLength(object);
                for (int i = 0; i < len; i++) {
                    Object o = Array.get(object, i);
                    String res = ex(o);
                    if (res != null) item.append(res);
                }
            } else {
                System.out.println(object);
            }
            return item.toString();
        }

        public static Map<String, Object> regroup1(Map<String, Object> data, String divide) {
            Object obj;
            Map<String, Object> res = new HashMap<>();
            for (String key : data.keySet()) {
                obj = data.get(key);
                if (obj instanceof Map) {
                    Map map = regroup1((Map<String, Object>) obj, divide);
                    res.put(key, map);
                }
                if (obj instanceof List) {
                    Map<String, List> map = regroup((List<Map>) obj, divide);
                    res.put(key, map);
                }
            }
            return res;
        }


        public static List orderBy(List data, Sort... sorts) {
            return data;
        }

        public static Sort create(String field, Order order) {
            return new Sort(field, order);
        }

        public static class Sort {

            public String field;

            public Order order;

            public Sort(String field, Order order) {
                this.field = field;
                this.order = order;
            }
        }

        public static enum Order {
            asc,
            desc
        }
    }
}
