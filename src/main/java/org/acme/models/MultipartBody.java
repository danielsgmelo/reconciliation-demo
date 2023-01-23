package org.acme.models;

import java.io.InputStream;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class MultipartBody {
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    @Schema(type = SchemaType.STRING, format = "binary", description = "A csv dataset. Try one of the test datasets in src/test/resources", required = true)
    public InputStream file;

    @FormParam("fileName")
    @PartType(MediaType.TEXT_PLAIN)
    @Schema(type = SchemaType.STRING, format = "string", description = "An optional file name", required = false)
    public String fileName;
}
