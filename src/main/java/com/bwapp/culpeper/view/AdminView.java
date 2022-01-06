package com.bwapp.culpeper.view;

import com.bwapp.culpeper.helper.BlobConverter;
import com.bwapp.culpeper.model.Ailment;
import com.bwapp.culpeper.model.Plant;
import com.bwapp.culpeper.model.Use_;
import com.bwapp.culpeper.service.AilmentService;
import com.bwapp.culpeper.service.PlantService;
import com.bwapp.culpeper.service.ResourceService;
import com.bwapp.culpeper.service.UseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Administration")
@Route(value = "admin", layout = MainView.class)
public class AdminView extends Div {

    private final PlantService plantService;
    private final AilmentService ailmentService;
    private final UseService useService;
    private final ResourceService resourceService;

    private final Binder<Plant> plantBinder;
    private final Binder<Ailment> ailmentBinder;
    private final Binder<Use_> useBinder;

    private final Div checkboxDiv, gridDiv, formDiv, buttonDiv;

    private Plant plant;
    private Ailment ailment;
    private Use_ use;

    private final Grid<Plant> plantGrid;
    private final Grid<Ailment> ailmentGrid;
    private final Grid<Use_> useGrid;

    private final FormLayout plantForm, ailmentForm, useForm;

    private final TextField latinName = new TextField();
    private final TextField commonName = new TextField();
    private final TextArea description = new TextArea();
    private final TextArea place = new TextArea();
    private final TextArea time = new TextArea();
    private final Select<String> government = new Select<>("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto");
    private final TextArea otherNames = new TextArea();
    private Image selectedImage = new Image();
    private final TextField ailmentName = new TextField();
    private final TextField bodyPart = new TextField();
    private final TextField virtue = new TextField();
    private final TextField virtueA = new TextField();
    private final List<ComboBox<String>> ailmentGroup = new ArrayList<>();
    private final ComboBox<String> ailmentFirstBox = new ComboBox<>();
    private final ComboBox<String> plantSelect = new ComboBox<>();
    private final TextField part = new TextField();
    private final TextArea direction = new TextArea();

    private Checkbox imageBox, descBox, placeBox, timeBox, namesBox, directBox;

    private final Button cancel = new Button("Cancel");
    private final Button savePlant = new Button("Save");
    private final Button saveAilment = new Button("Save");
    private final Button saveUse = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button addAilment = new Button("+ Add ailment");

    private final BlobConverter bc = new BlobConverter();

    public AdminView(@Autowired PlantService plantService, AilmentService ailmentService, UseService useService, ResourceService resourceService) {
        this.plantService = plantService;
        this.ailmentService = ailmentService;
        this.useService = useService;
        this.resourceService = resourceService;

        setSizeFull();

        SplitLayout layout = new SplitLayout();
        layout.setOrientation(SplitLayout.Orientation.VERTICAL);
        layout.setSizeFull();
        layout.setSplitterPosition(15);

        SplitLayout innerLayout = new SplitLayout();
        innerLayout.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        innerLayout.setHeightFull();
        innerLayout.setSecondaryStyle("width", "30%");
        innerLayout.setSecondaryStyle("max-width", "38%");

        HorizontalLayout selectionArea = new HorizontalLayout();
        selectionArea.setAlignItems(FlexComponent.Alignment.CENTER);

        checkboxDiv = new Div();
        checkboxDiv.getStyle().set("margin-left", "30px");
        gridDiv = new Div();
        gridDiv.setSizeFull();
        formDiv = new Div();
        formDiv.setWidthFull();
        formDiv.getStyle().set("padding", "20px");
        buttonDiv = new Div();
        buttonDiv.getStyle().set("padding-top", "20px");

        plantBinder = new Binder<>(Plant.class);
        plantBinder.bindInstanceFields(this);

        ailmentBinder = new Binder<>(Ailment.class);
        ailmentBinder.bindInstanceFields(this);

        useBinder = new Binder<>(Use_.class);
        useBinder.bindInstanceFields(this);

        Select<String> modelSelect = new Select<>();
        modelSelect.setLabel("Select an entity to edit");
        modelSelect.setItems("Plant", "Ailment", "Use");
        modelSelect.addValueChangeListener(e -> displayGridAndForm(e.getValue()));
        modelSelect.getStyle().set("padding-left", "20px").set("width", "200px");

        plantGrid = new Grid<>(Plant.class, false);
        ailmentGrid = new Grid<>(Ailment.class, false);
        useGrid = new Grid<>(Use_.class, false);

        plantForm = new FormLayout();
        ailmentForm = new FormLayout();
        useForm = new FormLayout();

        configureGrids();
        configureCheckboxes();
        configureForms();
        configureButtons();

        selectionArea.add(modelSelect, checkboxDiv);
        innerLayout.addToPrimary(gridDiv);
        innerLayout.addToSecondary(formDiv);
        layout.addToPrimary(selectionArea);
        layout.addToSecondary(innerLayout);
        add(layout);
    }

