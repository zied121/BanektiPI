package Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class PdfGenerator {

    public static void generatePdf(String fileName, String content) { //filename :nom document ,content:contenu
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName)); //instance de PdfWriter qui ecrit le contrnu de pdf
            document.open(); //ouverture du document
            document.add(new Paragraph(content)); //ajour d'un paragraphe
            document.close(); // fermeture de pdf
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
