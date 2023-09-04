package biz.bna.core.dto;

import javax.persistence.Column;

public class ColumnMetadata {

    @Column(name = "column_name")
    private String columnName;

    @Column(name = "data_type")
    private String dataTypeName;

    @Column(name = "max_length")
    private Integer maxLength;

    @Column(name = "description")
    private String columnDescription;

    @Column(name = "nullable")
    private Integer nullable;

    @Column(name = "pk")
    private Integer isPrimaryKey;

    public ColumnMetadata() {
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }

    public void setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getColumnDescription() {
        return columnDescription;
    }

    public void setColumnDescription(String columnDescription) {
        this.columnDescription = columnDescription;
    }

    public Integer getNullable() {
        return nullable;
    }

    public void setNullable(Integer nullable) {
        this.nullable = nullable;
    }

    public Integer getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(Integer isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }
}
