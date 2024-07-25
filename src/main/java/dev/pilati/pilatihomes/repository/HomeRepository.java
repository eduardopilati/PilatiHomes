package dev.pilati.pilatihomes.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.World;

import dev.pilati.pilatihomes.PilatiHomes;
import dev.pilati.pilatihomes.model.Home;

public class HomeRepository extends Repository<Integer, Home> {

    private Home convert(ResultSet resultSet) throws SQLException {
        Home home = new Home();
        home.setId(resultSet.getInt("id"));
        home.setOwner(UUID.fromString(resultSet.getString("player_uuid")));
        home.setName(resultSet.getString("name"));

        World world = PilatiHomes.getInstance().getServer().getWorld(resultSet.getString("location_world"));
        Location location = new Location(
            world,
            resultSet.getDouble("location_x"),
            resultSet.getDouble("location_y"),
            resultSet.getDouble("location_z"),
            resultSet.getFloat("location_yaw"),
            resultSet.getFloat("location_pitch")
        );

        home.setLocation(location);
        return home;
    }

    @Override
    public Home find(Integer id) throws SQLException {
        try {
            return (Home) executeQuery(
                "SELECT * FROM pilatihomes_homes WHERE id = ?", 
                statement -> {
                    statement.setInt(1, id);
                    return null;
                },
                resultSet -> {
                    if (resultSet.next()) {
                        return convert(resultSet);
                    }
                    return null;
                }
            );
        } catch (SQLException e) {
            PilatiHomes.getInstance().getLogger().log(Level.SEVERE, "Error while trying to find home with id " + id, e);
            throw e;
        } 
    }

    @SuppressWarnings("unchecked")
    public List<Home> find(UUID owner) throws SQLException {
        try {
            return (List<Home>) executeQuery(
                "SELECT * FROM pilatihomes_homes WHERE player_uuid = ?", 
                statement -> {
                    statement.setString(1, owner.toString());
                    return null;
                },
                resultSet -> {
                    List<Home> homes = new ArrayList<>(resultSet.getFetchSize());
                    while (resultSet.next()) {
                        homes.add(convert(resultSet));
                    }
                    return homes;
                }
            );
        } catch (SQLException e) {
            PilatiHomes.getInstance().getLogger().log(Level.SEVERE, "Error while trying to find homes for player " + owner, e);
            throw e;
        }
    }

    public Home find(String nome, UUID owner) throws SQLException {
        try {
            return (Home) executeQuery(
                "SELECT * FROM pilatihomes_homes WHERE name = ? AND player_uuid = ?", 
                statement -> {
                    statement.setString(1, nome);
                    statement.setString(2, owner.toString());
                    return null;
                },
                resultSet -> {
                    if (resultSet.next()) {
                        return convert(resultSet);
                    }
                    return null;
                }
            );
        } catch (SQLException e) {
            PilatiHomes.getInstance().getLogger().log(Level.SEVERE, "Error while trying to find home with name " + nome + " for player " + owner, e);
            throw e;
        }
    }

    protected void update(Home model) throws SQLException {
        try {
            executeUpdate(
                "UPDATE pilatihomes_homes SET name = ?, location_world = ?, location_x = ?, location_y = ?, location_z = ?, location_yaw = ?, location_pitch = ? WHERE id = ?", 
                statement -> {
                    statement.setString(1, model.getName());
                    statement.setString(2, model.getLocation().getWorld().getName());
                    statement.setDouble(3, model.getLocation().getX());
                    statement.setDouble(4, model.getLocation().getY());
                    statement.setDouble(5, model.getLocation().getZ());
                    statement.setFloat(6, model.getLocation().getYaw());
                    statement.setFloat(7, model.getLocation().getPitch());
                    statement.setInt(8, model.getId());
                    return null;
                }
            );
        } catch (SQLException e) {
            PilatiHomes.getInstance().getLogger().log(Level.SEVERE, "Error while trying to update home with id " + model.getId(), e);
            throw e;
        }
    }

    protected void insert(Home model) throws SQLException {
        try {
            Integer id = executeInsert(
                "INSERT INTO pilatihomes_homes (player_uuid, name, location_world, location_x, location_y, location_z, location_yaw, location_pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", 
                statement -> {
                    statement.setString(1, model.getOwner().toString());
                    statement.setString(2, model.getName());
                    statement.setString(3, model.getLocation().getWorld().getName());
                    statement.setDouble(4, model.getLocation().getX());
                    statement.setDouble(5, model.getLocation().getY());
                    statement.setDouble(6, model.getLocation().getZ());
                    statement.setFloat(7, model.getLocation().getYaw());
                    statement.setFloat(8, model.getLocation().getPitch());
                    return null;
                },
                resultSet -> {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                    return null;
                }
            );

            model.setId(id);
        } catch (SQLException e) {
            PilatiHomes.getInstance().getLogger().log(Level.SEVERE, "Error while trying to insert home for player " + model.getOwner(), e);
            throw e;
        }
    }

    @Override
    public void save(Home model) throws SQLException {
        if (model.getId() != null) {
            update(model);
            return;
        }
        
        insert(model);
    }

    @Override
    public void delete(Home model) throws SQLException {
        try {
            executeUpdate(
                "DELETE FROM pilatihomes_homes WHERE id = ?", 
                statement -> {
                    statement.setInt(1, model.getId());
                    return null;
                }
            );
        } catch (SQLException e) {
            PilatiHomes.getInstance().getLogger().log(Level.SEVERE, "Error while trying to delete home with id " + model.getId(), e);
            throw e;
        }
    }
    
}