    private void displayGridAndForm(String s) {
        gridDiv.removeAll();
        formDiv.removeAll();
        clearFormsAndGrids();
        switch (s) {
            case "Plant" -> {
                gridDiv.add(plantGrid);
                formDiv.add(plantForm, createButtons(new Button[]{savePlant, cancel, delete}));
            }
            case "Ailment" -> {
                gridDiv.add(ailmentGrid);
                formDiv.add(ailmentForm, createButtons(new Button[]{saveAilment, cancel, delete}));
            }
            case "Use" -> {
                gridDiv.add(useGrid);
                formDiv.add(useForm, addAilment, createButtons(new Button[]{saveUse, cancel, delete}));
            }
        }
        createCheckboxes(s);
    }

    private void configureGrids() {
        //configure plant grid
        plantGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        plantGrid.addComponentColumn(plant -> {
                    if(plant.getResource() != null) {
                        Image plantImage = bc.blobToImage(plant.getResource().getFile(), plant.getResource().getFileName());
                        plantImage.setMaxWidth("100px");
                        return plantImage;
                    }else {
                        return new Text("No image");
                    }
                })
                .setHeader("Image")
                .setResizable(true)
                .setKey("img")
                .setWidth("10%");
        plantGrid.addColumn(Plant::getLatinName).setHeader("Latin Name").setResizable(true).setWidth("8%").setSortable(true);
        plantGrid.addColumn(Plant::getCommonName).setHeader("Common Name").setResizable(true).setWidth("7%").setSortable(true);
        plantGrid.addColumn(plant ->
                        plant.getDescription() != null && plant.getDescription().length() > 200
                                ? plant.getDescription().substring(0, 200) + "..."
                                : plant.getDescription())
                .setHeader("Description")
                .setKey("desc")
                .setResizable(true)
                .setWidth("22%");
        plantGrid.addColumn(plant ->
                        plant.getPlace() != null && plant.getPlace().length() > 100
                                ? plant.getPlace().substring(0, 100) + "..."
                                : plant.getPlace())
                .setHeader("Place")
                .setKey("place")
                .setResizable(true)
                .setWidth("12%");
        plantGrid.addColumn(plant ->
                        plant.getTime() != null && plant.getTime().length() > 100
                                ? plant.getTime().substring(0, 100) + "..."
                                : plant.getTime())
                .setHeader("Time")
                .setKey("time")
                .setResizable(true)
                .setWidth("12%");
        plantGrid.addColumn(Plant::getGovernment).setHeader("Government").setResizable(true).setWidth("8%").setSortable(true);
        plantGrid.addColumn(Plant::getVirtue).setHeader("Virtue").setResizable(true).setWidth("7%").setSortable(true);
        plantGrid.addColumn(plant ->
                        plant.getOtherNames() != null && plant.getOtherNames().length() > 100
                                ? plant.getOtherNames().substring(0, 100) + "..."
                                : plant.getOtherNames())
                .setHeader("Other Names")
                .setKey("names")
                .setResizable(true)
                .setWidth("12%");
        plantGrid.setSizeFull();
        plantGrid.setItems(plantService.findAll());

        plantGrid.asSingleSelect().addValueChangeListener(e -> {
            if(e.getValue() != null) {
                Plant plantFromBackend = plantService.findById(e.getValue().getId());
                populatePlantForm(plantFromBackend);
            } else {
                plantGrid.setItems(plantService.findAll());
                clearFormsAndGrids();
            }
        });

        //configure ailment grid
        ailmentGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        ailmentGrid.addColumn(Ailment::getAilmentName).setHeader("Name").setResizable(true).setWidth("40%").setSortable(true);
        ailmentGrid.addColumn(Ailment::getBodyPart).setHeader("Body Part").setResizable(true).setWidth("30%").setSortable(true);
        ailmentGrid.addColumn(Ailment::getVirtueA).setHeader("Virtue").setResizable(true).setWidth("30%").setSortable(true);
        ailmentGrid.setSizeFull();
        ailmentGrid.setItems(ailmentService.findAll());

        ailmentGrid.asSingleSelect().addValueChangeListener(e -> {
            if(e.getValue() != null) {
                Ailment ailmentFromBackend = ailmentService.findById(e.getValue().getId());
                populateAilmentForm(ailmentFromBackend);
            } else {
                ailmentGrid.setItems(ailmentService.findAll());
                clearFormsAndGrids();
            }
        });

        //configure use grid
        useGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        useGrid.addColumn(use_ ->
                use_.getPlant() != null ? use_.getPlant().getCommonName() : "")
                .setHeader("Plant").setResizable(true).setWidth("10%").setSortable(true);
        useGrid.addColumn(Use_::getPart).setHeader("Part").setResizable(true).setWidth("10%").setSortable(true);
        useGrid.addColumn(use ->
                        use.getDirection() != null && use.getDirection().length() > 200
                                ? use.getDirection().substring(0, 200) + "..."
                                : use.getDirection())
                .setHeader("Directions")
                .setKey("direct")
                .setResizable(true)
                .setWidth("80%");
        useGrid.setSizeFull();
        useGrid.setItems(useService.findAll());

        useGrid.asSingleSelect().addValueChangeListener(e -> {
            clearAilmentBoxes();
            if(e.getValue() != null) {
                Use_ useFromBackend = useService.findById(e.getValue().getId());
                populateUseForm(useFromBackend);
            } else {
                useGrid.setItems(useService.findAll());
                clearFormsAndGrids();
            }
        });
    }

