package com.jami.utilities.guildAdmin;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.jami.database.guild.guild;
import com.jami.database.guild.guildSettings.guildSettings;
import com.jami.database.guild.guildSettings.levellingRoles.levellingRole;

public class settings {
  private guild g;
  private guildSettings gs;

  public settings(long guildId) {
    this.g = new guild(guildId);
    this.gs = g.getSettings();
  }

  public void setExpSettings(int newExpInc, int newExpVar, long newExpCool) {
    gs.setExpIncrement(newExpInc);
    gs.setExpVariation(newExpVar);
    gs.setExpCooldown(newExpCool);

    g.commit();
  }

  public void setLevelSettings(long newLevelBase, double newLevelGrowth) {
    gs.setLevelBase(newLevelBase);
    gs.setLevelGrowth(newLevelGrowth);

    g.commit();
  }

  public void addLevellingRole(long roleId, int level) {
    levellingRole lr = new levellingRole(roleId, level);
    gs.addRoleLevel(level);
    gs.addLevellingRole(lr.toDocument());

    g.commit();
  }

  public boolean removeLevellingRole(String id) {
    ObjectId oid = new ObjectId(id);
    List<Document> lrs = gs.getLevellingRoles();
    for (Document doc : lrs) {
      if (doc.getObjectId("_id") == oid) {
        lrs.remove(doc);
        g.commit();
        return true;
      }
    }
    return false;
  }

  public List<Document> listLevellingRoles() {
    return gs.getLevellingRoles();
  }

  public void addWordsDisabledChannel(long id) {
    gs.addWordsDisabledChannel(id);
  }

  public void removeWordsDisabledChannel(long id) {
    gs.removeWordsDisabledChannel(id);
  }

  public List<Long> getWordsDisabledChannels() {
    return gs.getWordsDisabledChannels();
  }
}
