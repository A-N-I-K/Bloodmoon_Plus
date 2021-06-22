package org.spectralmemories.sqlaccess;

public class SQLField
{
    private String name;
    private FieldType fieldType;
    private boolean isPrimary;
    private boolean isAutoIncrement;

    public SQLField(String name, FieldType fieldType, boolean isPrimary, boolean isAutoIncrement)
    {
        this.name = name;
        this.fieldType = fieldType;
        this.isPrimary = isPrimary;
        this.isAutoIncrement = isAutoIncrement;
    }

    public String getName()
    {
        return name;
    }

    public FieldType getFieldType()
    {
        return fieldType;
    }

    public boolean isPrimary()
    {
        return isPrimary;
    }

    public boolean isAutoIncrement()
    {
        return isAutoIncrement;
    }
}