    private void configureCheckboxes() {
        imageBox = new Checkbox("Hide Image");
        imageBox.addValueChangeListener(e -> plantGrid.getColumnByKey("img").setVisible(!e.getValue()));

        descBox = new Checkbox("Hide description");
        descBox.addValueChangeListener(e -> plantGrid.getColumnByKey("desc").setVisible(!e.getValue()));

        placeBox = new Checkbox("Hide place");
        placeBox.addValueChangeListener(e -> plantGrid.getColumnByKey("place").setVisible(!e.getValue()));

        timeBox = new Checkbox("Hide time");
        timeBox.addValueChangeListener(e -> plantGrid.getColumnByKey("time").setVisible(!e.getValue()));

        namesBox = new Checkbox("Hide other names");
        namesBox.addValueChangeListener(e -> plantGrid.getColumnByKey("names").setVisible(!e.getValue()));

        directBox = new Checkbox("Hide directions");
        directBox.addValueChangeListener(e -> useGrid.getColumnByKey("direct").setVisible(!e.getValue()));
    }

    private void createCheckboxes(String s) {
        checkboxDiv.removeAll();
        switch (s) {
            case "Plant" -> {
                checkboxDiv.add(imageBox, descBox, placeBox, timeBox, namesBox);
                imageBox.setValue(!plantGrid.getColumnByKey("img").isVisible());
                descBox.setValue(!plantGrid.getColumnByKey("desc").isVisible());
                placeBox.setValue(!plantGrid.getColumnByKey("place").isVisible());
                timeBox.setValue(!plantGrid.getColumnByKey("time").isVisible());
                namesBox.setValue(!plantGrid.getColumnByKey("names").isVisible());
            }
            case "Use" -> {
                checkboxDiv.add(directBox);
                directBox.setValue(!useGrid.getColumnByKey("direct").isVisible());
            }
        }
    }

