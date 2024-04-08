package co.istad.mbanking.features.madia;

import co.istad.mbanking.features.madia.dto.MediaResponse;
import co.istad.mbanking.util.MediaUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaServiceImpl implements MediaService {
    @Value("${media.server-path}")
    private String serverPath;
    @Value("${media.base-uri}")
    private String baseUri;



    @Override
    public MediaResponse uploadSingle(MultipartFile file, String folderName) {
        //Generate new unique name for file upload
        String newName=  UUID.randomUUID().toString(); // generte with uuid

        // Extract extension from file upload
        // Assume profile.png


        String extension = MediaUtil.extractExtension(file.getOriginalFilename());
        newName = newName + "." + extension;

        // copy FILE TO SERVER
        Path path = Paths .get(serverPath + folderName +"\\" + newName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }catch( IOException ex){
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getLocalizedMessage());

        }
        return MediaResponse.builder()
                .name(newName)
                .contentType(file.getContentType())
                .extension(extension)
                .uri(String.format("%s%s/%s",baseUri, folderName, newName))
                .size(file.getSize())
                .build();
    }

    @Override
    public List<MediaResponse> uploadMultiple(List<MultipartFile> files, String folderName) {
        // create empty array list , wait for adding upload file
        List<MediaResponse> mediaResponses = new ArrayList<>();

            // use loop
        files.forEach(file->{               // use structure of uploadSingle to upload multi
           MediaResponse mediaResponse= this.uploadSingle(file, folderName);
           mediaResponses.add(mediaResponse);
        });


        return mediaResponses;
    }

    @Override
    public MediaResponse loadMediaByName(String mediaName, String folderName) {
        // Create absolute path
        Path path = Paths.get(serverPath + folderName+"\\"+ mediaName);
        try{
            Resource resource = new UrlResource(path.toUri());
            if(!resource.exists()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Media has not been found!");
            }
            return MediaResponse.builder()
                    .name(mediaName)
                    .contentType(Files.probeContentType(path))
                    .extension(MediaUtil.extractExtension(mediaName))
                    .uri(String.format("%s%s/%s",baseUri, folderName, mediaName))
                    .size(resource.contentLength())
                    .build();
        }catch  (MalformedURLException e) {
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getLocalizedMessage());
        }  catch (IOException e) {
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getLocalizedMessage());
        }

    }

    @Override
    public MediaResponse deleteMediaByName(String mediaName, String folderName) {
        // create absolute path
        Path path = Paths.get(serverPath + folderName+"\\"+ mediaName);
         try{
             long size = Files.size(path);
             if(Files.deleteIfExists(path)){
                 return MediaResponse.builder()
                         .name(mediaName)
                         .contentType(Files.probeContentType(path))
                         .extension(MediaUtil.extractExtension(mediaName))
                         .uri(String.format("%s%s/%s",baseUri, folderName, mediaName))
                         .size(size)
                         .build();
             }
             throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Media has not been found!");

         } catch (IOException e) {
             throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                     String.format("Media path %s cannot be deleted!"));
         }


    }

    @Override
    public List<MediaResponse> loadAllMedias(String folderName) {
        Path path = Paths.get(serverPath + folderName + "\\" );
        List<MediaResponse>mediaResponses = new ArrayList<>();
        // use loop for loop media
        try {
            Files.list(path).forEach(media -> {
                try {

                    Resource resource = new UrlResource(path.toUri());
                    String mediaName = media.getFileName().toString();
                    if (!resource.exists()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Media has not been found!");
                    }
                    MediaResponse mediaResponse = MediaResponse.builder()
                            .name(mediaName)
                            .contentType(Files.probeContentType(media)).extension(MediaUtil.extractExtension(mediaName))
                            .uri(String.format("%s/%s", baseUri, mediaName))
                            .size(resource.contentLength())
                            .build();
                    mediaResponses.add(mediaResponse);

                } catch (IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            e.getLocalizedMessage());
                }
            });
        }catch (IOException e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getLocalizedMessage());
        }
        return mediaResponses;




    }

    @Override
    public ResponseEntity<Resource> dowloadByName(String folderName, String fileName) {
        try {
            Path path = Paths.get(serverPath+folderName+"\\"+fileName);
            Resource resource = new UrlResource(path.toUri());
            if(resource.exists())
            {
                return   ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);

            }else{
                throw new RuntimeException("images url not found");
            }
        }catch (MalformedURLException e)
        {
            e.getLocalizedMessage();
        }
        return null;
    }

    @Override
    public Resource downloadMediaByName(String mediaName, String folderName) {
        Path path = Paths.get(serverPath + folderName +"\\" +mediaName);
        try {
            Resource resource = new UrlResource(path.toUri());
            return resource;
        }catch ( MalformedURLException e){
            e.getLocalizedMessage();
        };

        return null;
    }
}
