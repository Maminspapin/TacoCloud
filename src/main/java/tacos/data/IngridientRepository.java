package tacos.data;

import tacos.Ingredient;

public interface IngridientRepository {

    Iterable<Ingredient> findAll();

    Ingredient findOne(String id);

    Ingredient save(Ingredient ingredient);

}
