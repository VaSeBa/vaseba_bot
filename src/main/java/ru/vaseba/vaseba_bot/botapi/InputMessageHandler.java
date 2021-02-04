package ru.vaseba.vaseba_bot.botapi;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


public interface InputMessageHandler {
    SendMessage handler(Message message);

    BotState getHandlerName();
}
