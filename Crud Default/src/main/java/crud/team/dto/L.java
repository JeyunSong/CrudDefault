package crud.team.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class L {
    private int likeNum;

    public static L toDto(int likeNum) {
        return new L(
                likeNum
        );
    }

}
