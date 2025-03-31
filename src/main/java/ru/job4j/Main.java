package ru.job4j;

import org.apache.log4j.Logger;
import ru.job4j.grabber.service.*;
import ru.job4j.grabber.stores.JdbcStore;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) throws ClassNotFoundException {
        var config = new Config();
        config.load("src/main/resources/application.properties");
        try {
            Class.forName(config.get("db.driver-class-name"));
            var connection = DriverManager.getConnection(
                    config.get("db.url"),
                    config.get("db.username"),
                    config.get("db.password"));
            var store = new JdbcStore(connection);
            HabrCareerParse parse = new HabrCareerParse(new HabrCareerDateTimeParser());
            parse.fetch().forEach(store::save);
            var scheduler = new SchedulerManager();
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store);
            new Web(store).start(Integer.parseInt(config.get("server.port")));
        } catch (SQLException e) {
            LOG.error("When create a connection", e);
        }
    }
}
