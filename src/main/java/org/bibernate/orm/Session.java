package org.bibernate.orm;

import org.bibernate.orm.actions.Action;
import org.bibernate.orm.actions.DeleteAction;
import org.bibernate.orm.actions.InsertAction;
import org.bibernate.orm.annotation.Column;
import org.bibernate.orm.annotation.Table;
import org.bibernate.orm.exception.ORMException;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Session {
    Map<String, Map<Integer, Object>> objectsCash = new HashMap<>();
    DataSource dataSource;
    Connection connection;

    Queue<Action> actionQueue = new LinkedList<>();

    public Session (DataSource dataSource) {
        this.dataSource = dataSource;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T find(Class<T> entityType, Object id) {

        if (objectsCash.containsKey(entityType.getName())) {
            var objectFromCash = objectsCash.get(entityType.getName()).get((Integer) id);
            if (objectFromCash != null) {
                System.out.println("Found entity in cash");
                return (T) objectFromCash;
            }
        }

        if (entityType.isAnnotationPresent(Table.class)) {
            String tableName = entityType.getAnnotation(Table.class).value();
            if (tableName.isBlank()) tableName = entityType.getSimpleName().toLowerCase();
            try {
                var objectInstance = (T) entityType.getDeclaredConstructors()[0].newInstance();
                var preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE id=?");
                preparedStatement.setInt(1, (Integer)id);

                var resulSet = preparedStatement.executeQuery();

                if (resulSet.next()) {
                    var fields = entityType.getFields();
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(Column.class)) {
                            var columnName = field.getAnnotation(Column.class).value();
                            if (columnName.isBlank()) columnName = field.getName().toLowerCase();

//                           TODO
                            field.getType().equals(Integer.class);


                            String fieldValue = resulSet.getString(columnName);
                            field.setAccessible(true);
                            field.set(objectInstance,fieldValue);
                        }
                    }

                    if (!objectsCash.containsKey(entityType.getName())) {
                        objectsCash.put(entityType.getName(), new HashMap<>());
                    }

                    objectsCash.get(entityType.getName()).put((Integer)id, objectInstance);

                    return objectInstance;
                } else {
                    throw new ORMException("Record not found");
                }

            } catch (Exception e) {
                throw new ORMException(e.getMessage());
            }
        } else {
            throw new ORMException("Annotation @Table is not present in correspondent class: " + entityType.getSimpleName());
        }
    }

    public void persist(Object entity) {
//        TODO add create or update depends on ID availability
        actionQueue.add(new InsertAction(connection, entity));
    }

    public void remove(Object entity) {
        actionQueue.add(new DeleteAction(connection, entity));
    }

    public void flush() {
        actionQueue.stream().forEach(Action::execute);
    }

    public void close() {
        flush();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
