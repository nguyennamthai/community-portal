package thai.service.dto;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ProfileDto {
    @Size(max = 5000, message = "The information is too long")
    private String info;
}
