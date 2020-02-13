package edu.fgcu.eagle.cfbotelho9922.cvstodatabase;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Main {
    public static void main(String[] args) {

        //Iteration 1
        try {
            CSVReader r = new CSVReader(new FileReader("SEOExample.csv"));
            for(String[] line : r.readAll()){
                for (String field : line){
                    System.out.println(field + " , ");
                }
            }
            r.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (CsvException e) {
            System.out.println(e.getMessage());
        }


        //Iteration 2
        try {
            Connection conn = DriverManager.getConnection("BookData.db");

            CSVReader r = new CSVReader(new FileReader("bookstore_report2"));
            PreparedStatement statement = conn.prepareStatement("INSERT INTO book(isbn, book_title, author_name) VALUES (?,?,?)");
            for (String[] line : r.readAll()){
                String isbn = line[0];
                String title = line[1];
                String author = line[2];
                statement.setString(1, isbn);
                statement.setString(2, title);
                statement.setString(3, author);
                statement.execute();
            }
            statement.close();

            Gson gson = new Gson();
            JsonReader jread = new JsonReader(new FileReader("src/Data/authors.json"));
            AuthorParser[] authors = gson.fromJson(jread, AuthorParser[].class);

            PreparedStatement statement1 = conn.prepareStatement("INSERT INTO author(author_name, author_email, author_url) VALUES (?,?,?)");
            for (AuthorParser element : authors) {
                statement1.setString(1,element.getName());
                statement1.setString(2,element.getEmail());
                statement1.setString(3,element.getUrl());
                statement1.execute();
            }
            statement1.close();
            conn.close();
            r.close();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (CsvException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
}
