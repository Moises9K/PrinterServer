package moises.impresoraservidor.service;


import moises.impresoraservidor.constant.PageSize;
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
import java.util.UUID;

@Service
public class ImpresoraService {

    @Autowired
    ValidatorService validatorService;

    public boolean imprimir(MultipartFile[] files) {
        return printImage(files);

    }

    private boolean printImage(MultipartFile[] files) {
        //scalate image
        // posicionate in a A4 PAGE if its only one image

        if(files.length == 0 || files[0].isEmpty()){
            System.out.println("Error al imprimir imagen");
            return false;
        }

        try(PDDocument doc = new PDDocument()){
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try(PDPageContentStream contentStream = new PDPageContentStream(doc,page)){
                if(files.length == 1){
                    BufferedImage image = ImageIO.read(files[0].getInputStream());
                    BufferedImage resizedimage = Scalr.resize(image,Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH,300);
                    PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc,toByteArray(resizedimage),"imagen");
                    float x = (PDRectangle.A4.getWidth() - pdImage.getWidth()) / 2;
                    float y = (PDRectangle.A4.getHeight() - pdImage.getHeight()) / 2;
                    contentStream.drawImage(pdImage, x, y);

                }
                else if (files.length == 2) {
                    BufferedImage image1 = ImageIO.read(files[0].getInputStream());
                    BufferedImage resizedImage1 = Scalr.resize(image1, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, 300);
                    PDImageXObject pdImage1 = PDImageXObject.createFromByteArray(doc, toByteArray(resizedImage1), "imagen 1");

                    float x1 = (PDRectangle.A4.getWidth() - pdImage1.getWidth()) / 2;
                    float y1 = PDRectangle.A4.getHeight() - pdImage1.getHeight() - 100;
                    contentStream.drawImage(pdImage1, x1, y1);

                    BufferedImage image2 = ImageIO.read(files[1].getInputStream());
                    BufferedImage resizedImage2 = Scalr.resize(image2, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, 400);
                    PDImageXObject pdImage2 = PDImageXObject.createFromByteArray(doc, toByteArray(resizedImage2), "imagen 2");

                    float x2 = (PDRectangle.A4.getWidth() - pdImage2.getWidth()) / 2;
                    float y2 = 100;
                    contentStream.drawImage(pdImage2, x2, y2);

                }
            }
            File archivo = new File("D:\\Projectos pr\\JAVA\\ImpresoraServidor\\src\\main\\java\\moises\\impresoraservidor\\pdf\\" + files[0].getOriginalFilename() + UUID.randomUUID() + ".pdf" );
            doc.save(archivo);
            return true;
        } catch (IOException e) {
            System.out.println("Error al crear el PDF : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


        /*else if (files.length == 1){
            try(PDDocument doc = new PDDocument()){

                BufferedImage image = ImageIO.read(files[0].getInputStream());
                BufferedImage resizedimage = Scalr.resize(image,Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH,300);
                PDPage page = new PDPage(PDRectangle.A4);
                doc.addPage(page);

                PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc,toByteArray(resizedimage),"imagen");
               try (PDPageContentStream contentStream = new PDPageContentStream(doc,page, PDPageContentStream.AppendMode.APPEND, true, true)){
                   float coordenadasCentroX = 207.6f;
                   float coordenadasCentroY = 420.9f;

                   contentStream.drawImage(pdImage, coordenadasCentroX, coordenadasCentroY);

               };
                File archivo = new File("D:\\Projectos pr\\JAVA\\ImpresoraServidor\\src\\main\\java\\moises\\impresoraservidor\\pdf\\" + files[0].getOriginalFilename() + UUID.randomUUID() + ".pdf" );
                doc.save(archivo);
                success = true;
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

                for (int i = 0; i<files.length; i++){
                    BufferedImage image = ImageIO.read(files[i].getInputStream());
                    BufferedImage resizedimage = Scalr.resize(image,Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH,300);

                    PDImageXObject pdfImage = PDImageXObject.createFromByteArray(pdDocument,toByteArray(resizedimage),"imagen " + i);
                    PDPageContentStream contentStream = new PDPageContentStream(pdDocument,pagina, PDPageContentStream.AppendMode.APPEND, true, true);
                    float topmargen = 100f;
                    float coordenadasCentroX = PageSize.A4_WIDTH / 2;
                    float coordenadasTopCentroY = PageSize.A4_HEIGHT - topmargen;
                    float coordenadasBottomCentroY = 0 + topmargen;
                    if(i == 0){
                        contentStream.drawImage(pdfImage, coordenadasCentroX, coordenadasTopCentroY);
                    }
                    else {
                        contentStream.drawImage(pdfImage, coordenadasCentroX, coordenadasBottomCentroY);
                        File archivo = new File("D:\\Projectos pr\\JAVA\\ImpresoraServidor\\src\\main\\java\\moises\\impresoraservidor\\pdf\\" + files[1].getOriginalFilename() + UUID.randomUUID() + ".pdf" );
                        pdDocument.save(archivo);
                        success = true;
                    }
                }


            }catch (IOException e){
                System.out.println(e.getMessage());
            }

        }
        return success;
    }*/

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
