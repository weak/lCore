package dev.lallemand.lcore.chat.filter.impl;

import dev.lallemand.lcore.chat.filter.ChatFilter;

import java.util.Arrays;
import java.util.List;

public class ContainsFilter extends ChatFilter {

    // TODO: add more
    private static final List<String> filteredWords = Arrays.asList("puta", "noob");

    public ContainsFilter(String command) {
        super(command);
    }

    @Override
    public boolean isFiltered(String message, String[] words) {
        for (String word : words) {
            for (String filteredWord : filteredWords) {
                if (word.contains(filteredWord)) {
                    return true;
                }
            }
        }

        return false;
    }

}
