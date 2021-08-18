package com.bwapp.culpeper.view;

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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModalImageGallery extends Dialog {

    private final ResourceService resourceService;

    private final Div details;
    private final FlexLayout galleryLayout;
    private String fileName;
    private File file;
    private final Image image;
    private String selectedImage;
    private Paragraph text;

    public ModalImageGallery(@Autowired ResourceService resourceService, Image img) {
        this.resourceService = resourceService;
        this.image = img;

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
                file = new File("Culpeper/src/main/resources/META-INF/resources/images/" + fileName);
                FileOutputStream fos = new FileOutputStream(file);
                byte [] contentInBytes = buffer.getInputStream().readAllBytes();
                fos.write(contentInBytes);
                fos.flush();
                fos.close();
                Resource newImage = new Resource("images/" + fileName);
                resourceService.update(newImage);
            } catch (IOException ignored) {
            } finally {
                displayImages();
            }
        });

        Button selectImageButton = new Button("Select");
        selectImageButton.addClickListener(e -> {
            try {
                image.setSrc(selectedImage);
                image.setAlt(selectedImage + " image");
                close();
            }catch (IllegalArgumentException exception) {
                Notification.show("No image selected");
            }
        });

        Button cancelButton = new Button(("Cancel"));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(e -> {
            selectedImage = null;
            close();
        });

        Button removeButton = new Button("Delete");
        removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        ConfirmDialog dialogImage = new ConfirmDialog("Confirm delete",
                "Are you sure you want to delete the image?",
                "Delete", this::deleteImage, "Cancel", this::cancelDelete);
        dialogImage.setConfirmButtonTheme("error primary");
        removeButton.addClickListener(e -> {
            if(selectedImage != null) dialogImage.open();
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
            Image image = new Image(r.getPath(), r.getPath());
            images.add(image);
            image.setMaxHeight("200px");
            image.setMaxWidth("200px");
            image.getStyle().set("margin", "0 25px 25px 0");
            image.addClickListener(e -> {
                for(Image i : images) {
                    i.getStyle().set("border", "none");
                    details.setText("");
                }
                if(!image.getSrc().equals(selectedImage)) {
                    image.getStyle().set("border", "3px solid green");
                    details.setText("Name: " + image.getSrc().substring(image.getSrc().indexOf("/") + 1));
                    selectedImage =  image.getSrc();
                }else {
                    selectedImage = null;
                }
                text.setText("Selected image: " + (selectedImage != null ? "" : "none"));
            });
            galleryLayout.add(image);
        }
    }

    private void deleteImage(ConfirmDialog.ConfirmEvent confirmEvent) {
        try{
            resourceService.delete(resourceService.findByPath(selectedImage));
            File f = new File("Culpeper/src/main/resources/META-INF/resources/" + selectedImage);
            if(f.delete()) Notification.show("Image removed successfully");
        }catch (Exception e) {
            Notification.show("Error deleting database entry");
        }finally {
            displayImages();
        }
    }

    private void cancelDelete(ConfirmDialog.CancelEvent cancelEvent) {
    }
}
