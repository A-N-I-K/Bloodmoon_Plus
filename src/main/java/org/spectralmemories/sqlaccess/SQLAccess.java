package org.spectralmemories.sqlaccess;

import javax.print.DocFlavor;
import java.io.Closeable;
import java.io.File;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class SQLAccess implements Closeable
{
    public static final String JDBC_SQLITE = "jdbc:sqlite:";

    private Connection database;

    public SQLAccess (File database)
    {
        CreateDatabaseFile(database);

        try
        {
            this.database = DriverManager.getConnection(JDBC_SQLITE + database.getAbsolutePath());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public SQLAccess (String path)
    {
        this (new File(path));
    }

    public boolean DatabaseExists (File file)
    {
        return file.exists();
    }

    public static boolean CreateDatabaseFile (File file)
    {
        if (! file.exists())
        {
            try
            {
                file.createNewFile();
                return true;
            }
            catch (IOException e)
            {
                return false;
            }
        }
        return false;
    }

    public boolean TableExists (String tableName)
    {
        try
        {
            ResultSet set = ExecuteSQLQuery("SELECT null FROM 'sqlite_master' WHERE type = 'table' AND name = '" + tableName + "';");

            boolean exists = set.next();

            set.close();
            return exists;
        } catch (SQLException e)
        {
            return false;
        }
    }

    public boolean CreateTable (SQLTable table)
    {
        String sql = "CREATE TABLE IF NOT EXISTS ";
        sql += table.getTableName();
        sql += "(";

        for (SQLField field : table.getFields())
        {
            sql += field.getName() + " " + field.getFieldType().toString();
            if (field.isPrimary()) sql += " PRIMARY KEY";
            if (field.isAutoIncrement()) sql += " AUTOINCREMENT";
            sql += ",";
        }
        sql = sql.substring(0, sql.length() - 1); //remove last comma
        sql += ");";


        try
        {
            Statement statement = database.createStatement();
            boolean b = statement.execute(sql);
            statement.close();

            return b;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean DeleteTable (String tableName)
    {
        String sql = "DROP TABLE IF EXISTS " + tableName + ";";

        try
        {
            Statement statement = database.createStatement();
            return statement.execute(sql);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean AddFieldToTable (String tableName, SQLField newField)
    {
        String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + newField.getName() + " " + newField.getFieldType().toString().toLowerCase();

        try
        {
            Statement statement = database.createStatement();
            return statement.execute(sql);
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public boolean RenameTable (String oldName, String newName)
    {
        String sql = "ALTER TABLE " + oldName + " RENAME TO " + newName + ";";

        try
        {
            Statement statement = database.createStatement();
            return statement.execute(sql);
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public boolean ExecuteSQLOperation (String sql) throws SQLException
    {
        Statement statement = database.createStatement();
        boolean worked = (statement.executeUpdate(sql) > 0);

        statement.close();
        return worked;
    }

    public ResultSet ExecuteSQLQuery (String sql) throws SQLException
    {
        Statement statement = database.createStatement();
        ResultSet set = statement.executeQuery(sql);

        return set;
    }

    //Todo: support multi private key
    public boolean EntryExist (String table, SQLField primaryKey, Object entryPrimaryKeyValue) throws SQLException
    {
        String sql = "SELECT * FROM " + table + " WHERE ";
        sql += primaryKey.getName();
        sql += " = ";

        boolean needsQuotes = false;
        if (primaryKey.getFieldType() == FieldType.TEXT) needsQuotes = true;

        if (needsQuotes) sql += "'";
        sql += String.valueOf(entryPrimaryKeyValue);
        if (needsQuotes) sql += "'";
        sql += ";";

        ResultSet set = ExecuteSQLQuery(sql);


        boolean exists = set.next();

        set.close();
        return exists;
    }


    @Override
    public void close()
    {
        if (database != null)
        {
            try
            {
                database.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
}
