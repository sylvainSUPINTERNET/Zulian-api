import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        tags = {
                @Tag(name="Users", description="CRUD Users"),
                @Tag(name="Token", description="Generation JWT to exploit API")
        },
        info = @Info(
                title="ZG API",
                version = "0.0.1",
                contact = @Contact(
                        name = "Sylvain Joly",
                        url = "https://github.com/sylvainSUPINTERNET",
                        email = "sylvain.joly00@gmail.com"),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class CustomApi extends Application {
}
