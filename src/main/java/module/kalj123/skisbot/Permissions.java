package module.kalj123.skisbot;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

/**
 * Project: skisbot
 * Created by Kale on 13/06/2017 at 3:07 AM.
 */
public class Permissions {

    private IRole getHighestRole(IGuild guild) {
        List<IUser> users = guild.getUsers();
        IRole highestRole = users.get(0).getRolesForGuild(guild).get(0);
        for (IUser user: users) {
            for (IRole role: user.getRolesForGuild(guild)) {
                if (role.getPosition() > highestRole.getPosition()) {
                    highestRole = role;
                }
            }
        }
        return highestRole;
    }

    public boolean isUserHighestRole(IUser user, IGuild guild) {
        IRole highestRole = getHighestRole(guild);
        for (IRole role: user.getRolesForGuild(guild)) {
            if (role == highestRole && highestRole != guild.getEveryoneRole()) {
                return true;
            } else {
                if (user == guild.getOwner()) {
                    return true;
                }
            }
        }
        return false;
    }
}
