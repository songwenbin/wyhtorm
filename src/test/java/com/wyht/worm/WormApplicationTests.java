package com.wyht.worm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class WormApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void TestDB() throws SQLException {
        DB db = new DB("test");
        db.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/test", "root", "root", null);
        db.exec("insert into t1 values(30);");

        Model mode = new Model("test", "t1");
        mode.set("id", "1");
        mode.save();
    }
}
