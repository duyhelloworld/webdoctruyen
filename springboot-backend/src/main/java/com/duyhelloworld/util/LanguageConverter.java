package com.duyhelloworld.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class LanguageConverter {
    public static String convert(String vietnameseString) {
        String normalizedString = Normalizer.normalize(vietnameseString, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String englishString = pattern.matcher(normalizedString).replaceAll("");
        return englishString.replaceAll(" ", "_");
    }
}
