package lt.zymantas.vocabulary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String word;
    private Integer count;

    @Override
    public String toString(){
        return word + " " + count.toString();
    }
}
