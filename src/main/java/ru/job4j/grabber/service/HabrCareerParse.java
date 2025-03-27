package ru.job4j.grabber.service;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final Logger LOG = Logger.getLogger(HabrCareerParse.class);
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PREFIX = "/vacancies?page=";
    private static final String SUFFIX = "&q=Java%20developer&type=all";
    private static final int VACANCY_PAGES = 5;
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> fetch() {
        var result = new ArrayList<Post>();
        for (int pageNumber = 1; pageNumber <= VACANCY_PAGES; pageNumber++) {
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
            result.addAll(list(fullLink));
        }
        return result;
    }

    @Override
    public List<Post> list(String fullLink) {
        List<Post> posts = new ArrayList<>();
        var connection = Jsoup.connect(fullLink);
        try {
            Document document = connection.get();
            var rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                var titleElement = row.select(".vacancy-card__title").first();
                var linkElement = titleElement.child(0);
                var created = row.select(".vacancy-card__date").first();
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                var post = new Post(
                        vacancyName,
                        link,
                        retrieveDescription(link),
                        dateTimeParser.parse(created.child(0)
                                        .attr("datetime"))
                                .atZone(ZoneId.systemDefault())
                                .toEpochSecond());
                posts.add(post);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }

    private String retrieveDescription(String link) {
        String result;
        try {
            var connection = Jsoup.connect(link);
            Document document = connection.get();
            var rows = document.select(".vacancy-description__text");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                builder.append(rows.get(0).getElementsByTag("h3").get(i).text());
                builder.append(": ");
                builder.append(rows.get(0).select(".style-ugc").get(i).text());
            }
            result = builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void main(String[] args) {
        HabrCareerParse parse = new HabrCareerParse(new HabrCareerDateTimeParser());
        parse.fetch().forEach(System.out::println);
    }
}
