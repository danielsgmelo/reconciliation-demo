package org.acme;

import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "Reconciliation Demo API", version = "1.0.0"))
public class ApiApplication extends Application {

}
