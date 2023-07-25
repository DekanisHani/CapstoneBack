package com.example.app.service;

import com.example.app.model.Material;
import com.example.app.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    private MaterialRepository materialRepository;

    @Autowired
    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public Material addMaterial(Material material) {
        return materialRepository.save(material);
    }

    public void deleteMaterial(Long materialId) {
        materialRepository.deleteById(materialId);
    }

    public void modifyMaterial(Material material) {
        materialRepository.save(material);
    }

    public List<Material> listMaterials() {
        return (List<Material>) materialRepository.findAll();
    }

    public Optional<Material> getMaterialById(Long materialId) {
        return materialRepository.findById(materialId);
    }
}
