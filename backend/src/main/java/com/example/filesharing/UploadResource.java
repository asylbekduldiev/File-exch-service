package com.example.filesharing.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Paths;

@Path("/upload")
public class UploadResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {

        try {
            File uploadsDir = new File("uploads");
            if (!uploadsDir.exists()) uploadsDir.mkdirs();

            String fileName = Paths.get(fileDetail.getFileName()).getFileName().toString(); // безопасность имени
            File file = new File(uploadsDir, fileName);

            try (FileOutputStream out = new FileOutputStream(file)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            return Response.ok("File uploaded: " + file.getAbsolutePath()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Upload failed").build();
        }
    }
}