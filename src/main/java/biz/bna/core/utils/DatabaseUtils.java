package biz.bna.core.utils;

import biz.bna.core.dto.ColumnMetadata;
import biz.bna.core.utils.OrmUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class DatabaseUtils {
    public static void setSequenceValue(String sequence, Integer value){
        JdbcTemplate jdbcTemplate = OrmUtils.getJDBCTemplate();
        String sql = "ALTER SEQUENCE %s RESTART WITH %d";
        jdbcTemplate.execute(String.format(sql, sequence, value));
    }

    public static Integer getSequenceNextValue(String sequence){
        JdbcTemplate jdbcTemplate = OrmUtils.getJDBCTemplate();
        String sql = "SELECT nextval('controlobjectrole_id_gen') ";
        return jdbcTemplate.queryForObject(String.format(sql, sequence), Integer.class);
    }

    public static List<ColumnMetadata> extractColumnMetadata(String table){
        return extractColumnMetadata(table, "dbo");
    }

    public static List<ColumnMetadata> extractColumnMetadata(String table, String schema){
        String sql = "" +
                "SELECT " +
                "    c.column_name, " +
                "    c.data_type, " +
                "    c.character_maximum_length as max_lenght, " +
                "    pgd.description, " +
                "    c.table_name , " +
                "    case when c.is_nullable = 'YES' then 1 else 0 end as \"nullable\", " +
                "    ( " +
                "       SELECT 1 " +
                "       FROM information_schema.table_constraints tc " +
                "       JOIN information_schema.constraint_column_usage AS ccu USING (constraint_schema, constraint_name) " +
                "       WHERE constraint_type = 'PRIMARY KEY' and tc.table_name = :table and ccu.column_name = c.column_name " +
                "     ) pk " +
                "FROM pg_catalog.pg_statio_all_tables AS st " +
                "INNER JOIN pg_catalog.pg_description pgd ON pgd.objoid = st.relid " +
                "INNER JOIN information_schema.columns c ON ( " +
                "    pgd.objsubid   = c.ordinal_position and " +
                "    c.table_schema = st.schemaname  and " +
                "    c.table_name   = st.relname " +
                ") " +
                "WHERE st.schemaname = :schema " +
                "AND st.relname = :table";
        Map<String, Object> params = Map.of("table", table, "schema", schema);
        return SqlUtils.findListForObject(sql, params, ColumnMetadata.class);
    }

}
