package biz.spring.core.controllers;

import biz.spring.core.config.Config;
import biz.spring.core.dto.{modelName}DTO;
import biz.spring.core.utils.GridDataOption;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class {modelName}ControllerTest extends IntegratedTest{
    //TODO определить контур
    private final String CONTOUR;
    //TODO определить модуль
    private final String MODULE;

    @Test
    @Transactional
    @Rollback
    public void getListTest() throws Exception{
        //Стандартная проверка getlist
        GridDataOption gridDataOption = new GridDataOption.Builder()
                .setOrderBy("{modelParamName}Id")
                .setRowCount(10)
                .build();

        this.mockMvc.perform(post("/v" + Config.CURRENT_VERSION + "/apps/" + CONTOUR + "/" + MODULE + "/{tableName}/getlist")
                        .content(new ObjectMapper().writeValueAsString(gridDataOption))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("\"errorCode\""))))
                //TODO определить ROW_COUNT
                .andExpect(content().string(not(containsString("\"rowCount\":" + ROW_COUNT))));

        //проверка getlist с фильтрвми
        gridDataOption = new GridDataOption.Builder()
                .setOrderBy("{modelParamName}Id")
                .setParam("{modelParamName}Visible", 1)
                .setRowCount(10)
                .build();

        this.mockMvc.perform(post("/v" + Config.CURRENT_VERSION + "/apps/" + CONTOUR + "/" + MODULE + "/{tableName}/getlist")
                        .content(new ObjectMapper().writeValueAsString(gridDataOption))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("\"errorCode\""))))
                //TODO определить ROW_COUNT
                .andExpect(content().string(not(containsString("\"rowCount\":" + ROW_COUNT))));
    }

    @Test
    @Transactional
    @Rollback
    public void getTest() throws Exception{
        //проверка получения записи без ошибки
        this.mockMvc.perform(post("/v" + Config.CURRENT_VERSION + "/apps/" + CONTOUR + "/" + MODULE + "/{tableName}/get")
                        .content("1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("\"errorCode\""))));

        //Проверка получения записи с ошибкой
        this.mockMvc.perform(post("/v" + Config.CURRENT_VERSION + "/apps/" + CONTOUR + "/" + MODULE + "/{tableName}/get")
                        .content("-1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("\"errorCode\""))));
    }

    @Test
    @Transactional
    @Rollback
    public void saveTest() throws Exception{
        {modelName}DTO dto = new {modelName}DTO();
        //TODO определить FieldName и checkValue
        String fieldName = FIELD_NAME;
        String checkValue = CHECK_VALUE;
        dto.set FIELD_NAME(checkValue);
        //TODO заполнить обязательные поля
        this.mockMvc.perform(post("/v" + Config.CURRENT_VERSION + "/apps/" + CONTOUR + "/" + MODULE + "/{tableName}/save")
                        .content(new ObjectMapper().writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.format("\"%s\":%s", fieldName, checkValue))));
    }
}
