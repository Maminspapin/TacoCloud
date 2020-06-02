package tacos.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tacos.Ingredient;
import tacos.Taco;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Repository
public class JdbcTacoRepository implements TacoRepository {

    private JdbcTemplate jdbc;

    @Autowired
    public JdbcTacoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    };

    @Override
    public Taco save(Taco design) {

        long designId = saveTacoInfo(design);
        design.setId(designId);

        for (Ingredient ingredient : design.getIngredients()) {
            saveIngredientToTaco(ingredient, designId);
        }

        return design;
    }

    private long saveTacoInfo(Taco design) {

        design.setCreatedAt(new Date());

        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into Taco (name, createdAt) values (?, ?)",
                Types.VARCHAR, Types.TIMESTAMP
        );

        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        design.getName(),
                        new Timestamp(design.getCreatedAt().getTime())));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);

        return keyHolder.getKey().longValue();
    }

    private void saveIngredientToTaco(Ingredient ingredient, long designId) {
        jdbc.update("insert into Taco_Ingredients (taco, ingredient) values (?, ?)",
                designId, ingredient.getId());
    }
}
