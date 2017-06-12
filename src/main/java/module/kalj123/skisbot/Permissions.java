package module.kalj123.skisbot;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

/**
 * Created by Kale on 13/06/2017.
 */
public class Permissions {

    private List<IRole> roles;
    private List<IUser> users;

    private IRole getHighestRole(IGuild guild) {
        setRolesAndUsers(guild);
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

    private void setRolesAndUsers(IGuild guild) {
        roles = guild.getRoles();
        users = guild.getUsers();
    }
}
