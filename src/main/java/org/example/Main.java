package org.example;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.command.DbMigrate.FlywayMigrateException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String DB_USER = "postgres";
    static final String DB_PASS = "2468";
    static final Flyway flyway = Flyway.configure().dataSource(DB_URL,DB_USER,DB_PASS).load();

    static final Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        }catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) {
        try {
            flyway.migrate();
        }catch (FlywayMigrateException e){
            System.exit(1);
        }

        while(true){
            System.out.println("Input:");
            final String line = new Scanner(System.in).nextLine();
            final String[] words = line.split("\\s");

            if(words.length < 3){
                System.out.println("please enter at least three words!!");
                break;
            }
            try {
                connection.createStatement().execute(String.format("INSERT INTO PERSON (id, name, phone_number,modified_timestamp) VALUES (%s, %s, %s, NOW())",
                       words[0], words[1], words[2] ));
                System.out.println("Inserted");
            } catch (SQLException e) {
                System.out.println("Error occured"+ e);
             }
        }

    }
}