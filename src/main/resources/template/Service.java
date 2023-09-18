package {packageName};

import biz.spring.core.model.{modelName};
import biz.spring.core.repository.{modelName}Repository;
import biz.spring.core.utils.GridDataOption;
import biz.spring.core.utils.Query;
import biz.spring.core.validator.{modelName}Validator;
import biz.spring.core.view.AccessRoleView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class {modelName}Service extends BaseService<{modelName}> {

    @Autowired
    private {modelName}Repository {modelParamName}Repository;
    @Autowired
    private {modelName}Validator {modelParamName}Validator;

    @Override
    @PostConstruct
    public void init() {
        init({modelParamName}Repository, {modelParamName}Validator);
    }

    //TODO указать пакет
    @Value("classpath:/script/PACKAGE/{tableName}/mainSql.sql")
    Resource mainSQL;

    @Value("classpath:/script/PACKAGE/{tableName}/mainSqlForOne.sql")
    Resource mainSqlForOne;

    public List<{modelName}View> getAll(GridDataOption gridDataOption){
        return new Query.QueryBuilder<{modelName}View>(mainSQL)
                .forClass({modelName}View.class, "m0")
                .setOrderBy(gridDataOption.getOrderBy())
                .setLimit(gridDataOption.buildPageRequest())
                .setSearch(gridDataOption.getSearch())
                .setParams(gridDataOption.buildParams())
                .build()
                .execute();
    }

    public Integer getCount(GridDataOption gridDataOption){
        return new Query.QueryBuilder<{modelName}View>(mainSQL)
                .forClass({modelName}View.class, "m0")
                .setOrderBy(gridDataOption.getOrderBy())
                .setSearch(gridDataOption.getSearch())
                .setParams(gridDataOption.buildParams())
                .build()
                .count();
    }

    public {modelName}View getOne(Integer id){
        return new Query.QueryBuilder<{modelName}View>(mainSqlForOne)
                .forClass({modelName}View.class, "m0")
                .build()
                .executeOne(id);
    }

}
