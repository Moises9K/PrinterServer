package moises.impresoraservidor.service;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.printing.PDFPageable;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class ImpresoraService {

    @Autowired
    ValidatorService validatorService;

    public void imprimir(MultipartFile[] files) {
        if (validatorService.validateEveryFileImage(files)){
            printImage(files);
        }

    }

    private void printImage(MultipartFile[] files) {
        //scalate image
        // posicionate in a A4 PAGE if its only one image
        if(files.length == 0 || files[0].isEmpty()){
            System.out.println("Error al imprimir imagen");
        }
        else if (files.length == 1){
            try(PDDocument doc = new PDDocument()){

                BufferedImage image = ImageIO.read(files[0].getInputStream());
                BufferedImage resizedimage = Scalr.resize(image,Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH,300);
                PDPage page = new PDPage(PDRectangle.A4);
                doc.addPage(page);

                PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc,toByteArray(resizedimage),"imagen");
                PDPageContentStream contentStream = new PDPageContentStream(doc,page, PDPageContentStream.AppendMode.APPEND, true, true);
                float coordenadasCentroX = 207.6f;
                float coordenadasCentroY = 420.9f;

                contentStream.drawImage(pdImage, coordenadasCentroX, coordenadasCentroY);
                File archivo = new File("D:\\Projectos pr\\JAVA\\ImpresoraServidor\\src\\main\\java\\moises\\impresoraservidor\\pdf\\" + files[0].getOriginalFilename() + ".pdf" );
                doc.save(archivo);

                PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
                PrinterJob printerJob = PrinterJob.getPrinterJob();
                printerJob.setPageable(new PDFPageable(doc));
                printerJob.print();

            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }

        }
        else if ( files.length == 2){
            try(PDDocument pdDocument = new PDDocument()){
                PDPage pagina = new PDPage(PDRectangle.A4);
                pdDocument.addPage(pagina);

                for (MultipartFile file: files){
                    BufferedImage image = ImageIO.read(file.getInputStream());
                    BufferedImage resizedimage = Scalr.resize(image,Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH,300);

                    PDImageXObject pdfImage = PDImageXObject.createFromByteArray(pdDocument,toByteArray(resizedimage),"imagen");
                    PDPageContentStream contentStream = new PDPageContentStream(pdDocument,pagina, PDPageContentStream.AppendMode.APPEND, true, true);


                }

            }catch (IOException e){
                System.out.println(e.getMessage());
            }

        }

    }

    private static byte[] toByteArray(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            ImageIO.write(image,"jpg",baos);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        return baos.toByteArray();
    }




}
