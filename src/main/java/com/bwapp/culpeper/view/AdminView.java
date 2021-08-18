package com.bwapp.culpeper.view;

import com.bwapp.culpeper.model.Ailment;
import com.bwapp.culpeper.model.Plant;
import com.bwapp.culpeper.model.Use_;
import com.bwapp.culpeper.service.AilmentService;
import com.bwapp.culpeper.service.PlantService;
import com.bwapp.culpeper.service.UseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import java.util.List;

@PageTitle("Administration")
@Route(value = "admin", layout = MainView.class)
public class AdminView extends Div {

    private final PlantService plantService;
    private final AilmentService ailmentService;
    private final UseService useService;

    private Binder<Plant> plantBinder;
    private Binder<Ailment> ailmentBinder;
    private Binder<Use_> useBinder;

    private Plant plant1 = new Plant();
    private Ailment ailment1 = new Ailment();
    private Use_ use1 = new Use_();

    private final SplitLayout layout;
    private final SplitLayout innerLayout;

    private final Grid<Plant> plantGrid;
    private final Grid<Ailment> ailmentGrid;
    private final Grid<Use_> useGrid;

    private FormLayout plantForm;
    private FormLayout ailmentForm;
    private FormLayout useForm;

    private TextField latinName = new TextField();
    private TextField commonName = new TextField();
    private TextArea description = new TextArea();
    private TextArea place = new TextArea();
    private TextArea time = new TextArea();
    private Select<String> government = new Select<>("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto");
    private TextField virtue = new TextField();
    private TextArea otherNames = new TextArea();

    private TextField ailmentName = new TextField();
    private TextField bodyPart = new TextField();

    private ComboBox<List<Ailment>> ailments = new ComboBox<>();
    private Select<Plant> plant = new Select<>();
    private TextField part = new TextField();
    private TextArea direction = new TextArea();

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    public AdminView(@Autowired PlantService plantService, AilmentService ailmentService, UseService useService) {
        this.plantService = plantService;
        this.ailmentService = ailmentService;
        this.useService = useService;

        setSizeFull();

        layout = new SplitLayout();
        layout.setOrientation(SplitLayout.Orientation.VERTICAL);
        layout.setSizeFull();
        layout.setSplitterPosition(15);

        innerLayout = new SplitLayout();
        innerLayout.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        innerLayout.setHeightFull();

        Select<String> modelSelect = new Select<>();
        modelSelect.setLabel("Select an entity to edit");
        modelSelect.setItems("Plant", "Ailment", "Use");
        modelSelect.addValueChangeListener(e -> displayGridAndForm(e.getValue()));
        modelSelect.getStyle().set("padding-left", "20px").set("width", "200px");

        plantGrid = new Grid<>(Plant.class, false);
        ailmentGrid = new Grid<>(Ailment.class, false);
        useGrid = new Grid<>(Use_.class, false);

        plantBinder = new Binder<>(Plant.class);
        plantBinder.bindInstanceFields(this);

        ailmentBinder = new Binder<>(Ailment.class);
        ailmentBinder.bindInstanceFields(this);

        useBinder = new Binder<>(Use_.class);
        useBinder.bindInstanceFields(this);

        configureGrids();

        layout.addToPrimary(modelSelect);
        layout.addToSecondary(innerLayout);
        add(layout);
    }

    private void displayGridAndForm(String s) {
        switch (s) {
            case "Plant" -> {
                configurePlantHeader();
                innerLayout.addToPrimary(plantGrid);
                innerLayout.addToSecondary(createPlantForm(), createButtonLayout());
            }
            case "Ailment" -> {
                innerLayout.addToPrimary(ailmentGrid);
                innerLayout.addToSecondary(createAilmentForm(), createButtonLayout());
            }
            case "Use" -> {
                innerLayout.addToPrimary(useGrid);
                innerLayout.addToSecondary(createUseForm(), createButtonLayout());
            }
        }
        innerLayout.setSecondaryStyle("width", "20%");
    }

