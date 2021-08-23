package com.bwapp.culpeper.helper;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class BlobConverter {

    public Blob bytesToBlob(byte[] imageAsBytes) {
        Blob blob = null;
        try {
            blob = new SerialBlob(imageAsBytes);
        } catch (SQLException e) {
            System.out.println("blob conversion error - bytes to blob");
        }
        return blob;
    }

    public Image blobToImage(Blob imageData) {
        Image image = null;
        try{
            int blobLength = (int) imageData.length();
            byte[] blobAsBytes = imageData.getBytes(1, blobLength);
            StreamResource streamResource = new StreamResource("isr", (InputStreamFactory) () -> new ByteArrayInputStream(blobAsBytes));
            image =new Image(streamResource, "photo");
        }catch (Exception e) {
            System.out.println("blob conversion error - blob to image");
        }
        return image;
    }
}
