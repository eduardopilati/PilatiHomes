package dev.pilati.pilatihomes.command;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.pilati.pilatihomes.PilatiHomes;
import dev.pilati.pilatihomes.model.Home;
import dev.pilati.pilatihomes.services.HomeServices;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class HomesCommand implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("pilatihomes.home")) {
            sender.sendMessage("§cVocê não tem permissão para executar este comando.");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cEste comando só pode ser executado por jogadores.");
            return true;
        }

        List<Home> homes;
        try {
            homes = new HomeServices().getHomes(player);
        } catch (SQLException e) {
            sender.sendMessage("§cOcorreu um erro ao buscar as homes.");
            PilatiHomes.getInstance().getLogger().log(Level.SEVERE, "Error while trying to get homes for player " + player.getName(), e);
            return true;
        }

        if (homes.isEmpty()) {
            sender.sendMessage("§cVocê não possui nenhuma home.");
            return true;
        }

        sender.sendMessage("§a ----- [Homes] -----");
        homes.forEach(home -> {
            TextComponent message = new TextComponent("§7- " + home.getName());
            message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/home " + home.getName()));
            player.spigot().sendMessage(message);
        });
        sender.sendMessage("§a ------------------");

        return true;
    }

}
