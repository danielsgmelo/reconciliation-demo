package org.acme.resources;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.acme.services.ReconcileService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Path("/reconcile")
@Produces(MediaType.APPLICATION_JSON)
public class ReconcileResource {

    @Inject
    ReconcileService reconciliationService;

    @POST
    @Path("/{dataset1}/{dataset2}")
    @Operation(summary = "Reconcile a set of 2 datasets")
    @Parameter(description = "First dataset id", required = true, name = "dataset1")
    @Parameter(description = "Second dataset id", required = true, name = "dataset2")
    @APIResponse(responseCode = "204")
    public Response reconcile(@PathParam("dataset1") String dataset1, @PathParam("dataset2") String dataset2) {
        reconciliationService.reconcile(dataset1, dataset2);
        return Response.noContent().build();
    }

}