package org.acme.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.acme.models.Record;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;

@Path("/records")
@Produces(MediaType.APPLICATION_JSON)
public class RecordResource {
    @GET
    @Operation(summary = "List all records")

    public Response listAll() {
        return Response.ok(Record.listAll()).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a record")
    @Parameter(description = "Record id", required = true, name = "id", schema = @Schema(implementation = String.class))
    @APIResponseSchema(value = Record.class)
    public Response get(@PathParam("id") ObjectId id) {
        return Response.ok(Record.findById(id)).build();
    }
}