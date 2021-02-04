package ru.vaseba.vaseba_bot.botapi.handlers.askdestiny;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.vaseba.vaseba_bot.botapi.BotState;
import ru.vaseba.vaseba_bot.botapi.InputMessageHandler;
import ru.vaseba.vaseba_bot.cache.UserDataCache;
import ru.vaseba.vaseba_bot.service.ReplyMessageService;

@Slf4j
@Component
public class AskDestinyHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessageService messageService;

    public AskDestinyHandler(UserDataCache userDataCache,
                             ReplyMessageService messageService) {
        this.userDataCache = userDataCache;
        this.messageService = messageService;
    }


    @Override
    public SendMessage handler(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_DESTINY;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        int useId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        SendMessage replyToUser = messageService.getReplyMessage(chatId, "reply.askDestiny");
        userDataCache.setUsersCurrentBotState(useId, BotState.PROFILE_FILLED);

        return replyToUser;
    }


}
