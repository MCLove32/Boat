package azisaba.net.boat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BoatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!commandSender.hasPermission("boat.command.boat")) {
            commandSender.sendMessage(Component.text("権限がありません。", NamedTextColor.RED));
            return true;
        }
        Boat.get().reloadConfig();
        return true;
    }
}
