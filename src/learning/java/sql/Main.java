package learning.java.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final static String URL = System.getenv("DB_URL");
    private final static String USER = System.getenv("DB_USER");
    private final static String PASSWORD = System.getenv("DB_PASSWORD");
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Cosa vuoi cercare: ");
        String userChoice = scan.next();

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)){
            String query = """
                    SELECT countries.name, countries.country_id, regions.name, continents.name FROM countries
                    INNER JOIN regions on countries.region_id = regions.region_id
                    INNER JOIN continents on regions.continent_id = continents.continent_id
                    WHERE countries.name LIKE ?
                    ORDER BY countries.name;
                    """;

            try(PreparedStatement ps = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

                ps.setString(1,"%"+userChoice+"%");

                try(ResultSet rs = ps.executeQuery()){

                    if(!rs.next()){
                        System.out.println("Non ci sono risultati per " + userChoice);
                    } else {
                        rs.beforeFirst();
                    }

                    while (rs.next()){
                        String regionName = rs.getString(1);
                        int countryId = rs.getInt(2);
                        String continentName = rs.getString(3);
                        String countryName = rs.getString(4);

                        System.out.println(regionName + " " + countryId + " " + continentName + " " + countryName);
                    }
                }
            }
        } catch (SQLException e) {
           e.printStackTrace();
        }

    }
}

/*MILESTONE 1
        Creare un nuovo database in DBeaver e importare lo schema in allegato.
        Scrivere una query SQL che restituisca la lista di tutte le nazioni mostrando nome, id,
        nome della regione e nome del continente, ordinata per nome della nazione.

        MILESTONE 2
        Creare un progetto Java e includere nelle librerie il MySQLConnector/J. Nel progetto creare un
        programma che esegua la query della Milestone 1 e stampi a video il risultato.

        MILESTONE 3
        Modificare il programma precedente per fare in modo che un utente possa inserire una ricerca e
        filtrare i risultati:
        - chiedere all’utente di inserire una stringa di ricerca da terminale
        - usare quella stringa come parametro aggiuntivo della query in
        modo che i risultati vengano filtrati con un contains (ad esempio se l’utente cerca per “ita”,
        il risultato della query conterrà sia Italy che Mauritania.

        BONUS
         Dopo aver stampato a video l’elenco delle country, chiedere all’utente di inserire l’id di
         una delle country.
        Sulla base di quell’id eseguire ulteriori ricerche su database, che restituiscano:
        ● tutte le lingue parlate in quella country
        ● le statistiche più recenti per quella country
        Stampare a video i risultati.*/;