    private void configureForms() {
        plantForm.addFormItem(latinName, "Latin Name");
        plantForm.addFormItem(commonName, "Common Name");
        plantForm.addFormItem(description, "Description");
        plantForm.addFormItem(place, "Place");
        plantForm.addFormItem(time, "Time");
        plantForm.addFormItem(government, "Government");
        plantForm.addFormItem(virtue, "Virtue");
        plantForm.addFormItem(otherNames, "Other Names");
        plantForm.addFormItem(selectedImage, "Image");
        selectedImage.setWidth("100px");
        Button imageSelect = new Button("Select new image");
        imageSelect.addClickListener(e -> new ModalImageGallery(resourceService, selectedImage).open());
        plantForm.add(imageSelect);

        ailmentForm.addFormItem(ailmentName, "Name");
        ailmentForm.addFormItem(bodyPart, "Body Part");
        ailmentForm.addFormItem(virtueA, "Virtue");

        useForm.addFormItem(plantSelect, "Plant");
        useForm.addFormItem(part, "Part");
        useForm.addFormItem(direction, "Directions");
        useForm.addFormItem(ailmentFirstBox, "Ailments");
        addAilment.addClickListener(e -> createAilmentBox(null));

        Component[] components = new Component[]{latinName, commonName, description, place, time,
                government, virtue, virtueA, otherNames, ailmentName, bodyPart, ailmentFirstBox,
                plantSelect, part, direction, addAilment, plantForm, ailmentForm, useForm};

        for(Component c : components) {
            ((HasStyle) c).addClassName("full-width");
        }

        ailmentFirstBox.setItems(ailmentService.getAllNames().stream().sorted());
        ailmentFirstBox.addCustomValueSetListener(e -> ailmentFirstBox.setValue(e.getDetail()));
        ailmentFirstBox.setClearButtonVisible(true);
        plantSelect.setItems(plantService.getAllNames().stream().sorted());
        plantSelect.setClearButtonVisible(true);
    }

    private void configureButtons() {
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        ConfirmDialog dialog = new ConfirmDialog("Confirm delete",
                "Are you sure you want to delete the item?",
                "Delete", this::deleteEntry, "Cancel", this::cancelDelete);
        dialog.setConfirmButtonTheme("error primary");
        delete.addClickListener(e -> dialog.open());

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.getStyle().set("margin", "0 20px");
        cancel.addClickListener(e -> clearFormsAndGrids());

        savePlant.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        savePlant.addClickListener(event -> {
            try {
                if (this.plant == null) {
                    this.plant = new Plant();
                }
                plantBinder.writeBean(this.plant);
                plant.setResource(resourceService.findByFileName(selectedImage.getAlt().get()));
                plantService.update(this.plant);
                clearFormsAndGrids();
                Notification.show("Plant details stored.");
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the plant details.");
            } finally {
                plantGrid.setItems(plantService.findAll());
            }
        });

        saveAilment.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveAilment.addClickListener(event -> {
            try {
                if (this.ailment == null) {
                    this.ailment = new Ailment();
                }
                ailmentBinder.writeBean(this.ailment);
                ailmentService.update(this.ailment);
                clearFormsAndGrids();
                Notification.show("Ailment details stored.");
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the ailment details.");
            } finally {
                ailmentGrid.setItems(ailmentService.findAll());
            }
        });

        saveUse.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveUse.addClickListener(event -> {
            try {
                if (this.use == null) {
                    this.use = new Use_();
                }

                Set<Ailment> updatedAilments = new HashSet<>();
                if(!ailmentService.exists(ailmentFirstBox.getValue())) {
                    this.ailment = new Ailment();
                    ailment.setAilmentName(ailmentFirstBox.getValue());
                    ailmentService.update(ailment);
                }
                updatedAilments.add(ailmentService.findByName(ailmentFirstBox.getValue()));

                for (ComboBox<String> cb : ailmentGroup) {
                    if(!ailmentService.exists(cb.getValue())) {
                        this.ailment = new Ailment();
                        ailment.setAilmentName(cb.getValue());
                        ailmentService.update(ailment);
                    }
                    updatedAilments.add(ailmentService.findByName(cb.getValue()));
                }
                use.setAilments(updatedAilments);
                use.setPlant(plantService.findByName(plantSelect.getValue()));

                useBinder.writeBean(this.use);
                useService.update(this.use);
                clearFormsAndGrids();
                Notification.show("Use details stored.");
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the use details.");
            } finally {
                useGrid.setItems(useService.findAll());
            }
        });
    }

