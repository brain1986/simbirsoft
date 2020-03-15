package ru.iprustam.trainee.simbirchat.service.wscom.handler;

import ru.iprustam.trainee.simbirchat.entity.ChatMessage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для преобразования текстовых команд, полученных от пользователя в объект
 */
public class ChatCommand {
    private String command;
    private ChatMessage chatMessage;
    private Map<String, String> params;

    private ChatCommand(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public static ChatCommand createChatCommand(ChatMessage chatMessage) {
        ChatCommand chatCommand = new ChatCommand(chatMessage);
        chatCommand.parse(chatMessage.getMessage());
        return chatCommand;
    }

    public String getCommand() {
        return command;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public String getParam(String paramName) {
        return params.get(paramName);
    }

    public boolean hasParam(String paramName) {
        return params.containsKey(paramName);
    }

    private void parse(String message) {
        params = new LinkedHashMap<>();

        String pattern = "^//(?<commandName>\\w+)((?<params>\\s+.*)|)$";
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(message);
        System.out.println(message);
        if (m.find()) {
            command = m.group("commandName");

            //Parse params and values
            String paramsStr = m.group("params");
            if (paramsStr != null) {
                pattern = "(?<name>\\s+(\\w+|-\\w{1}))(((\\s+(?!-)((\\{(?<value1>.*)\\})|(?<value2>[\\S]+)))|))";

                Pattern p2 = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                Matcher m2 = p2.matcher(paramsStr);
                while (m2.find()) {
                    String value = m2.group("value1") != null ? m2.group("value1") :  m2.group("value2");
                    params.put(
                            m2.group("name").trim().replace("-", ""),
                            value
                    );
                }
            }
        }
    }
}
