package moises.impresoraservidor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ValidatorService {

    @Autowired
    private FileValidationService fileValidationService;

    public boolean validateEveryFilePDF(MultipartFile[] files) {

        boolean todossonPDF = false;
        for (MultipartFile file : files) {
            if(fileValidationService.isPDF(file)) {
                todossonPDF = true;
            }
            else{
                todossonPDF = false;
            }
        }
        return todossonPDF;
    }

    public boolean validateEveryFileImage(MultipartFile[] files) {
        boolean todossonImage = false;
        for (MultipartFile file : files) {
            if(fileValidationService.isImage(file)) {
                todossonImage = true;
            }
            else{
                todossonImage = false;
            }
        }
        return todossonImage;
    }
}
