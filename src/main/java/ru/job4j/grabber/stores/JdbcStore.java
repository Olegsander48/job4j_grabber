package ru.job4j.grabber.stores;

import ru.job4j.grabber.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcStore implements Store {
    private final Connection connection;

    public JdbcStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO agregator.post(title, description, link, created) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getDescription());
            preparedStatement.setString(3, post.getLink());
            preparedStatement.setTimestamp(4, new Timestamp(post.getTime()));
            preparedStatement.executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> postsList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("select * from agregator.post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    postsList.add(createPostFromResultSet(resultSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postsList;
    }

    @Override
    public Optional<Post> findById(Long id) {
        Optional<Post> result = Optional.empty();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM agregator.post WHERE id = (?)")) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    result = Optional.of(createPostFromResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    private Post createPostFromResultSet(ResultSet resultSet) {
        Post element = null;
        try {
            element = new Post(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getString("link"),
                    resultSet.getString("description"),
                    resultSet.getTimestamp("created").getTime()
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return element;
    }
}