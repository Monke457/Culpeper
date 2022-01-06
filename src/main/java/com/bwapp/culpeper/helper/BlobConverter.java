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

    public Image blobToImage(Blob imageData, String name) {
        Image image = null;
        try{
            int blobLength = (int) imageData.length();
            byte[] blobAsBytes = imageData.getBytes(1, blobLength);
            StreamResource streamResource = new StreamResource(name, (InputStreamFactory) () -> new ByteArrayInputStream(blobAsBytes));
            streamResource.setContentType(name.substring(name.indexOf(".")));
            image = new Image(streamResource, name.substring(0, name.indexOf(".")));
        }catch (Exception e) {
            System.out.println("blob conversion error - blob to image");
        }
        return image;
    }
}
