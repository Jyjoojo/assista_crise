package fr.assistacrise.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.cache.autoconfigure.JCacheManagerCustomizer;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        var ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, fr.assistacrise.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, fr.assistacrise.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, fr.assistacrise.domain.User.class.getName());
            createCache(cm, fr.assistacrise.domain.Authority.class.getName());
            createCache(cm, fr.assistacrise.domain.User.class.getName() + ".authorities");
            createCache(cm, fr.assistacrise.domain.Utilisateur.class.getName());
            createCache(cm, fr.assistacrise.domain.Utilisateur.class.getName() + ".salons");
            createCache(cm, fr.assistacrise.domain.Crise.class.getName());
            createCache(cm, fr.assistacrise.domain.Demande.class.getName());
            createCache(cm, fr.assistacrise.domain.Demande.class.getName() + ".offres");
            createCache(cm, fr.assistacrise.domain.Offre.class.getName());
            createCache(cm, fr.assistacrise.domain.Offre.class.getName() + ".demandes");
            createCache(cm, fr.assistacrise.domain.Information.class.getName());
            createCache(cm, fr.assistacrise.domain.Salon.class.getName());
            createCache(cm, fr.assistacrise.domain.Salon.class.getName() + ".participants");
            createCache(cm, fr.assistacrise.domain.Message.class.getName());
            createCache(cm, fr.assistacrise.domain.Moderation.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }
}
