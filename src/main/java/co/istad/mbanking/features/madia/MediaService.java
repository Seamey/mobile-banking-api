package co.istad.mbanking.features.madia;

import co.istad.mbanking.features.madia.dto.MediaRequest;
import co.istad.mbanking.features.madia.dto.MediaResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MediaService {
    // type of file when client submit is MUlitipartfile
    // if multiple file it 's be the list
    MediaResponse uploadSingle(MultipartFile file,String folderName);
    //multiple
    List<MediaResponse> uploadMultiple(List<MultipartFile> files, String folderName);
    MediaResponse loadMediaByName(String mediaName, String folderName);
    MediaResponse deleteMediaByName(String mediaName, String folderName);
    List<MediaResponse> loadAllMedias(String folderName) throws IOException;
    ResponseEntity<Resource> dowloadByName(String folderName, String filename);
    Resource downloadMediaByName(String mediaName, String folderName);


}
