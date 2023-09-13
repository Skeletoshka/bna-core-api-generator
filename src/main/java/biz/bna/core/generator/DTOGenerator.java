package biz.bna.core.generator;

import biz.bna.core.dto.ColumnMetadata;
import biz.bna.core.utils.DatabaseUtils;
import biz.bna.core.utils.FileWriter;
import biz.bna.core.utils.OrmUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DTOGenerator implements Runnable{

    private final String packageName;
    private List<ColumnMetadata> columnMetadata;

    //Сущности, для которых генерим файлы
    private String entities;

    public DTOGenerator(String packageName){
        this.packageName = packageName;
    }

    public void forEntities(String entities){
        this.entities = entities;
    }

    private List<String> buildFieldsPlaceholder(String entity, String schema){
        columnMetadata = DatabaseUtils.extractColumnMetadata(entity, schema);
        return columnMetadata.stream().map(metadata -> String.format("" +
                        (metadata.getIsPrimaryKey()!= null && metadata.getIsPrimaryKey().equals(1)?"\t@Id\n":"") +
                        String.format("\t@Schema(description = \"%s\")\n\t", metadata.getColumnDescription()) +
                        "private %s %s;\n",
                OrmUtils.convertPgTypeToJavaType(metadata.getDataTypeName()),
                OrmUtils.getAttributeName(metadata.getColumnName()))).collect(Collectors.toList());
    }

    private List<String> buildMethodsPlaceholder(String table){
        List<String> columnsSet = new ArrayList<>();
        List<String> getSetPlaceholder = columnMetadata.stream().map(metadata -> {
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
            columnsSet.add(String.format("entity.set%s(this.%s);\n\t\t",
                    OrmUtils.getAttributeName(metadata.getColumnName()).substring(0, 1).toUpperCase()
                            + OrmUtils.getAttributeName(metadata.getColumnName()).substring(1),
                    OrmUtils.getAttributeName(metadata.getColumnName())));
            return getMethod.concat(setMethod);
        }).collect(Collectors.toList());
        String toEntityPlaceholder = String.format("\tpublic %s toEntity(){\n\t\treturn toEntity(new %1$s())\n\t}\n\n\t",
                        OrmUtils.getClassName(table))
                .concat(String.format("public %s toEntity(%1$s entity){\n\t\t", OrmUtils.getClassName(table))
                        .concat(String.join("", columnsSet))
                        .concat("return entity\n\t}\n"));
        getSetPlaceholder.add(toEntityPlaceholder);
        return getSetPlaceholder;
    }

    public String buildConstructorPlaceholder(String table){
        String noParameterConstructor = String.format("\n\tpublic %sDTO(){\n\n\t}\n\n", OrmUtils.getClassName(table));
        String parameterConstructor = String.format("\tpublic %sDTO(\n\t\t\t",
                        OrmUtils.getClassName(table)).concat(columnMetadata.stream()
                        .map(metadata -> OrmUtils.convertPgTypeToJavaType(metadata.getDataTypeName()).concat(" ")
                                .concat(OrmUtils.getAttributeName(metadata.getColumnName()))).collect(Collectors.joining(",\n\t\t\t")))
                .concat("){\n\t\t")
                .concat(columnMetadata.stream().map(metadata -> "this.".concat(OrmUtils.getAttributeName(metadata.getColumnName()))
                        .concat(" = ").concat(OrmUtils.getAttributeName(metadata.getColumnName()))).collect(Collectors.joining(";\n\t\t")))
                .concat(";\n\t}\n\n");
        return noParameterConstructor.concat(parameterConstructor);
    }

    @Override
    public void run() {
        Arrays.stream(entities.split(",")).forEach(entity -> {
            try {
                List<String> importPlaceholder = List.of(
                        "import javax.persistence.Id;",
                        String.format("import biz.spring.core.model.%s;", OrmUtils.getClassName(entity)),
                        "import io.swagger.v3.oas.annotations.media.Schema;"
                );
                List<String> fields = buildFieldsPlaceholder(entity, OrmUtils.getSchemaName());
                List<String> methods = buildMethodsPlaceholder(entity);
                String constructPlaceholder = buildConstructorPlaceholder(entity);
                String file = "package " + packageName.concat("\n\n")
                        .concat(String.join("\n", importPlaceholder))
                        .concat(String.format("\n\npublic class %sDTO{\n\n",
                                OrmUtils.getClassName(entity)))
                        .concat(String.join("\n", fields))
                        .concat(constructPlaceholder)
                        .concat(String.join("\n", methods))
                        .concat("}");
                FileWriter.writeFile(FileWriter.DTO_PATH, OrmUtils.getClassName(entity).concat("DTO.java"), file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
