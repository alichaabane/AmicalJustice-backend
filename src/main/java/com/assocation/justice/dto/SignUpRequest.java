package com.assocation.justice.dto;

import com.assocation.justice.util.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean confirmed;
    private Role role;
}