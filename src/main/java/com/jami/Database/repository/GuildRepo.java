package com.jami.Database.repository;

import java.util.List;

import com.jami.Database.Guild.GuildPunishmentRecord;
import com.jami.Database.Guild.GuildRecord;
import com.jami.Database.Guild.GuildUserRecord;
import com.jami.Database.Guild.GuildWordRecord;

public interface GuildRepo {
  // Guild
  GuildRecord getById(long id);
  void setCount(long id, long newValue);
  void setHighestCount(long id, long newValue);
  void save(GuildRecord record);

  // Guild User
  GuildUserRecord getUserById(long guildId, long userId);
  void updateLastMessage(long guildId, long userId);
  void incrementExp(long guildId, long userId, int exp);
  void incrementLevel(long guildId, long userId);
  void saveUser(GuildUserRecord record);

  // Guild Words
  GuildWordRecord getGuildWordByWord(long guildId, String word);
  List<GuildWordRecord> getWords(long guildId, int amount, int page, String order, boolean reverseOrder);
  void incrementWord(long guildId, String word);
  void saveWord(GuildWordRecord record);

  // Guild Punishments
  GuildPunishmentRecord getPunishmentById(String id);
  List<GuildPunishmentRecord> getPunishmentsByUser(long guildId, long userId, int page, int amount, String order, boolean reverseOrder);
  List<GuildPunishmentRecord> getPunishmentsByGuild(long guildId, int page, int amount, String order, boolean reverseOrder);
  void savePunishment(GuildPunishmentRecord record);
}
