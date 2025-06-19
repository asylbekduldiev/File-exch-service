package com.example.filesharing.resource;

import com.example.filesharing.Model.Storage;
import com.example.filesharing.Model.FileInfo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.*;
import java.util.UUID;

@Path("/upload")
public class UploadResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {

        File uploadsDir = new File("uploads");
        if (!uploadsDir.exists() && !uploadsDir.mkdirs()) {
            return Response.serverError().entity("Cannot create upload directory").build();
        }

        String fileName = new File(fileDetail.getFileName()).getName();
        File file = new File(uploadsDir, fileName);

        try (OutputStream out = new FileOutputStream(file)) {
            fileInputStream.transferTo(out);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().entity("File write failed").build();
        }

        String token = UUID.randomUUID().toString();
        Storage.downloadMap.put(token, new FileInfo(file.getAbsolutePath(), System.currentTimeMillis()));

        return Response.ok("Uploaded. Download token: " + token).build();
    }
}