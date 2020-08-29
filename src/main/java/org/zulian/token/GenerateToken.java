package org.zulian.token;


import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;


@Path("/api/auth")
public class GenerateToken {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getToken(){

        long nowMillis = System.currentTimeMillis();
        long exp = nowMillis + 86400000; // 24h

        Map<String, Object> claimMap = new HashMap<>();
        claimMap.put("iss", "https://kostenko.org");
        claimMap.put("sub", "jwt-rbac");
        claimMap.put("exp", exp);
        claimMap.put("jti", UUID.randomUUID().toString());
        claimMap.put("upn", "UPN");
        claimMap.put("groups", new HashSet<>(Arrays.asList("User", "Admin")));
        claimMap.put("raw_token", UUID.randomUUID().toString());


        return Jwt.claims(claimMap).jws().signatureKeyId("META-INF/resources/private_key.pem").sign();
    }
}