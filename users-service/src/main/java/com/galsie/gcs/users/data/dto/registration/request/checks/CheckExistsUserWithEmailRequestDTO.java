package com.galsie.gcs.users.data.dto.registration.request.checks;


import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.common.EmailHolder;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@GalDTO
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class CheckExistsUserWithEmailRequestDTO implements EmailHolder {
    @NotNull
    String email;
}
