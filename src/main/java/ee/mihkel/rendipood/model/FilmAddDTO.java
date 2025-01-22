package ee.mihkel.rendipood.model;

import ee.mihkel.rendipood.Entity.FilmType;
import lombok.Data;

@Data
public class FilmAddDTO {
    private String name;
    private FilmType type;
}
