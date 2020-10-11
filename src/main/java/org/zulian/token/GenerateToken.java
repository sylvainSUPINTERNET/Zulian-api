package org.zulian.token;


import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.zulian.DTO.UserLoginDto;
import org.zulian.services.UsersService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;





@RequestScoped
@Path("/api/v1/auth")
public class GenerateToken {

    @Inject
    UsersService usersService;

    @Inject
    JWTParser parser;

    @Inject
    RoleImpl roleImpl;

    @POST
    @Path("token")
    @Produces(MediaType.APPLICATION_JSON)
    public Response responseEntity(@RequestBody UserLoginDto userLoginDto) {
        return usersService.loginUser(userLoginDto);
    }

    @GET
    @Path("token")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getToken(){


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
        claimMap.put("groups", "User");
        claimMap.put("raw_token", UUID.randomUUID().toString());

        // TODO
        // get user info are generation and add data to the claims like
        //DefaultJWTCallerPrincipal{id='25c60e2a-d3b7-49b6-ad7a-a0f0bb9d7fc6', name='sylvainneung@zg-api', expiration=1598794424386, notBefore=0, issuedAt=1598708024, issuer='https://zg-api', audience=[zg-api], subject='jwt-rbac', type='JWT', issuedFor='null', authTime=0, givenName='null', familyName='null', middleName='null', nickName='null', preferredUsername='null', email='null', emailVerified=null, allowedOrigins=null, updatedAt=0, acr='null', groups=[User,Admin]}


        // https://dzone.com/articles/how-to-use-cookies-in-spring-boo

        Map<String, String> resp = new HashMap<>();
        resp.put("access_token", Jwt.claims(claimMap).sign());

        NewCookie cookie = new NewCookie("zg-access-token", Jwt.claims(claimMap).sign(), "/", "",
                "ZG session", 86400, false, true); // 86400 -> 24h in seconds

        return Response.status(200).entity(resp).cookie(cookie).build();
    }


    @GET
    @Path("verify/{role}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verify(@Context HttpHeaders headers, @PathParam(value = "role") String role){
        Map<String, Object> resp = new HashMap<>();

        if ( headers.getCookies().get("zg-access-token") == null) {
            resp.put("message", "No access token provided");
            resp.put("error", true);
            return Response.status(Response.Status.FORBIDDEN).entity(resp).build();
        } else {

            try {
                JsonWebToken jwt = parser.parse(headers.getCookies().get("zg-access-token").getValue());
                // TODO -> compare with role given if its in groups
                System.out.println("GROUPS FoUND");
                System.out.println(jwt.getGroups());
                boolean hasRole = false;

                for ( String roleName : jwt.getGroups()) {
                    if ( roleName.equals(role)) {
                        hasRole = true;
                    }
                }

                if ( hasRole ) {
                    return Response.status(Response.Status.OK).entity(jwt).build();
                }

                resp.put("message", "No permission to access this resource");
                resp.put("error", "unauthorized");
                return Response.status(Response.Status.UNAUTHORIZED).entity(resp).build();

            } catch (ParseException e) {
                e.printStackTrace();
                return Response.status(Response.Status.FORBIDDEN).entity(e).build();
            }
        }

    }


    /*
    @GET
    @Path("verify")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verify(@QueryParam("token") String jwtToken){
        try {
            JsonWebToken jwt = parser.parse(jwtToken);
            return Response.status(200).entity(jwt).build();
        } catch (ParseException e) {
            e.printStackTrace();
            return Response.status(200).entity(e).build();
        }
    }

     */
}