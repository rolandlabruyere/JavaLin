package org.restserver;

import org.apache.commons.lang3.StringUtils;

import java.sql.*;

public class DbConnect {
    static String[] connectionUrl = {"jdbc:mysql://localhost:3306/voorthuiscustomersales", "jdbc:mysql://localhost:3306/voorthuishtmlpages"};
    static Connection conn;
    FuncsAndProcs fps = new FuncsAndProcs();

    public void connect(int index) throws SQLException {
       conn = DriverManager.getConnection(connectionUrl[index], "root", "w2YNcM01||P3n1sl@nD01");
    }

    public void execSql(String icudQuery, String valueString) throws SQLException {
        int count = StringUtils.countMatches(icudQuery, "?");
        String[] values = valueString.split(";");

        if (values.length != count){
            throw new RuntimeException("The number of query parameters doesn't match the number of passed values.");
        }

        PreparedStatement ps = conn.prepareStatement(icudQuery);

        for(int t = 0; t < values.length; t++) {
            ps.setString(t + 1, values[t]);
        }

        assert ps != null;
        try {
            ps.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public ResultSet openSql(String selectQuery, String valueString) throws SQLException {
        int count = StringUtils.countMatches(selectQuery, "?");
        String[] values = valueString.split(";");

        if (values.length != count){
            throw new RuntimeException("The number of query parameters doesn't match the number of passed values.");
        }

        PreparedStatement ps = conn.prepareStatement(selectQuery);
        for(int t = 0; t < values.length; t++) {
            ps.setString(t + 1, values[t]);
        }
        assert ps != null;
        return ps.executeQuery();
    }

    public String fetchSql(String selectQuery, String value, String columnLabel) throws Throwable {
        PreparedStatement ps = conn.prepareStatement(selectQuery);
        ps.setString(1, value);

        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getString(columnLabel);
        }catch (SQLException e){
            return e.getMessage();
        }
    }
    public void close() throws SQLException {
        conn.close();
    }
}
