module KCLC.School.Management.System {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.swing;
    requires com.jfoenix;

    requires poi.ooxml;
    requires poi.ooxml.schemas;
    requires poi;
    //requires commons.math3;
    //requires org.apache.commons.codec;
   // requires org.apache.commons.collections4;
    //requires org.apache.commons.compress;
    //requires curvesapi;
    //requires SparseBitSet;
    //requires xmlbeans;


    requires sqlite.jdbc;
    requires java.sql;
    opens kclc.model to javafx.base;
    opens kclc;
    //opens kclc to javafx.fxml;
    //exports kclc;
}