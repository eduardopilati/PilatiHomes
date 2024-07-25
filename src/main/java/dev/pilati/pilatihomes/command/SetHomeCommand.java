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

import dev.pilati.pilatihomes.PilatiHomes;
import dev.pilati.pilatihomes.model.Home;
import dev.pilati.pilatihomes.services.HomeServices;

public class SetHomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("pilatihomes.sethome")) {
            sender.sendMessage("§cVocê não tem permissão para executar este comando.");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cEste comando só pode ser executado por jogadores.");
            return true;
        }

        Home home = new Home();
        home.setOwner(player.getUniqueId());
        home.setLocation(player.getLocation());

        if (args.length > 0) {
            home.setName(args[0]);
        }

        try {
            HomeServices services = new HomeServices();
            services.setHome(home);
            sender.sendMessage("§aHome [" + home.getName() + "] definida com sucesso.");
        } catch (SQLException e) {
            sender.sendMessage("§cOcorreu um erro ao definir a home.");
            PilatiHomes.getInstance().getLogger().log(Level.SEVERE, "Error while trying to set home for player " + player.getName(), e);
        }
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> parameters = new ArrayList<>();
        
        if( args.length > 0 ) {
            parameters.add("[Nome]");
        }

        return parameters;
    }
}
