package ee.mihkel.rendipood.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double initialFee;
    private double lateFee;
    @ColumnDefault("0")
    private int bonusDaysUsed;

    @ManyToOne
    private Person person;

}
