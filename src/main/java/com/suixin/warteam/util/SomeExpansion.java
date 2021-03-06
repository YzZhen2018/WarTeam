package com.suixin.warteam.util;

import com.suixin.warteam.WarTeam;
import com.suixin.warteam.entity.WarTeamMemberEntity;
import com.suixin.warteam.handler.WarTeamMemBerDatabaseHandler;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * This class will be registered through the register-method in the plugins
 * onEnable-method.
 */
public class SomeExpansion extends PlaceholderExpansion {

    private WarTeam plugin;

    /**
     * Since we register the expansion inside our own plugin, we can simply use
     * this method here to get an instance of our plugin.
     *
     * @param plugin The instance of our plugin.
     */
    public SomeExpansion(WarTeam plugin) {
        this.plugin = plugin;
    }

    /*
     *上面是使用构造函数传入插件主类实例
     *主类实例的传入方法很多
     *这里不多作讲解了
     */
    /**
     * Because this is an internal class, you must override this method to let
     * PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * 这里指是不是要让papi扩展持久化
     * 不持久化则使用reload指令之后你的变量就没了
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist() {
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed and we can
     * simply return {@code true}
     *
     * 不用管这个，直接return true就可以了
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * <br>For convienience do we return the author from the plugin.yml
     *
     * papi扩展的作者，这里使用的是插件的作者
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest method to
     * obtain a value if a placeholder starts with our identifier.
     * <br>This must be unique and can not contain % or _
     *
     * 对于%someplugin_placeholder1%，这里需要返回someplugin
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier() {
        return "WarTeam";
    }

    /**
     * This is the version of the expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * For convienience do we return the version from the plugin.yml
     *
     * papi扩展的版本，和plugin.yml当中版本的版本号规范是一样的
     * 这边方便一点使用插件作者
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier is found
     * and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * 真正实现papi变量返回值的地方
     *
     * @param player A {@link Player Player}.
     * @param identifier A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return "";
        }
        WarTeamMemberEntity warTeamMemberEntity = WarTeamMemBerDatabaseHandler.selectWarTeamMemBerByUid(player.getName());
        if (warTeamMemberEntity.getId() == null) {
            return "-";
        }
        if (identifier.equals("name")) {
            return warTeamMemberEntity.getWarTeamName();
        }
        return null;
    }
}