package dev.lallemand.lcore.chat.filter.impl;

import dev.lallemand.lcore.chat.filter.ChatFilter;
import dev.lallemand.lcore.lCore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: does not flag
public class LinkFilter extends ChatFilter {

    private static final Pattern URL_REGEX = Pattern.compile(
        "^(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-.][a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?$");
    private static final Pattern IP_REGEX = Pattern.compile(
        "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])([.,])){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

    public LinkFilter(String command) {
        super(command);
    }

    @Override
    public boolean isFiltered(String message, String[] words) {
        for (String word : message.replace("(dot)", ".").replace("[dot]", ".").trim().split(" ")) {
            boolean continueIt = false;

            for (String phrase : lCore.get().getChat().getLinkWhitelist()) {
                if (word.toLowerCase().contains(phrase)) {
                    continueIt = true;
                    break;
                }
            }

            if (!continueIt) {
                Matcher matcher = IP_REGEX.matcher(word);

                if (matcher.matches()) {
                    return true;
                }

                matcher = URL_REGEX.matcher(word);

                if (matcher.matches()) {
                    return true;
                }
            }
        }

        return false;
    }

}
