package com.example.app.controller;

import com.example.app.model.Material;
import com.example.app.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materials")
public class MaterialController {

    @Autowired
    private MaterialService materialService;


    @PostMapping("")
    public Material addMaterial(@RequestBody Material material) {
        return materialService.addMaterial(material);
    }

    @DeleteMapping("{id}")
    public void deleteMaterial(@PathVariable("id") Long materialId) {
        materialService.deleteMaterial(materialId);
    }

    @PutMapping
    public void modifyMaterial(@RequestBody Material material) {
        materialService.modifyMaterial(material);
    }

    @GetMapping
    public List<Material> listMaterials() {
        return materialService.listMaterials();
    }
}
