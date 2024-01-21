package com.galsie.gcs.homescommondata.bootstrap.galmodel;

import com.galsie.gcs.homescommondata.bootstrap.helpers.TypedEntityBootstrapService;
import com.galsie.gcs.homescommondata.data.entity.protocol.galsie.CategoryTypeEntity;
import com.galsie.gcs.homescommondata.repository.protocol.galsie.CategoryTypeRepository;
import com.galsie.lib.datamodel.galsie.CategoryTypesList;
import com.galsie.lib.datamodel.galsie.types.CategoryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class CategoriesBootststrapService {

    @Autowired
    CategoryTypeRepository categoryTypeRepository;

    private TypedEntityBootstrapService<CategoryType, CategoryTypeEntity> categoryTypeBootstrapService;

    @PostConstruct
    private void onBeansInit(){
        categoryTypeBootstrapService = new TypedEntityBootstrapService<>(categoryTypeRepository, (dbEnt, dataEnt) -> {
            var updated = false;
            var dbEntExtendsCategory = dbEnt.getExtendsCategory();
            var dataEntExtendsCategory = dataEnt.getExtendsCategory();

            if (!Objects.equals(dbEntExtendsCategory, dataEntExtendsCategory)) {
                dbEnt.setExtendsCategory(dataEntExtendsCategory);
                updated = true;
            }

            if (dbEnt.isAbstract()  != dataEnt.isAbstract()){
                dbEnt.setAbstract(dataEnt.isAbstract());
                updated = true;
            }
            return updated;
        });
    }
    public void bootstrap(CategoryTypesList categoryTypesList) throws Exception {

        // map stores for each type, the entity
        HashMap<CategoryType, CategoryTypeEntity> categoryTypeEntities = new HashMap<>();

        // loop through all category types
        for (CategoryType catType: categoryTypesList.getCategoryTypes()){
            var catTypeEntity = CategoryTypeEntity.builder()
                    .typeId(catType.getId())
                    .definition(catType.getDefinition())
                    .name(catType.getName())
                    .isAbstract(catType.isAbstract())
                    .build();// create the category type

            var extendsCategory = catType.getExtendsCategory(); // get the category it extends
            // if this category extends another
            if (extendsCategory != null){
                if (categoryTypeEntities.containsKey(extendsCategory)) { // check if it an entity for it was previously created through this loop
                    // If it wasn't, cant do an association
                    throw new Exception("Category Type must extend a type that is loaded before it. Couldn't find  " + extendsCategory.getDefinition() + " in the loaded entities MAP.");
                }
                // otherwise, can do the association
                catTypeEntity.setExtendsCategory(categoryTypeEntities.get(extendsCategory));
            }
            // Now put this category in the map, so other categories can extend it
            categoryTypeEntities.put(catType, catTypeEntity);

        }
      categoryTypeBootstrapService.bootstrapManyDataObjects(categoryTypesList.getCategoryTypes(), categoryTypeEntities::get, (categoryType, dbCatType) -> {

      }, true);
    }
}
