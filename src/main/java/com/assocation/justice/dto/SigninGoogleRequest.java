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
public class SigninGoogleRequest {
    private String token;
    private String idToken;
    private Source source;
}
