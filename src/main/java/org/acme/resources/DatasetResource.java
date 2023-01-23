package org.acme.resources;

import java.io.IOException;
import java.net.URI;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.acme.models.Dataset;
import org.acme.models.MultipartBody;
import org.acme.models.Record;
import org.acme.services.DatasetService;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

@Path("/datasets")
@Produces(MediaType.APPLICATION_JSON)
public class DatasetResource {

    @Inject
    DatasetService datasetService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    @Operation(summary = "Create a dataset")
    @APIResponseSchema(value = Dataset.class, responseCode = "201")
    @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA, schema = @Schema(implementation = MultipartBody.class)))
    public Response create(@MultipartForm MultipartBody data)
            throws IOException {
        Dataset dataset = datasetService.loadCsvDatasetIntoMongo(data.fileName, data.file);
        return Response.created(URI.create("/datasets/" + dataset.id)).entity(dataset).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Delete a dataset")
    @Parameter(description = "Dataset id", required = true, name = "id", schema = @Schema(implementation = String.class))
    @APIResponse(responseCode = "204")
    public Response delete(@PathParam("id") ObjectId id) {
        Record.delete("datasetName", id.toString());
        Dataset.deleteById(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @APIResponseSchema(value = Dataset.class)
    @Operation(summary = "Get a dataset")
    @Parameter(description = "Dataset id", required = true, name = "id", schema = @Schema(implementation = String.class))

    public Response get(@PathParam("id") ObjectId id) {
        return Response.ok(Dataset.findById(id)).build();
    }

    @GET
    @Operation(summary = "List all datasets")
    public Response listAll() {
        return Response.ok(Dataset.listAll()).build();
    }

    @GET
    @Path("/{id}/records")
    @Operation(summary = "List all records of a given dataset")
    @Parameter(description = "Dataset id", required = true, name = "id", schema = @Schema(implementation = String.class))
    public Response listRecords(@PathParam("id") String id) {
        return Response.ok(Record.list("datasetName", id)).build();
    }
}