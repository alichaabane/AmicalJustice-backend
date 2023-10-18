package com.assocation.justice.dto;

import com.assocation.justice.util.enumeration.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
        private String firstName;
        private String lastName;
        private String username;
        @JsonIgnore
        private String password;
        private Role role;
        private boolean confirmed;
}
