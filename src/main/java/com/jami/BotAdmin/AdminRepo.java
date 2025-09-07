package com.jami.BotAdmin;

public interface AdminRepo {
  void createConfig(String[] args);

  void setConfig(String[] args);

  void loadConfig(String[] args);

  void saveConfig();

  void showConfig(String[] args);

  void showConfigConsole();

  void setConfigName(String[] args);

  void setBotStatus(String[] args);

  void setBotColor(String[] args);

  void setExpIncrement(String[] args);

  void setExpVariation(String[] args);

  void setExpCooldown(String[] args);

  void setLevelBase(String[] args);

  void setLevelGrowth(String[] args);

  void addDisabledFeature(String[] args);

  void removeDisabledFeature(String[] args);

  void addDisabledCommand(String[] args);

  void removeDisabledCommand(String[] args);

  void addAdmin(String[] args);

  void removeAdmin(String[] args);

  void parseWiktionary();

  void shutdown();
}
