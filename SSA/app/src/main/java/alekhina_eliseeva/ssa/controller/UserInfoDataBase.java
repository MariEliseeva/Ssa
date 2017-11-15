package alekhina_eliseeva.ssa.controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


class UserInfoDataBase {
    private static String URL = "";
    private static String tableName = "userInfo";

    private static ArrayList<RatingLine> makeRating(Connection connection) throws SQLException {
        String query = "select name, score from " + tableName + " order by score";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ArrayList<RatingLine> arrayList = new ArrayList<>();
        while (resultSet.next()) {
            arrayList.add(new RatingLine(resultSet.getString("name"),
                    Integer.parseInt(resultSet.getString("score"))));
        }
        statement.close();
        return arrayList;
    }

    static ArrayList<RatingLine> getRating() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);
        ArrayList<RatingLine> result = makeRating(connection);
        connection.close();
        return result;
    }

    static boolean checkPassword(String name, String password) throws SQLException {
        Connection connection = DriverManager.getConnection(URL);
        boolean result;
        String query = "select name from " + tableName + " where name like '" + name + "'";
        Statement statement = connection.createStatement();;
        if (statement.executeQuery(query).getString("password").compareTo(password) != 0) {
            result = false;
        }
        result = true;
        statement.close();
        connection.close();
        return result;
    }

    static boolean addUser(String name, String password) throws SQLException {
        Connection connection = DriverManager.getConnection(URL);
        boolean result;
        String query = "select name from " + tableName + " where name like '" + name + "'";
        Statement statement = connection.createStatement();;
        if (statement.execute(query)) {
            result = false;
        } else {
            query = "insert into " + tableName + " (name, password, score) values ("
                    + name + ", " + password + ", 0);";
            statement.executeUpdate(query);
            result = true;
        }
        statement.close();
        connection.close();
        return result;
    }

    static void changeScore(String name, int score) throws SQLException {
        Connection connection = DriverManager.getConnection(URL);
        String query = "update " + tableName + " set score = " + score + " where name like '" + name + "'";
        Statement statement = connection.createStatement();;
        statement.executeUpdate(query);
        statement.close();
        connection.close();
    }
}