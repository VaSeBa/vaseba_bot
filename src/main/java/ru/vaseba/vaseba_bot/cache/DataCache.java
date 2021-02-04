package ru.vaseba.vaseba_bot.cache;

import ru.vaseba.vaseba_bot.botapi.BotState;
import ru.vaseba.vaseba_bot.botapi.handlers.fillingprofile.UserProfileData;

public interface DataCache {
    void setUsersCurrentBotState (int userId, BotState botState);

    BotState getUserCurrentBotState (int userId);

    UserProfileData getUserProfileData (int userId);

    void saveUserProfileData (int userId, UserProfileData userProfileData);
}
