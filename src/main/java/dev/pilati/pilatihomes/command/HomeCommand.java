package dev.pilati.pilatihomes.command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import dev.pilati.pilatihomes.PilatiHomes;
import dev.pilati.pilatihomes.model.Home;
import dev.pilati.pilatihomes.services.HomeServices;
import dev.pilati.pilatihomes.services.TeleportServices;

public class HomeCommand implements CommandExecutor, TabCompleter{

    private final HomeServices homeServices = new HomeServices();

    private final TeleportServices teleportServices = new TeleportServices();

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

        String homeName = HomeServices.DEFAULT_HOME_NAME;
        if (args.length > 0) {
            homeName = args[0];
        }

        if(teleportServices.isInCooldown(player)) {
            int remaining = teleportServices.getCooldownRemainingSeconds(player);
            sender.sendMessage("§cVocê precisa esperar " + remaining + "s antes de usar o comando novamente.");
            return true;
        }

        try {
            Home home = homeServices.getHome(homeName, player);
            
            if (home == null) {
                sender.sendMessage("§cHome [" + homeName + "] não encontrada.");
                return true;
            }

            teleportServices.teleportToHome(home, player);
        } catch (SQLException e) {
            sender.sendMessage("§cOcorreu um erro ao buscar a home.");
            PilatiHomes.getInstance().getLogger().log(Level.SEVERE, "Error while trying to get home for player " + player.getName(), e);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> parameters = new ArrayList<>();

        if (!sender.hasPermission("pilatihomes.home")) {
            return parameters;
        }

        if (!(sender instanceof Player player)) {
            return parameters;
        }

        try {
            StringUtil.copyPartialMatches(
                args[0], 
                homeServices.getHomes(player)
                    .stream()
                    .map(home -> home.getName())
                    .toList(),
                parameters
            );
        } catch (SQLException e) {
            PilatiHomes.getInstance().getLogger().log(Level.SEVERE, "Error while trying to set home for player " + player.getName(), e);
        }
        
        return parameters;
    }
    
}
