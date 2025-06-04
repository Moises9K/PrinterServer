package moises.impresoraservidor.service;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class FileValidationService {

    private final Tika tika = new Tika();
    private static final List<String> ALLOWED_IMAGE_MIME_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    private static final String PDF_MIME_TYPE = "application/pdf";

    private static final String DOCX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";


    public boolean isImage(MultipartFile file) {
        if (file != null || file.isEmpty()){
            return false;
        }
        return isAllowedType(file,ALLOWED_IMAGE_MIME_TYPES);

    }

    public boolean isPDF(MultipartFile file){
        if(file != null || file.isEmpty()){
            return false;
        }
        return isAllowedType(file,List.of(PDF_MIME_TYPE));
    }

    private boolean isAllowedType(MultipartFile file,List<String> allowedMimeTypes){
        String nombreOriginal = file.getOriginalFilename();

        boolean isAllowed = false;
        try(InputStream stream = file.getInputStream()){
            String tipo = tika.detect(stream);
            if(tipo != null && allowedMimeTypes.contains(tipo.toLowerCase(Locale.ROOT))){
                isAllowed = true;
            }


        }catch(IOException e){
            System.err.println("Error detectando el archivo: "+ nombreOriginal +  e.getMessage());
            isAllowed = false;
        }
        return isAllowed;
    }


}
