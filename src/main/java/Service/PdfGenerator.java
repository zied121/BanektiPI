package Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class PdfGenerator {

    public static void generatePdf(String fileName, String content) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            document.add(new Paragraph(content));
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
