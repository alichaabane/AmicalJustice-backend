package com.assocation.justice.dto;

import com.assocation.justice.util.enumeration.Source;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequest {
    private String username;
    private String email;
    private String password;
    private Source source;
}