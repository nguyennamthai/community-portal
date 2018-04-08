package thai.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class UserDtoTest {
    @Test
    public void test() {
        UserDto userDto = new UserDto();
        userDto.setUsername("johndoe");
        userDto.setEmail("johndoe@company.com");
        userDto.setPassword("password");
        userDto.setPassRetyped("password");

        assertThat(userDto)
          .extracting(UserDto::getUsername, UserDto::getEmail, UserDto::getPassword, UserDto::getPassRetyped)
          .containsExactly("johndoe", "johndoe@company.com", "password", "password");
    }
}
