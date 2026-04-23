package fr.assistacrise.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UtilisateurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Utilisateur getUtilisateurSample1() {
        return new Utilisateur()
            .id(1L)
            .login("login1")
            .email("email1")
            .motDePasse("motDePasse1")
            .prenom("prenom1")
            .nom("nom1")
            .telephone("telephone1");
    }

    public static Utilisateur getUtilisateurSample2() {
        return new Utilisateur()
            .id(2L)
            .login("login2")
            .email("email2")
            .motDePasse("motDePasse2")
            .prenom("prenom2")
            .nom("nom2")
            .telephone("telephone2");
    }

    public static Utilisateur getUtilisateurRandomSampleGenerator() {
        return new Utilisateur()
            .id(longCount.incrementAndGet())
            .login(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .motDePasse(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString());
    }
}
