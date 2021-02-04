package ru.vaseba.vaseba_bot.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.vaseba.vaseba_bot.botapi.BotState;
import ru.vaseba.vaseba_bot.botapi.handlers.fillingprofile.UserProfileData;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class UserDataCache implements DataCache{
    private Map<Integer, BotState> userBotState = new HashMap<>();
    private Map<Integer, UserProfileData> userProfileDataMap = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(int userId, BotState botState) {
        userBotState.put(userId, botState);
    }

    @Override
    public BotState getUserCurrentBotState(int userId) {
        BotState botState = userBotState.get(userId);
        if (botState == null) {
            botState = BotState.ASK_DESTINY;
        }
        return botState;
    }

    @Override
    public UserProfileData getUserProfileData(int userId) {
        UserProfileData userProfileData = userProfileDataMap.get(userId);
        if (userProfileData == null) {
            userProfileData = new UserProfileData();
        }
        return userProfileData;
    }

    @Override
    public void saveUserProfileData(int userId, UserProfileData userProfileData) {
        userProfileDataMap.put(userId, userProfileData);
    }
}
