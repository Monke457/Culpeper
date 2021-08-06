package com.bwapp.culpeper.view;

import com.bwapp.culpeper.model.*;
import com.bwapp.culpeper.service.UseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@PageTitle("Home View")
@Route(value = "", layout = MainView.class)
public class HomeView extends Div {

    private final UseService useService;

    private final Grid<Use_> plantGrid = new Grid<>(Use_.class, false);

    private TextField searchPlants;
    private final Div display= new Div();

    public HomeView(@Autowired UseService useService) {
        this.useService = useService;

        SplitLayout layout = new SplitLayout();
        layout.setOrientation(SplitLayout.Orientation.VERTICAL);
        layout.addToPrimary(createSearch());

        SplitLayout innerLayout = new SplitLayout();
        innerLayout.getStyle().set("margin", "auto");
        innerLayout.setSizeFull();
        innerLayout.setPrimaryStyle("min-width", "50%");

        //display plants by description and use
        innerLayout.addToPrimary(plantGrid);
        plantGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);   //text wrap within cell
        plantGrid.addColumn(use -> use.getPlant().getLatinName()).setHeader("Latin Name").setResizable(true).setWidth("220px");
        plantGrid.addColumn(use -> use.getPlant().getCommonName()).setHeader("Common Name").setResizable(true).setWidth("160px");
        plantGrid.addColumn(use -> use.getPlant().getDescription()).setHeader("Description").setResizable(true).setWidth("320px");
        plantGrid.addColumn(use -> displayVirtues(use.getPlant().getVirtue())).setHeader("Virtue").setResizable(true).setWidth("150px");

        //style for usage display
        display.getStyle().set("text-align", "center");
        innerLayout.addToSecondary(display);

        //populate details view
        plantGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                createDetailsView(event.getValue().getPlant().getId());     //parameter: selected plant id
            } else {
                clearDetailsView();
            }
        });

        layout.addToSecondary(innerLayout);

        add(layout);
    }

    private Div createSearch() {
        Div main = new Div();
        main.getStyle().set("margin", "auto").set("text-align", "center");

        searchPlants = new TextField("Search by plant, term, or ailment");
        searchPlants.getStyle().set("margin-right", "30px");
        searchPlants.setWidth("400px");

        Button searchButton = new Button("Search");
        searchButton.addClickListener(e -> createResults());
        searchButton.getStyle().set("margin-top", "30px");

        main.add(new H2("Culpeper's Complete Herbal"), searchPlants, new Paragraph(searchButton));

        return main;
    }

    private void createResults() {
        String term = searchPlants.getValue();

        if(!term.isBlank()) {
            plantGrid.setItems(useService.findByAilmentTerm(term));
        }
    }

    private String displayVirtues(Virtue virtue) {
        StringBuilder content = new StringBuilder();

        boolean check = false;

        switch(virtue.getHot()) {
            case ONE -> content.append("Hot 1, ");
            case TWO -> content.append("Hot 2, ");
            case THREE -> content.append("Hot 3, ");
            case FOUR -> content.append("Hot 4, ");
        }

        switch(virtue.getCold()) {
            case ONE -> content.append("Cold 1, ");
            case TWO -> content.append("Cold 2, ");
            case THREE -> content.append("Cold 3, ");
            case FOUR -> content.append("Cold 4, ");
        }

        if(content.isEmpty()) {
            content.append("Temperate, ");
        }

        switch(virtue.getDry()) {
            case ONE -> content.append("Dry 1");
            case TWO -> content.append("Dry 2");
            case THREE -> content.append("Dry 3");
            case FOUR -> content.append("Dry 4");
            default -> check = true;
        }

        switch(virtue.getMoist()) {
            case ONE -> content.append("Moist 1");
            case TWO -> content.append("Moist 2");
            case THREE -> content.append("Moist 3");
            case FOUR -> content.append("Moist 4");
            default -> content.append(check ? "Temperate" : "");
        }

        return content.toString();
    }

    //display usages by plant
    private void createDetailsView(Long plantId) {
        display.removeAll();
        Plant plant = useService.findByPlantId(plantId).get(0).getPlant();

        if(plant.getImgPath() != null) {
            Image plantImage = new Image(plant.getImgPath(), plant.getLatinName() + "_image");
            plantImage.setMaxWidth("500px");
            plantImage.setMaxHeight("500px");
            display.add(plantImage);
        }

        List<Use_> uses = useService.findByPlantId(plantId);

        Comparator<Use_> compareByAilment = Comparator.comparing(o -> o.getAilment().getAilmentName());
        uses.sort(compareByAilment);

        List<String> ailmentCheck = new ArrayList<>();

        for(Use_ u : uses) {
            Ailment a = u.getAilment();
            if(!ailmentCheck.contains(a.getAilmentName())) {
                ailmentCheck.add(a.getAilmentName());
                display.add(new H2("For " + a.getAilmentName().toLowerCase() + "s"));
                display.add(new Paragraph("Affected area: " + a.getBodyPart()));
                display.add(new Paragraph(a.getAilmentName() + " virtues: " + displayVirtues(a.getVirtue())));
            }

            display.add(new Paragraph("Part of plant to use: " + (u.getPart() != null ? u.getPart().getPartName() : "whole plant")));
            display.add(new Paragraph("Directions: " + u.getDirection()));
            display.add(new Hr());
        }
        ailmentCheck.clear();
    }

    private void clearDetailsView() {
        display.removeAll();
    }
}
