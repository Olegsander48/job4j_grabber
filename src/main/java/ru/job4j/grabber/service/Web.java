package ru.job4j.grabber.service;

import io.javalin.Javalin;
import ru.job4j.grabber.stores.Store;

public class Web {
    private final Store store;

    public Web(Store store) {
        this.store = store;
    }

    public void start(int port) {
        var app = Javalin.create(javalinConfig -> javalinConfig.http.defaultContentType = "text/html; charset=UTF-8");
        app.start(port);
        var page = new StringBuilder();
        store.getAll().forEach(post -> page.append(post.toString()).append(System.lineSeparator()));
        app.get("/", ctx -> ctx.result(page.toString()));
    }
}
