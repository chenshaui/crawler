package com.webmajic.task;


import java.util.ArrayList;
import java.util.List;

public class MessageUtil {

    public List<String> message(String s) {
        //String s = "武汉-洪山区    5-7年经验    大专    招5人    02-03发布";
        String s1 = "\\u00A0" + "\\u00A0" + "\\u00A0" + "\\u00A0";
        String[] split = s.split(s1);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            list.add(split[i]);
        }
        return list;
    }
}
