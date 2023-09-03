package biz.bna.core.generator;

import biz.bna.core.ConsoleApplication;
import biz.bna.core.dto.ColumnMetadata;
import biz.bna.core.utils.FileWriter;
import biz.bna.core.utils.OrmUtils;
import biz.bna.core.utils.SqlUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ModelGenerator implements Runnable {

    private final String packageName;
    private List<ColumnMetadata> columnMetadata;

    //Сущности, для которых генерим файлы
    private String entities;

    public ModelGenerator(String packageName){
        this.packageName = packageName;
    }

    public void forEntities(String entities){
        this.entities = entities;
    }

    private List<String> buildFieldsPlaceholder(String entity, String schema){
        String sql = "" +
                "SELECT c.column_name, " +
                "    c.data_type, " +
                "    c.character_maximum_length AS max_lenght, " +
                "    pgd.description, " +
                "    CASE WHEN c.is_nullable = 'YES' THEN 1 ELSE 0 END AS \"nullable\" " +
                "FROM pg_catalog.pg_statio_all_tables as st " +
                "INNER JOIN pg_catalog.pg_description pgd ON ( " +
                "    pgd.objoid = st.relid " +
                ") " +
                "INNER JOIN information_schema.columns c ON ( " +
                "    pgd.objsubid   = c.ordinal_position AND " +
                "    c.table_schema = :schema AND " +
                "    c.table_name   = :table " +
                ")";
        Map<String, Object> params = Map.of("table", entity, "schema", schema);
        columnMetadata = SqlUtils.findListForObject(sql, params, ColumnMetadata.class);
        return columnMetadata.stream().map(metadata -> {
            String nullAnnotation = String.format("\t@NotNull(message = \"Поле \\\"%s\\\" не может быть пустым\")\n",
                    metadata.getColumnDescription());
            String sizeAnnotation = String.format("\t@Size(max = %d, message = \"Поле \\\"%s\\\" не может иметь более {max} символов)\n",
                    metadata.getMaxLength(), metadata.getColumnDescription());
            return String.format("" +
                    "\t@Column(name = \"%s\", nullable = %s)\n" +
                    (metadata.getNullable().equals(0)?nullAnnotation:"") +
                    (metadata.getMaxLength()!=null?sizeAnnotation:"") +
                    "\tprivate %s %s;\n", metadata.getColumnName(),
                    metadata.getNullable().equals(0),
                    OrmUtils.convertPgTypeToJavaType(metadata.getDataTypeName()),
                    OrmUtils.getAttributeName(metadata.getColumnName()));
        }).collect(Collectors.toList());
    }

    private List<String> buildMethodsPlaceholder(){
        return columnMetadata.stream().map(metadata -> {
            String getMethod = String.format("" +
                    "\tpublic %s get%s(){\n\t\treturn %s;\n\t}\n\n",
                    OrmUtils.convertPgTypeToJavaType(metadata.getDataTypeName()),
                    OrmUtils.getAttributeName(metadata.getColumnName()).substring(0, 1).toUpperCase()
                            + OrmUtils.getAttributeName(metadata.getColumnName()).substring(1),
                    OrmUtils.getAttributeName(metadata.getColumnName()));
            String setMethod = String.format("" +
                            "\tpublic %s set%s(%s value){\n\t\tthis.%s = value;\n\t}\n",
                    OrmUtils.convertPgTypeToJavaType(metadata.getDataTypeName()),
                    OrmUtils.getAttributeName(metadata.getColumnName()).substring(0, 1).toUpperCase()
                            + OrmUtils.getAttributeName(metadata.getColumnName()).substring(1),
                    OrmUtils.convertPgTypeToJavaType(metadata.getDataTypeName()),
                    OrmUtils.getAttributeName(metadata.getColumnName()));
            return getMethod.concat(setMethod);
        }).collect(Collectors.toList());
    }

    public String buildConstructorPlaceholder(String table){
        String noParameterConstructor = String.format("\n\tpublic %s(){\n\n\t}\n\n", OrmUtils.getClassName(table));
        String parameterConstructor = String.format("\tpublic %s(\n\t\t\t",
                        OrmUtils.getClassName(table)).concat(columnMetadata.stream()
                .map(metadata -> OrmUtils.convertPgTypeToJavaType(metadata.getDataTypeName()).concat(" ")
                        .concat(OrmUtils.getAttributeName(metadata.getColumnName()))).collect(Collectors.joining(",\n\t\t\t")))
                .concat("){\n\t\t")
                .concat(columnMetadata.stream().map(metadata -> "this.".concat(OrmUtils.getAttributeName(metadata.getColumnName()))
                        .concat(" = ").concat(OrmUtils.getAttributeName(metadata.getColumnName()))).collect(Collectors.joining(";\n\t\t")))
                .concat("\n\t}\n\n");
        return noParameterConstructor.concat(parameterConstructor);
    }

    @Override
    public void run() {
        Arrays.stream(entities.split(",")).forEach(entity -> {
            try {
                List<String> importPlaceholder = List.of(
                        "import javax.persistence.Column;",
                        "import javax.persistence.Entity;",
                        "import javax.persistence.Id;",
                        "import javax.persistence.Table;",
                        "import javax.validation.constraints.NotNull;",
                        "import javax.validation.constraints.Size;"
                );
                List<String> fields = buildFieldsPlaceholder(entity, OrmUtils.getSchemaName());
                List<String> methods = buildMethodsPlaceholder();
                String constructPlaceholder = buildConstructorPlaceholder(entity);
                String file = "package " + packageName.concat("\n\n")
                        .concat(String.join("\n", importPlaceholder))
                        .concat(String.format("\n\n@Entity\n@Table(name = \"%s\")\n public class %s{\n\n",
                                entity, OrmUtils.getClassName(entity)))
                        .concat(String.join("\n", fields))
                        .concat(constructPlaceholder)
                        .concat(String.join("\n", methods))
                        .concat("}");
                FileWriter.writeFile(FileWriter.MODEL_PATH, OrmUtils.getClassName(entity).concat(".java"), file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
/*select
    c.column_name,
    c.data_type,
    c.character_maximum_length as max_lenght,
    pgd.description,
    case when c.is_nullable = 'YES' then 1 else 0 end as "nullable"
from pg_catalog.pg_statio_all_tables as st
inner join pg_catalog.pg_description pgd on (
    pgd.objoid = st.relid
)
inner join information_schema.columns c on (
    pgd.objsubid   = c.ordinal_position and
    c.table_schema = st.schemaname and
    c.table_name   = st.relname
);

SELECT c.column_name, c.data_type
FROM information_schema.table_constraints tc
JOIN information_schema.constraint_column_usage AS ccu USING (constraint_schema, constraint_name)
JOIN information_schema.columns AS c ON c.table_schema = tc.constraint_schema
  AND tc.table_name = c.table_name AND ccu.column_name = c.column_name
WHERE constraint_type = 'PRIMARY KEY' and tc.table_name = 'controlobject';*/