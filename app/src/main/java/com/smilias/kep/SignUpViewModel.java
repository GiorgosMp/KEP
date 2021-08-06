package com.smilias.kep;

import com.smilias.kep.model.Citizen;

import androidx.lifecycle.ViewModel;

public class SignUpViewModel extends ViewModel {


    public String signUp(Citizen citizen, String password, String username) {
        if (username.isEmpty()) return "username";
        if (citizen.getFirstName().isEmpty()) return "firstName";
        if (citizen.getLastName().isEmpty()) return "lastName";
        if (citizen.getFatherName().isEmpty()) return "fatherName";
        if (citizen.getMotherName().isEmpty()) return "motherName";
        if (citizen.getBirthDate().isEmpty()) return "birthDate";
        if (citizen.getId().isEmpty()) return "id";
        if (citizen.getAmka().length() != 11) return "amka";
        if (citizen.getTaxNumber().length() != 9) return "taxNumber";
        if (citizen.getAddress().isEmpty()) return "address";
        if (!(citizen.getEmail().contains("@"))) return "email";
        if (password.length() < 5) return "password";
        return "go";

    }

    public String editUserCorrectCredits(Citizen citizen) {
        if (citizen.getFirstName().isEmpty()) return "firstName";
        if (citizen.getLastName().isEmpty()) return "lastName";
        if (citizen.getFatherName().isEmpty()) return "fatherName";
        if (citizen.getMotherName().isEmpty()) return "motherName";
        if (citizen.getBirthDate().isEmpty()) return "birthDate";
        if (citizen.getId().isEmpty()) return "id";
        if (citizen.getAmka().length() != 11) return "amka";
        if (citizen.getTaxNumber().length() != 9) return "taxNumber";
        if (citizen.getAddress().isEmpty()) return "address";
        if (!(citizen.getEmail().contains("@"))) return "email";
        return "go";

    }

}
