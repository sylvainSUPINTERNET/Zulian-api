package org.zulian.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.elytron.security.common.BcryptUtil;
import org.jose4j.json.internal.json_simple.JSONArray;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.zulian.DTO.ErrorDto;
import org.zulian.DTO.UserCreateDto;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class UsersService {

    UsersService(){}


    public String createUser(UserCreateDto userCreateDto){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String hashPassword = BcryptUtil.bcryptHash(userCreateDto.getPassword());
            return "ok";

        } catch (Exception e) {

            //JSONObject json = new JSONObject();
            //JSONArray array = new JSONArray();
            //JSONObject item = new JSONObject();
            //json.put("error", e.getMessage());
            //System.out.println(json.toString());

            //objectMapper.readValue(json.toJSONString(),ErrorDto.class);

            return e.getMessage();
        }

    }


}
