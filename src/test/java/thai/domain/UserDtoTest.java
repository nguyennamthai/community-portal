package thai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import thai.service.dto.UserDto;

public class UserDtoTest {
    @Test
    public void test() {
        String username = "johndoe";
        String email = "johndoe@company.com";
        String password = "password";

        UserDto userDto = new UserDto();
        userDto.setUsername("johndoe");
        userDto.setEmail("johndoe@company.com");
        userDto.setPassword("password");

        assertThat(userDto)
          .extracting(UserDto::getUsername, UserDto::getEmail, UserDto::getPassword)
          .containsExactly(username, email, password);
    }
}