    private Div createButtons(Button[] buttons) {
        buttonDiv.removeAll();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        for(Button button : buttons) {
            buttonLayout.add(button);
        }

        buttonDiv.add(buttonLayout);
        return buttonDiv;
    }

    private void populatePlantForm(Plant value) {
        this.plant = value;
        plantBinder.readBean(value);
        if(value != null && value.getResource() != null) {
            Image img = bc.blobToImage(value.getResource().getFile(), value.getResource().getFileName());
            selectedImage.setSrc(img.getSrc());
            selectedImage.setAlt(img.getAlt().get());
        }
    }

    private void populateAilmentForm(Ailment value) {
        this.ailment = value;
        ailmentBinder.readBean(value);
    }

    private void populateUseForm(Use_ value) {
        this.use = value;

        if(value != null) {
            Plant p = value.getPlant() != null ? value.getPlant() : null;
            plantSelect.setValue(p != null ? p.getCommonName() : "");

            //sort by name
            List<Ailment> aList = value.getAilments()
                    .stream().sorted(Comparator.comparing(Ailment::getAilmentName))
                    .collect(Collectors.toList());

            if(aList.iterator().hasNext()) {
                Ailment aFirst = aList.iterator().next();
                ailmentFirstBox.setValue(aFirst.getAilmentName());
                aList.remove(aFirst);
            }

            //create and display comboBox group
            for(Ailment a : aList) {
                createAilmentBox(a);
            }
        }
        useBinder.readBean(value);
    }

    private void createAilmentBox(Ailment a) {
        ComboBox<String> cb = new ComboBox<>();
        cb.setItems(ailmentService.getAllNames().stream().sorted());
        cb.setClearButtonVisible(true);
        cb.addCustomValueSetListener(e -> cb.setValue(e.getDetail()));
        cb.addValueChangeListener(e -> {
            if(e.getValue() == null) useForm.remove(cb);
        });

        if(a != null) {
            cb.setValue(a.getAilmentName());
        }

        ailmentGroup.add(cb);
        useForm.add(cb);
    }

    private void deleteEntry(ConfirmDialog.ConfirmEvent confirmEvent){
        if(plant != null) {
            try{
                plantService.delete(plant.getId());
                plantGrid.setItems(plantService.findAll());
            }catch(DataIntegrityViolationException e) {
                Notification.show("Could not delete 'Plant' entry: \n" +
                        "Make sure there are no linked 'Use' entries before deleting.");
            }
        }else if(ailment != null) {
            try {
                ailmentService.delete(ailment.getId());
                ailmentGrid.setItems(ailmentService.findAll());
            }catch(DataIntegrityViolationException e) {
                Notification.show("Could not delete 'Ailment' entry. \n" +
                        "Make sure there are no linked 'Use' entries before deleting.");
            }
        }else if(use != null) {
            try{
                useService.delete(use.getId());
                useGrid.setItems(useService.findAll());
            }catch(DataIntegrityViolationException e) {
                Notification.show("Could not delete 'Use' entry");
            }
        }
    }

    private void cancelDelete(ConfirmDialog.CancelEvent cancelEvent) {
    }

    private void clearFormsAndGrids() {
        plantSelect.setValue(null);
        clearAilmentBoxes();
        selectedImage.setSrc("");
        selectedImage.setAlt("");

        populateAilmentForm(null);
        populatePlantForm(null);
        populateUseForm(null);

        useGrid.select(null);
        plantGrid.select(null);
        ailmentGrid.select(null);

        plant = null;
        ailment = null;
        use = null;

        refreshGrids();
    }

    private void refreshGrids() {
        plantGrid.setItems(plantService.findAll());
        ailmentGrid.setItems(ailmentService.findAll());
        useGrid.setItems(useService.findAll());
    }

    private void clearAilmentBoxes() {
        ailmentFirstBox.setValue(null);
        for (ComboBox<String> cb : ailmentGroup) {
            useForm.remove(cb);
        }
        ailmentGroup.clear();
    }
}
