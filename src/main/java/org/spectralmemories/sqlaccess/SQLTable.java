package org.spectralmemories.sqlaccess;


import java.util.List;

public class SQLTable
{
    private String tableName;
    private List<SQLField> fields;

    public SQLTable(String tableName, List<SQLField> fields)
    {
        this.tableName = tableName;
        this.fields = fields;
    }

    public String getTableName()
    {
        return tableName;
    }

    public List<SQLField> getFields()
    {
        return fields;
    }
}
