package ru.job4j;

import org.apache.log4j.Logger;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.Config;
import ru.job4j.grabber.stores.JdbcStore;
import ru.job4j.grabber.service.SchedulerManager;
import ru.job4j.grabber.service.SuperJobGrab;

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
            var post1 = new Post(
                    "Super Java Job",
                    "https://rabota.by/vacancy/117717563?query=java&hhtmFrom=vacancy_search_list",
                    "Internship in Java with subsequent employment",
                    1742553478000L
            );
            var post2 = new Post(
                    "Super Java Job",
                    "https://hh.ru/vacancy/118078469?query=java",
                    "Internship Java Developer. The best will get their first job in IT!",
                    1742560710L
            );
            store.save(post1);
            store.save(post2);
            var scheduler = new SchedulerManager();
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store);
        } catch (SQLException e) {
            LOG.error("When create a connection", e);
        }
    }
}
