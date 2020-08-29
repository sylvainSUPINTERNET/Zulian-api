package org.zulian.token;


import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import org.eclipse.microprofile.jwt.Claims;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Arrays;
import java.util.HashSet;


@Path("/api/auth")
public class GenerateToken {


    @GET
    public String getToken(){
        JwtClaimsBuilder tokenClaim = Jwt.claims();
        tokenClaim.issuer("https://quarkus.io/using-jwt-rbac")
                .upn("jdoe@quarkus.io")
                .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                .claim(Claims.birthdate.name(), "2001-07-13")
                .sign();

        return tokenClaim.json();
    }
}