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

public class DelHomeCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("pilatihomes.delhome")) {
			sender.sendMessage("Você não tem permissão para executar este comando.");
			return true;
		}

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cEste comando só pode ser executado por jogadores.");
            return true;
        }

		HomeServices services = new HomeServices();
        String homeName = HomeServices.DEFAULT_HOME_NAME;
        if (args.length > 0) {
            homeName = args[0];
        }

        try {
            Home home = services.getHome(homeName, player);

            if (home == null) {
                sender.sendMessage("§cHome [" + homeName + "] não encontrada.");
                return true;
            }

            services.deleteHome(home);
            sender.sendMessage("§aHome [" + home.getName() + "] deletada com sucesso.");
        } catch (SQLException e) {
            sender.sendMessage("Ocorreu um erro ao deletar a home.");
            PilatiHomes.getInstance().getLogger().log(Level.SEVERE, "Error while trying to delete home for player " + player.getName(), e);
        }

		return true;
	}

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> parameters = new ArrayList<>();

        if (!sender.hasPermission("pilatihomes.sethome")) {
            return parameters;
        }

        if (!(sender instanceof Player player)) {
            return parameters;
        }

        try {
            HomeServices services = new HomeServices();

            StringUtil.copyPartialMatches(
                args[0], 
                services.getHomes(player)
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
