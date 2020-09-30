package org.zulian.token;


import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.*;




@RequestScoped
@Path("/api/auth")
public class GenerateToken {

    @Inject
    JWTParser parser;

    @Inject
    RoleImpl roleImpl;


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
}