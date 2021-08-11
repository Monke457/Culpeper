package com.bwapp.culpeper.view;

import com.bwapp.culpeper.model.Ailment;
import com.bwapp.culpeper.model.Plant;
import com.bwapp.culpeper.model.Use_;
import com.bwapp.culpeper.service.AilmentService;
import com.bwapp.culpeper.service.UseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Culpeper")
@Route(value = "", layout = MainView.class)
public class HomeView extends Div {

    private final UseService useService;
    private final AilmentService ailmentService;

    private final SplitLayout innerLayout;

    private final Grid<Use_> plantGrid;
    private final Grid<Ailment> ailmentGrid;

    private TextField searchPlants;
    private TextField searchAilments;
    private final Div display= new Div();

    public HomeView(@Autowired UseService useService, AilmentService ailmentService) {
        this.useService = useService;
        this.ailmentService = ailmentService;

        setSizeFull();

        //configure main layout - search bars and details view
        SplitLayout layout = new SplitLayout();
        layout.setOrientation(SplitLayout.Orientation.VERTICAL);
        layout.addToPrimary(createSearch());
        layout.setSizeFull();
        layout.setSplitterPosition(30);

        //configure details view split layout
        innerLayout = new SplitLayout();
        innerLayout.getStyle().set("margin", "auto");
        innerLayout.setSizeFull();
        innerLayout.setPrimaryStyle("width", "0");
        innerLayout.setSecondaryStyle("width", "0");

        //plant grid settings
        plantGrid = new Grid<>(Use_.class, false);
        plantGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);   //text wrap within cell
        plantGrid.addColumn(use -> use.getPlant().getLatinName()).setHeader("Latin Name").setResizable(true);
        plantGrid.addColumn(use -> use.getPlant().getCommonName()).setHeader("Common Name").setResizable(true);
        plantGrid.addColumn(use -> use.getPlant().getGovernment()).setHeader("Government").setResizable(true);
        plantGrid.addColumn(use -> use.getPlant().getVirtue()).setHeader("Virtue").setResizable(true);
        plantGrid.setHeightFull();


