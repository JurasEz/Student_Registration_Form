module com.studentregistrationform {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.pdfbox;
    requires com.opencsv;

    opens com.studentregistrationform to javafx.fxml;
    exports com.studentregistrationform;
}