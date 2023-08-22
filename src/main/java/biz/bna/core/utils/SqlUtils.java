package biz.bna.core.utils;

import biz.bna.core.rowmapper.RowMapForObject;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

public class SqlUtils {

    public static <T> List<T> findListForObject(String sql, Map<String, Object> params, Class<T> cls){
        NamedParameterJdbcTemplate jdbc = OrmUtils.getJDBC();
        RowMapForObject rowMapper = new RowMapForObject(cls);
        OrmUtils.loggerSql(sql);
        return (List<T>)jdbc.query(sql, params, rowMapper);
    }
}
