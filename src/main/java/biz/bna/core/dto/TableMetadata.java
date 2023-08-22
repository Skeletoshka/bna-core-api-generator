package biz.bna.core.dto;

import javax.persistence.Column;
import java.util.List;

public class TableMetadata {

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "table_desc")
    private String tableDesc;

    @Column(name = "field_name")
    private String primaryKey;

    private List<ColumnMetadata> columns;

    public TableMetadata() {
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableDesc() {
        return tableDesc;
    }

    public void setTableDesc(String tableDesc) {
        this.tableDesc = tableDesc;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<ColumnMetadata> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnMetadata> columns) {
        this.columns = columns;
    }
}
