package ru.vaseba.vaseba_bot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.vaseba.vaseba_bot.cache.UserDataCache;

@Component
@Slf4j
public class TelegramFacade {
    private static final Logger log =org.slf4j.LoggerFactory.getLogger(TelegramFacade.class);
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
    }

    public SendMessage handlerUpdate(Update update) {
        SendMessage replyMessage = null;

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId:{}, with:{}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String  inputMsg = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start" :
                botState = BotState.ASK_DESTINY;
                break;
            case "Получить предсказание" :
                botState = BotState.FILLING_PROFILE;
                break;
            case "Помощь" :
                botState = BotState.SHOW_HELP_MENU;
                break;
            default:
                botState = userDataCache.getUserCurrentBotState(userId);
                break;
        }
        userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

}
