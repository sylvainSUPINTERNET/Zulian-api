package org.zulian.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import org.jose4j.json.internal.json_simple.JSONArray;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zulian.DTO.ErrorDto;
import org.zulian.DTO.UserCreateDto;
import org.zulian.DTO.UserLoginDto;
import org.zulian.entities.Users;
import org.zulian.repositories.UsersRepository;
import org.zulian.token.RoleImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class UsersService {

    @Inject
    UsersRepository usersRepository;

    @Inject
    RoleImpl roleImpl;


    private ModelMapper modelMapper = new ModelMapper();

    UsersService(){}

    @Transactional
    public Response createUser(UserCreateDto userCreateDto){
        HashMap<Object, Object> resp = new HashMap<>();


        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashPassword = encoder.encode(userCreateDto.getPassword());

            if (usersRepository.findByEmail(userCreateDto.getEmail()) != null) {
                resp.put("message", "Cette email est déjà utilisé");
                resp.put("error", "User already exists");
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(resp)
                        .build();
            }


            Users userNew = this.modelMapper.map(userCreateDto, Users.class);
            userNew.setPassword(hashPassword);

            try {

                usersRepository.persistAndFlush(userNew);

                resp.put("message", userNew);
                resp.put("success", "User created with success");
                return Response
                        .status(Response.Status.OK)
                        .entity(resp)
                        .build();
            } catch (PersistenceException pe) {
                resp.put("message", pe.getMessage());
                resp.put("error", pe.toString());
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(resp)
                        .build();
            }


        } catch (Exception e) {
            resp.put("message", e.getMessage());
            resp.put("error", e.toString());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(resp)
                    .build();
        }

    }


    @Transactional
    public Response loginUser(UserLoginDto userLoginDto) {
        HashMap<Object, Object> resp = new HashMap<>();

        try {
            Users user = this.usersRepository.findByEmail(userLoginDto.getEmail());
            if(user == null) {
                resp.put("message", "Aucun utilisateur pour cette email");
                resp.put("error", "No user found for this email");
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(resp)
                        .build();
            }

            String plainPassword = userLoginDto.getPassword();

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean isValidPassword = encoder.matches(plainPassword,  user.getPassword());

            if (!isValidPassword) {
                resp.put("message", "Mot de passe incorrect");
                resp.put("error", true);
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(resp)
                        .build();
            }


            long nowMillis = System.currentTimeMillis();
            long exp = nowMillis + 86400000; // 24h

            Map<String, Object> claimMap = new HashMap<>();
            // ISSUER + EXP are set automaticaly via configuration (if not exist, default value from RBAC)

            claimMap.put("exp", exp);
            claimMap.put("iss","https://zg-api");
            claimMap.put("aud","zg-api");
            claimMap.put("sub", "jwt-rbac");
            claimMap.put("jti", UUID.randomUUID().toString());
            claimMap.put("upn", "sylvainneung@zg-api");
            //claimMap.put("groups", new HashSet<>(Arrays.asList("User", "Admin")));
            claimMap.put("groups", roleImpl.getUserRole());
            claimMap.put("raw_token", UUID.randomUUID().toString());

            resp.put("message", Jwt.claims(claimMap).sign());
            resp.put("error", false);

            NewCookie cookie = new NewCookie("zg-access-token", Jwt.claims(claimMap).sign(), "/", "",
                    "ZG session", 86400, false, true); // 86400 -> 24h in seconds

            return Response.status(Response.Status.OK).entity(resp).cookie(cookie).build();


        } catch (Exception e) {
            resp.put("message", "Une erreur est surevenue");
            resp.put("error", "Unexpected error");
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(resp)
                    .build();
        }


    }

}
