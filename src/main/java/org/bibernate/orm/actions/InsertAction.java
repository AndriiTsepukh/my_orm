package org.bibernate.orm.actions;

import org.bibernate.orm.exception.ORMException;
import org.bibernate.orm.helpers.AnnotationsHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.stream.Collectors;

public class InsertAction implements Action {

    Connection connection;
    Object object;
    public InsertAction(Connection connection, Object object) {
        this.connection = connection;
        this.object = object;
    }

    @Override
    public void execute() {
        String tableName = AnnotationsHelper.getTableName(object);
        var columns = AnnotationsHelper.getColumns(object);
        String columnNames = String.join(",", columns.keySet());
        String valuesPlaceholders = columns.keySet().stream().map(v -> "?").collect(Collectors.joining(","));
        try {
            var preparedStatement = connection.prepareStatement("INSERT INTO " + tableName
                    + " (" + columnNames +") VALUES (" + valuesPlaceholders  + ")", Statement.RETURN_GENERATED_KEYS);
            int i=1;
            for (Map.Entry<String, Object> entry: columns.entrySet()){
//                TODO parse different object types
                preparedStatement.setString(i, (String) entry.getValue());
                i++;
            }
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new ORMException("INSERT operation failed. Number of affected rows: 0");
            }
            var keys = preparedStatement.getGeneratedKeys();
            int id;
            if (keys.next()) {
                id = keys.getInt(1);
                System.out.println("assigned key: " + id);
            } else {
                throw new ORMException("INSERT operation failed. No id obtained.");
            }

            var idField = AnnotationsHelper.getIdColumn(object);
            idField.setAccessible(true);
            idField.set(object, id);
            connection.commit();
        } catch (SQLException | IllegalAccessException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new ORMException(ex.getMessage());
            }
            throw new ORMException(e.getMessage());
        }
    }
}