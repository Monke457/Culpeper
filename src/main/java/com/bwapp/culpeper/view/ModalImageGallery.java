package com.bwapp.culpeper.view;

import com.bwapp.culpeper.helper.BlobConverter;
import com.bwapp.culpeper.model.Resource;
import com.bwapp.culpeper.service.ResourceService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModalImageGallery extends Dialog {

    private final ResourceService resourceService;

    private final Div details;
    private final FlexLayout galleryLayout;
    private final Paragraph text;
    private final BlobConverter bc = new BlobConverter();
    private String fileName;
    private String selectedSrc;
    private String selectedAlt;

    public ModalImageGallery(@Autowired ResourceService resourceService, Image image) {
        this.resourceService = resourceService;

        setHeight("80vh");
        setWidth("80vw");

        SplitLayout mainLayout = new SplitLayout();
        mainLayout.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        mainLayout.setHeightFull();
        mainLayout.setSecondaryStyle("max-width", "300px");
        mainLayout.setSecondaryStyle("padding", "25px");
        mainLayout.setSecondaryStyle("text-align", "center");

        VerticalLayout rightLayout = new VerticalLayout();
        rightLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        text = new Paragraph("Selected image: none");
        details = new Div();

        galleryLayout = new FlexLayout();
        galleryLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        displayImages();

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload;

        upload = new Upload(buffer);
        upload.setDropLabel(new Span("Upload image"));
        upload.addSucceededListener(e -> {
            fileName = e.getFileName();
            try {
                byte [] contentInBytes = buffer.getInputStream().readAllBytes();
                Blob imageAsBlob = bc.bytesToBlob(contentInBytes);
                Resource newImage = new Resource(fileName, imageAsBlob);
                resourceService.update(newImage);
            } catch (IOException ignored) {
            } finally {
                displayImages();
            }
        });

        Button selectImageButton = new Button("Select");
        selectImageButton.addClickListener(e -> {
            try {
                image.setSrc(selectedSrc);
                image.setAlt(selectedAlt);
                close();
            }catch (IllegalArgumentException exception) {
                Notification.show("No image selected");
            }
        });

        Button cancelButton = new Button(("Cancel"));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(e -> {
            selectedSrc = null;
            close();
        });

        Button removeButton = new Button("Delete");
        removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        ConfirmDialog dialogImage = new ConfirmDialog("Confirm delete",
                "Are you sure you want to delete the image?",
                "Delete", this::deleteImage, "Cancel", this::cancelDelete);
        dialogImage.setConfirmButtonTheme("error primary");
        removeButton.addClickListener(e -> {
            if(selectedSrc == null) dialogImage.open();
        });

        rightLayout.add(text, details, upload, new HorizontalLayout(selectImageButton, cancelButton, removeButton));
        mainLayout.addToPrimary(galleryLayout);
        mainLayout.addToSecondary(rightLayout);
        add(mainLayout);
    }

    private void displayImages() {
        galleryLayout.removeAll();
        List<Resource> resources = resourceService.findAll();

        List<Image> images = new ArrayList<>();

        for (Resource r : resources) {
            Image tmpImg = bc.blobToImage(r.getFile());
            tmpImg.setAlt(r.getFileName());
            images.add(tmpImg);
            tmpImg.setMaxHeight("200px");
            tmpImg.setMaxWidth("200px");
            tmpImg.getStyle().set("margin", "0 25px 25px 0");
            tmpImg.addClickListener(e -> {
                for(Image i : images) {
                    i.getStyle().set("border", "none");
                    details.setText("");
                }
                if(!tmpImg.getSrc().equals(selectedSrc)) {
                    tmpImg.getStyle().set("border", "3px solid green");
                    details.setText("Name: " + tmpImg.getAlt().get());
                    selectedSrc = tmpImg.getSrc();
                    selectedAlt = tmpImg.getAlt().get();
                }else {
                    selectedSrc = null;
                }
                text.setText("Selected image: " + (selectedSrc != null ? "" : "none"));
            });
            galleryLayout.add(tmpImg);
        }
    }

    private void deleteImage(ConfirmDialog.ConfirmEvent confirmEvent) {
        try{
            resourceService.delete(resourceService.findByFileName(selectedAlt));
        }catch (Exception e) {
            Notification.show("Error deleting database entry");
        }finally {
            displayImages();
        }
    }

    private void cancelDelete(ConfirmDialog.CancelEvent cancelEvent) {
    }
}
