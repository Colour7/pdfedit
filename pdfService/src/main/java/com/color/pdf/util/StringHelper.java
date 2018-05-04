package com.color.pdf.util;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {

    public static final String PHONE_CHECK = "^0?1[3|4|5|6|7|8|9][0-9]\\d{8}$";//"^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9])|(14[0-9]))\\\\d{8}$";
    public static final String ONE_TWO_ZREO_CHECK = "^0|1|2$";
    public static final String DATE_CHECK = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
    public static final String EMAIL_CHECK = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
    public static final String NUMBER_CHECK = "^[0-9]+$";
    public static final String LOGINPWD_CHECK = "^[A-Za-z0-9]{6,10}$";
    public static final String NUMBER_JOINT_CHECK = "^([0-9]+[,]?)+$";
    public static final String CHINESE_NUM_ENG_CHECK = "^[a-zA-Z0-9_\\u4e00-\\u9fa5_\\-,.?:;'\"!`]+$";
    public static final String CHINESE_ONLY_CHECK = "^[\\u4e00-\\u9fa5]*$";
    public static final String PASSWORD_CHECK = "^[A-Za-z0-9]{6,20}$";
    public static final String USERNAME_CHECK = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
    public static final String ACCOUNT_LOGINPWD_CHECK = "^[A-Za-z0-9]{6,12}$";

    /**
     * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
     *
     * @param sourceDate
     * @param formatLength
     * @return 重组后的数据
     */
    public static String addzero(int sourceDate, int formatLength) {
        /*
         * 0 指前面补充零
         * formatLength 字符总长度为 formatLength
         * d 代表为正数。
         */
        String newString = String.format("%0" + formatLength + "d", sourceDate);
        return newString;
    }

    /**
     * String 转Integer
     *
     * @param strValue
     * @param defValue
     * @return
     */
    public static Integer string2Integer(String strValue, Integer defValue) {
        if (!StringUtils.isEmpty(strValue)) {
            try {
                return Integer.parseInt(strValue);
            } catch (NumberFormatException e) {
                return defValue;
            }
        }
        return defValue;
    }

    public static Float string2Float(String strValue, Float defValue) {
        if (!StringUtils.isEmpty(strValue)) {
            try {
                return Float.parseFloat(strValue);
            } catch (NumberFormatException e) {
                return defValue;
            }
        }
        return defValue;
    }

    /**
     * String 转Long
     *
     * @param strValue
     * @param defValue
     * @return
     */
    public static Long string2Long(String strValue, Long defValue) {
        if (!StringUtils.isEmpty(strValue)) {
            try {
                return Long.parseLong(strValue);
            } catch (NumberFormatException e) {
                return defValue;
            }
        }
        return defValue;
    }

    /**
     * 正则校验
     *
     * @param value 值
     * @param regex 正则
     * @return
     */
    public static boolean patternRegex(String value, String regex) {
        if (regex != null && value != null) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(value);
            if (m.matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取随机数
     *
     * @param size     长度
     * @param type     类型[1:纯数字,2:字母,其他:数字和字母]
     * @param castRule 1:小写, 2:大写，其他不区分
     * @return
     */
    public static String getRandomNumbs(int size, int type, Integer castRule) {
        String baseStr = null;
        if (type == 1) {
            baseStr = "0123456789";
        } else if (type == 2) {
            baseStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        } else {
            baseStr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }

        int baseStrSize = baseStr.length();
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size; i++) {
            int number = random.nextInt(baseStrSize);
            sb.append(baseStr.charAt(number));
        }

        if (castRule != null) {
            if (castRule.intValue() == 1) {
                return sb.toString().toLowerCase();
            } else if (castRule.intValue() == 2) {
                return sb.toString().toUpperCase();
            }
        }

        return sb.toString();
    }

    /**
     * 判断参数是否为空或null
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<String, String> map) {
        if (map == null || map.size() <= 0)
            return true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (org.apache.commons.lang.StringUtils.isEmpty(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * list转字符串加入分隔符
     *
     * @param list
     * @param separator
     * @return
     */
    public static String listToString(List list, String separator) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * string字符串转List<Long>
     *
     * @param ids
     * @return
     */
    public static List<Long> buildLongListFromString(String ids) {
        if (!StringUtils.isEmpty(ids)) {
            String[] idStr = ids.split(",");
            List<Long> result = new ArrayList<>();
            for (String id : idStr) {
                result.add(Long.parseLong(id));
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * string字符串转List<Integer>
     *
     * @param ids
     * @return
     */
    public static List<Integer> buildIntegerListFromString(String ids) {
        if (!StringUtils.isEmpty(ids)) {
            String[] idStr = ids.split(",");
            List<Integer> result = new ArrayList<>();
            for (String id : idStr) {
                result.add(Integer.parseInt(id));
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * string字符串转List<String>
     *
     * @param ids
     * @return
     */
    public static List<String> buildStringListFromString(String ids) {
        if (!StringUtils.isEmpty(ids)) {
            String[] idStr = ids.split(",");
            List<String> result = new ArrayList<>();
            for (String id : idStr) {
                result.add(id);
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
