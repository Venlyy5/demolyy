package com.dfds.demolyy.utils.otherUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Holder;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckRegexUtil {
    public static final String EN_CHAR = "[A-Za-z]+";
    public static final String EN_LOWER_CHAR = "[a-z]+";
    public static final String EN_UPPER_CHAR = "[A-Z]+";
    public static final String ZH_CHAR = "[\\u4e00-\\u9fa5]+";
    public static final String ALL_GENERAL_AND_ZH_CHAR = "[一-\u9fff\\w]+";
    public static final String GENERAL_CHAR = "\\w+";
    public static final String CITIZEN_ID15 = "((1[1-5]|2[1-3]|3[1-7]|4[1-3]|5[0-4]|6[1-5])\\d{4})((\\d{2}((0[13578]|1[02])(0[1-9]|[12]\\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\\d|30)|02(0[1-9]|1\\d|2[0-8])))|([02468][048]|[13579][26])0229)(\\d{3})";
    public static final String CITIZEN_ID18 = "((1[1-5]|2[1-3]|3[1-7]|4[1-3]|5[0-4]|6[1-5])\\d{4})((\\d{4}((0[13578]|1[02])(0[1-9]|[12]\\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\\d|30)|02(0[1-9]|1\\d|2[0-8])))|([02468][048]|[13579][26])0229)(\\d{3}(\\d|X))";
    public static final String DATE = "(?:(?!0000)[0-9]{4}([-/.]?)(?:(?:0?[1-9]|1[0-2])\\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\\1(?:29|30)|(?:0?[13578]|1[02])\\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)0?2\\2(?:29))";
    public static final String EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";
    public static final String FLOAT = "-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)";
    public static final String GROUP_VAR = "\\$(\\d+)";
    public static final String HEX = "[a-f0-9]+";
    public static final String INTEGER = "-?[1-9]\\d*";
    public static final String IPV4 = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
    public static final String IPV6 = "(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|::(ffff(:0{1,4})?:)?((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))";
    public static final String MAC = "((([a-fA-F0-9][a-fA-F0-9]+[-]){5}|([a-fA-F0-9][a-fA-F0-9]+[:]){5})([a-fA-F0-9][a-fA-F0-9])$)|(^([a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9]+[.]){2}([a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9]))";
    public static final String MOBILE = "(?:\\+?86)?(\\s)?1(?:3\\d{3}|5[^4\\D]\\d{2}|8\\d{3}|7(?:[01356789]\\d{2}|4(?:0\\d|1[0-2]|9\\d))|9[189]\\d{2}|6[567]\\d{2}|4[579]\\d{2})\\d{6}";
    public static final String NEGATIVE_FLOAT = "-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*)";
    public static final String NEGATIVE_INTEGER = "-[1-9]\\d*";
    public static final String NONE_GENERAL_CHAR = "\\W+";
    public static final String NONE_NUM_CHAR = "[\\D]*";
    public static final String NOT_NEGATIVE_FLOAT = "[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0";
    public static final String NOT_NEGATIVE_INTEGER = "[1-9]\\d*|0";
    public static final String NUMBERS = "\\d+";
    public static final String NUMBER_DIGIT = "\\d{%d}";
    public static final String NUMBER_DIGIT_MORE = "\\d{%d,}";
    public static final String NUMBER_DIGIT_RANGE = "\\d{%d,%d}";
    public static final String PHONE = "(\\d{3,4}-)?\\d{6,8}";
    public static final String PLATE_NUMBER = "[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z][A-Z][DF]?[A-Z0-9]{4}[A-Z0-9挂学警港澳]";
    public static final String POSITIVE_FLOAT = "[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*";
    public static final String POSITIVE_INTEGER = "[1-9]\\d*";
    public static final Set<Character> REGEX_ESCAPE_CHARS = CollectionUtil.newHashSet(new Character[]{'$', '(', ')', '*', '+', '.', '[', ']', '?', '\\', '^', '{', '}', '|'});
    public static final String REGEX_MARKDOWN_IMAGE_TAG = "!\\[.+\\]";
    public static final String TEXT_LENGTH_RANGE = ".{%d,%d}";
    public static final String TIME = "([0-1][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])";
    public static final String URL = "(ht|f)(tp|tps)\\://[a-zA-Z0-9\\-\\.]+\\.([a-zA-Z]{2,3})?(/\\S*)?";
    public static final String URL_HTTP = "https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,}";
    public static final String UUID = "[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}";
    public static final String ZIP_CODE = "[1-9]\\d{5}(?!\\d)";

    public CheckRegexUtil() {
    }

    public static boolean contains(CharSequence text, String regex) {
        if (!StrUtil.isBlank(text) && !StrUtil.isBlank(regex)) {
            Pattern pattern = Pattern.compile(regex);
            return contains(text, pattern);
        } else {
            return false;
        }
    }

    public static boolean contains(CharSequence text, Pattern pattern) {
        return !StrUtil.isBlank(text) && pattern != null ? pattern.matcher(text).find() : false;
    }

    public static int count(CharSequence text, String regex) {
        if (!StrUtil.isBlank(text) && !StrUtil.isBlank(regex)) {
            Pattern pattern = Pattern.compile(regex);
            return count(text, pattern);
        } else {
            return 0;
        }
    }

    public static int count(CharSequence text, Pattern pattern) {
        if (!StrUtil.isBlank(text) && pattern != null) {
            int count = 0;

            for(Matcher matcher = pattern.matcher(text); matcher.find(); ++count) {
            }

            return count;
        } else {
            return 0;
        }
    }

    public static String escape(CharSequence text) {
        if (StrUtil.isBlank(text)) {
            return StrUtil.str(text);
        } else {
            StringBuilder builder = new StringBuilder();
            int len = text.length();

            for(int i = 0; i < len; ++i) {
                char current = text.charAt(i);
                if (REGEX_ESCAPE_CHARS.contains(current)) {
                    builder.append('\\');
                }

                builder.append(current);
            }

            return builder.toString();
        }
    }

    public static String escape(char c) {
        StringBuilder builder = new StringBuilder();
        if (REGEX_ESCAPE_CHARS.contains(c)) {
            builder.append('\\');
        }

        builder.append(c);
        return builder.toString();
    }

    public static String extractMulti(CharSequence content, String regex, String template) {
        if (null != content && null != regex && null != template) {
            Pattern pattern = getPattern(regex, 32);
            return extractMulti(pattern, content, template);
        } else {
            return null;
        }
    }

    public static String extractMulti(Pattern pattern, CharSequence content, String template) {
        if (null != content && null != pattern && null != template) {
            TreeSet<Integer> varNums = new TreeSet(new Comparator<Integer>() {
                public int compare(Integer o1, Integer o2) {
                    return ObjectUtil.compare(o2, o1);
                }
            });
            Pattern groupPattern = getPattern("\\$(\\d+)", 0);
            Matcher matcherForTemplate = groupPattern.matcher(template);

            while(matcherForTemplate.find()) {
                varNums.add(Integer.parseInt(matcherForTemplate.group(1)));
            }

            Matcher matcher = pattern.matcher(content);
            if (!matcher.find()) {
                return null;
            } else {
                Integer group;
                for(Iterator var7 = varNums.iterator(); var7.hasNext(); template = template.replace("$" + group, matcher.group(group))) {
                    group = (Integer)var7.next();
                }

                return template;
            }
        } else {
            return null;
        }
    }

    public static Pattern getPattern(String regex, int flag) {
        return StrUtil.isBlank(regex) ? null : Pattern.compile(regex, flag);
    }

    public static String extractMultiAndDelPre(String regex, Holder<CharSequence> contentHolder, String template) {
        if (null != contentHolder && null != regex && null != template) {
            Pattern pattern = getPattern(regex, 32);
            return extractMultiAndDelPre(pattern, contentHolder, template);
        } else {
            return null;
        }
    }

    public static String extractMultiAndDelPre(Pattern pattern, Holder<CharSequence> contentHolder, String template) {
        if (null != contentHolder && null != pattern && null != template) {
            HashSet<String> varNums = (HashSet)getAll(template, (String)"\\$(\\d+)", 1, new HashSet());
            CharSequence content = (CharSequence)contentHolder.get();
            Matcher matcher = pattern.matcher(content);
            if (!matcher.find()) {
                return null;
            } else {
                String var;
                int group;
                for(Iterator var6 = varNums.iterator(); var6.hasNext(); template = template.replace("$" + var, matcher.group(group))) {
                    var = (String)var6.next();
                    group = Integer.parseInt(var);
                }

                contentHolder.set(StrUtil.sub(content, matcher.end(), content.length()));
                return template;
            }
        } else {
            return null;
        }
    }

    public static <T extends Collection<String>> T getAll(CharSequence text, String regex, int group, T collection) {
        return null == regex ? collection : getAll(text, Pattern.compile(regex, 32), group, collection);
    }

    public static <T extends Collection<String>> T getAll(CharSequence text, Pattern pattern, int group, T collection) {
        if (null != pattern && null != text) {
            if (null == collection) {
                throw new NullPointerException("Null collection param provided!");
            } else {
                Matcher matcher = pattern.matcher(text);

                while(matcher.find()) {
                    collection.add(matcher.group(group));
                }

                return collection;
            }
        } else {
            return null;
        }
    }

    public static List<String> getAll(CharSequence text, String regex, int group) {
        return (List)getAll(text, (String)regex, group, new ArrayList());
    }

    public static List<String> getAllGroups(CharSequence text, Pattern pattern) {
        return getAllGroups(text, pattern, true);
    }

    public static List<String> getAllGroups(CharSequence text, Pattern pattern, boolean withGroup0) {
        if (null != text && null != pattern) {
            ArrayList<String> result = new ArrayList();
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                int startGroup = withGroup0 ? 0 : 1;
                int groupCount = matcher.groupCount();

                for(int i = startGroup; i <= groupCount; ++i) {
                    result.add(matcher.group(i));
                }
            }

            return result;
        } else {
            return null;
        }
    }

    public static List<String> getAllInJson(String text, String key) {
        String pattern = String.format("\"%s\":\"?(\\w+)\"?", key);
        return getAll(text, (String)pattern);
    }

    public static List<String> getAll(CharSequence text, String regex) {
        return getAll(text, Pattern.compile(regex));
    }

    public static List<String> getAll(CharSequence text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) {
            return null;
        } else {
            int count = matcher.groupCount();
            List<String> list = new LinkedList();

            for(int i = 1; i <= count; ++i) {
                list.add(matcher.group(i));
            }

            return list;
        }
    }

    public static String getFirst(CharSequence text, String regex) {
        return get(text, (String)regex, 0);
    }

    public static String get(CharSequence text, String regex, int group) {
        if (!StrUtil.isBlank(text) && !StrUtil.isBlank(regex)) {
            Pattern pattern = Pattern.compile(regex);
            return get(text, pattern, group);
        } else {
            return null;
        }
    }

    public static String get(CharSequence text, Pattern pattern, int group) {
        if (!StrUtil.isBlank(text) && pattern != null) {
            Matcher matcher = pattern.matcher(text);
            return matcher.find() ? matcher.group(group) : null;
        } else {
            return null;
        }
    }

    public static boolean matches(CharSequence text, String regex) {
        return matches(text, regex, 0);
    }

    public static boolean matches(CharSequence text, String regex, int flag) {
        if (!StrUtil.isBlank(text) && !StrUtil.isBlank(regex)) {
            Pattern pattern = Pattern.compile(regex, flag);
            return matches(text, pattern);
        } else {
            return false;
        }
    }

    public static boolean matches(CharSequence text, Pattern pattern) {
        return !StrUtil.isBlank(text) && pattern != null ? pattern.matcher(text).matches() : false;
    }

    public static String removeAll(CharSequence text, Pattern pattern) {
        return replaceAll(text, pattern, "");
    }

    public static String replaceAll(CharSequence text, Pattern pattern, String replacement) {
        return !StrUtil.isBlank(text) && pattern != null && replacement != null ? pattern.matcher(text).replaceAll(replacement) : StrUtil.str(text);
    }

    public static String removeAll(CharSequence text, String regex) {
        return replaceAll(text, regex, "");
    }

    public static String replaceAll(CharSequence text, String regex, String replacement) {
        if (!StrUtil.isBlank(text) && !StrUtil.isBlank(regex) && replacement != null) {
            Pattern pattern = Pattern.compile(regex);
            return replaceAll(text, pattern, replacement);
        } else {
            return StrUtil.str(text);
        }
    }

    public static String removeFirst(CharSequence text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return removeFirst(text, pattern);
    }

    public static String removeFirst(CharSequence text, Pattern pattern) {
        return replaceFirst(text, pattern, "");
    }

    public static String replaceFirst(CharSequence text, Pattern pattern, String replacement) {
        return !StrUtil.isBlank(text) && pattern != null && replacement != null ? pattern.matcher(text).replaceFirst(replacement) : StrUtil.str(text);
    }

    public static String removePre(CharSequence text, String regex) {
        if (!StrUtil.isBlank(text) && !StrUtil.isBlank(regex)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            return matcher.find() ? StrUtil.sub(text, matcher.end(), text.length()) : StrUtil.str(text);
        } else {
            return StrUtil.str(text);
        }
    }

    public static String replaceAll(CharSequence text, String regex, Func1<Matcher, String> callback) {
        return replaceAll(text, Pattern.compile(regex), callback);
    }

    public static String replaceAll(CharSequence text, Pattern pattern, Func1<Matcher, String> callback) {
        if (!StrUtil.isBlank(text) && pattern != null && callback != null) {
            Matcher matcher = pattern.matcher(text);
            StringBuffer buffer = new StringBuffer();

            while(matcher.find()) {
                try {
                    matcher.appendReplacement(buffer, (String)callback.call(matcher));
                } catch (Exception var6) {
                    throw new UtilException(var6);
                }
            }

            matcher.appendTail(buffer);
            return buffer.toString();
        } else {
            return StrUtil.str(text);
        }
    }

    public static String replaceAll(String text, String regex, List<String> values) {
        Pattern pattern = Pattern.compile(regex);
        return replaceAll(text, pattern, values);
    }

    public static String replaceAll(String text, Pattern pattern, List<String> values) {
        Matcher matcher = pattern.matcher(text);
        StringBuffer buffer = new StringBuffer();
        Iterator iterator = values.iterator();

        while(matcher.find() && iterator.hasNext()) {
            try {
                matcher.appendReplacement(buffer, (String)iterator.next());
            } catch (Exception var7) {
                throw new UtilException(var7);
            }
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public static String replaceFirst(CharSequence text, String regex, String replacement) {
        if (!StrUtil.isBlank(text) && !StrUtil.isBlank(regex) && replacement != null) {
            Pattern pattern = Pattern.compile(regex);
            return replaceFirst(text, pattern, replacement);
        } else {
            return StrUtil.str(text);
        }
    }
}