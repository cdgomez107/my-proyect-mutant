package meli.dbo;

import io.agroal.api.AgroalDataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;

@ApplicationScoped
public class DatabaseUtils {
    @Inject
    AgroalDataSource dataSource;

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    public void saveInformation(String[] dna, boolean isMutant) throws SQLException {
        try{
            connection = dataSource.getConnection();
            preparedStatement = connection
                    .prepareStatement("INSERT INTO HUMANS (dna_human,is_mutant) values (?,?)");
            preparedStatement.setString(1, Arrays.toString(dna));
            preparedStatement.setBoolean(2, isMutant);
            preparedStatement.execute();
        }catch (Exception e){
            throw e;
        }finally {
            close();
        }
    }

    public HashMap<String, String> readDatabase() throws SQLException {
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement
                    .executeQuery("SELECT is_mutant,count(*) FROM HUMANS group by is_mutant;");
            HashMap<String, String> list = new HashMap<>();
            if(resultSet.next()) {
                do {
                    list.put(resultSet.getString("is_mutant"),
                            resultSet.getString("count(*)"));
                } while(resultSet.next());
            }
            return list;
        }catch (Exception e){
            throw e;
        }finally {
            close();
        }
    }

    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {

        }
    }

}
