package com.example.filesharing;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.*;

@Path("/upload")
public class MultipartParser {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response handleUploadAndReturnFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {

        String fileName = fileDetail.getFileName();
        File uploadsDir = new File("uploads");
        if (!uploadsDir.exists() && !uploadsDir.mkdirs()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unable to create upload directory").build();
        }

        File uploadedFile = new File(uploadsDir, new File(fileName).getName());

        try (OutputStream out = new FileOutputStream(uploadedFile)) {
            uploadedInputStream.transferTo(out);
            System.out.println("Saved file: " + uploadedFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("File write failed").build();
        }

        return Response.ok("Uploaded to: " + uploadedFile.getAbsolutePath()).build();
    }
}