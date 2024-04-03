package co.istad.mbanking.features.madia;

import co.istad.mbanking.features.madia.dto.MediaResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medias")
public class MediaController {
    private final MediaService mediaService;
    @ResponseStatus(HttpStatus.CREATED)

    @PostMapping("/upload-single")          // file is importance is the name of file
    MediaResponse uploadSingle(@RequestPart MultipartFile file){
        return mediaService.uploadSingle(file,"IMAGE");
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/upload-multiple")
    List<MediaResponse> uploadMultiple (@RequestPart List<MultipartFile> files){
        return mediaService.uploadMultiple(files ,"IMAGE");

    }
    @GetMapping("/{mediaName}")
    MediaResponse loadMediaByName(@PathVariable String mediaName){
        return mediaService.loadMediaByName(mediaName,"IMAGE");
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{mediaName}")
    MediaResponse deleteMediaByName(@PathVariable String mediaName){
        return  mediaService.deleteMediaByName(mediaName,"IMAGE");
    }
    @GetMapping
    List<MediaResponse> loadAllMedias() {
        try {
            return mediaService.loadAllMedias("IMAGE");
        }catch(IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getLocalizedMessage());
        }
    }
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadMediaByName( @PathVariable String fileName) {
        return mediaService.dowloadByName("IMAGE", fileName);
    }

}
