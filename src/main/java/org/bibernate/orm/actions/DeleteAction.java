package org.bibernate.orm.actions;

import org.bibernate.orm.exception.ORMException;
import org.bibernate.orm.helpers.AnnotationsHelper;

import java.sql.Connection;
import java.sql.SQLException;

public class DeleteAction implements Action{
    Connection connection;
    Object object;
    public DeleteAction(Connection connection, Object object) {
        this.connection = connection;
        this.object = object;
    }

    @Override
    public void execute() {
        String tableName = AnnotationsHelper.getTableName(object);
        Integer id = AnnotationsHelper.getId(object);
        String idColumnName = AnnotationsHelper.getIdColumnName(object);

        try {
            var preparedStatement = connection.prepareStatement("DELETE FROM " + tableName +" WHERE " + idColumnName + " = ?");
            preparedStatement.setInt(1, id);
        } catch (SQLException e) {
            throw new ORMException(e.getMessage());
        }
    }
}
