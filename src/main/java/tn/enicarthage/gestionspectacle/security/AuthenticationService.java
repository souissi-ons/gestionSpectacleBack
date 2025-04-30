package tn.enicarthage.gestionspectacle.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.enicarthage.gestionspectacle.dtos.AuthenticationRequest;
import tn.enicarthage.gestionspectacle.dtos.AuthenticationResponse;
import tn.enicarthage.gestionspectacle.dtos.RegisterRequest;
import tn.enicarthage.gestionspectacle.model.Utilisateur;
import tn.enicarthage.gestionspectacle.repository.UtilisateurRepository;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final tn.enicarthage.gestionspectacle.security.SecurityService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (request.getMotDePasse() == null || request.getMotDePasse().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire");
        }

        var user = Utilisateur.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .build();

        utilisateurRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getMotDePasse()
                )
        );
        var user = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken((UserDetails) user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }
}