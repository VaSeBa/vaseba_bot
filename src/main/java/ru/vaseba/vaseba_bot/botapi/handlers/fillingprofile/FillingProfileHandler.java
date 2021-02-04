package ru.vaseba.vaseba_bot.botapi.handlers.fillingprofile;

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
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessageService messageService;

    public FillingProfileHandler(UserDataCache userDataCache, ReplyMessageService messageService) {
        this.userDataCache = userDataCache;
        this.messageService = messageService;
    }

    @Override
    public SendMessage handler(Message message) {
        if (userDataCache.getUserCurrentBotState(message.getFrom().getId())
        .equals(BotState.FILLING_PROFILE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(),
                    BotState.ASK_NAME);
        }
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUserCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_NAME)){
            replyToUser = messageService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);
        }
        if (botState.equals(BotState.ASK_AGE)){
            replyToUser = messageService.getReplyMessage(chatId, "reply.askAge");
            profileData.setName(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_GENDER);
        }
        if (botState.equals(BotState.ASK_GENDER)){
            replyToUser = messageService.getReplyMessage(chatId, "reply.askGender");
            profileData.setAge(Integer.parseInt(usersAnswer));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_NUMBER);
        }
        if (botState.equals(BotState.ASK_NUMBER)){
            replyToUser = messageService.getReplyMessage(chatId, "reply.askNumber");
            profileData.setGender(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_NUMBER);
        }
        if (botState.equals(BotState.ASK_COLOR)){
            replyToUser = messageService.getReplyMessage(chatId, "reply.askColor");
            profileData.setNumber(Integer.parseInt(usersAnswer));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_MOVIE);
        }
        if (botState.equals(BotState.ASK_MOVIE)){
            replyToUser = messageService.getReplyMessage(chatId, "reply.askMovie");
            profileData.setColor(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_SONG);
        }
        if (botState.equals(BotState.ASK_SONG)){
            replyToUser = messageService.getReplyMessage(chatId, "reply.askSong");
            profileData.setMovie(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
        }
        if (botState.equals(BotState.PROFILE_FILLED)){
            profileData.setSong(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_DESTINY);
            replyToUser = new SendMessage(chatId, String.format("%s %s", "Данные по вашей анкете", profileData));
        }

        userDataCache.saveUserProfileData(userId, profileData);

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }
}