        //ailment grid settings
        ailmentGrid = new Grid<>(Ailment.class, false);
        ailmentGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);      //text wrap
        ailmentGrid.addColumn(Ailment::getAilmentName).setHeader("Ailment").setResizable(true);
        ailmentGrid.addColumn(Ailment::getBodyPart).setHeader("Body Part").setResizable(true);
        ailmentGrid.addColumn(Ailment::getVirtue).setHeader("Virtue").setResizable(true);
        ailmentGrid.setHeightFull();

        //style for usage display
        display.getStyle().set("padding", "10px");
        innerLayout.addToSecondary(display);

        //listener on grid items for details display
        plantGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                clearDetailsView();
                createDetailsViewByPlant(event.getValue().getPlant().getId());     //parameter: selected plant id
            } else {
                clearDetailsView();
            }
        });

        //listener on grid items for details display
        ailmentGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                clearDetailsView();
                createDetailsViewByAilment(event.getValue().getId());     //parameter: selected ailment id
            } else {
                clearDetailsView();
            }
        });

        layout.addToSecondary(innerLayout);
        add(layout);
    }

    private Div createSearch() {
        Div main = new Div();
        main.setWidthFull();
        main.getStyle().set("margin", "auto").set("text-align", "center");

        HorizontalLayout searchLayout = new HorizontalLayout();
        searchLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        searchPlants = new TextField("Search for plant");
        searchPlants.setWidth("300px");

        Span orLabel = new Span("or");
        orLabel.getStyle().set("margin", "0 30px");

        searchAilments = new TextField("Search for ailment");
        searchAilments.setWidth("300px");

        Button searchButton = new Button("Search");
        searchButton.addClickListener(e -> createResults());
        searchButton.getStyle().set("margin-top", "30px");

        searchLayout.add(searchPlants, orLabel, searchAilments);

        main.add(new H2("Culpeper's Complete Herbal"), searchLayout, searchButton);

        return main;
    }

    private void createResults() {
        String termP = searchPlants.getValue();
        String termA = searchAilments.getValue();

        clearDetailsView();

        if(!termP.isBlank()) {
            innerLayout.addToPrimary(plantGrid);
            plantGrid.setItems(useService.findByPlantTerm(termP));
        }

        if(!termA.isBlank()) {
            innerLayout.addToPrimary(ailmentGrid);
            ailmentGrid.setItems(ailmentService.findByTerm(termA));
        }
    }

    //display usages and details by plant
    private void createDetailsViewByPlant(Long plantId) {
        display.removeAll();

        HorizontalLayout detailsLayout = new HorizontalLayout();
        Div imageDiv = new Div();
        Div textDiv = new Div();
        textDiv.getStyle().set("width", "100%");

        List<Use_> uses = useService.findByPlantId(plantId);
        Plant plant = uses.get(0).getPlant();

        //display plant image and details
        if(plant.getImgPath() != null) {
            Image plantImage = new Image("images/" + plant.getImgPath() + ".jpg", plant.getLatinName() + " image");
            plantImage.getStyle().set("width", "100%");
            imageDiv.add(plantImage);
        }

        Paragraph description = new Paragraph("Description");
        description.getStyle().set("font-weight", "bold");
        textDiv.add(description, new Span(plant.getDescription()));

        Paragraph aka = new Paragraph("Also called");
        aka.getStyle().set("font-weight", "bold");

        detailsLayout.add(imageDiv, textDiv);
        display.add(detailsLayout, aka, new Span(plant.getOtherNames()), new Hr());

        //display usage details per ailment
        for(Use_ u : uses) {        //iterate through all uses
            display.add(new H3(u.getAilments().size() > 1 ? "For ailments: " : "For ailment: "));

            for(Ailment a : u.getAilments()) {       //iterate through all ailments for each use
                display.add(new ListItem(a.getAilmentName()));
            }

            Paragraph part = new Paragraph("Part of plant to use");
            part.getStyle().set("font-weight", "bold");

            Paragraph direct = new Paragraph("Directions");
            direct.getStyle().set("font-weight", "bold");

            display.add(part, new Span(u.getPart() == null || u.getPart().isBlank() ? "whole plant" : u.getPart()));
            display.add(direct, new Span(u.getDirection()));
            display.add(new Hr());
        }
    }

    private void createDetailsViewByAilment(Long ailmentId) {
        display.removeAll();
        display.getStyle().set("text-align", "left").set("vertical-align", "top");

        HorizontalLayout detailsLayout;
        Div imageDiv;
        Div textDiv;
        List<Use_> uses = useService.findByAilmentId(ailmentId);

        for(Use_ u : uses) {
            Plant p = u.getPlant();

            detailsLayout = new HorizontalLayout();
            imageDiv = new Div();
            textDiv = new Div();
            textDiv.getStyle().set("width", "100%");

            Image plantImage = new Image("images/" + p.getImgPath() + ".jpg", p.getLatinName() + " image");
            plantImage.getStyle().set("width", "100%");

            Paragraph part = new Paragraph("Part to use: ");
            part.getStyle().set("font-weight", "bold");

            Paragraph direct = new Paragraph("Directions: ");
            direct.getStyle().set("font-weight", "bold");

            Span ln = new Span(p.getLatinName());
            ln.getStyle().set("font-style", "italic");
            Span cn = new Span(p.getCommonName());

            H2 plantTitle = new H2();
            plantTitle.add(ln);
            plantTitle.add(", ");
            plantTitle.add(cn);
            plantTitle.getStyle().set("margin-top", "0");

            imageDiv.add(plantImage);
            imageDiv.getStyle().set("margin", "auto");

            textDiv.add(plantTitle);
            textDiv.add(part, new Span(u.getPart() != null && !u.getPart().isBlank() ? u.getPart() : "whole plant"));
            textDiv.add(direct, new Span(u.getDirection()));

            detailsLayout.add(imageDiv, textDiv);
            display.add(detailsLayout, new Hr());
        }
    }

    private void clearDetailsView() {
        display.removeAll();
    }
}
