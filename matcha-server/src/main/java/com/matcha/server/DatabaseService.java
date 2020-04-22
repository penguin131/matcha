package com.matcha.server;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;

@Controller
public class DatabaseService {

    //todo фабрику подключений
    @Resource
    private DataSource dataSource;
    private Connection connection;

    @PostConstruct
    private void init() throws ClassNotFoundException, SQLException {
        HikariDataSource dSource = (HikariDataSource) dataSource;
        Class.forName(dSource.getDriverClassName());
        connection = DriverManager.getConnection(
                dSource.getJdbcUrl(),
                dSource.getUsername(),
                dSource.getPassword()
        );
    }

    @RequestMapping(value = "/kek")
    @ResponseBody
    public String sendGet() throws SQLException {

        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("select * from my_db.super_table");
        StringBuilder sb = new StringBuilder();

        while (rs.next()) {
            sb.append(String.format("%s:%s;", rs.getString("number"), rs.getString("name")));
        }

        return sb.toString();
    }
}
