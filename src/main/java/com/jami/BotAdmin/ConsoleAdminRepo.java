package com.jami.BotAdmin;

import com.jami.App;

public class ConsoleAdminRepo implements AdminRepo {

  @Override
  public void createConfig(String[] args) {}

  @Override
  public void setConfig(String[] args) {}

  @Override
  public void loadConfig(String[] args) {}

  @Override
  public void saveConfig() {}

  @Override
  public void showConfig(String[] args) {}

  @Override
  public void showConfigConsole() {}

  @Override
  public void setConfigName(String[] args) {}

  @Override
  public void setBotStatus(String[] args) {}

  @Override
  public void setBotColor(String[] args) {}

  @Override
  public void setExpIncrement(String[] args) {}

  @Override
  public void setExpVariation(String[] args) {}

  @Override
  public void setExpCooldown(String[] args) {}

  @Override
  public void setLevelBase(String[] args) {}

  @Override
  public void setLevelGrowth(String[] args) {}

  @Override
  public void addDisabledFeature(String[] args) {}

  @Override
  public void removeDisabledFeature(String[] args) {}

  @Override
  public void addDisabledCommand(String[] args) {}

  @Override
  public void removeDisabledCommand(String[] args) {}

  @Override
  public void addAdmin(String[] args) {}

  @Override
  public void removeAdmin(String[] args) {}

  @Override
  public void parseWiktionary() {}

  @Override
  public void shutdown() {
    App.shutdown();
  }
}
