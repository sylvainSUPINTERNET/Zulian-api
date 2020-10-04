package org.zulian.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.elytron.security.common.BcryptUtil;
import org.jose4j.json.internal.json_simple.JSONArray;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.zulian.DTO.ErrorDto;
import org.zulian.DTO.UserCreateDto;
import org.zulian.entities.Users;
import org.zulian.repositories.UsersRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class UsersService {

    @Inject
    UsersRepository usersRepository;

    private ModelMapper modelMapper = new ModelMapper();

    UsersService(){}

    @Transactional
    public Map<Object, Object> createUser(UserCreateDto userCreateDto){
        HashMap<Object, Object> resp = new HashMap<>();


        try {
            String hashPassword = BcryptUtil.bcryptHash(userCreateDto.getPassword());

            if (usersRepository.findByEmail(userCreateDto.getEmail()) != null) {
                resp.put("message", "This email is already taken.");
                resp.put("error", "User already exists");
                return resp;
            }


            Users userNew = this.modelMapper.map(userCreateDto, Users.class);
            userNew.setPassword(hashPassword);

            try {

                usersRepository.persistAndFlush(userNew);

                resp.put("message", userNew);
                resp.put("success", "User created with success");
                return resp;
            } catch (PersistenceException pe) {
                resp.put("message", pe.getMessage());
                resp.put("error", pe.toString());
                return resp;
            }


        } catch (Exception e) {
            resp.put("message", e.getMessage());
            resp.put("error", e.toString());
            return resp;
        }

    }


}
