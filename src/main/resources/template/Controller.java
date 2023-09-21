package {packageName};

import biz.spring.core.config.Config;
import biz.spring.core.dto.{modelName}DTO;
import biz.spring.core.model.{modelName};
import biz.spring.core.response.DataResponse;
import biz.spring.core.service.{modelName}Service;
import biz.spring.core.service.BaseService;
import biz.spring.core.utils.GridDataOption;
import biz.spring.core.view.{modelName}View;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO определить CONTOUR и MODULE
@RestController
@Tag(name = "Контроллер для ролей", description = "Контроллер для получения {modelName}")
@RequestMapping(value = "/v" + Config.CURRENT_VERSION + "/apps/CONTOUR/MODULE",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@Transactional
public class {modelName}Controller {

    static class GridDataOption{modelName} extends GridDataOption {
        @Schema(description = "" +
                "<ul>" +
                //TODO определить фильтры
                "<ul>")
        public List<NamedFilter> getNamedFilters(){
            return super.getNamedFilters();
        }
    }

    @Autowired
    private {modelName}Service {modelParamName}Service;

    @Operation(summary = "Возвращает список объектов \"{modelName}\"",
            description = "Вовзращает список объектов согласно переданным фильтрам")
    @RequestMapping(value = "/{tableName}/getlist", method = RequestMethod.POST)
    public DataResponse<{modelName}View> getList(@RequestBody GridDataOption{modelName} gridDataOption){
        //Проверка фильтров
        /*
        boolean visibleFound = gridDataOption.getNamedFilters().stream().anyMatch(nf -> nf.getName().equals("accessRoleVisible"));
            Если фильтр не обязательный
            if(!visibleFound){
                gridDataOption.getNamedFilters().add(new GridDataOption.NamedFilter("accessRoleVisible", -1));
            }

            Если фильтр обязательный
            if(!visibleFound){
                throw new RuntimeException("Отсутствует фильтр \"accessRoleVisible\"");
            }
        */
        List<{modelName}View> result = {modelParamName}Service.getAll(gridDataOption);
        Integer count = {modelParamName}Service.getCount(gridDataOption);
        return BaseService.buildResponse(result, gridDataOption, count);
    }

    @Operation(summary = "Возвращает объект \"{modelName}\"",
            description = "Вовзращает список объект \"{modelName}\" по его идентификатору. Если идентификатора нет - " +
                    "возвращается объект по умолчанию")
    @RequestMapping(value = "/{tableName}/get", method = RequestMethod.POST)
    public {modelName}DTO get(@RequestBody(required = false) Integer id){
        if(id == null){
            return new {modelName}DTO();
        } else {
            {modelName}View view = {modelParamName}Service.getOne(id);
            {modelName}DTO dto = new {modelName}DTO();
            BeanUtils.copyProperties(view, dto);
            return dto;
        }
    }

    @Operation(summary = "Метод для сохранения объекта \"{modelName}\"",
            description = "Запись с заполненным идентификатором обновляется, с пустым - вставляется")
    @RequestMapping(value = "/{tableName}/save", method = RequestMethod.POST)
    public {modelName}View save(@RequestBody {modelName}DTO dto){
        {modelName} {modelParamName};
        if(dto.get{modelName}Id()==null){
            result = {modelParamName}Service.add(dto.toEntity());
        }else {
            result = {modelParamName}Service.edit(dto.toEntity());
        }
        return {modelParamName}Service.getOne(result.get{modelName}Id());
    }

    @Operation(summary = "Метод для удаления объекта \"{modelName}\"",
            description = "Удаляются записи с переданными идентификаторами")
    @RequestMapping(value = "/{tableName}/delete", method = RequestMethod.POST)
    public String delete(@RequestBody int[] ids){
        {modelParamName}Service.delete(ids);
        return BaseService.STANDARD_SUCCESS;
    }
}
