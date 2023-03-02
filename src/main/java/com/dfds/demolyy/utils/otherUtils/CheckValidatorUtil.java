package com.dfds.demolyy.utils.otherUtils;

import cn.hutool.core.util.StrUtil;

public class CheckValidatorUtil {
    private CheckValidatorUtil() {
    }

    public static boolean hasForbiddenChar(String text, CharSequence... forbiddenChars) {
        if (forbiddenChars != null && !StrUtil.isBlank(text)) {
            String forbidden = String.join("", forbiddenChars);
            String regex = String.format("[%s]+", forbidden);
            return CheckRegexUtil.contains(text, regex);
        } else {
            return false;
        }
    }

    public static boolean hasChineseChar(String text) {
        return CheckRegexUtil.contains(text, "[\\u4e00-\\u9fa5]+");
    }

    public static boolean isAllChineseChar(String text) {
        return CheckRegexUtil.matches(text, "[\\u4e00-\\u9fa5]+");
    }

    public static boolean hasEnglishChar(String text) {
        return CheckRegexUtil.contains(text, "[A-Za-z]+");
    }

    public static boolean isAllEnglishChar(String text) {
        return CheckRegexUtil.matches(text, "[A-Za-z]+");
    }

    public static boolean isAllGeneralAndChineseChar(String text) {
        return CheckRegexUtil.matches(text, "[一-\u9fff\\w]+");
    }

    public static boolean isAllGeneralChar(String text) {
        return CheckRegexUtil.matches(text, "\\w+");
    }

    public static boolean isAllLowerEnglishChar(String text) {
        return CheckRegexUtil.matches(text, "[a-z]+");
    }

    public static boolean isAllUpperEnglishChar(String text) {
        return CheckRegexUtil.matches(text, "[A-Z]+");
    }

    public static boolean isCitizenId15(String text) {
        return CheckRegexUtil.matches(text, "((1[1-5]|2[1-3]|3[1-7]|4[1-3]|5[0-4]|6[1-5])\\d{4})((\\d{2}((0[13578]|1[02])(0[1-9]|[12]\\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\\d|30)|02(0[1-9]|1\\d|2[0-8])))|([02468][048]|[13579][26])0229)(\\d{3})");
    }

    public static boolean isCitizenId18(String text) {
        return CheckRegexUtil.matches(text, "((1[1-5]|2[1-3]|3[1-7]|4[1-3]|5[0-4]|6[1-5])\\d{4})((\\d{4}((0[13578]|1[02])(0[1-9]|[12]\\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\\d|30)|02(0[1-9]|1\\d|2[0-8])))|([02468][048]|[13579][26])0229)(\\d{3}(\\d|X))");
    }

    public static boolean isDate(String text) {
        return CheckRegexUtil.matches(text, "(?:(?!0000)[0-9]{4}([-/.]?)(?:(?:0?[1-9]|1[0-2])\\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\\1(?:29|30)|(?:0?[13578]|1[02])\\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)0?2\\2(?:29))");
    }

    public static boolean isDigitNumber(String text, int n) {
        String format = String.format("\\d{%d}", n);
        return CheckRegexUtil.matches(text, format);
    }

    public static boolean isEmail(String text) {
        return CheckRegexUtil.matches(text, "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])", 2);
    }

    public static boolean isFloat(String text) {
        return CheckRegexUtil.matches(text, "-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)");
    }

    public static boolean isHttpUrl(String text) {
        return CheckRegexUtil.matches(text, "https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,}");
    }

    public static boolean isInteger(String text) {
        return CheckRegexUtil.matches(text, "-?[1-9]\\d*");
    }

    public static boolean isIpv4(String text) {
        return CheckRegexUtil.matches(text, "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
    }

    public static boolean isIpv6(String text) {
        return CheckRegexUtil.matches(text, "(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|::(ffff(:0{1,4})?:)?((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))");
    }

    public static boolean isLeastDigitNumber(String text, int n) {
        String format = String.format("\\d{%d,}", n);
        return CheckRegexUtil.matches(text, format);
    }

    public static boolean isLengthInRange(String text, int min, int max) {
        String format = String.format(".{%d,%d}", min, max);
        return CheckRegexUtil.matches(text, format);
    }

    public static boolean isMac(String text) {
        return CheckRegexUtil.matches(text, "((([a-fA-F0-9][a-fA-F0-9]+[-]){5}|([a-fA-F0-9][a-fA-F0-9]+[:]){5})([a-fA-F0-9][a-fA-F0-9])$)|(^([a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9]+[.]){2}([a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9]))", 2);
    }

    public static boolean isMarkdownImageTag(String text) {
        return CheckRegexUtil.matches(text, "!\\[.+\\]");
    }

    public static boolean isMobile(String text) {
        return CheckRegexUtil.matches(text, "(?:\\+?86)?(\\s)?1(?:3\\d{3}|5[^4\\D]\\d{2}|8\\d{3}|7(?:[01356789]\\d{2}|4(?:0\\d|1[0-2]|9\\d))|9[189]\\d{2}|6[567]\\d{2}|4[579]\\d{2})\\d{6}");
    }

    public static boolean isNegativeFloat(String text) {
        return CheckRegexUtil.matches(text, "-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*)");
    }

    public static boolean isNegativeInteger(String text) {
        return CheckRegexUtil.matches(text, "-[1-9]\\d*");
    }

    public static boolean isNoneGeneralChar(String text) {
        return CheckRegexUtil.matches(text, "\\W+");
    }

    public static boolean isNoneNumberChar(String text) {
        return CheckRegexUtil.matches(text, "[\\D]*");
    }

    public static boolean isNotPositiveFloat(String text) {
        return !isNotNegativeFloat(text);
    }

    public static boolean isNotNegativeFloat(String text) {
        return CheckRegexUtil.matches(text, "[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0");
    }

    public static boolean isNotPositiveInteger(String text) {
        return !isNotNegativeInteger(text);
    }

    public static boolean isNotNegativeInteger(String text) {
        return CheckRegexUtil.matches(text, "[1-9]\\d*|0");
    }

    public static boolean isNumber(String text) {
        return CheckRegexUtil.matches(text, "\\d+");
    }

    public static boolean isPhone(String text) {
        return CheckRegexUtil.matches(text, "(\\d{3,4}-)?\\d{6,8}");
    }

    public static boolean isPlateNumber(String text) {
        return CheckRegexUtil.matches(text, "[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z][A-Z][DF]?[A-Z0-9]{4}[A-Z0-9挂学警港澳]");
    }

    public static boolean isPositiveFloat(String text) {
        return CheckRegexUtil.matches(text, "[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*");
    }

    public static boolean isPositiveInteger(String text) {
        return CheckRegexUtil.matches(text, "[1-9]\\d*");
    }

    public static boolean isRangeDigitNumber(String text, int min, int max) {
        String format = String.format("\\d{%d,%d}", min, max);
        return CheckRegexUtil.matches(text, format);
    }

    public static boolean isTime(String text) {
        return CheckRegexUtil.matches(text, "([0-1][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])");
    }

    public static boolean isUrl(String text) {
        return CheckRegexUtil.matches(text, "(ht|f)(tp|tps)\\://[a-zA-Z0-9\\-\\.]+\\.([a-zA-Z]{2,3})?(/\\S*)?");
    }

    public static boolean isUuid(String text) {
        return CheckRegexUtil.matches(text, "[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}");
    }

    public static boolean isZipCode(String text) {
        return CheckRegexUtil.matches(text, "[1-9]\\d{5}(?!\\d)");
    }
}