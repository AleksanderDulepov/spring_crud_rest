package com.dulepov.spring.rest.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CommonServiceImpl implements CommonService{

    //вызываем бин objectMapper как компонент в spring контейнере (см. MyConfig)
    @Autowired
    private ObjectMapper objectMapper;

    //PARTIAL UPDATE UNIVERSAL
    @Override
    @Transactional
    public <T> T applyPatchToEmployee(JsonMergePatch patchJson, T target, Class<T> targetClass) {

        //сериализуем обьект, который хоти изменить  в json
        JsonNode targetJson=objectMapper.convertValue(target, JsonNode.class);


        try {
            //мэтчим старый и новый jsonы воедино
            JsonNode matchedJson=patchJson.apply(targetJson);

            //десериализуем получившийся комбо-json в обьект класса
            return objectMapper.convertValue(matchedJson,targetClass);


        } catch (JsonPatchException e) {
            throw new RuntimeException(e);
        }
    }
}
