package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.model.SeniorDeveloper;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DeveloperController {

    private Map<Integer, Developer> developers;
    private Taxable tax;

    @Autowired
    public DeveloperController(Taxable tax) {
        this.tax = tax;
    }

    @PostConstruct
    public void constructMap() {

        developers = new HashMap<>();

        developers.put(1, new SeniorDeveloper(1, "Deniz Acay", 10000.0));
    }

    @GetMapping("/developers")
    public List<Developer> listDevelopers() {

        return new ArrayList<>(developers.values());
    }

    @PostMapping("/developers")
    public void addDevelooper(@RequestBody Developer developer) {

        if (developers.containsKey(developer.getId()))
            return;

        Double finalSalary = developer.getSalary();

        switch (developer.getExperience()) {

            case JUNIOR:

                finalSalary = finalSalary - finalSalary * tax.getSimpleTaxRate();
                break;

            case MID:

                finalSalary = finalSalary - finalSalary * tax.getMiddleTaxRate();
                break;

            case SENIOR:

                finalSalary = finalSalary - finalSalary * tax.getUpperTaxRate();
                break;
        }

        developer.setSalary(finalSalary);

        developers.put(developer.getId(), developer);
    }

    @GetMapping("/developers/{id}")
    public Developer findDeveloper(@PathVariable("id") Integer id) {

        return developers.get(id);
    }

    @PutMapping("/developers/{id}")
    public void updateDeveloper(@PathVariable("id") Integer id, @RequestBody Developer developer) {

        developers.put(id, developer);
    }

    @DeleteMapping("/developers/{id}")
    public void deleteDeveloper(@PathVariable("id") Integer id) {

        developers.remove(id);
    }
}
