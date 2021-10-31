package com.itechart.finnhubapi;

import com.itechart.finnhubapi.initializer.InitMySQL;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


//@Sql("/sql/data.sql")
//@ContextConfiguration(initializers = {
//        InitMySQL.Initializer.class
//})
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public abstract class IntegrationTestBase {

//    @BeforeAll
//    static void init() {
//        InitMySQL.container.start();
//    }

}