    private void configureGrids() {
        //configure plant grid
        plantGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        plantGrid.addColumn(Plant::getLatinName).setHeader("Latin Name").setResizable(true).setWidth("8%").setSortable(true);
        plantGrid.addColumn(Plant::getCommonName).setHeader("Common Name").setResizable(true).setWidth("7%").setSortable(true);
        plantGrid.addColumn(plant ->
                        plant.getDescription().length() > 200
                                ? plant.getDescription().substring(0, 200) + "..."
                                : plant.getDescription())
                .setHeader("Description")
                .setResizable(true)
                .setWidth("25%");
        plantGrid.addColumn(plant ->
                        plant.getPlace().length() > 100
                                ? plant.getPlace().substring(0, 100) + "..."
                                : plant.getPlace())
                .setHeader("Place")
                .setResizable(true)
                .setWidth("14%");
        plantGrid.addColumn(plant ->
                        plant.getTime().length() > 100
                                ? plant.getTime().substring(0, 100) + "..."
                                : plant.getTime())
                .setHeader("Time")
                .setResizable(true)
                .setWidth("14%");
        plantGrid.addColumn(Plant::getGovernment).setHeader("Government").setResizable(true).setWidth("8%").setSortable(true);
        plantGrid.addColumn(Plant::getVirtue).setHeader("Virtue").setResizable(true).setWidth("7%").setSortable(true);
        plantGrid.addColumn(plant ->
                        plant.getOtherNames().length() > 100
                                ? plant.getOtherNames().substring(0, 100) + "..."
                                : plant.getOtherNames())
                .setHeader("Other Names")
                .setResizable(true)
                .setWidth("15%");
        plantGrid.setHeightFull();
        plantGrid.setWidth("70%");
        plantGrid.setItems(plantService.findAll());

        plantGrid.asSingleSelect().addValueChangeListener(e -> {
            if(e.getValue() != null) {
                Plant plantFromBackend = plantService.findById(e.getValue().getId());
                populatePlantForm(plantFromBackend);
            } else {
                plantGrid.setItems(plantService.findAll());
                populatePlantForm(null);
            }
        });

        //configure ailment grid
        ailmentGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        ailmentGrid.addColumn(Ailment::getAilmentName).setHeader("Name").setResizable(true).setWidth("40%").setSortable(true);
        ailmentGrid.addColumn(Ailment::getBodyPart).setHeader("Body Part").setResizable(true).setWidth("30%").setSortable(true);
        ailmentGrid.addColumn(Ailment::getVirtue).setHeader("Virtue").setResizable(true).setWidth("30%").setSortable(true);
        ailmentGrid.setHeightFull();
        ailmentGrid.setWidth("70%");
        ailmentGrid.setItems(ailmentService.findAll());

        ailmentGrid.asSingleSelect().addValueChangeListener(e -> {
            if(e.getValue() != null) {
                Ailment ailmentFromBackend = ailmentService.findById(e.getValue().getId());
                populateAilmentForm(ailmentFromBackend);
            } else {
                ailmentGrid.setItems(ailmentService.findAll());
                populatePlantForm(null);
            }
        });

        //configure use grid
        useGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        useGrid.addColumn(use_ -> use_.getPlant().getCommonName()).setHeader("Plant").setResizable(true).setWidth("10%").setSortable(true);
        useGrid.addColumn(Use_::getPart).setHeader("Part").setResizable(true).setWidth("10%").setSortable(true);
        useGrid.addColumn(use ->
                        use.getDirection().length() > 200
                                ? use.getDirection().substring(0, 200) + "..."
                                : use.getDirection())
                .setHeader("Directions")
                .setResizable(true)
                .setWidth("80%");
        useGrid.setHeightFull();
        useGrid.setWidth("70%");
        useGrid.setItems(useService.findAll());

        useGrid.asSingleSelect().addValueChangeListener(e -> {
            if(e.getValue() != null) {
                Use_ useFromBackend = useService.findById(e.getValue().getId());
                populateUseForm(useFromBackend);
            } else {
                useGrid.setItems(useService.findAll());
                populatePlantForm(null);
            }
        });
    }

    private void configurePlantHeader() {
        //here will be checkboxes to hide columns
    }

    private Div createPlantForm() {
        Div formDiv = new Div();
        formDiv.getStyle().set("padding", "20px");

        latinName.setWidth("300%");
        commonName.setWidth("300%");
        description.setWidth("300%");
        place.setWidth("300%");
        time.setWidth("300%");
        government.setWidth("300%");
        virtue.setWidth("300%");
        otherNames.setWidth("300%");

        plantForm = new FormLayout();
        plantForm.addFormItem(latinName, "Latin Name");
        plantForm.addFormItem(commonName, "Common Name");
        plantForm.addFormItem(description, "Description");
        plantForm.addFormItem(place, "Place");
        plantForm.addFormItem(time, "Time");
        plantForm.addFormItem(government, "Government");
        plantForm.addFormItem(virtue, "Virtue");
        plantForm.addFormItem(otherNames, "Other Names");

        plantForm.setWidth("30%");

        formDiv.add(plantForm);
        return formDiv;
    }

    private Div createAilmentForm() {
        Div formDiv = new Div();
        formDiv.getStyle().set("padding", "20px");

        ailmentName.setWidth("300%");
        bodyPart.setWidth("300%");
        virtue.setWidth("300%");

        ailmentForm = new FormLayout();
        ailmentForm.addFormItem(ailmentName, "Name");
        ailmentForm.addFormItem(bodyPart, "Body Part");
        ailmentForm.addFormItem(virtue, "Virtue");     //change to select

        ailmentForm.setWidth("30%");

        formDiv.add(ailmentForm);
        return formDiv;
    }

    private Div createUseForm() {
        Div formDiv = new Div();
        formDiv.getStyle().set("padding", "20px");

        ailments.setItems(ailmentService.findAll());
        plant.setItems(plantService.findAll());

        ailments.setWidth("300%");
        plant.setWidth("300%");
        part.setWidth("300%");
        direction.setWidth("300%");

        useForm = new FormLayout();
        useForm.addFormItem(ailments, "ailment");      //change to combo box ???
        useForm.addFormItem(plant, "Plant");       //change to select
        useForm.addFormItem(part, "Part");         //change to select
        useForm.addFormItem(direction, "Directions");

        useForm.setWidth("30%");

        formDiv.add(useForm);
        return formDiv;
    }

    private Div createButtonLayout() {
        Div buttonDiv = new Div();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.getStyle().set("padding", "0, 20px");
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel, delete);
        buttonDiv.add(buttonLayout);
        return buttonDiv;
    }

    private void populatePlantForm(Plant value) {
        this.plant1 = value;
        plantBinder.readBean(this.plant1);
    }

    private void populateAilmentForm(Ailment value) {
        this.ailment1 = value;
        ailmentBinder.readBean(this.ailment1);
    }

    private void populateUseForm(Use_ value) {
        this.use1 = value;
        useBinder.readBean(this.use1);
    }
}
